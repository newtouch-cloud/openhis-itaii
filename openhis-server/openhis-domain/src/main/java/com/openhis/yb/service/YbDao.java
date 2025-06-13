/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.core.common.utils.DateUtils;
import com.openhis.common.enums.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.core.common.core.domain.R;
import com.core.common.exception.ServiceException;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.SecurityUtils;
import com.core.common.utils.StringUtils;
import com.core.system.service.impl.SysUserServiceImpl;
import com.openhis.administration.domain.Account;
import com.openhis.administration.domain.Encounter;
import com.openhis.administration.domain.Patient;
import com.openhis.administration.domain.Practitioner;
import com.openhis.administration.service.*;
import com.openhis.common.constant.YbCommonConstants;
import com.openhis.common.enums.ybenums.*;
import com.openhis.financial.domain.PaymentRecDetail;
import com.openhis.financial.domain.PaymentReconciliation;
import com.openhis.financial.service.IContractService;
import com.openhis.financial.service.IPaymentRecDetailService;
import com.openhis.financial.service.IPaymentReconciliationService;
import com.openhis.medication.service.IMedicationDispenseService;
import com.openhis.workflow.service.IDeviceDispenseService;
import com.openhis.workflow.service.IServiceRequestService;
import com.openhis.yb.domain.*;
import com.openhis.yb.domain.ClinicReg;
import com.openhis.yb.dto.*;
import com.openhis.yb.dto.Sign;
import com.openhis.yb.mapper.MedicalInsuranceMapper;
import com.openhis.yb.model.Clinic2207OrderModel;
import com.openhis.yb.model.Clinic2207OrderParam;
import com.openhis.yb.service.impl.DirectoryCheckRecordServiceImpl;
import com.openhis.yb.util.YbParamBuilderUtil;

/**
 * 医保表的增删改查接口
 *
 * @author SunJQ
 * @date 2025-04-11
 */
@Service
public class YbDao {
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
    private AssignSeqUtil assignSeqUtil;
    @Autowired
    private IInvoiceService invoiceService;
    @Autowired
    private IPatientService iPatientService;
    @Autowired
    private IContractService iContractService;
    @Autowired
    private MedicalInsuranceMapper medicalInsuranceMapper;

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
     * 获取读卡信息
     *
     * @return 【1101】入参
     */

    public Info1101ReadcardParam getReadCard(String certType, String certNo) {
        return ybUtil.getReadCard(certType, certNo);
    }

    /**
     * 保存数据
     * 
     * @param readcard 读卡信息
     * @param perInfo 身份信息
     */

    public void saveReadcardAndPerinfo(Info1101ReadcardParam readcard, Info1101Output perInfo) {
        InfoPerson infoPerson = new InfoPerson();
        BeanUtils.copyProperties(readcard, infoPerson);
        BeanUtils.copyProperties(perInfo, infoPerson);
        infoPerson.setResult1101(JSON.toJSONString(perInfo)).setParam1101(JSON.toJSONString(readcard));
        iPerinfoService.save(infoPerson);
    }

    /**
     * 【2201】医保挂号
     *
     * @param encounterId 就诊id
     * @param tenantId 租户
     * @return 【2201】入参
     */

    public ClinicReg getReg(YbMdtrtCertType ybMdtrtCertTypeLong, String busiCardInfo, Long encounterId,
        YbMedType medType, Integer tenantId) {
        return ybUtil.getReg(ybMdtrtCertTypeLong, busiCardInfo, encounterId, medType, tenantId);
    }

    /**
     * 保存挂号信息
     *
     * @param regResult 2201挂号信息
     */

    public void saveReg(ClinicReg2201Output regResult) {
        ClinicReg clinicReg = new ClinicReg();
        BeanUtils.copyProperties(regResult, clinicReg);
        InfoPerson one =
            iPerinfoService.getOne(new LambdaQueryWrapper<InfoPerson>().eq(InfoPerson::getPsnNo, regResult.getPsnNo())
                .orderByDesc(InfoPerson::getCreateTime).last(YbCommonConstants.sqlConst.LIMIT1));
        clinicReg.setInsuplcAdmdvs(one.getInsuplcAdmdvs()).setDeleteFlag(DelFlag.NO.getCode());
        iRegService.save(clinicReg);
    }

    /**
     * 退号变更挂号状态
     * 
     * @param regResult
     */

    public void UpdateRegByMdtrtId(ClinicReg regResult) {
        if (StringUtils.isEmpty(regResult.getMdtrtId())) {
            throw new ServiceException("医保挂号编号为空");
        }
        ClinicReg one =
            iRegService.getOne(new LambdaQueryWrapper<ClinicReg>().eq(ClinicReg::getMdtrtId, regResult.getMdtrtId()));
        if (one == null) {
            throw new ServiceException("查询医保信息为空");
        }
        iRegService.updateStatus(one.getId(), YbCommonConstants.ClincStatusConst.CANCLE);
    }

    /**
     * 【2203】就诊信息上传
     *
     * @param encounterId 就诊id
     * @param tenantId 租户
     * @return 【2203】就诊信息
     */

    public Clinic2203MedicalParam getClinicMedical2203Param(Long encounterId, Integer tenantId,
        ClinicReg2201Output reg2201Output) {
        return ybUtil.getClinicMedical2203Param(encounterId, tenantId, reg2201Output);
    }

    /**
     * 【2204】诊断信息上传
     *
     * @param tenantId 租户id
     * @param chargeItemIds 收费项id集合
     * @return 【2204】参数
     */

    public Clinic2204OrderParam getClinicOrder2204Param(Integer tenantId, List<Long> chargeItemIds,
        ClinicReg2201Output reg2201Output) {
        return ybUtil.getClinicOrder2204Param(tenantId, chargeItemIds, reg2201Output);
    }

    /**
     * 【2206】预结算
     *
     * @param payFee 应收金额
     * @param chrgBchno 收费批次号（payment的busNo）
     * @return 【2206】预结算
     */
    public Clinic2206OrderParam getClinicOrder2206Param(BigDecimal payFee, ClinicReg2201Output reg, String chrgBchno,
        String busiCardInfo) {
        return ybUtil.getClinicOrder2206Param(payFee, reg, chrgBchno, busiCardInfo);
    }

    /**
     * 【2205】费用明细撤销
     *
     * @param encounterId 就诊id
     * @param chrgBchno 收费批次号
     * @return 【2205】参数
     */
    public Clinic2205OrderParam getClinic2205OrderParam(Long encounterId, String chrgBchno) {
        return ybUtil.getClinic2205OrderParam(encounterId, chrgBchno);
    }

    /**
     * 【2207】结算
     *
     * @param encounterId 就诊id
     * @param tenantId 租户id
     * @param psnSetlWay 支付方式
     * @param chrgBchno 收费批次号（payment的busNo）
     * @return 【2207】结算
     */
    public Clinic2207OrderParam getClinicOrder2207(Long encounterId, Integer tenantId, String psnSetlWay,
        String chrgBchno, String medType) {
        return ybUtil.getClinicOrder2207(encounterId, tenantId, psnSetlWay, chrgBchno, medType);
    }

    /**
     * 保存数据
     * 
     * @param paymentNo 首款业务流水号
     * @param clinicOrder2207 2207入参
     * @param clinic2207OrderResult 2207出参
     */

    public void saveClinic2207OrderResult(String paymentNo, Clinic2207OrderParam clinicOrder2207,
        Clinic2207OrderModel clinic2207OrderResult) {
        // 保存结算信息
        ClinicSettle clinicSettle = new ClinicSettle();
        BeanUtils.copyProperties(clinic2207OrderResult, clinicSettle);
        clinicSettle.setParam2207(JSON.toJSONString(clinicOrder2207))
            .setResult2207(JSON.toJSONString(clinic2207OrderResult));
        iClinicSettleService.save(clinicSettle);
    }

    /**
     * 保存数据
     * 
     * @param settleId 医保结算id
     * @param clinicOrder2207 2207入参
     * @param clinic2207OrderResult 2207出参
     */

    public void saveUnSettleRecord(String settleId, Clinic2208UnSetlInfoParam clinicOrder2207,
        Clinic2208UnSetlInfoOutput clinic2207OrderResult) {

        ClinicUnSettle clinicSettle = new ClinicUnSettle();
        BeanUtils.copyProperties(clinic2207OrderResult, clinicSettle);
        clinicSettle.setParam2208(JSON.toJSONString(clinicOrder2207))
            .setResult2208(JSON.toJSONString(clinic2207OrderResult)).setSettleId(settleId);
        // 保存结算信息
        iClinicUnSettleService.save(clinicSettle);
    }

    /**
     * 反结
     * 
     * @param paymentReconciliation 付款实体
     */
    private void saveUnPaymentDetail(PaymentReconciliation paymentReconciliation) {
        List<PaymentRecDetail> paymentRecDetails =
            iPaymentRecDetailService.list(new LambdaQueryWrapper<PaymentRecDetail>()
                .eq(PaymentRecDetail::getReconciliationId, paymentReconciliation.getId()));
        // 新增详情信息
        PaymentRecDetail paymentRecDetail;
        List<PaymentRecDetail> addDetailList = new ArrayList<>();
        for (PaymentRecDetail paymentDetail : paymentRecDetails) {
            paymentRecDetail = new PaymentRecDetail();
            com.core.common.utils.bean.BeanUtils.copyProperties(paymentDetail, paymentRecDetail);
            paymentRecDetail.setPredecessorId(paymentReconciliation.getId());
            paymentRecDetail.setAccountId(paymentDetail.getAccountId() == null ? 0 : paymentDetail.getAccountId());
            paymentRecDetail.setResultEnum(PaymentResult.REFUNDED.getValue());
            paymentRecDetail.setAmount(paymentRecDetail.getAmount().multiply(new BigDecimal("-1")));
            addDetailList.add(paymentRecDetail);
        }
        iPaymentRecDetailService.saveBatch(addDetailList);

        // 修改详情信息
        List<PaymentRecDetail> list = new ArrayList<>();
        for (PaymentRecDetail item : paymentRecDetails) {
            item.setResultEnum(1);
            list.add(item);
        }
        iPaymentRecDetailService.updateBatchById(list);
    }

