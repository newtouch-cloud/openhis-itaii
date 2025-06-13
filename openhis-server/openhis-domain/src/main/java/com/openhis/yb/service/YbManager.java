/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.core.web.controller.common.CommonController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.core.common.core.domain.R;
import com.core.common.core.redis.RedisCache;
import com.core.common.exception.ServiceException;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.SecurityUtils;
import com.core.system.service.impl.SysUserServiceImpl;
import com.openhis.administration.domain.*;
import com.openhis.administration.service.*;
import com.openhis.common.constant.YbCommonConstants;
import com.openhis.common.enums.AssignSeqEnum;
import com.openhis.common.enums.Whether;
import com.openhis.common.enums.ybenums.YbHospApprFlag;
import com.openhis.common.enums.ybenums.YbMdtrtCertType;
import com.openhis.common.enums.ybenums.YbMedType;
import com.openhis.common.enums.ybenums.YbPsnSetlWay;
import com.openhis.financial.domain.PaymentReconciliation;
import com.openhis.financial.model.PaymentResult;
import com.openhis.financial.service.IContractService;
import com.openhis.financial.service.IPaymentRecDetailService;
import com.openhis.financial.service.IPaymentReconciliationService;
import com.openhis.medication.service.IMedicationDispenseService;
import com.openhis.workflow.service.IDeviceDispenseService;
import com.openhis.workflow.service.IInventoryItemService;
import com.openhis.workflow.service.IServiceRequestService;
import com.openhis.yb.domain.ClinicPreSettle;
import com.openhis.yb.domain.ClinicReg;
import com.openhis.yb.domain.InfoPerson;
import com.openhis.yb.dto.*;
import com.openhis.yb.model.CancelRegPaymentModel;
import com.openhis.yb.model.Clinic2207OrderModel;
import com.openhis.yb.model.Clinic2207OrderParam;
import com.openhis.yb.model.OutpatientRegistrationModel;
import com.openhis.yb.service.impl.DirectoryCheckRecordServiceImpl;
import com.openhis.yb.util.CommonConstant;
import com.openhis.yb.util.YbParamBuilderUtil;

/**
 * 医保服务（与ybController方法一致，原定是ui触发医保controller，2025/05/05由于缺少ui资源，欲使用对应的业务controller直接触发医保服务，故将其下沉到Service层）
 *
 * @author SunJQ
 * @date 2025-05-05
 */
@Service
public class YbManager {

    @Autowired
    private YbDao ybBaseService;
    @Autowired
    private YbHttpUtils ybHttpService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private AssignSeqUtil assignSeqUtil;