    /**
     * 根据医保2207结果和前台参数添加结算记录
     * 
     * @param clinicOrder2207Result
     * @param payment
     * @param paymentDetails
     */
    private void saveBy2207Result(Clinic2207OrderResult clinicOrder2207Result, PaymentReconciliation payment,
        List<PaymentDetailDto> paymentDetails) {
        // 保存付款详情
        List<PaymentRecDetail> paymentRecDetails = new ArrayList<>();
        BigDecimal amount = BigDecimal.ZERO;
        String AccountEnum = "04";
        Account account = accountService.getById(payment.getAccountId());
        AccountEnum = account.getTypeCode();
        PaymentRecDetail paymentRecDetail;
        for (PaymentDetailDto paymentDetail : paymentDetails) {
            paymentRecDetail = new PaymentRecDetail();
            if (YbPayment.SELF_CASH_VX_VALUE.getValue().equals(paymentDetail.getPayEnum())) {
                paymentRecDetail.setReconciliationId(payment.getId()).setAccountCode(AccountEnum)
                    .setPayEnum(YbPayment.SELF_CASH_VX_VALUE.getValue())
                    .setPayLevelEnum(YbPayment.SELF_CASH_VX_VALUE.getLevel()).setAmount(paymentDetail.getAmount())
                    .setResultEnum(PaymentResult.PAID.getValue());
            }
            if (YbPayment.SELF_CASH_ALI_VALUE.getValue().equals(paymentDetail.getPayEnum())) {
                paymentRecDetail.setReconciliationId(payment.getId()).setAccountCode(AccountEnum)
                    .setPayEnum(YbPayment.SELF_CASH_ALI_VALUE.getValue())
                    .setPayLevelEnum(YbPayment.SELF_CASH_ALI_VALUE.getLevel()).setAmount(paymentDetail.getAmount())
                    .setResultEnum(PaymentResult.PAID.getValue());
            }
            if (YbPayment.SELF_CASH_UNION_VALUE.getValue().equals(paymentDetail.getPayEnum())) {
                paymentRecDetail.setReconciliationId(payment.getId()).setAccountCode(AccountEnum)
                    .setPayEnum(YbPayment.SELF_CASH_UNION_VALUE.getValue())
                    .setPayLevelEnum(YbPayment.SELF_CASH_UNION_VALUE.getLevel()).setAmount(paymentDetail.getAmount())
                    .setResultEnum(PaymentResult.PAID.getValue());
            }
            if (YbPayment.SELF_CASH_VALUE.getValue().equals(paymentDetail.getPayEnum())) {
                paymentRecDetail.setReconciliationId(payment.getId()).setAccountCode(AccountEnum)
                    .setPayEnum(YbPayment.SELF_CASH_VALUE.getValue())
                    .setPayLevelEnum(YbPayment.SELF_CASH_VALUE.getLevel()).setAmount(paymentDetail.getAmount())
                    .setResultEnum(PaymentResult.PAID.getValue());
            }
            paymentRecDetails.add(paymentRecDetail);
            amount = amount.add(paymentDetail.getAmount());
        }
        // 个人现金支出
        BigDecimal psnCashPay = clinicOrder2207Result.getPsnCashPay();
        if (psnCashPay.compareTo(amount) != 0) {
            throw new ServiceException("金额校验失败");
        }
        PaymentRecDetail paymentRecDetail10 = new PaymentRecDetail();
        paymentRecDetail10.setReconciliationId(payment.getId()).setAccountCode(AccountEnum)
            .setPayEnum(YbPayment.SELF_CASH_VALUE.getValue()).setPayLevelEnum(YbPayment.SELF_CASH_VALUE.getLevel())
            .setAmount(psnCashPay).setResultEnum(PaymentResult.PAID.getValue());
        paymentRecDetails.add(paymentRecDetail10);
        // 个人负担总金额
        BigDecimal psnPartAmt = clinicOrder2207Result.getPsnPartAmt();
        PaymentRecDetail paymentRecDetail1 = new PaymentRecDetail();
        paymentRecDetail1.setReconciliationId(payment.getId()).setAccountCode(AccountEnum)
            .setPayEnum(YbPayment.SELF_PAY.getValue()).setPayLevelEnum(YbPayment.SELF_PAY.getLevel())
            .setAmount(psnPartAmt).setResultEnum(PaymentResult.PAID.getValue());
        paymentRecDetails.add(paymentRecDetail1);
        // 基本医疗保险统筹基金支出
        BigDecimal hifpPay = clinicOrder2207Result.getHifpPay();
        PaymentRecDetail paymentRecDetail2 = new PaymentRecDetail();
        paymentRecDetail2.setReconciliationId(payment.getId()).setAccountCode(AccountEnum)
            .setPayEnum(YbPayment.YB_TC_FUND_AMOUNT.getValue()).setPayLevelEnum(YbPayment.YB_TC_FUND_AMOUNT.getLevel())
            .setAmount(hifpPay).setResultEnum(PaymentResult.PAID.getValue());
        paymentRecDetails.add(paymentRecDetail2);
        // 公务员医疗补助资金支出
        BigDecimal cvlservPay = clinicOrder2207Result.getCvlservPay();
        PaymentRecDetail paymentRecDetail3 = new PaymentRecDetail();
        paymentRecDetail3.setReconciliationId(payment.getId()).setAccountCode(AccountEnum)
            .setPayEnum(YbPayment.YB_BC_GWY_BZ_VALUE.getValue())
            .setPayLevelEnum(YbPayment.YB_BC_GWY_BZ_VALUE.getLevel()).setAmount(cvlservPay)
            .setResultEnum(PaymentResult.PAID.getValue());
        paymentRecDetails.add(paymentRecDetail3);
        // 企业补充医疗保险基金支出
        BigDecimal hifesPay = clinicOrder2207Result.getHifesPay();
        PaymentRecDetail paymentRecDetail4 = new PaymentRecDetail();
        paymentRecDetail4.setReconciliationId(payment.getId()).setAccountCode(AccountEnum)
            .setPayEnum(YbPayment.YB_BC_FUND_AMOUNT.getValue()).setPayLevelEnum(YbPayment.YB_BC_FUND_AMOUNT.getLevel())
            .setAmount(hifesPay).setResultEnum(PaymentResult.PAID.getValue());
        paymentRecDetails.add(paymentRecDetail4);
        // 居民大病保险资金支出
        BigDecimal hifmiPay = clinicOrder2207Result.getHifmiPay();
        PaymentRecDetail paymentRecDetail5 = new PaymentRecDetail();
        paymentRecDetail5.setReconciliationId(payment.getId()).setAccountCode(AccountEnum)
            .setPayEnum(YbPayment.YB_BC_JM_DB_VALUE.getValue()).setPayLevelEnum(YbPayment.YB_BC_JM_DB_VALUE.getLevel())
            .setAmount(hifmiPay).setResultEnum(PaymentResult.PAID.getValue());
        paymentRecDetails.add(paymentRecDetail5);
        // 职工大额医疗费用补助基金支出
        BigDecimal hifobPay = clinicOrder2207Result.getHifobPay();
        PaymentRecDetail paymentRecDetail6 = new PaymentRecDetail();
        paymentRecDetail6.setReconciliationId(payment.getId()).setAccountCode(AccountEnum)
            .setPayEnum(YbPayment.YB_BC_ZG_DE_BZ_VALUE.getValue())
            .setPayLevelEnum(YbPayment.YB_BC_ZG_DE_BZ_VALUE.getLevel()).setAmount(hifobPay)
            .setResultEnum(PaymentResult.PAID.getValue());
        paymentRecDetails.add(paymentRecDetail5);
        // 职工大额医疗费用补助基金支出
        BigDecimal mafPay = clinicOrder2207Result.getMafPay();
        PaymentRecDetail paymentRecDetail7 = new PaymentRecDetail();
        paymentRecDetail7.setReconciliationId(payment.getId()).setAccountCode(AccountEnum)
            .setPayEnum(YbPayment.YB_JZ_FUND_AMOUNT.getValue()).setPayLevelEnum(YbPayment.YB_JZ_FUND_AMOUNT.getLevel())
            .setAmount(mafPay).setResultEnum(PaymentResult.PAID.getValue());
        paymentRecDetails.add(paymentRecDetail7);
        // 其他支出
        BigDecimal othPay = clinicOrder2207Result.getOthPay();
        PaymentRecDetail paymentRecDetail8 = new PaymentRecDetail();
        paymentRecDetail8.setReconciliationId(payment.getId()).setAccountCode(AccountEnum)
            .setPayEnum(YbPayment.OTHER_PAY.getValue()).setPayLevelEnum(YbPayment.OTHER_PAY.getLevel())
            .setAmount(othPay).setResultEnum(PaymentResult.PAID.getValue());
        paymentRecDetails.add(paymentRecDetail8);
        // 基金支付总额
        BigDecimal fundPaySumamt = clinicOrder2207Result.getFundPaySumamt();
        PaymentRecDetail paymentRecDetail9 = new PaymentRecDetail();
        paymentRecDetail9.setReconciliationId(payment.getId()).setAccountCode(AccountEnum)
            .setPayEnum(YbPayment.YB_FUND_PAY.getValue()).setPayLevelEnum(YbPayment.YB_FUND_PAY.getLevel())
            .setAmount(fundPaySumamt).setResultEnum(PaymentResult.PAID.getValue());
        paymentRecDetails.add(paymentRecDetail9);
        // 个人账户支出
        BigDecimal acctPay = clinicOrder2207Result.getAcctPay();
        PaymentRecDetail paymentRecDetail11 = new PaymentRecDetail();
        paymentRecDetail11.setReconciliationId(payment.getId()).setAccountCode(AccountEnum)
            .setPayEnum(YbPayment.SELF_YB_ZH_PAY.getValue()).setPayLevelEnum(YbPayment.SELF_YB_ZH_PAY.getLevel())
            .setAmount(acctPay).setResultEnum(PaymentResult.PAID.getValue());
        paymentRecDetails.add(paymentRecDetail11);
        // 医院负担金额
        BigDecimal hospPartAmt = clinicOrder2207Result.getHospPartAmt();
        PaymentRecDetail paymentRecDetail12 = new PaymentRecDetail();
        paymentRecDetail12.setReconciliationId(payment.getId()).setAccountCode(AccountEnum)
            .setPayEnum(YbPayment.OTHER_PAY.getValue()).setPayLevelEnum(YbPayment.OTHER_PAY.getLevel())
            .setAmount(hospPartAmt).setResultEnum(PaymentResult.PAID.getValue());
        paymentRecDetails.add(paymentRecDetail12);
        // 个人账户共济支付金额
        BigDecimal acctMulaidPay = clinicOrder2207Result.getAcctMulaidPay();
        PaymentRecDetail paymentRecDetail13 = new PaymentRecDetail();
        paymentRecDetail13.setReconciliationId(payment.getId()).setAccountCode(AccountEnum)
            .setPayEnum(YbPayment.SELF_YB_ZH_GJ_VALUE.getValue())
            .setPayLevelEnum(YbPayment.SELF_YB_ZH_GJ_VALUE.getLevel()).setAmount(acctMulaidPay)
            .setResultEnum(PaymentResult.PAID.getValue());
        paymentRecDetails.add(paymentRecDetail13);
        for (Clinic2206FundPaymentResult clinic2206FundPaymentResult : clinicOrder2207Result.getSetldetail()) {
            PaymentRecDetail detail = new PaymentRecDetail();
            YbPayment ybPayment = YbPayment.getByValue(Integer.parseInt(clinic2206FundPaymentResult.getFundPayType()));
            detail.setReconciliationId(payment.getId()).setAccountCode(AccountEnum).setPayEnum(ybPayment.getValue())
                .setPayLevelEnum(ybPayment.getLevel())
                .setAmount(new BigDecimal(clinic2206FundPaymentResult.getFundPayamt().toString()))
                .setResultEnum(PaymentResult.PAID.getValue());
            paymentRecDetails.add(detail);
        }

    }

    /**
     * 根据医保2207结果和前台参数添加结算记录（弃用）该方法内赋值有错误，若启用或copy需要重新定义里面的赋值内容
     *
     * @param clinic2208UnSetlInfoResult
     * @param payment
     * @param paymentDetails
     */
    private void saveBy2208Result(Clinic2208UnSetlInfoResult clinic2208UnSetlInfoResult, PaymentReconciliation payment,
        List<PaymentDetailDto> paymentDetails) {
        // 保存付款详情 todo:医保反结是否是负数
        ArrayList<PaymentRecDetail> paymentRecDetails = new ArrayList<>();
        BigDecimal amount = BigDecimal.ZERO;
        String AccountEnum = "04";
        for (PaymentDetailDto paymentDetail : paymentDetails) {
            PaymentRecDetail paymentRecDetail = new PaymentRecDetail();
            com.core.common.utils.bean.BeanUtils.copyProperties(paymentDetail, paymentRecDetail);
            paymentRecDetail.setReconciliationId(payment.getId());
            paymentRecDetail.setPredecessorId(payment.getId());
            paymentRecDetail.setResultEnum(PaymentResult.PAID.getValue());
            paymentRecDetails.add(paymentRecDetail);
            amount = amount.add(paymentDetail.getAmount());
        }
        // 个人负担总金额
        BigDecimal psnPartAmt = clinic2208UnSetlInfoResult.getPsnPartAmt();
        PaymentRecDetail paymentRecDetail1 = new PaymentRecDetail();
        paymentRecDetail1.setReconciliationId(payment.getId()).setAccountCode(AccountEnum)
            .setPayEnum(YbPayment.SELF_PAY.getValue()).setPayLevelEnum(YbPayment.SELF_PAY.getLevel())
            .setAmount(psnPartAmt).setResultEnum(PaymentResult.PAID.getValue());
        paymentRecDetails.add(paymentRecDetail1);
        // 基本医疗保险统筹基金支出
        BigDecimal hifpPay = clinic2208UnSetlInfoResult.getHifpPay();
        PaymentRecDetail paymentRecDetail2 = new PaymentRecDetail();
        paymentRecDetail2.setReconciliationId(payment.getId()).setAccountCode(AccountEnum)
            .setPayEnum(YbPayment.YB_TC_FUND_AMOUNT.getValue()).setPayLevelEnum(YbPayment.YB_TC_FUND_AMOUNT.getLevel())
            .setAmount(hifpPay).setResultEnum(PaymentResult.PAID.getValue());
        paymentRecDetails.add(paymentRecDetail2);
        // 公务员医疗补助资金支出
        BigDecimal cvlservPay = clinic2208UnSetlInfoResult.getCvlservPay();
        PaymentRecDetail paymentRecDetail3 = new PaymentRecDetail();
        paymentRecDetail3.setReconciliationId(payment.getId()).setAccountCode(AccountEnum)
            .setPayEnum(YbPayment.YB_BC_GWY_BZ_VALUE.getValue())
            .setPayLevelEnum(YbPayment.YB_BC_GWY_BZ_VALUE.getLevel()).setAmount(cvlservPay)
            .setResultEnum(PaymentResult.PAID.getValue());
        paymentRecDetails.add(paymentRecDetail3);
        // 企业补充医疗保险基金支出
        BigDecimal hifesPay = clinic2208UnSetlInfoResult.getHifesPay();
        PaymentRecDetail paymentRecDetail4 = new PaymentRecDetail();
        paymentRecDetail4.setReconciliationId(payment.getId()).setAccountCode(AccountEnum)
            .setPayEnum(YbPayment.YB_BC_FUND_AMOUNT.getValue()).setPayLevelEnum(YbPayment.YB_BC_FUND_AMOUNT.getLevel())
            .setAmount(hifesPay).setResultEnum(PaymentResult.PAID.getValue());
        paymentRecDetails.add(paymentRecDetail4);
        // 居民大病保险资金支出
        BigDecimal hifmiPay = clinic2208UnSetlInfoResult.getHifmiPay();
        PaymentRecDetail paymentRecDetail5 = new PaymentRecDetail();
        paymentRecDetail5.setReconciliationId(payment.getId()).setAccountCode(AccountEnum)
            .setPayEnum(YbPayment.YB_BC_JM_DB_VALUE.getValue()).setPayLevelEnum(YbPayment.YB_BC_JM_DB_VALUE.getLevel())
            .setAmount(hifmiPay).setResultEnum(PaymentResult.PAID.getValue());
        paymentRecDetails.add(paymentRecDetail5);
        // 职工大额医疗费用补助基金支出
        BigDecimal hifobPay = clinic2208UnSetlInfoResult.getHifobPay();
        PaymentRecDetail paymentRecDetail6 = new PaymentRecDetail();
        paymentRecDetail6.setReconciliationId(payment.getId()).setAccountCode(AccountEnum)
            .setPayEnum(YbPayment.YB_BC_ZG_DE_BZ_VALUE.getValue())
            .setPayLevelEnum(YbPayment.YB_BC_ZG_DE_BZ_VALUE.getLevel()).setAmount(hifobPay)
            .setResultEnum(PaymentResult.PAID.getValue());
        paymentRecDetails.add(paymentRecDetail5);
        // 职工大额医疗费用补助基金支出
        BigDecimal mafPay = clinic2208UnSetlInfoResult.getMafPay();
        PaymentRecDetail paymentRecDetail7 = new PaymentRecDetail();
        paymentRecDetail7.setReconciliationId(payment.getId()).setAccountCode(AccountEnum)
            .setPayEnum(YbPayment.YB_JZ_FUND_AMOUNT.getValue()).setPayLevelEnum(YbPayment.YB_JZ_FUND_AMOUNT.getLevel())
            .setAmount(mafPay).setResultEnum(PaymentResult.PAID.getValue());
        paymentRecDetails.add(paymentRecDetail7);
        // 其他支出
        BigDecimal othPay = clinic2208UnSetlInfoResult.getOthPay();
        PaymentRecDetail paymentRecDetail8 = new PaymentRecDetail();
        paymentRecDetail8.setReconciliationId(payment.getId()).setAccountCode(AccountEnum)
            .setPayEnum(YbPayment.OTHER_PAY.getValue()).setPayLevelEnum(YbPayment.OTHER_PAY.getLevel())
            .setAmount(othPay).setResultEnum(PaymentResult.PAID.getValue());
        paymentRecDetails.add(paymentRecDetail8);
        // 基金支付总额
        BigDecimal fundPaySumamt = clinic2208UnSetlInfoResult.getFundPaySumamt();
        PaymentRecDetail paymentRecDetail9 = new PaymentRecDetail();
        paymentRecDetail9.setReconciliationId(payment.getId()).setAccountCode(AccountEnum)
            .setPayEnum(YbPayment.YB_FUND_PAY.getValue()).setPayLevelEnum(YbPayment.YB_FUND_PAY.getLevel())
            .setAmount(fundPaySumamt).setResultEnum(PaymentResult.PAID.getValue());
        paymentRecDetails.add(paymentRecDetail9);
        // 个人账户支出
        BigDecimal acctPay = clinic2208UnSetlInfoResult.getAcctPay();
        PaymentRecDetail paymentRecDetail11 = new PaymentRecDetail();
        paymentRecDetail11.setReconciliationId(payment.getId()).setAccountCode(AccountEnum)
            .setPayEnum(YbPayment.SELF_YB_ZH_PAY.getValue()).setPayLevelEnum(YbPayment.SELF_YB_ZH_PAY.getLevel())
            .setAmount(acctPay).setResultEnum(PaymentResult.PAID.getValue());
        paymentRecDetails.add(paymentRecDetail11);
        // 医院负担金额
        BigDecimal hospPartAmt = clinic2208UnSetlInfoResult.getHospPartAmt();
        PaymentRecDetail paymentRecDetail12 = new PaymentRecDetail();
        paymentRecDetail12.setReconciliationId(payment.getId()).setAccountCode(AccountEnum)
            .setPayEnum(YbPayment.OTHER_PAY.getValue()).setPayLevelEnum(YbPayment.OTHER_PAY.getLevel())
            .setAmount(hospPartAmt).setResultEnum(PaymentResult.PAID.getValue());
        paymentRecDetails.add(paymentRecDetail12);
        // 个人账户共济支付金额
        BigDecimal acctMulaidPay = clinic2208UnSetlInfoResult.getAcctMulaidPay();
        PaymentRecDetail paymentRecDetail13 = new PaymentRecDetail();
        paymentRecDetail13.setReconciliationId(payment.getId()).setAccountCode(AccountEnum)
            .setPayEnum(YbPayment.SELF_YB_ZH_GJ_VALUE.getValue())
            .setPayLevelEnum(YbPayment.SELF_YB_ZH_GJ_VALUE.getLevel()).setAmount(acctMulaidPay)
            .setResultEnum(PaymentResult.PAID.getValue());
        paymentRecDetails.add(paymentRecDetail13);
        for (Clinic2206FundPaymentResult clinic2206FundPaymentResult : clinic2208UnSetlInfoResult.getSetldetail()) {
            PaymentRecDetail detail = new PaymentRecDetail();
            YbPayment ybPayment = YbPayment.getByValue(Integer.parseInt(clinic2206FundPaymentResult.getFundPayType()));
            detail.setReconciliationId(payment.getId()).setAccountCode(AccountEnum).setPayEnum(ybPayment.getValue())
                .setPayLevelEnum(ybPayment.getLevel())
                .setAmount(new BigDecimal(clinic2206FundPaymentResult.getFundPayamt().toString()))
                .setResultEnum(PaymentResult.PAID.getValue());
            paymentRecDetails.add(detail);
        }

    }

    /**
     * 保存预结算接口的记录信息
     * 
     * @param clinic2206OrderParam 2206医保入参
     * @param clinic2206OrderResult 2206医保输出
     * @param clinic2203MedicalParam 2203医保入参
     * @param clinic2204OrderParam 2204医保入参
     * @param clinic2204OrderResult 2204医保输出
     */

    public void saveClinicOrder2206Param(Clinic2206OrderParam clinic2206OrderParam,
        Clinic2206OrderOutput clinic2206OrderResult, Clinic2203MedicalParam clinic2203MedicalParam,
        Clinic2204OrderParam clinic2204OrderParam, Clinic2204OrderResult clinic2204OrderResult) {

        ClinicPreSettle clinicPreSettle = new ClinicPreSettle();
        BeanUtils.copyProperties(clinic2204OrderParam, clinicPreSettle);
        BeanUtils.copyProperties(clinic2206OrderResult, clinicPreSettle);
        clinicPreSettle.setParam2203(JSON.toJSONString(clinic2203MedicalParam))
            .setParam2204(JSON.toJSONString(clinic2204OrderParam)).setParam2206(JSON.toJSONString(clinic2206OrderParam))
            .setResult2204(JSON.toJSONString(clinic2204OrderResult))
            .setResult2206(JSON.toJSONString(clinic2206OrderResult)).setChrgBchno(clinic2206OrderParam.getChrgBchno())
            .setMdtrtId(clinic2206OrderParam.getMdtrtId()).setPsnNo(clinic2206OrderParam.getPsnNo())
            .setDeleteFlag(DelFlag.NO.getCode());

        iClinicPreSettleService.save(clinicPreSettle);
    }

    /**
     * 保存签到信息
     *
     * @param signParam 【9001】参数
     * @param signResult 【9001】参数
     */