    /******************************** 业务服务 ***********************************/
    @Autowired
    private IPaymentRecDetailService iPaymentRecDetailService;
    @Autowired
    private IPaymentReconciliationService iPaymentReconciliationService;
    @Autowired
    private SysUserServiceImpl userService;
    @Autowired
    private IPractitionerService iPractitionerService;
    @Autowired
    private IEncounterService iEncounterService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IChargeItemService iChargeItemService;
    @Autowired
    private IInvoiceService invoiceService;
    @Autowired
    private IPatientService iPatientService;
    @Autowired
    private IContractService iContractService;
    @Autowired
    private IOrganizationService iOrganizationService;
    @Autowired
    private IInventoryItemService inventoryItemService;
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);
    /****************************** 基础医保服务 *********************************/

    @Autowired
    private IPerinfoService iPerinfoService;
    @Autowired
    private IRegService iRegService;
    @Autowired
    private IClinicUnRegService iClinicUnRegService;
    @Autowired
    private ISignService iSignService;
    @Autowired
    private IClinicPreSettleService iClinicPreSettleService;
    @Autowired
    private IClinicSettleService iClinicSettleService;
    @Autowired
    private IClinicUnSettleService iClinicUnSettleService;
    @Autowired
    private DirectoryCheckRecordServiceImpl directoryCheckRecordService;
    @Autowired
    private IInventorySaleReturnRecordService iInventorySaleReturnRecordService;
    @Autowired
    private IInventorySaleRecordService iInventorySaleRecordService;
    @Autowired
    private IInventoryPurchaseReturnRecordService iInventoryPurchaseReturnRecordService;
    @Autowired
    private IInventoryPurchaseRecordService iInventoryPurchaseRecordService;
    @Autowired
    private IInventoryDelRecordService iInventoryDelRecordService;
    @Autowired
    private IInventoryCheckRecordService iInventoryCheckRecordService;
    @Autowired
    private IInventoryChangeRecordService iInventoryChangeRecordService;
    @Autowired
    private IFinancialReconcileRecordService iFinancialReconcileRecordService;
    @Autowired
    private IFinancialApplyRecordService iFinancialApplyRecordService;
    @Autowired
    private YbParamBuilderUtil ybUtil;
    @Autowired
    private IMedicationDispenseService medicationDispenseService;
    @Autowired
    private IDeviceDispenseService deviceDispenseService;
    @Autowired
    private IServiceRequestService serviceRequestService;

    /**
     * 【9001】
     *
     * @param userId 就诊id
     * @param mac 加密后的mac地址
     * @return 结果
     */
    public R<?> sign(String userId, String mac, String ip) {
        Sign signParam = ybBaseService.getSignParam(userId, mac, ip);
        Sign9001Result signResult = ybHttpService.sign(signParam);
        if (signResult != null) {
            ybBaseService.saveSign(signParam, signResult);
            return R.ok(signResult);
        }
        return R.fail();
    }

    /**
     * 【1101】
     *
     * @return 结果
     */
    public R<?> getPerInfo(String certType, String certNo) {
        Info1101ReadcardParam readcard = ybBaseService.getReadCard(certType, certNo);
        Info1101Output perInfo = ybHttpService.getPerInfo(readcard);
        if (perInfo != null) {
            ybBaseService.saveReadcardAndPerinfo(readcard, perInfo);
        }
        return R.ok(perInfo);
    }

    /**
     * 【2201】 为了慢特病自动挂号封装
     * 
     * @param encounterId 就诊id
     * @return 结果
     */
    public ClinicReg2201Output createRegWithMedType(YbMdtrtCertType ybMdtrtCertTypeLong, String busiCardInfo,
        Long encounterId, YbMedType medType) {
        ClinicReg reg = ybBaseService.getReg(ybMdtrtCertTypeLong, busiCardInfo, encounterId, medType,
            SecurityUtils.getLoginUser().getTenantId());
        reg.setMedType(medType.getValue());
        ClinicReg2201Output regResult = ybHttpService.reg(reg);
        if (regResult != null) {
            ybBaseService.saveReg(regResult);
        }
        return regResult;
    }

    /**
     * 【2202】
     *
     * @param cancelRegPaymentDto 就诊id
     * @return 结果
     */
    public R<?> cancelReg(CancelRegPaymentModel cancelRegPaymentDto) {

        Integer tenantId = SecurityUtils.getLoginUser().getTenantId();

        PaymentReconciliation paymentReconciliation =
            iPaymentReconciliationService.getById(cancelRegPaymentDto.getId());

        if (paymentReconciliation == null) {
            throw new ServiceException("未查询到付款信息");
        }
        if (paymentReconciliation.getYbSettleIds() == null) {
            throw new ServiceException("未查询到医保付款信息");
        }

        String ybSettleId = paymentReconciliation.getYbSettleIds().split(",")[0];// 理论上挂号的这个只有一个参数，没有逗号
        // 医保退费
        Clinic2208UnSetlInfoParam clinicOrder2208 = ybBaseService.getClinicOrder2208(tenantId, ybSettleId);
        if (clinicOrder2208 == null) {
            throw new ServiceException("未查询到医保付款信息");
        }
        Clinic2208UnSetlInfoOutput clinic2208UnSetlInfoOutput = ybHttpService.unSettle(clinicOrder2208);
        if (clinic2208UnSetlInfoOutput == null) {
            throw new ServiceException("未查询到医保返回信息");
        }

        cancelRegPaymentDto.setSetlId(clinic2208UnSetlInfoOutput.getSetlId());
        ybBaseService.saveUnSettleRecord(ybSettleId, clinicOrder2208, clinic2208UnSetlInfoOutput);

        ClinicReg reg = ybBaseService.getUnReg(cancelRegPaymentDto.getEncounterId(), tenantId);

        ClinicReg2201Output regResult = ybHttpService.cancelReg(reg);
        if (regResult != null) {
            ybBaseService.saveUnReg(regResult);
        }
        return R.ok(regResult);
    }

    /**
     * 【2202】
     *
     * @param clinicRegId 就诊id
     * @return 结果
     */
    public R<?> cancelRegById(Long clinicRegId) {
        ClinicReg clinicReg = iRegService.getById(clinicRegId);
        ClinicReg2201Output regResult = ybHttpService.cancelReg(clinicReg);
        if (regResult != null) {
            ybBaseService.saveUnReg(regResult);
            return R.ok(regResult);
        }
        return R.fail(regResult, "医保退号失败");
    }

    /**
     * 【2206】2203-2206
     *
     * @param cancelRegPaymentModel 挂号信息
     * @return 结果
     */
    public Clinic2206OrderOutput getPreSettleInfo(OutpatientRegistrationModel cancelRegPaymentModel, Patient patient,
        Organization organization, Practitioner doctor) {
        if (patient == null || organization == null || doctor == null) {
            throw new ServiceException("未查询到相关信息");
        }

        InfoPerson perinfo = iPerinfoService.getOne(new LambdaQueryWrapper<InfoPerson>()
            .eq(InfoPerson::getCertno, patient.getIdCard()).eq(InfoPerson::getTenantId, patient.getTenantId())
            .orderByDesc(InfoPerson::getCreateTime).last(YbCommonConstants.sqlConst.LIMIT1));
        if (perinfo == null) {
            throw new ServiceException("未查询到身份信息");
        }

        YbMdtrtCertType mdtrtCertType = YbMdtrtCertType.getByValue(cancelRegPaymentModel.getMdtrtCertType());
        if (mdtrtCertType == null) {
            throw new ServiceException("传参异常，未查询到就诊凭证类型");
        }

        // 拼接医保挂号参数
        ClinicReg reg = new ClinicReg();
        reg.setPsnNo(perinfo.getPsnNo()).setInsutype(perinfo.getInsutype()).setBegntime(new Date())
            .setMdtrtCertType(mdtrtCertType.getValue()).setMdtrtCertNo(cancelRegPaymentModel.getBusiCardInfo())
            .setIptOtpNo(assignSeqUtil.getSeq(AssignSeqEnum.ENCOUNTER_NUM.getPrefix(), 8)).setAtddrNo(doctor.getYbNo())
            .setDrName(doctor.getName()).setDeptCode(organization.getYbNo()).setDeptName(organization.getName())
            .setCaty(organization.getCaty() == null ? organization.getYbNo() : organization.getCaty())
            .setInsuplcAdmdvs(perinfo.getInsuplcAdmdvs()).setAtddrNo(doctor.getYbNo()).setPsnType("11")
            .setPsnCertType("01").setCertno(perinfo.getCertno()).setPsnName(perinfo.getPsnName());

        // reg.setAtddrNo("D220172023129").setDrName("郭建林").setCaty("B19").setDeptCode("B19");

        // 发送医保挂号请求
        ClinicReg2201Output clinicReg2201Output = ybHttpService.reg(reg);
        if (clinicReg2201Output == null) {
            throw new ServiceException("未接受到医保挂号接口返回参数");
        }
        clinicReg2201Output.setMedType(YbMedType.GENERAL_OUTPATIENT.getValue());
        // 保存挂号数据
        ybBaseService.saveReg(clinicReg2201Output);

        // 查询医疗类型
        YbMedType medType = YbMedType.GENERAL_OUTPATIENT;

        List<Clinic2203DiseInfoParam> diseinfos = new ArrayList<>();

        // 拼接2203参数
        Clinic2203DiseInfoParam diseinfo = new Clinic2203DiseInfoParam();
        diseinfo.setDiagDept(organization.getYbNo()).setDiseDorName(doctor.getName()).setDiseDorNo(doctor.getYbNo())
            .setDiagTime(new Date()).setValiFlag(Whether.YES.getValue().toString()).setDiagType("1").setDiagSrtNo("1")
            .setDiagCode("Z00.001").setDiagName("健康查体").setMdtrtId(clinicReg2201Output.getMdtrtId());
        // .setDiseDorNo("D220172023129").setDiseDorName("郭建林").setDiagDept("B19");

        diseinfos.add(diseinfo);

        Clinic2203MedicalParam medical2203Param = new Clinic2203MedicalParam();
        medical2203Param.setMdtrtId(clinicReg2201Output.getMdtrtId()).setPsnNo(reg.getPsnNo())
            .setBegntime(reg.getBegntime()).setMedType(medType.getValue()).setMainCondDscr("")
            .setInsuplcAdmdvs(perinfo.getInsuplcAdmdvs()).setDiseCodg("Z00.001").setDiseName("健康查体")
            .setDiseinfoList(diseinfos).setInsuplcAdmdvs(perinfo.getInsuplcAdmdvs());

        // 2203接口上传就诊信息
        ybHttpService.upload2203Record(medical2203Param);

        YbPsnSetlWay finCategory = YbPsnSetlWay.getByValue(cancelRegPaymentModel.getPsnSetlWay());
        if (finCategory == null) {
            throw new ServiceException("请选择收费方式");
        }

        // 拼接2204参数
        Clinic2204OrderParam clinic2204OrderParam = new Clinic2204OrderParam();
        // 排番
        clinic2204OrderParam.setChrgBchno(AssignSeqEnum.YB_CLINIC_ORDER.getPrefix() + new Date().getTime());// 医保FeedetlSn字段最大长度30，前期设置20也被check了，故此怀疑是加密后不可超过30
        List<Clinic2204FeeDetailParam> clincFeedetailList = new ArrayList<>();
        Clinic2204FeeDetailParam clinicFeedetail = new Clinic2204FeeDetailParam();
        BigDecimal totalPrice = cancelRegPaymentModel.getTotalPrice();
        if (totalPrice == null) {
            throw new ServiceException("未查询到挂号费用金额");
        }
        // 外购处方标志（文档上没有详细介绍） 2025/04/14经确认，暂定非处方流转传0，处方流转传1
        clinicFeedetail.setMedListCodg("001101000010000-100000002");
        clinicFeedetail.setFeedetlSn(clinic2204OrderParam.getChrgBchno()).setMdtrtId(clinicReg2201Output.getMdtrtId())
            .setPsnNo(reg.getPsnNo()).setChrgBchno(clinic2204OrderParam.getChrgBchno()).setFeeOcurTime(new Date())
            .setMedinsListCodg("1922546598919725058").setDetItemFeeSumamt(totalPrice.doubleValue()).setCnt(1.0)
            .setPric(totalPrice.doubleValue()).setInsuplcAdmdvs(perinfo.getInsuplcAdmdvs())
            .setBilgDeptCodg(organization.getYbNo()).setBilgDeptName(organization.getName())
            .setBilgDrCodg(doctor.getYbNo())
            // 默认无需审批
            .setBilgDrName(doctor.getName()).setHospApprFlag(YbHospApprFlag.NO_APPROVAL_REQUIRED.getValue())
            .setRxCircFlag("0").setBilgDrCodg(doctor.getYbNo());
        // .setBilgDrName("郭建林").setBilgDrCodg("D220172023129")
        // .setBilgDeptCodg("B19").setBilgDeptName(organization.getName());

        clincFeedetailList.add(clinicFeedetail);
        clinic2204OrderParam.setFeedetail(clincFeedetailList).setInsuplcAdmdvs(perinfo.getInsuplcAdmdvs());

        // 接收2204接口返回参数
        Clinic2204OrderResult clinic2204OrderResult = ybHttpService.upload2204Record(clinic2204OrderParam);
        if (clinic2204OrderResult == null) {
            throw new ServiceException("未接收2204接口返回参数");
        }

        // 个人结算方式
        YbPsnSetlWay setlWay = YbPsnSetlWay.PSN_SETLWAY01;
        // 拼接2206预结算参数
        Clinic2206OrderParam clinicOrder = new Clinic2206OrderParam();
        clinicOrder.setPsnNo(clinicReg2201Output.getPsnNo()).setMdtrtCertType(clinicReg2201Output.getMdtrtCertType())
            .setMdtrtCertNo(clinicReg2201Output.getMdtrtCertNo()).setMedType(medType.getValue())
            .setMedfeeSumamt(totalPrice.doubleValue()).setPsnSetlway(setlWay.getValue())
            .setMdtrtId(clinicReg2201Output.getMdtrtId()).setChrgBchno(clinic2204OrderParam.getChrgBchno())
            .setAcctUsedFlag(String.valueOf(Whether.YES.getValue())).setInsutype(clinicReg2201Output.getInsutype())
            .setInsuplcAdmdvs(perinfo.getInsuplcAdmdvs());

        // 接收2206接口返回参数
        Clinic2206OrderOutput clinic2206OrderResult = ybHttpService.upload2206Record(clinicOrder);
        if (clinic2206OrderResult == null) {
            throw new ServiceException("未接收2206接口返回参数");
        }
        // 保存预结算参数
        ybBaseService.saveClinicOrder2206Param(clinicOrder, clinic2206OrderResult, medical2203Param,
            clinic2204OrderParam, clinic2204OrderResult);
        // 缓存预结算结果
        redisCache.setCacheObject("PRE-SETTLE:PRE_SETTLE_:" + clinic2204OrderParam.getChrgBchno(),
            clinic2206OrderResult, 1800, TimeUnit.SECONDS);
        clinic2206OrderResult.setChrgBchno(clinic2204OrderParam.getChrgBchno()).setBusNo(reg.getIptOtpNo());
        // 返回预结算结果
        return clinic2206OrderResult;
    }

    public Clinic2206OrderOutput preSettle(Long encounterId, YbMdtrtCertType ybMdtrtCertType, String busiCardInfo,
        ClinicReg2201Output reg2201Output, String payWay, List<Long> chargeItemIds) {
        YbPsnSetlWay ybPsnSetlWay = YbPsnSetlWay.getByValue(payWay);
        if (ybPsnSetlWay == null) {
            ybPsnSetlWay = YbPsnSetlWay.PSN_SETLWAY01;
        }

        Integer tenantId = SecurityUtils.getLoginUser().getTenantId();
        // 获取2203接口和2204接口参数
        Clinic2203MedicalParam clinic2203MedicalParam =
            ybBaseService.getClinicMedical2203Param(encounterId, tenantId, reg2201Output);
        if (clinic2203MedicalParam == null) {
            throw new ServiceException("未生成2203接口参数");
        }

        Result result = ybHttpService.upload2203Record(clinic2203MedicalParam);

        Clinic2204OrderParam clinic2204OrderParam =
            ybBaseService.getClinicOrder2204Param(tenantId, chargeItemIds, reg2201Output);
        if (clinic2204OrderParam == null) {
            throw new ServiceException("未生成2204接口参数");
        }

        // 接收2204接口返回参数
        Clinic2204OrderResult clinic2204OrderResult = ybHttpService.upload2204Record(clinic2204OrderParam);
        if (clinic2204OrderResult == null) {
            throw new ServiceException("未接收2204接口参数");
        }
        // 2204上报费用明细金额总数在2206中使用
        BigDecimal sum = BigDecimal.ZERO;
        for (Clinic2204FeeDetailResult clinic2204FeeDetailResult : clinic2204OrderResult.getResult()) {
            sum = sum.add(clinic2204FeeDetailResult.getDetItemFeeSumamt());
        }

        // 获取2206参数
        Clinic2206OrderParam clinic2206OrderParam = ybBaseService.getClinicOrder2206Param(sum, reg2201Output,
            clinic2204OrderParam.getChrgBchno(), busiCardInfo);
        if (clinic2206OrderParam == null) {
            throw new ServiceException("未生成2206接口参数");
        }
        // 接收2206接口返回参数
        Clinic2206OrderOutput clinic2206OrderResult = ybHttpService.upload2206Record(clinic2206OrderParam);
        if (clinic2206OrderResult == null) {
            throw new ServiceException("未接收2206接口参数");
        }
        // 保存预结算参数
        ybBaseService.saveClinicOrder2206Param(clinic2206OrderParam, clinic2206OrderResult, clinic2203MedicalParam,
            clinic2204OrderParam, clinic2204OrderResult);
        // 缓存预结算结果
        // redisCache.setCacheObject("PRE-SETTLE:PRE_SETTLE_:" + clinic2206OrderParam.getChrgBchno(),
        // clinic2206OrderResult, 1800, TimeUnit.SECONDS);
        clinic2206OrderResult.setChrgBchno(clinic2206OrderParam.getChrgBchno());
        return clinic2206OrderResult;
    }

    /**
     * 【2207】
     *
     * @param paymentNo 统一收费业务流水号
     * @param encounterId 就诊id
     * @param chrgBchno 付款批次号
     * @param payWay 支付方式
     * @return 结果
     */
    public Clinic2207OrderModel settle(String paymentNo, Long encounterId, String chrgBchno, String payWay) {
        Integer tenantId = SecurityUtils.getLoginUser().getTenantId();
        Clinic2207OrderParam clinicOrder2207 =
            ybBaseService.getClinicOrder2207(encounterId, tenantId, payWay, chrgBchno, null);
        if (clinicOrder2207 == null) {
            throw new ServiceException("未生成2207接口参数");
        }
        Clinic2207OrderModel clinic2207OrderResult = ybHttpService.settle(clinicOrder2207);
        if (clinic2207OrderResult == null) {
            throw new ServiceException("未接收2207接口参数");
        }
        // 保存数据
        ybBaseService.saveClinic2207OrderResult(paymentNo, clinicOrder2207, clinic2207OrderResult);
        return clinic2207OrderResult;
    }

    /**
     * 【2208】
     *
     * @param settleId 付款id
     * @return 结果
     */
    public R<?> unSettle(String settleId) {
        Integer tenantId = SecurityUtils.getLoginUser().getTenantId();
        Clinic2208UnSetlInfoParam clinicOrder2208 = ybBaseService.getClinicOrder2208(tenantId, settleId);
        // clinicOrder2208.setMdtrtId("220000202505161931470129200037").setSetlId("220000202505161932020113002625");
        if (clinicOrder2208 == null) {
            throw new ServiceException("未生成2208接口参数");
        }
        Clinic2208UnSetlInfoOutput clinic2208UnSetlInfoResult = ybHttpService.unSettle(clinicOrder2208);
        // 业务表中进行操作
        ybBaseService.saveUnSettleRecord(settleId, clinicOrder2208, clinic2208UnSetlInfoResult);
        return R.ok(clinic2208UnSetlInfoResult);
    }

    /**
     * 【3301】目录对照
     *
     * @param tableName 表名
     * @param id 主键id
     * @return 结果
     */
    public R<?> directoryCheck(String tableName, Long id) {

        MedicalDirectory3302Param medicalDirectory3302Param = ybBaseService.getMedicalDirectory3302Param(tableName, id);
        Result result3302Info = ybHttpService.directoryUnCheck(medicalDirectory3302Param);
        if (result3302Info.getCode().equals(CommonConstant.SC_OK_200)) {
            ybBaseService.saveDirectoryHistory(tableName, id, 3302, medicalDirectory3302Param);
        } else {
            log.error("目录对照前，撤销对照失败");
        }
        MedicalDirectory3301Param medicalDirectory3301Param = ybBaseService.getMedicalDirectory3301Param(tableName, id);
        ArrayList<MedicalDirectory3301Param> list = new ArrayList<>();
        list.add(medicalDirectory3301Param);
        MedicalDirectory3301ListParam medicalDirectory3301ListParam = new MedicalDirectory3301ListParam();
        medicalDirectory3301ListParam.setData(list);
        Result resultInfo = ybHttpService.directoryCheck(medicalDirectory3301ListParam);
        if (resultInfo.getCode().equals(CommonConstant.SC_OK_200)) {
            ybBaseService.saveDirectoryHistory(tableName, id, 3301, medicalDirectory3301Param);
            return R.ok("对照成功");
        }
        throw new ServiceException("药品对照失败");
    }

    /**
     * 【3302】目录对照撤销
     *
     * @param tableName 就诊id
     * @param id 付款id
     * @return 结果
     */
    public R<?> directoryUnCheck(String tableName, Long id) {

        MedicalDirectory3302Param medicalDirectory3302Param = ybBaseService.getMedicalDirectory3302Param(tableName, id);
        Result resultInfo = ybHttpService.directoryUnCheck(medicalDirectory3302Param);
        if (resultInfo.getCode().equals(CommonConstant.SC_OK_200)) {
            ybBaseService.saveDirectoryHistory(tableName, id, 3302, medicalDirectory3302Param);
            return R.ok("撤销成功");
        }
        return R.fail("撤销失败");
    }

    /**
     * 【3201】对总账
     *
     * @param settlement3201WebParam 3201入参条件
     * @return 结果
     */
    public R<?> reconcileGeneralLedger(Settlement3201WebParam settlement3201WebParam) {

        Financial3201Param financial3201Param = ybBaseService.getFinancial3201Param(settlement3201WebParam);
        if (financial3201Param == null) {
            throw new ServiceException("未生成3201参数");
        }
        Result result = ybHttpService.reconcileGeneralLedger(financial3201Param);
        if (result == null) {
            throw new ServiceException("未接收3201医保返回参数");
        }
        //ybBaseService.saveReconcileGeneralLedger(result);
        return R.ok();
    }

    /**
     * 【3202】对明细账
     *
     * @param settlement3202WebParam 3202入参条件
     * @return 结果
     */
    public R<?> reconcileGeneralLedgerDetail(Settlement3202WebParam settlement3202WebParam) {

        FinancialSettlement3202Param financial3202Param =
            ybBaseService.getFinancialSettlement3202Param(settlement3202WebParam);
        FinancialSettlement3202Result resultInfo = ybHttpService.reconcileGeneralLedgerDetail(financial3202Param);

        return R.ok(resultInfo);
    }

    /**
     * 【3209A】第三方数据查询
     *
     * @param settlement3209AWebParam 3209A入参条件
     * @return 结果
     */
    public R<?> threePartSearch(Settlement3209AWebParam settlement3209AWebParam) {

        FinancialSettlement3209AParam financialSettlement3209AParam =
            ybBaseService.getFinancialSettlement3209AParam(settlement3209AWebParam);
        List<FinancialSettlement3209AResult> list = ybHttpService.threePartSearch(financialSettlement3209AParam);

        return R.ok();
    }

    /**
     * 【3203A】清算申请(吉林省)
     *
     * @param financial3203AWebParam 3203A入参条件
     * @return 结果
     */
    public R<?> applyFinancialClearing(Financial3203AWebParam financial3203AWebParam) {

        Financial3203AParam financial3203AParam = ybBaseService.getFinancial3203AParam(financial3203AWebParam);
        String s = ybHttpService.applyFinancialClearing(financial3203AParam);
        ybBaseService.save3203AFinancialClearingApplycation(financial3203AParam, s);
        return R.ok(s);
    }

    /**
     * 【3204A】清算申请撤销(吉林省)
     *
     * @param clrAppyEvtId 3203A入参条件
     * @return 结果
     */
    public R<?> cancelFinancialClearing(String clrAppyEvtId, String clrOptins) {

        Financial3204Param financial3204Param = new Financial3204Param();
        financial3204Param.setClrOptins(clrOptins).setClrAppyEvtId(clrAppyEvtId);
        Result resultInfo = ybHttpService.cancelFinancialClearing(financial3204Param);
        if (resultInfo.getCode().equals(CommonConstant.SC_OK_200)) {
            ybBaseService.save3204AFinancialClearingApplycation(clrAppyEvtId, clrOptins);
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 【3205A】清算申请状态查询
     *
     * @param clearing3205AWebParma 3205A入参条件
     * @return 结果
     */
    public R<?> getFinancialClearingStatus(Clearing3205AWebParma clearing3205AWebParma) {

        Clearing3205AParma clearing3205AParma = ybBaseService.getClearing3205AParma(clearing3205AWebParma);
        Clearing3205AResult clearing3205AResult = ybHttpService.getFinancialClearingStatus(clearing3205AParma);

        return R.ok(clearing3205AResult);
    }

    /**
     * 【3501】商品盘存上传
     *
     * @param medicalInventory3501Param 供应申请详细信息
     * @param stocktakingTime 盘存时间
     * @return 结果
     */
    public R<?> uploadInventoryCount(MedicalInventory3501Param medicalInventory3501Param, Date stocktakingTime) {
        Result resultInfo = ybHttpService.uploadInventoryCount(medicalInventory3501Param);
        if (resultInfo.getCode().equals(CommonConstant.SC_OK_200)) {
            ybBaseService.saveInventoryCheckRecord(medicalInventory3501Param, resultInfo.getMessage());
            return R.ok();
        }
        ybBaseService.saveInventoryCheckRecord(medicalInventory3501Param, resultInfo.getMessage());
        return R.fail();
    }

    /**
     * 【3502】商品信息变更
     *
     * @param medicalInventory3502Param 供应申请详细信息
     * @return 结果
     */
    public R<?> updateInventoryCount(MedicalInventory3502Param medicalInventory3502Param) {
        Result resultInfo = ybHttpService.updateInventoryCount(medicalInventory3502Param);
        if (resultInfo.getCode().equals(CommonConstant.SC_OK_200)) {
            ybBaseService.saveInventoryChangeRecord(medicalInventory3502Param, resultInfo.getMessage());
            return R.ok();
        }
        ybBaseService.saveInventoryChangeRecord(medicalInventory3502Param, resultInfo.getMessage());
        return R.fail();
    }

    /**
     * 【3503】商品采购
     *
     * @param medical3503Param 供应申请详细信息
     * @return 结果
     */
    public R<?> procurement(Medical3503Param medical3503Param) {
        Result resultInfo = ybHttpService.procurement(medical3503Param);
        if (resultInfo.getCode().equals(CommonConstant.SC_OK_200)) {
            ybBaseService.saveInventoryPurchaseRecord(medical3503Param, resultInfo.getMessage());
            return R.ok();
        }
        ybBaseService.saveInventoryPurchaseRecord(medical3503Param, resultInfo.getMessage());
        return R.fail();
    }

    /**
     * 【3504】商品采购退货
     *
     * @param medicalPurchase3504Param 供应申请详细信息
     * @return 结果
     */
    public R<?> cancelProcurement(MedicalPurchase3504Param medicalPurchase3504Param) {
        Result resultInfo = ybHttpService.cancelProcurement(medicalPurchase3504Param);
        if (resultInfo.getCode().equals(CommonConstant.SC_OK_200)) {
            ybBaseService.saveInventoryPurchaseReturnRecord(medicalPurchase3504Param, resultInfo.getMessage());
            return R.ok();
        }
        ybBaseService.saveInventoryPurchaseReturnRecord(medicalPurchase3504Param, resultInfo.getMessage());
        return R.fail();
    }

    /**
     * 【3505】商品销售
     *
     * @param medical3505Param 发药详细信息
     * @return 结果
     */
    public R<?> merchandise(Medical3505Param medical3505Param) {

        Result result = ybHttpService.merchandise(medical3505Param);
        Medical3505Result medical3505Result =
            JSON.parseObject(JSON.toJSONString(result.getResult()), Medical3505Result.class);
        if ("1".equals(medical3505Result.getRetRslt())) {
            ybBaseService.saveInventorySaleRecord(medical3505Param, medical3505Result.getMsgRslt());
            return R.ok();
        }
        ybBaseService.saveInventorySaleRecord(medical3505Param, medical3505Result.getMsgRslt());
        return R.fail();
    }

    /**
     * 【3506】商品销售退货
     *
     * @param medical3506Param 退货详细信息
     * @return 结果
     */
    public R<?> cancelMerchandise(Medical3506Param medical3506Param) {
        Result result = ybHttpService.cancelMerchandise(medical3506Param);
        Medical3505Result medical3505Result =
            JSON.parseObject(JSON.toJSONString(result.getResult()), Medical3505Result.class);
        if ("1".equals(medical3505Result.getRetRslt())) {
            ybBaseService.saveInventorySaleReturnRecord(medical3506Param, medical3505Result.getMsgRslt());
            return R.ok();
        }
        ybBaseService.saveInventorySaleReturnRecord(medical3506Param, medical3505Result.getMsgRslt());
        return R.fail();
    }

    /**
     * 【3507】商品信息删除
     *
     * @param id 供应申请id
     * @return 结果
     */
    public R<?> deleteGoodsInfo(Long id, String ListType) {
        Medical3507Param medical3507Param = ybBaseService.getMedical3507Param(id, ListType);
        Result resultBody = ybHttpService.deleteGoodsInfo(medical3507Param);
        if (resultBody.getCode().equals(CommonConstant.SC_OK_200)) {
            ybBaseService.saveInventoryDelRecord(medical3507Param);
            return R.ok();
        }
        return R.fail();
    }

    public Clinic2207OrderModel settle(String chrgBchno, String busiCardInfo, YbMdtrtCertType ybMdtrtCertType,
        Integer minpacuntDrugTracCnt, Integer mcsTracCnt) {
        // 分别查询2206和2201记录的参数信息
        ClinicPreSettle clinicPreSettle = iClinicPreSettleService
            .getOne(new LambdaQueryWrapper<ClinicPreSettle>().eq(ClinicPreSettle::getChrgBchno, chrgBchno));
        if (clinicPreSettle == null) {
            throw new ServiceException("为查询到预结算信息");
        }
        ClinicReg clinicReg = iRegService
            .getOne(new LambdaQueryWrapper<ClinicReg>().eq(ClinicReg::getMdtrtId, clinicPreSettle.getMdtrtId()));
        if (clinicPreSettle == null) {
            throw new ServiceException("为查询到挂号信息");
        }
        Clinic2206OrderOutput clinic2206OrderResult =
            JSONObject.parseObject(clinicPreSettle.getResult2206(), Clinic2206OrderOutput.class);
        // 组装2207接口参数
        Clinic2207OrderParam clinicOrder = new Clinic2207OrderParam();
        clinicOrder.setPsnNo(clinicPreSettle.getPsnNo()).setMdtrtCertType(ybMdtrtCertType.getValue())
            .setMdtrtCertNo(busiCardInfo).setMedType(clinic2206OrderResult.getMedType())
            .setMedfeeSumamt(clinic2206OrderResult.getMedfeeSumamt().doubleValue()).setPsnSetlway("01")
            .setMdtrtId(clinic2206OrderResult.getMdtrtId()).setChrgBchno(chrgBchno)
            .setAcctUsedFlag(Whether.YES.getValue().toString()).setInsutype(clinic2206OrderResult.getInsutype())
            .setFulamtOwnpayAmt(clinic2206OrderResult.getFulamtOwnpayAmt().doubleValue())
            .setOverlmtSelfpay(clinic2206OrderResult.getOverlmtSelfpay().doubleValue())
            .setPreselfpayAmt(clinic2206OrderResult.getPreselfpayAmt().doubleValue())
            .setInscpScpAmt(clinic2206OrderResult.getInscpScpAmt().doubleValue())
            .setInsuplcAdmdvs(clinicReg.getInsuplcAdmdvs()).setMinpacuntDrugTracCnt(minpacuntDrugTracCnt)
            .setMcsTracCnt(mcsTracCnt);
        // 发送http请求给app
        Clinic2207OrderModel settle = ybHttpService.settle(clinicOrder);
        if (settle == null) {
            throw new ServiceException("未接收2207接口参数");
        }
        // 保存记录通信记录
        ybBaseService.saveClinic2207Record(clinicOrder, settle);
        return settle.setChrgBchno(chrgBchno);
    }

    public PaymentResult settle(String chrgBchno) {

        ClinicPreSettle clinicPreSettle = iClinicPreSettleService
            .getOne(new LambdaQueryWrapper<ClinicPreSettle>().eq(ClinicPreSettle::getChrgBchno, chrgBchno));

        if (clinicPreSettle == null) {
            throw new ServiceException("为查询到预结算信息");
        }
        ClinicReg clinicReg = iRegService
            .getOne(new LambdaQueryWrapper<ClinicReg>().eq(ClinicReg::getMdtrtId, clinicPreSettle.getMdtrtId()));

        if (clinicPreSettle == null) {
            throw new ServiceException("为查询到挂号信息");
        }

        Clinic2206OrderOutput clinic2206OrderResult =
            JSONObject.parseObject(clinicPreSettle.getResult2206(), Clinic2206OrderOutput.class);

        Clinic2207OrderParam clinicOrder = new Clinic2207OrderParam();
        clinicOrder.setPsnNo(clinicPreSettle.getPsnNo()).setMdtrtCertType(clinic2206OrderResult.getMdtrtCertType())
            // todo：sjq 医疗费用总额需要用ChargeItem的和
            .setMdtrtCertNo(clinic2206OrderResult.getMdtrtCertNo()).setMedType(clinic2206OrderResult.getMedType())
            .setMedfeeSumamt(clinic2206OrderResult.getMedfeeSumamt().doubleValue()).setPsnSetlway("01")
            .setMdtrtId(clinic2206OrderResult.getMdtrtId()).setChrgBchno(chrgBchno)
            .setAcctUsedFlag(Whether.YES.getValue().toString()).setInsutype(clinic2206OrderResult.getInsutype())
            .setFulamtOwnpayAmt(clinic2206OrderResult.getFulamtOwnpayAmt().doubleValue())
            .setOverlmtSelfpay(clinic2206OrderResult.getOverlmtSelfpay().doubleValue())
            .setPreselfpayAmt(clinic2206OrderResult.getPreselfpayAmt().doubleValue())
            .setInscpScpAmt(clinic2206OrderResult.getInscpScpAmt().doubleValue())
            .setInsuplcAdmdvs(clinicReg.getInsuplcAdmdvs());

        Clinic2207OrderModel settle = ybHttpService.settle(clinicOrder);

        if (settle == null) {
            throw new ServiceException("未接收2207接口参数");
        }
        // 统一生成业务流水
        String paymentNo = assignSeqUtil.getSeqByDay(AssignSeqEnum.PAYMENT_NO.getPrefix(), 20);
        // 业务表中进行操作
        ybBaseService.saveClinic2207Record(paymentNo, clinicOrder, settle);
        PaymentResult paymentResult = new com.openhis.financial.model.PaymentResult();
        BeanUtils.copyProperties(settle, paymentResult);
        paymentResult.setPaymentNo(paymentNo);
        return paymentResult;
    }

    /**
     * 医保退号
     *
     * @param byId 就诊实体
     * @param account 账户实体
     */
    public void cancelReg(Encounter byId, Account account) {
        PaymentReconciliation paymentReconciliation = iPaymentReconciliationService.getOne(
            new LambdaQueryWrapper<PaymentReconciliation>().eq(PaymentReconciliation::getPatientId, byId.getPatientId())
                .eq(PaymentReconciliation::getEncounterId, byId.getId()));
        if (paymentReconciliation == null) {
            throw new ServiceException("未查询到付款信息");
        }
        String ybSettleIds = paymentReconciliation.getYbSettleIds();
        if (ybSettleIds.contains(",")) {
            throw new ServiceException("付款信息异常");
        }

        // Clinic2208UnSetlInfoParam clinicOrder2208 = ybBaseService.getClinicOrder2208(byId.getId(),
        // paymentReconciliation.getTenantId(), ybSettleIds);
        // if(clinicOrder2208==null){
        // throw new ServiceException("未生成2208接口参数");
        // }

        // Clinic2208UnSetlInfoOutput clinic2208UnSetlInfoResult = ybHttpService.unSettle(clinicOrder2208);
        // if(clinic2208UnSetlInfoResult==null){
        // throw new ServiceException("未生成2208接口返回参数");
        // }

        // ybBaseService.saveUnSettleRecord(ybSettleIds,clinicOrder2208,clinic2208UnSetlInfoResult);
        ClinicReg reg = ybBaseService.get2202Reg(byId.getId(), SecurityUtils.getLoginUser().getTenantId());
        ClinicReg2201Output regResult = ybHttpService.cancelReg(reg);
        if (regResult != null) {
            ybBaseService.saveUnReg(regResult);
        }
    }

    public ClinicReg2201Output getClinicRegByEncounterId(Long encounterId) {
        return ybBaseService.getClinicRegByEncounterId(encounterId);
    }

    public void unPreSettle(Long encounterId, String s) {
        Clinic2205OrderParam clinic2205OrderParam = ybBaseService.getClinic2205OrderParam(encounterId, s);
        if (clinic2205OrderParam == null) {
            throw new ServiceException("未生成2205接口参数");
        }
        ybHttpService.unPreSettle(clinic2205OrderParam);
    }
}