    public void saveSign(Sign signParam, Sign9001Result signResult) {
        com.openhis.yb.domain.Sign sign = new com.openhis.yb.domain.Sign();
        BeanUtils.copyProperties(signParam, sign);
        BeanUtils.copyProperties(signResult, sign);
        sign.setSignNo(signResult.getSignNo()).setOpterNo(signParam.getOpterNo());
        iSignService.save(sign);
    }

    /**
     * 取消挂号
     *
     * @param regResult 【2202】参数
     */

    public void saveUnReg(ClinicReg2201Output regResult) {
        ClinicUnReg clinicUnReg = new ClinicUnReg();
        BeanUtils.copyProperties(regResult, clinicUnReg);
        clinicUnReg.setUnRegTime(new Date());
        iClinicUnRegService.save(clinicUnReg);
    }

    /**
     * 【2208】医保反结算
     *
     * @param tenantId 租户id
     * @param settleId 结算id
     * @return 【2208】参数
     */

    public Clinic2208UnSetlInfoParam getClinicOrder2208(Integer tenantId, String settleId) {
        return ybUtil.getClinicOrder2208(tenantId, settleId);
    }

    /**
     * 【3301】目录对照
     * 
     * @param tableName 表名
     * @param id 主键
     * @return 【3301】入参
     */

    public MedicalDirectory3301Param getMedicalDirectory3301Param(String tableName, Long id) {
        return ybUtil.getMedicalDirectory3301Param(tableName, id);
    }

    /**
     * 【3302】目录对照撤销
     * 
     * @param tableName 表名
     * @param id 主键
     * @return
     */

    public MedicalDirectory3302Param getMedicalDirectory3302Param(String tableName, Long id) {
        return ybUtil.getMedicalDirectory3302Param(tableName, id);
    }

    /**
     * 【3201】医药机构费用结算对总账
     * 
     * @param reconciliation3201Param 参数信息
     * @return 入参
     */

    public Financial3201Param getFinancial3201Param(Settlement3201WebParam reconciliation3201Param) {
        String insuType = reconciliation3201Param.getInsuType();
        if(StringUtils.isEmpty(insuType)){
            //insuType = "310";
            //reconciliation3201Param.setInsuType("310");
            throw new ServiceException("请选择险种类型");
        }
        String clrType = reconciliation3201Param.getClrType();
        if(StringUtils.isEmpty(clrType)){
            throw new ServiceException("请选择医疗类型：住院/门诊");
        }
        String contractNo = reconciliation3201Param.getContractNo();
        if(StringUtils.isEmpty(contractNo)){
            throw new ServiceException("请选择合同类型");
        }
        Settlement3201DetailDto settlement3201DetailDto = null;
        List<Settlement3201DetailDto> settlement3201DetailDtos = this.reconcileGeneralLedgerDetail(reconciliation3201Param);
        for (Settlement3201DetailDto item : settlement3201DetailDtos) {
            if(contractNo.equals(item.getContractNo())&&insuType.equals(item.getInsutype().toString())&&clrType.equals(item.getClrType())){
                settlement3201DetailDto = item;
            }
        }
        if(settlement3201DetailDto==null){
            throw new ServiceException("未查询到相关信息");
        }
        return ybUtil.getFinancial3201Param(reconciliation3201Param,settlement3201DetailDto);
    }

    /**
     * 保存记录
     * 
     * @param tableName 表名
     * @param id 主键id
     * @param s 类型 3301/3302
     * @param medicalDirectory3301Param 参数信息
     */

    public void saveDirectoryHistory(String tableName, Long id, Integer s,
        MedicalDirectory3301Param medicalDirectory3301Param) {
        Long hospitalId = SecurityUtils.getLoginUser().getHospitalId();
        DirectoryCheckRecord directoryCheckRecord = new DirectoryCheckRecord();
        directoryCheckRecord.setTableName(tableName).setTableId(id).setOrgId(hospitalId).setType(s)
            .setParam(JSON.toJSONString(medicalDirectory3301Param));
        directoryCheckRecordService.save(directoryCheckRecord);
    }

    /**
     * 保存记录
     * 
     * @param tableName 表名
     * @param id 主键id
     * @param s 类型 3301/3302
     * @param medicalDirectory3302Param 参数信息
     */

    public void saveDirectoryHistory(String tableName, Long id, Integer s,
        MedicalDirectory3302Param medicalDirectory3302Param) {
        Long hospitalId = SecurityUtils.getLoginUser().getHospitalId();
        DirectoryCheckRecord directoryCheckRecord = new DirectoryCheckRecord();
        directoryCheckRecord.setTableName(tableName).setTableId(id).setOrgId(hospitalId).setType(s)
            .setParam(JSON.toJSONString(medicalDirectory3302Param));
        directoryCheckRecordService.save(directoryCheckRecord);
    }

    /**
     * 【9001】签到
     *
     * @param id 员工id
     * @return 9001参数
     */
    public Sign getSignParam(String id, String mac, String ip) {
        Practitioner practitioner = iPractitionerService.getById(Long.valueOf(id));
        if (practitioner == null) {
            throw new ServiceException("未查询到员工信息");
        }
        Sign sign = new Sign();
        sign.setOpterNo(practitioner.getBusNo()).setMac(mac.replace(" ", "+")).setIp(ip)
            .setOpterNo(String.valueOf(practitioner.getId()));
        return sign;
    }

    /**
     * 保存3201的信息
     * 
     * @param financial3201Output 对账结果信息
     */

    public void saveReconcileGeneralLedger(Financial3201Output financial3201Output) {
        FinancialReconcileRecord financialReconcileRecord = new FinancialReconcileRecord();
        BeanUtils.copyProperties(financial3201Output, financialReconcileRecord);
        financialReconcileRecord.setParam(JSON.toJSONString(financial3201Output));
        iFinancialReconcileRecordService.save(financialReconcileRecord);
    }

    /**
     * 【3202】明细对账
     * 
     * @param settlement3202WebParam 前台参数
     * @return 3202参数
     */

    public FinancialSettlement3202Param getFinancialSettlement3202Param(Settlement3202WebParam settlement3202WebParam) {
        return ybUtil.getFinancialSettlement3202Param(settlement3202WebParam);
    }

    /**
     * 【3209A】查询跨省三方对账未成功数据(吉林省)
     * 
     * @param settlement3209AWebParam 前台参数
     * @return 结果
     */

    public FinancialSettlement3209AParam
        getFinancialSettlement3209AParam(Settlement3209AWebParam settlement3209AWebParam) {
        return ybUtil.getFinancialSettlement3209AParam(settlement3209AWebParam);
    }

    /**
     * 【3203A】清算申请(吉林省)
     * 
     * @param financial3203AWebParam 前台参数
     * @return 结果
     */

    public Financial3203AParam getFinancial3203AParam(Financial3203AWebParam financial3203AWebParam) {
        //查询3202的所有实体记录
        List<Financial3202FileParam> financial3202FileParams = paymentCompareYbSettle(financial3203AWebParam.getSettlementIdList());

        BigDecimal medfeeSumamt = BigDecimal.ZERO;
        BigDecimal fundPaySumamt = BigDecimal.ZERO;
        BigDecimal medSumfee = BigDecimal.ZERO;
        BigDecimal acctPay = BigDecimal.ZERO;
        BigDecimal cashPay = BigDecimal.ZERO;
        for (Financial3202FileParam financial3202FileParam : financial3202FileParams) {
            if(financial3202FileParam instanceof Financial3202OtherParam otherParam){
                medfeeSumamt=medfeeSumamt.add(otherParam.getMedfeeSumamt());
                fundPaySumamt=fundPaySumamt.add(otherParam.getFundPaySumamt());
                acctPay=acctPay.add(otherParam.getAcctPay());
                cashPay=cashPay.add(otherParam.getCashPayamt());
                medSumfee=medSumfee.add(otherParam.getMedSumfee());
            }
        }
        Financial3203AParam busMonthSetlApply = new Financial3203AParam();
        busMonthSetlApply.setClrType(financial3203AWebParam.getClrType()).setMedfeeSumamt(medfeeSumamt)
                .setMedSumfee(medSumfee).setCashPayamt(cashPay)
                .setAcctPay(acctPay)
                .setBegndate(DateUtils.dateTime("yyyy-MM-dd", financial3203AWebParam.getStmtBegnDate().toString()))
                .setEnddate(DateUtils.dateTime("yyyy-MM-dd", financial3203AWebParam.getStmtEndDate().toString()))
                .setClrOptins(financial3203AWebParam.getClrOptins());
        return busMonthSetlApply;
    }

    /**
     * 【3205A】清算申请状态查询(吉林省)
     * 
     * @param clearing3205AWebParma 前台参数
     * @return 结果
     */

    public Clearing3205AParma getClearing3205AParma(Clearing3205AWebParma clearing3205AWebParma) {
        return ybUtil.getClearing3205AParma(clearing3205AWebParma);
    }

    /**
     * 【3501】商品盘存上传
     * 
     * @param id 供应申请id
     * @param talentId 租户id
     * @return 结果
     */

    public MedicalInventory3501Param getMedicalInventory3501Param(Long id, Integer talentId) {
        return ybUtil.getMedicalInventory3501Param(id, talentId);
    }

    /**
     * 【3502】库存信息变更
     * 
     * @param id 供应链发放id
     * @param invChgType 变更类型 参考枚举
     * @param talentId 租户id
     * @return 结果
     */

    public MedicalInventory3502Param getMedicalInventory3502Param(Long id, String invChgType, Integer talentId) {
        return ybUtil.getMedicalInventory3502Param(id, invChgType, talentId);
    }

    /**
     * 【3503】商品采购
     * 
     * @param id 供应发放id
     * @param talentId 租户id
     * @return 结果
     */

    public Medical3503Param getMedical3503Param(Long id, Integer talentId) {
        return ybUtil.getMedical3503Param(id, talentId);
    }

    /**
     * 【3504】商品采购退货
     *
     * @param id 供应发放id
     * @param talentId 租户id
     * @return 结果
     */

    public MedicalPurchase3504Param getMedicalPurchase3504Param(Long id, Integer talentId) {
        return ybUtil.getMedicalPurchase3504Param(id, talentId);
    }

    /**
     * 【3505】商品销售
     *
     * @param id 发放id
     * @param ListType 耗材/药品
     * @param talentId 租户id
     * @return 结果
     */

    public Medical3505Param getMedical3505Param(Long id, String ListType, Integer talentId) {
        return ybUtil.getMedical3505Param(id, ListType, talentId);
    }

    /**
     * 【3506】商品销售退货
     *
     * @param id 对应发放业务的主键id
     * @param ListType 目录种类
     * @param talentId 租户id
     * @return 3506参数
     */

    public Medical3506Param getMedical3506Param(Long id, String ListType, Integer talentId) {
        return ybUtil.getMedical3506Param(id, ListType, talentId);
    }

    /**
     * 【3507】商品信息删除参数
     *
     * @param id 库存项目id
     * @param invDataType 进销存类型
     * @return 3507参数
     */

    public Medical3507Param getMedical3507Param(Long id, String invDataType) {
        return ybUtil.getMedical3507Param(id, invDataType);
    }

    /**
     * 保存清算申请参数
     * 
     * @param financial3203AParam 3203A入参
     * @param s 一包返回结果
     */

    public void save3203AFinancialClearingApplycation(Financial3203AParam financial3203AParam, String s) {
        FinancialApplyRecord financialApplyRecord = new FinancialApplyRecord();
        BeanUtils.copyProperties(financial3203AParam, financialApplyRecord);
        financialApplyRecord.setParam(JSON.toJSONString(financial3203AParam)).setOutResult(s)
            .setPraId(SecurityUtils.getLoginUser().getPractitionerId()).setStatus("0");
        iFinancialApplyRecordService.save(new FinancialApplyRecord());
    }

    /**
     * 清算申请撤销
     * 
     * @param clrAppyEvtId 时间id
     * @param clrOptins 清算机构
     */

    public void save3204AFinancialClearingApplycation(String clrAppyEvtId, String clrOptins) {
        iFinancialApplyRecordService.update(new FinancialApplyRecord().setStatus("1"),
            new LambdaUpdateWrapper<FinancialApplyRecord>().eq(FinancialApplyRecord::getOutResult, clrAppyEvtId));
    }

    /**
     * 保存数据
     * 
     * @param medicalInventory3501Param 3501参数
     */

    public void saveInventoryCheckRecord(MedicalInventory3501Param medicalInventory3501Param, String resultMessage) {
        InventoryCheckRecord inventoryCheckRecord = new InventoryCheckRecord();
        BeanUtils.copyProperties(medicalInventory3501Param, inventoryCheckRecord);
        inventoryCheckRecord.setParam(JSON.toJSONString(medicalInventory3501Param));
        inventoryCheckRecord.setResult(JSON.toJSONString(resultMessage));
        iInventoryCheckRecordService.save(inventoryCheckRecord);
    }

    /**
     * 保存数据
     * 
     * @param medical3507Param 3507参数
     */

    public void saveInventoryDelRecord(Medical3507Param medical3507Param) {
        InventoryDelRecord inventoryDelRecord = new InventoryDelRecord();
        BeanUtils.copyProperties(medical3507Param, inventoryDelRecord);
        inventoryDelRecord.setParam(JSON.toJSONString(medical3507Param));
        iInventoryDelRecordService.save(inventoryDelRecord);
    }

    /**
     * 保存数据
     * 
     * @param medical3506Param 3506参数
     */

    public void saveInventorySaleReturnRecord(Medical3506Param medical3506Param, String retRslt) {
        InventorySaleReturnRecord inventorySaleReturnRecord = new InventorySaleReturnRecord();
        BeanUtils.copyProperties(medical3506Param, inventorySaleReturnRecord);
        inventorySaleReturnRecord.setParam(JSON.toJSONString(medical3506Param));
        inventorySaleReturnRecord.setOutResult(JSON.toJSONString(retRslt));
        iInventorySaleReturnRecordService.save(inventorySaleReturnRecord);
    }

    /**
     * 保存数据
     * 
     * @param medical3505Param 3505参数
     */

    public void saveInventorySaleRecord(Medical3505Param medical3505Param, String retRslt) {
        InventorySaleRecord inventorySaleRecord = new InventorySaleRecord();
        BeanUtils.copyProperties(medical3505Param, inventorySaleRecord);
        inventorySaleRecord.setParam(JSON.toJSONString(medical3505Param));
        inventorySaleRecord.setOutResult(JSON.toJSONString(retRslt));
        inventorySaleRecord.setDrugtracinfo(JSON.toJSONString(medical3505Param.getDrugtracinfo()));
        iInventorySaleRecordService.save(inventorySaleRecord);
    }

    /**
     * 保存数据
     * 
     * @param medicalPurchase3504Param 3504参数
     */

    public void saveInventoryPurchaseReturnRecord(MedicalPurchase3504Param medicalPurchase3504Param,
        String resultMessage) {
        InventoryPurchaseReturnRecord inventoryPurchaseReturnRecord = new InventoryPurchaseReturnRecord();
        BeanUtils.copyProperties(medicalPurchase3504Param, inventoryPurchaseReturnRecord);
        inventoryPurchaseReturnRecord.setParam(JSON.toJSONString(medicalPurchase3504Param));
        inventoryPurchaseReturnRecord.setOutResult(JSON.toJSONString(resultMessage));
        iInventoryPurchaseReturnRecordService.save(inventoryPurchaseReturnRecord);
    }

    /**
     * 保存数据
     * 
     * @param medical3503Param 3503参数
     */

    public void saveInventoryPurchaseRecord(Medical3503Param medical3503Param, String resultMessage) {
        InventoryPurchaseRecord inventoryPurchaseRecord = new InventoryPurchaseRecord();
        BeanUtils.copyProperties(medical3503Param, inventoryPurchaseRecord);
        inventoryPurchaseRecord.setParam(JSON.toJSONString(medical3503Param));
        inventoryPurchaseRecord.setOutResult(JSON.toJSONString(resultMessage));
        iInventoryPurchaseRecordService.save(inventoryPurchaseRecord);
    }

    /**
     * 保存数据
     * 
     * @param medicalInventory3502Param 3502参数
     */

    public void saveInventoryChangeRecord(MedicalInventory3502Param medicalInventory3502Param, String resultMessage) {
        InventoryChangeRecord inventoryChangeRecord = new InventoryChangeRecord();
        BeanUtils.copyProperties(medicalInventory3502Param, inventoryChangeRecord);
        inventoryChangeRecord.setParam(JSON.toJSONString(medicalInventory3502Param));
        inventoryChangeRecord.setResult(JSON.toJSONString(resultMessage));
        iInventoryChangeRecordService.save(inventoryChangeRecord);
    }

    public void saveClinic2207Record(String paymentNo, Clinic2207OrderParam clinicOrder2207,
        Clinic2207OrderModel clinic2206OrderResult) {
        ClinicSettle clinicSettle = new ClinicSettle();
        BeanUtils.copyProperties(clinic2206OrderResult, clinicSettle);
        clinicSettle.setParam2207(JSON.toJSONString(clinicOrder2207))
            .setResult2207(JSON.toJSONString(clinic2206OrderResult)).setPaymentNo(paymentNo);
        iClinicSettleService.save(clinicSettle);
    }

    public void saveClinic2207Record(Clinic2207OrderParam clinicOrder2207, Clinic2207OrderModel clinic2206OrderResult) {
        ClinicSettle clinicSettle = new ClinicSettle();
        BeanUtils.copyProperties(clinic2206OrderResult, clinicSettle);
        clinicSettle.setParam2207(JSON.toJSONString(clinicOrder2207))
            .setResult2207(JSON.toJSONString(clinic2206OrderResult));
        iClinicSettleService.save(clinicSettle);
    }

    public PatientInfoDto getPatent(Info1101Output perInfo) {
        // 查患者信息
        Patient patient =
            iPatientService.getOne(new LambdaQueryWrapper<Patient>().eq(Patient::getIdCard, perInfo.getCertno()));
        if (patient == null) {
            patient = new Patient();
            // 处理性别字段
            YbGender ybGender = YbGender.getByValue(perInfo.getGend());
            AdministrativeGender administrativeGender;
            switch (ybGender) {
                case MALE:
                    administrativeGender = AdministrativeGender.MALE;
                    break;
                case FEMALE:
                    administrativeGender = AdministrativeGender.FEMALE;
                    break;
                default:
                    administrativeGender = AdministrativeGender.UNKNOWN;
                    break;
            }
            // 新建患者
            patient.setIdCard(perInfo.getCertno()).setName(perInfo.getPsnName())
                .setGenderEnum(administrativeGender.getValue()).setActiveFlag(0).setTempFlag(0)
                .setOrganizationId(SecurityUtils.getLoginUser().getOrgId()).setBirthDate(perInfo.getBrdy());
            iPatientService.savePatient(patient);
        }
        // 赋值
        PatientInfoDto patientInfoDto = new PatientInfoDto();
        BeanUtils.copyProperties(patient, patientInfoDto);
        // 查合同 2025/05/23 弃用，费用性质固定，如农大只有省医保，费用性质只显示省医保和自费即可，由@GetMapping(value = "/contract-list")接口提供数据
        // Contract contract = iContractService
        // .getOne(new LambdaQueryWrapper<Contract>().eq(Contract::getAdmVs, perInfo.getInsuplcAdmdvs()));
        // if (contract != null) {
        // BeanUtils.copyProperties(contract, patientInfoDto);
        // patientInfoDto.setContractOrgId(contract.getOrgId()).setContractId(contract.getId())
        // .setContractBusNo(contract.getBusNo());
        // }
        patientInfoDto.setId(patient.getId()).setFeedetail(perInfo.getFeedetail());

        return patientInfoDto;
    }

    public ClinicReg getUnReg(Long encounterId, Integer tenantId) {

        Encounter encounter = iEncounterService.getById(encounterId);
        if (encounter == null) {
            throw new ServiceException("查询不到就诊信息");
        }

        ClinicReg one =
            iRegService.getOne(new LambdaQueryWrapper<ClinicReg>().eq(ClinicReg::getIptOtpNo, encounter.getBusNo()));
        if (one == null) {
            throw new ServiceException("查询不到医保挂号信息");
        }
        ClinicReg clinicReg = new ClinicReg().setPsnNo(one.getPsnNo()).setMdtrtId(one.getMdtrtId())
            .setIptOtpNo(encounter.getBusNo()).setInsuplcAdmdvs(one.getInsuplcAdmdvs());
        if (clinicReg == null) {
            throw new ServiceException("未生成2202参数");
        }
        return clinicReg;
    }

    public List<Settlement3201DetailDto> reconcileGeneralLedgerDetail(Settlement3201WebParam settlement3201WebParam) {
        //获取条件
        String clrType = settlement3201WebParam.getClrType();//住院 or 门诊
        String contractNo = settlement3201WebParam.getContractNo();//省市医保
        String insuType = settlement3201WebParam.getInsuType();//职工 or 居民保险
        String stmtBegnDate = settlement3201WebParam.getStmtBegnDate();//开始时间
        String stmtEndDate = settlement3201WebParam.getStmtEndDate();//结束时间
        Date startDate = DateUtils.parseDate(stmtBegnDate);
        Date endDate = DateUtils.parseDate(stmtEndDate);
        //处理时间
        if(StringUtils.isEmpty(stmtBegnDate)||StringUtils.isEmpty(stmtEndDate)){
            // 获取当前日期
            LocalDate currentDate = LocalDate.now();

            if(StringUtils.isEmpty(stmtBegnDate)) {
                // 获取当月的开始时间（当月的1号 00:00:00）
                LocalDateTime startOfMonth = LocalDateTime.of(currentDate, LocalTime.MIN);
                startDate = Date.from(startOfMonth.atZone(ZoneId.systemDefault()).toInstant());
                stmtBegnDate = DateUtils.parseDateToStr("yyyy-MM-dd hh:mm:ss.SSS",startDate);
            }

            if(StringUtils.isEmpty(stmtEndDate)) {
                // 获取当月的结束时间（当月的最后一天 23:59:59）
                LocalDate endOfMonthDate = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
                LocalDateTime endOfMonth = LocalDateTime.of(endOfMonthDate, LocalTime.MAX);
                endDate = Date.from(endOfMonth.atZone(ZoneId.systemDefault()).toInstant());
                stmtEndDate = DateUtils.parseDateToStr("yyyy-MM-dd hh:mm:ss.SSS",endDate);
            }
        }
        //查询该段时间内的payment
        List<PaymentReconciliation> paymentReconciliations = iPaymentReconciliationService.list(new LambdaQueryWrapper<PaymentReconciliation>().between(PaymentReconciliation::getBillDate, startDate, endDate).ne(PaymentReconciliation::getStatusEnum, PaymentStatus.DRAFT.getValue()).isNotNull(PaymentReconciliation::getYbSettleIds));
        //过滤医保结算id
        List<String> settleIdList = paymentReconciliations.stream().filter(e -> StringUtils.isNotEmpty(e.getYbSettleIds())).map(PaymentReconciliation::getYbSettleIds).collect(Collectors.toList());

        //List<String> allSettleIds = settleIdList.stream().flatMap(ids -> Stream.of(ids.split(","))).filter(id -> !id.isEmpty()).collect(Collectors.toList());
        //获取新的paymentId集合
        List<Long> paymentReconciliationIdList = paymentReconciliations.stream().map(PaymentReconciliation::getId).collect(Collectors.toList());
        //List<PaymentRecDetail> paymentRecDetailList = iPaymentRecDetailService.list(new LambdaQueryWrapper<PaymentRecDetail>().in(PaymentRecDetail::getReconciliationId, paymentReconciliationIdList));
        //List<Long> accountIds = paymentRecDetailList.stream().map(PaymentRecDetail::getAccountId).collect(Collectors.toList());
        //List<Account>  accountList = accountService.list(new LambdaQueryWrapper<Account>().in(Account::getId, accountIds));
        //查询支付详情记录信息
        List<PaymentDecDetailUniAccountDto> paymentDecDetailUniAccountDtos = medicalInsuranceMapper.getPaymentDecDetailUniAccountDtoListByPaymentIdList(paymentReconciliationIdList,startDate,endDate);
        //声明变量
        List<Settlement3201DetailDto> settlement3201DetailDtos = new ArrayList<>();
        //Settlement3201DetailDto settlement3201ProEMPOutDto = new Settlement3201DetailDto();//省医保门诊职工
        //Settlement3201DetailDto settlement3201CityEMPOutDto = new Settlement3201DetailDto();//市医保门诊居民
        //Settlement3201DetailDto settlement3201ProRESOutDto = new Settlement3201DetailDto();//省医保门诊职工
        //Settlement3201DetailDto settlement3201CityRESOutDto = new Settlement3201DetailDto();//市医保门诊居民
        //Settlement3201DetailDto settlement3201ProEMPInDto = new Settlement3201DetailDto();//省医保住院职工
        //Settlement3201DetailDto settlement3201CityEMPInDto = new Settlement3201DetailDto();//市医保住院居民
        //Settlement3201DetailDto settlement3201ProRESInDto = new Settlement3201DetailDto();//省医保住院职工
        //Settlement3201DetailDto settlement3201CityRESInDto = new Settlement3201DetailDto();//市医保住院居民
        Settlement3201DetailDto settlement3201Dto ;//声明变量
        //分组处理
        for (PaymentDecDetailUniAccountDto paymentDecDetailUniAccountDto : paymentDecDetailUniAccountDtos) {
            if(StringUtils.isEmpty(paymentDecDetailUniAccountDto.getContractNo())){
                System.out.println(paymentDecDetailUniAccountDto.getId());
            }
        }
        for (PaymentDecDetailUniAccountDto paymentDecDetailUniAccountDto : paymentDecDetailUniAccountDtos) {
            if(StringUtils.isEmpty(paymentDecDetailUniAccountDto.getContractNo())){
                System.out.println(paymentDecDetailUniAccountDto.getId()+"payment主键："+paymentDecDetailUniAccountDto.getReconciliationId());
            }
        }

        Map<String, List<PaymentDecDetailUniAccountDto>> paymentDecDetailUniAccountDtoMapGroupByContract = paymentDecDetailUniAccountDtos.stream().collect(Collectors.groupingBy(PaymentDecDetailUniAccountDto::getContractNo));
        //分省市医保
        for (Map.Entry<String, List<PaymentDecDetailUniAccountDto>> stringListEntry : paymentDecDetailUniAccountDtoMapGroupByContract.entrySet()) {
            if("0000".equals(stringListEntry.getKey())){
                continue;
            }
            if(stringListEntry.getValue().isEmpty()){
                continue;
            }
            if(!StringUtils.isEmpty(contractNo)&&!stringListEntry.getKey().equals(contractNo)){
                continue;
            }
            //分住院门诊
            Map<String, List<PaymentDecDetailUniAccountDto>> paymentDecDetailUniAccountDtoMapGroupByMedType = stringListEntry.getValue().stream().collect(Collectors.groupingBy(PaymentDecDetailUniAccountDto::getMedType));
            for (Map.Entry<String, List<PaymentDecDetailUniAccountDto>> listEntry : paymentDecDetailUniAccountDtoMapGroupByMedType.entrySet()) {
                if(listEntry.getValue().isEmpty()){
                    continue;
                }
                if(!StringUtils.isEmpty(clrType)&&!listEntry.getKey().equals(clrType)){
                    continue;
                }
                //分职工居民
                Map<String, List<PaymentDecDetailUniAccountDto>> collect = listEntry.getValue().stream().collect(Collectors.groupingBy(PaymentDecDetailUniAccountDto::getInsutype));
                for (Map.Entry<String, List<PaymentDecDetailUniAccountDto>> entry : collect.entrySet()) {
                    if(entry.getValue().isEmpty()){
                        continue;
                    }
                    if(!StringUtils.isEmpty(insuType)&&!entry.getKey().equals(insuType)){
                        continue;
                    }
                    int count = 0;
                    settlement3201Dto = new Settlement3201DetailDto();
                    settlement3201Dto.setContractNo(stringListEntry.getKey()).setInsutype(Integer.parseInt(entry.getKey()))
                            .setClrType(listEntry.getKey());
                    for (PaymentDecDetailUniAccountDto paymentDecDetailUniAccountDto : entry.getValue()) {
                        if(YbPayment.YB_FUND_PAY.getValue().equals(paymentDecDetailUniAccountDto.getPayEnum())){
                            //基金支付
                            settlement3201Dto.setFundPaySumAmt(paymentDecDetailUniAccountDto.getAmount().add(settlement3201Dto.getFundPaySumAmt()));
                            count++;
                        }
                        if(YbPayment.SELF_YB_ZH_PAY.getValue().equals(paymentDecDetailUniAccountDto.getPayEnum())){
                            //账户支付
                            settlement3201Dto.setAcctPay(paymentDecDetailUniAccountDto.getAmount().add(settlement3201Dto.getAcctPay()));
                        }
                        if(YbPayment.SELF_YB_ZH_GJ_VALUE.getValue().equals(paymentDecDetailUniAccountDto.getPayEnum())){
                            //账户共济支付
                            settlement3201Dto.setAcctGjPay(paymentDecDetailUniAccountDto.getAmount().add(settlement3201Dto.getAcctGjPay()));
                        }
                        if(YbPayment.SELF_PAY.getValue().equals(paymentDecDetailUniAccountDto.getPayEnum())||YbPayment.OTHER_PAY.getValue().equals(paymentDecDetailUniAccountDto.getPayEnum())||YbPayment.YB_FUND_PAY.getValue().equals(paymentDecDetailUniAccountDto.getPayEnum())){
                            //合计医疗费用
                            settlement3201Dto.setMedFeeSumAmt(paymentDecDetailUniAccountDto.getAmount().add(settlement3201Dto.getMedFeeSumAmt()));
                        }
                    }
                    //结算笔数
                    settlement3201Dto.setFixMedInsSetlCnt(count);
                    settlement3201DetailDtos.add(settlement3201Dto);
                }
            }
        }
        //返回参数
        return settlement3201DetailDtos;
    }

    public ClinicReg2201Output getClinicRegByEncounterId(Long encounterId) {
        Encounter encounter = iEncounterService.getById(encounterId);
        if (encounter == null) {
            throw new ServiceException("未查询到就诊信息");
        }
        ClinicReg clinicReg =
            iRegService.getOne(new LambdaQueryWrapper<ClinicReg>().eq(ClinicReg::getIptOtpNo, encounter.getBusNo()));
        ClinicReg2201Output clinicReg2201Output = new ClinicReg2201Output();
        BeanUtils.copyProperties(clinicReg, clinicReg2201Output);
        clinicReg2201Output.setMedType(clinicReg.getMedType());//copyProperties会有概率copy失败，此处打补丁赋值
        return clinicReg2201Output;
    }

    public ClinicReg get2202Reg(Long id, Integer tenantId) {
        Encounter encounter = iEncounterService.getById(id);
        if (encounter == null) {
            throw new ServiceException("未查询到就诊信息");
        }
        return iRegService.getOne(new LambdaQueryWrapper<ClinicReg>().eq(ClinicReg::getIptOtpNo, encounter.getBusNo()));
    }

    /**
     * 系統數據比較
     * @param settlement3202WebParam
     */
    public List<Financial3202FileParam> paymentCompareYbSettle(Settlement3202WebParam settlement3202WebParam) {
        //比较yb_clinic_settle表和payment系列表数据是否一致
        String stmtBegnDate = String.valueOf(settlement3202WebParam.getStmtBegnDate());
        String stmtEndDate = String.valueOf(settlement3202WebParam.getStmtEndDate());
        if(StringUtils.isEmpty(stmtBegnDate)||StringUtils.isEmpty(stmtEndDate)){
            throw new ServiceException("未選擇開始與結束時間");
        }

        Date startDate = DateUtils.parseDate(stmtBegnDate);
        Date endDate = DateUtils.parseDate(stmtEndDate);

        //查询该段时间内的payment
        List<PaymentReconciliation> paymentReconciliations = iPaymentReconciliationService.list(new LambdaQueryWrapper<PaymentReconciliation>().between(PaymentReconciliation::getBillDate, startDate, endDate).ne(PaymentReconciliation::getStatusEnum, PaymentStatus.DRAFT.getValue()).ne(PaymentReconciliation::getYbSettleIds,"0000").ne(PaymentReconciliation::getYbSettleIds,"null").isNotNull(PaymentReconciliation::getYbSettleIds));
        if(paymentReconciliations.isEmpty()){
            throw new ServiceException("该时间内无付款记录");
        }

        //过滤医保结算id
        List<String> settleIdList = paymentReconciliations.stream().filter(e -> StringUtils.isNotEmpty(e.getYbSettleIds())).map(PaymentReconciliation::getYbSettleIds).collect(Collectors.toList());

        //查询医保表数据
        List<ClinicSettle> clinicSettles = iClinicSettleService.list(new LambdaQueryWrapper<ClinicSettle>().in(ClinicSettle::getSetlId, settleIdList));
        List<ClinicUnSettle> clinicUnSettles = iClinicUnSettleService.list(new LambdaQueryWrapper<ClinicUnSettle>().in(ClinicUnSettle::getSettleId, settleIdList));

        //payment的id集合
        List<Long> paymentReconciliationIdList = paymentReconciliations.stream().map(PaymentReconciliation::getId).collect(Collectors.toList());

        //查询详情表数据
        List<PaymentRecDetail> paymentRecDetailList = iPaymentRecDetailService.list(new LambdaQueryWrapper<PaymentRecDetail>().in(PaymentRecDetail::getReconciliationId, paymentReconciliationIdList).ne(PaymentRecDetail::getPayTransNo, "0000").ne(PaymentRecDetail::getPayTransText, "0000").ne(PaymentRecDetail::getPayEnum, YbPayment.SELF_CASH_ALI_VALUE.getValue()).ne(PaymentRecDetail::getPayEnum, YbPayment.SELF_CASH_VX_VALUE.getValue()).ne(PaymentRecDetail::getPayEnum, YbPayment.SELF_CASH_UNION_VALUE.getValue()).ne(PaymentRecDetail::getPayEnum, YbPayment.SELF_CASH_VALUE.getValue()));
        if(paymentRecDetailList.isEmpty()){
            throw new ServiceException("该时间内无付款明细记录");
        }
        // 该部分代码为辅助调试时使用，无任何业务处理 start
        for (PaymentRecDetail paymentRecDetail : paymentRecDetailList) {
            if(StringUtils.isEmpty(paymentRecDetail.getPayTransText())){
                System.out.println("*******************************************************************");
                System.out.println(paymentRecDetail.getId()+"主键:"+paymentRecDetail.getReconciliationId());
                System.out.println(JSON.toJSONString(paymentRecDetail));
                //throw new ServiceException("paymentDetail无返回交易码");
            }
        }
        //end

        Map<String, List<PaymentRecDetail>> paymentRecDetailMapBySettleId = paymentRecDetailList.stream().collect(Collectors.groupingBy(PaymentRecDetail::getPayTransText));

        List<Financial3202FileParam> financial3202FileParamList = new ArrayList<>();
        Financial3202FileParam financial3202FileParam;
        for (Map.Entry<String, List<PaymentRecDetail>> stringListEntry : paymentRecDetailMapBySettleId.entrySet()) {
            //获取settleId
            String settleId = stringListEntry.getKey();
            if("0000".equals(settleId)){
                continue;
            }
            Optional<ClinicSettle> settleOptional = clinicSettles.stream().filter(e -> settleId.equals(e.getSetlId())).findAny();
            Optional<ClinicUnSettle> unSettleOptional = clinicUnSettles.stream().filter(e -> settleId.equals(e.getSettleId())).findAny();
            if(!settleOptional.isPresent()&&!unSettleOptional.isPresent()){
                throw new ServiceException("付款详情中结算id在医保记录中不存在，错误信息：paymentId："+stringListEntry.getValue().get(0).getReconciliationId()+"；医保结算id"+settleId);
            }
            if(settleOptional.isPresent()&&unSettleOptional.isPresent()){
                throw new ServiceException("付款详情中结算id在医保记录中同时存在正结和反结，错误信息：paymentId："+stringListEntry.getValue().get(0).getReconciliationId()+"；医保结算id"+settleId);
            }
            //校对金额
            BigDecimal medfeeSumamt = BigDecimal.ZERO;
            BigDecimal fundPaySumamt = BigDecimal.ZERO;
            BigDecimal acctPay = BigDecimal.ZERO;
            String refdSetlFlag = Whether.NO.getCode();//1 是退费; 0否
            String psnNo = "";
            String mdtrtId = "";
            String patientName = "";
            String setlTime = "";

            for (PaymentRecDetail paymentRecDetail : stringListEntry.getValue()) {
                if(YbPayment.YB_FUND_PAY.getValue().equals(paymentRecDetail.getPayEnum())){
                    medfeeSumamt = medfeeSumamt.add(paymentRecDetail.getAmount());
                    fundPaySumamt = fundPaySumamt.add(paymentRecDetail.getAmount());
                }
                if(YbPayment.SELF_PAY.getValue().equals(paymentRecDetail.getPayEnum())){
                    medfeeSumamt = medfeeSumamt.add(paymentRecDetail.getAmount());
                }
                //if(YbPayment.OTHER_PAY.getValue().equals(paymentRecDetail.getPayEnum())){
                //    medfeeSumamt = medfeeSumamt.add(paymentRecDetail.getAmount());
                //}
                if(YbPayment.SELF_YB_ZH_PAY.getValue().equals(paymentRecDetail.getPayEnum())){
                    acctPay = acctPay.add(paymentRecDetail.getAmount());
                }
            }

            if(settleOptional.isPresent()){
                ClinicSettle clinicSettle = settleOptional.get();
                if(clinicSettle.getAcctPay().compareTo(acctPay)!=0){
                    throw new ServiceException("医保正结记录与业务记录账户支付金额不等！请核对！错误信息：医保结算id："+clinicSettle.getSetlId()+" 账户支付："+clinicSettle.getAcctPay()+" payment记录账户支付金额："+acctPay);
                }
                if(clinicSettle.getMedfeeSumamt().compareTo(medfeeSumamt)!=0){
                    throw new ServiceException("医保正结记录与业务记录医疗总金额不等！请核对！错误信息：医保结算id："+clinicSettle.getSetlId()+" 医疗总金额："+clinicSettle.getMedfeeSumamt()+" payment记录医疗总金额："+medfeeSumamt);
                }
                if(clinicSettle.getFundPaySumamt().compareTo(fundPaySumamt)!=0){
                    throw new ServiceException("医保正结记录与业务记录基金金额不等！请核对！错误信息：医保结算id："+clinicSettle.getSetlId()+" 基金金额："+clinicSettle.getFundPaySumamt()+" payment记录基金金额："+fundPaySumamt);
                }
                String param2207 = clinicSettle.getParam2207();
                Clinic2207OrderParam clinic2207OrderParam = JSON.parseObject(param2207, Clinic2207OrderParam.class);
                psnNo = clinic2207OrderParam.getPsnNo();
                mdtrtId = clinic2207OrderParam.getMdtrtId();
                Clinic2207OrderModel clinic2207OrderModel = JSON.parseObject(clinicSettle.getResult2207(), Clinic2207OrderModel.class);
                patientName = clinic2207OrderModel.getPsnName();
                setlTime = String.valueOf(clinic2207OrderModel.getSetlTime());
            }

            if(unSettleOptional.isPresent()){
                ClinicUnSettle clinicSettle = unSettleOptional.get();
                if(clinicSettle.getAcctPay().compareTo(acctPay)!=0){
                    throw new ServiceException("医保反结记录与业务记录账户支付金额不等！请核对！错误信息：医保结算id："+clinicSettle.getSettleId()+" 账户支付："+clinicSettle.getAcctPay()+" payment记录账户支付金额："+acctPay);
                }
                if(clinicSettle.getMedfeeSumamt().compareTo(medfeeSumamt)!=0){
                    throw new ServiceException("医保反结记录与业务记录医疗费用总金额不等！请核对！错误信息：医保结算id："+clinicSettle.getSettleId()+" 医疗费用总金额："+clinicSettle.getMedfeeSumamt()+" payment记录医疗费用总金额："+medfeeSumamt);
                }
                if(clinicSettle.getFundPaySumamt().compareTo(fundPaySumamt)!=0){
                    throw new ServiceException("医保反结记录与业务记录基金金额不等！请核对！错误信息：医保结算id："+clinicSettle.getSettleId()+" 基金金额："+clinicSettle.getFundPaySumamt()+" payment记录基金金额："+fundPaySumamt);
                }
                refdSetlFlag = Whether.YES.getCode();
                String param2208 = clinicSettle.getParam2208();
                Clinic2208UnSetlInfoParam clinic2208UnSetlInfoParam = JSON.parseObject(param2208, Clinic2208UnSetlInfoParam.class);
                psnNo = clinic2208UnSetlInfoParam.getPsnNo();
                mdtrtId = clinic2208UnSetlInfoParam.getMdtrtId();
                Clinic2208UnSetlInfoOutput clinic2207OrderModel = JSON.parseObject(clinicSettle.getResult2208(), Clinic2208UnSetlInfoOutput.class);
                //patientName = clinic2207OrderModel.getPsnName();
                setlTime = String.valueOf(clinic2207OrderModel.getSetlTime());
            }

            financial3202FileParam = new Financial3202FileParam();

            financial3202FileParam.setSetlId(settleId).setAcctPay(acctPay).setFundPaySumamt(fundPaySumamt).setMdtrtId(mdtrtId).setPsnNo(psnNo).setMedfeeSumamt(medfeeSumamt).setRefdSetlFlag(refdSetlFlag).setPatientName(patientName).setSetlTime(setlTime);

            financial3202FileParamList.add(financial3202FileParam);
        }

        return financial3202FileParamList;
    }

    /**
     * 系統數據比較
     * @param settlementIdList
     */
    public List<Financial3202FileParam> paymentCompareYbSettle(List<String> settlementIdList) {
        //比较yb_clinic_settle表和payment系列表数据是否一致
        //查询医保表数据
        List<ClinicSettle> clinicSettles = iClinicSettleService.list(new LambdaQueryWrapper<ClinicSettle>().in(ClinicSettle::getSetlId, settlementIdList));
        List<ClinicUnSettle> clinicUnSettles = iClinicUnSettleService.list(new LambdaQueryWrapper<ClinicUnSettle>().in(ClinicUnSettle::getSettleId, settlementIdList));

        //payment的id集合
        //List<Long> paymentReconciliationIdList = paymentReconciliations.stream().map(PaymentReconciliation::getId).collect(Collectors.toList());

        //查询详情表数据
        List<PaymentRecDetail> paymentRecDetailList = iPaymentRecDetailService.list(new LambdaQueryWrapper<PaymentRecDetail>().in(PaymentRecDetail::getPayTransText, settlementIdList).ne(PaymentRecDetail::getPayTransNo, "0000").ne(PaymentRecDetail::getPayTransText,"0000").isNotNull(PaymentRecDetail::getPayTransText));
        if(paymentRecDetailList.isEmpty()){
            throw new ServiceException("该时间内无付款明细记录");
        }
        // 该部分代码为辅助调试时使用，无任何业务处理 start
        for (PaymentRecDetail paymentRecDetail : paymentRecDetailList) {
            if(StringUtils.isEmpty(paymentRecDetail.getPayTransNo())){
                System.out.println(JSON.toJSONString(paymentRecDetail));
                throw new ServiceException("paymentDetail无返回交易码");
            }
        }
        //end

        Map<String, List<PaymentRecDetail>> paymentRecDetailMapBySettleId = paymentRecDetailList.stream().collect(Collectors.groupingBy(PaymentRecDetail::getPayTransText));

        List<Financial3202FileParam> financial3202FileParamList = new ArrayList<>();
        Financial3202OtherParam financial3202OtherParam;
        for (Map.Entry<String, List<PaymentRecDetail>> stringListEntry : paymentRecDetailMapBySettleId.entrySet()) {
            //获取settleId
            String settleId = stringListEntry.getKey();
            if("0000".equals(settleId)){
                continue;
            }
            Optional<ClinicSettle> settleOptional = clinicSettles.stream().filter(e -> settleId.equals(e.getSetlId())).findAny();
            Optional<ClinicUnSettle> unSettleOptional = clinicUnSettles.stream().filter(e -> settleId.equals(e.getSettleId())).findAny();
            if(!settleOptional.isPresent()&&!unSettleOptional.isPresent()){
                throw new ServiceException("付款详情中结算id在医保记录中不存在，错误信息：paymentId："+stringListEntry.getValue().get(0).getReconciliationId()+"；医保结算id"+settleId);
            }
            if(settleOptional.isPresent()&&unSettleOptional.isPresent()){
                throw new ServiceException("付款详情中结算id在医保记录中同时存在正结和反结，错误信息：paymentId："+stringListEntry.getValue().get(0).getReconciliationId()+"；医保结算id"+settleId);
            }
            //校对金额
            BigDecimal medfeeSumamt = BigDecimal.ZERO;
            BigDecimal fundPaySumamt = BigDecimal.ZERO;
            BigDecimal acctPay = BigDecimal.ZERO;
            BigDecimal cashPay = BigDecimal.ZERO;
            BigDecimal medSumfee = BigDecimal.ZERO;
            String refdSetlFlag = Whether.NO.getCode();//1 是退费; 0否
            String psnNo = "";
            String mdtrtId = "";

            for (PaymentRecDetail paymentRecDetail : stringListEntry.getValue()) {
                if(YbPayment.YB_FUND_PAY.getValue().equals(paymentRecDetail.getPayEnum())){
                    medfeeSumamt = medfeeSumamt.add(paymentRecDetail.getAmount());
                    fundPaySumamt = fundPaySumamt.add(paymentRecDetail.getAmount());
                }
                if(YbPayment.SELF_PAY.getValue().equals(paymentRecDetail.getPayEnum())){
                    medfeeSumamt = medfeeSumamt.add(paymentRecDetail.getAmount());
                }
                if(YbPayment.OTHER_PAY.getValue().equals(paymentRecDetail.getPayEnum())){
                    medfeeSumamt = medfeeSumamt.add(paymentRecDetail.getAmount());
                }
                if(YbPayment.SELF_YB_ZH_PAY.getValue().equals(paymentRecDetail.getPayEnum())){
                    acctPay = acctPay.add(paymentRecDetail.getAmount());
                }
                if(YbPayment.SELF_CASH_PAY.getValue().equals(paymentRecDetail.getPayEnum())){
                    cashPay = cashPay.add(paymentRecDetail.getAmount());
                }
                if(YbPayment.INSCP_SCP_AMT.getValue().equals(paymentRecDetail.getPayEnum())){
                    medSumfee = medSumfee.add(paymentRecDetail.getAmount());
                }
            }

            if(settleOptional.isPresent()){
                ClinicSettle clinicSettle = settleOptional.get();
                if(clinicSettle.getAcctPay().compareTo(acctPay)!=0){
                    throw new ServiceException("医保正结记录与业务记录账户支付金额不等！请核对！错误信息：医保结算id："+clinicSettle.getSetlId()+" 账户支付："+clinicSettle.getAcctPay()+" payment记录账户支付金额："+acctPay);
                }
                if(clinicSettle.getMedfeeSumamt().compareTo(medfeeSumamt)!=0){
                    throw new ServiceException("医保正结记录与业务记录账户支付金额不等！请核对！错误信息：医保结算id："+clinicSettle.getSetlId()+" 账户支付："+clinicSettle.getMedfeeSumamt()+" payment记录账户支付金额："+medfeeSumamt);
                }
                if(clinicSettle.getFundPaySumamt().compareTo(fundPaySumamt)!=0){
                    throw new ServiceException("医保正结记录与业务记录账户支付金额不等！请核对！错误信息：医保结算id："+clinicSettle.getSetlId()+" 账户支付："+clinicSettle.getFundPaySumamt()+" payment记录账户支付金额："+fundPaySumamt);
                }
                String param2207 = clinicSettle.getParam2207();
                Clinic2207OrderParam clinic2207OrderParam = JSON.parseObject(param2207, Clinic2207OrderParam.class);
                psnNo = clinic2207OrderParam.getPsnNo();
                mdtrtId = clinic2207OrderParam.getMdtrtId();
            }

            if(unSettleOptional.isPresent()){
                ClinicUnSettle clinicSettle = unSettleOptional.get();
                if(clinicSettle.getAcctPay().compareTo(acctPay)!=0){
                    throw new ServiceException("医保反结记录与业务记录账户支付金额不等！请核对！错误信息：医保结算id："+clinicSettle.getSettleId()+" 账户支付："+clinicSettle.getAcctPay()+" payment记录账户支付金额："+acctPay);
                }
                if(clinicSettle.getMedfeeSumamt().compareTo(medfeeSumamt)!=0){
                    throw new ServiceException("医保反结记录与业务记录账户支付金额不等！请核对！错误信息：医保结算id："+clinicSettle.getSettleId()+" 账户支付："+clinicSettle.getMedfeeSumamt()+" payment记录账户支付金额："+medfeeSumamt);
                }
                if(clinicSettle.getFundPaySumamt().compareTo(fundPaySumamt)!=0){
                    throw new ServiceException("医保反结记录与业务记录账户支付金额不等！请核对！错误信息：医保结算id："+clinicSettle.getSettleId()+" 账户支付："+clinicSettle.getFundPaySumamt()+" payment记录账户支付金额："+fundPaySumamt);
                }
                refdSetlFlag = Whether.YES.getCode();
                String param2208 = clinicSettle.getParam2208();
                Clinic2208UnSetlInfoParam clinic2208UnSetlInfoParam = JSON.parseObject(param2208, Clinic2208UnSetlInfoParam.class);
                psnNo = clinic2208UnSetlInfoParam.getPsnNo();
                mdtrtId = clinic2208UnSetlInfoParam.getMdtrtId();
            }

            financial3202OtherParam = new Financial3202OtherParam();

            financial3202OtherParam.setSetlId(settleId).setAcctPay(acctPay).setFundPaySumamt(fundPaySumamt)
                    .setMdtrtId(mdtrtId).setPsnNo(psnNo).setMedfeeSumamt(medfeeSumamt).setRefdSetlFlag(refdSetlFlag);
            financial3202OtherParam.setCashPayamt(cashPay).setMedSumfee(medSumfee);
            financial3202FileParamList.add(financial3202OtherParam);
        }

        return financial3202FileParamList;
    }

    /**
     * 获取3202的现金支付金额
     * @param settlementIdList
     */
    public FinancialSettlement3202Param getFinancialSettlement3202Param(List<String> settlementIdList, BigDecimal a,BigDecimal b) {

        //Financial3202OtherParam financial3202OtherParam = new Financial3202OtherParam();
        BigDecimal cashPay = BigDecimal.ZERO;

        //比较yb_clinic_settle表和payment系列表数据是否一致
        //查询医保表数据
        List<ClinicSettle> clinicSettles = iClinicSettleService.list(new LambdaQueryWrapper<ClinicSettle>().in(ClinicSettle::getSetlId, settlementIdList));
        List<ClinicUnSettle> clinicUnSettles = iClinicUnSettleService.list(new LambdaQueryWrapper<ClinicUnSettle>().in(ClinicUnSettle::getSettleId, settlementIdList));
        //防空指针
        if(clinicSettles==null){
            clinicSettles = new ArrayList<>();
        }
        if(clinicUnSettles==null){
            clinicUnSettles = new ArrayList<>();
        }
        List<Date> clinicSettleTime = clinicSettles.stream().map(ClinicSettle::getSetlTime).collect(Collectors.toList());
        List<Date> clinicUnSettleTime = clinicUnSettles.stream().map(ClinicUnSettle::getSetlTime).collect(Collectors.toList());
        clinicSettleTime.addAll(clinicUnSettleTime);

        Date stmtBegndate = null;
        Date stmtEnddate = null;

        // 获取最早的日期
        Optional<Date> minDate = clinicSettleTime.stream()
                .min(Date::compareTo);

        // 获取最晚的日期
        Optional<Date> maxDate = clinicSettleTime.stream()
                .max(Date::compareTo);

        if(!minDate.isPresent()||!maxDate.isPresent()){
            throw new ServiceException("未查询到医保结算时间");
        }
        stmtBegndate = minDate.get();
        stmtEnddate = maxDate.get();

        //payment的id集合
        //List<Long> paymentReconciliationIdList = paymentReconciliations.stream().map(PaymentReconciliation::getId).collect(Collectors.toList());

        //查询详情表数据
        List<PaymentRecDetail> paymentRecDetailList = iPaymentRecDetailService.list(new LambdaQueryWrapper<PaymentRecDetail>().in(PaymentRecDetail::getPayTransText, settlementIdList).ne(PaymentRecDetail::getPayTransNo, "0000").ne(PaymentRecDetail::getPayTransText,"0000").isNotNull(PaymentRecDetail::getPayTransText));
        if(paymentRecDetailList.isEmpty()){
            throw new ServiceException("该时间内无付款明细记录");
        }
        // 该部分代码为辅助调试时使用，无任何业务处理 start
        for (PaymentRecDetail paymentRecDetail : paymentRecDetailList) {
            if(StringUtils.isEmpty(paymentRecDetail.getPayTransNo())){
                System.out.println(JSON.toJSONString(paymentRecDetail));
                throw new ServiceException("paymentDetail无返回交易码");
            }
        }
        //end

        Map<String, List<PaymentRecDetail>> paymentRecDetailMapBySettleId = paymentRecDetailList.stream().collect(Collectors.groupingBy(PaymentRecDetail::getPayTransText));

        //List<Financial3202FileParam> financial3202FileParamList = new ArrayList<>();
        //Financial3202FileParam financial3202FileParam;
        for (Map.Entry<String, List<PaymentRecDetail>> stringListEntry : paymentRecDetailMapBySettleId.entrySet()) {
            //获取settleId
            String settleId = stringListEntry.getKey();
            if("0000".equals(settleId)){
                continue;
            }
            Optional<ClinicSettle> settleOptional = clinicSettles.stream().filter(e -> settleId.equals(e.getSetlId())).findAny();
            Optional<ClinicUnSettle> unSettleOptional = clinicUnSettles.stream().filter(e -> settleId.equals(e.getSettleId())).findAny();
            if(!settleOptional.isPresent()&&!unSettleOptional.isPresent()){
                throw new ServiceException("付款详情中结算id在医保记录中不存在，错误信息：paymentId："+stringListEntry.getValue().get(0).getReconciliationId()+"；医保结算id"+settleId);
            }
            if(settleOptional.isPresent()&&unSettleOptional.isPresent()){
                throw new ServiceException("付款详情中结算id在医保记录中同时存在正结和反结，错误信息：paymentId："+stringListEntry.getValue().get(0).getReconciliationId()+"；医保结算id"+settleId);
            }
            //校对金额
            for (PaymentRecDetail paymentRecDetail : stringListEntry.getValue()) {
                if(YbPayment.SELF_CASH_PAY.getValue().equals(paymentRecDetail.getPayEnum())&&!"0000".equals(paymentRecDetail.getPayTransNo())&&!"0000".equals(paymentRecDetail.getPayTransText())){
                    cashPay = cashPay.add(paymentRecDetail.getAmount());
                }
            }
        }


        FinancialSettlement3202Param financialSettlement3202Param = new FinancialSettlement3202Param();
        financialSettlement3202Param.setCashPayamt(cashPay).setMedfeeSumamt(a)
                .setFundPaySumamt(b).setStmtBegndate(stmtBegndate).setStmtEnddate(stmtEnddate);

        return financialSettlement3202Param;
    }

    public void uploadFinancialSettlement3202Param(FinancialSettlement3202Param financialSettlement3202Param) {


    }
}
