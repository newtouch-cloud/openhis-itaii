/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.core.common.core.domain.model.LoginUser;
import com.core.common.exception.ServiceException;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.DateUtils;
import com.core.common.utils.SecurityUtils;
import com.core.common.utils.StringUtils;
import com.core.system.service.impl.SysUserServiceImpl;
import com.openhis.administration.domain.*;
import com.openhis.administration.service.*;
import com.openhis.clinical.domain.Condition;
import com.openhis.clinical.domain.ConditionDefinition;
import com.openhis.clinical.service.IConditionDefinitionService;
import com.openhis.clinical.service.IConditionService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.YbCommonConstants;
import com.openhis.common.enums.*;
import com.openhis.common.enums.ybenums.*;
import com.openhis.financial.domain.Contract;
import com.openhis.financial.domain.PaymentRecDetail;
import com.openhis.financial.domain.PaymentReconciliation;
import com.openhis.financial.service.IContractService;
import com.openhis.financial.service.IPaymentRecDetailService;
import com.openhis.financial.service.IPaymentReconciliationService;
import com.openhis.medication.domain.Medication;
import com.openhis.medication.domain.MedicationDefinition;
import com.openhis.medication.domain.MedicationDispense;
import com.openhis.medication.domain.MedicationRequest;
import com.openhis.medication.service.IMedicationDefinitionService;
import com.openhis.medication.service.IMedicationDispenseService;
import com.openhis.medication.service.IMedicationRequestService;
import com.openhis.medication.service.IMedicationService;
import com.openhis.workflow.domain.*;
import com.openhis.workflow.service.*;
import com.openhis.workflow.service.impl.InventoryItemServiceImpl;
import com.openhis.yb.domain.ClinicPreSettle;
import com.openhis.yb.domain.ClinicReg;
import com.openhis.yb.domain.ClinicSettle;
import com.openhis.yb.domain.InfoPerson;
import com.openhis.yb.dto.*;
import com.openhis.yb.mapper.MedicalInsuranceMapper;
import com.openhis.yb.model.Clinic2207OrderParam;
import com.openhis.yb.service.*;
import com.openhis.ybcatalog.domain.CatalogDrugInfo;
import com.openhis.ybcatalog.domain.CatalogMedicalHerbInfo;
import com.openhis.ybcatalog.service.ICatalogDrugInfoService;
import com.openhis.ybcatalog.service.ICatalogMedicalHerbInfoService;

/**
 * 医保接口调用工具 todo:sjq 若部署云服务，该工具类内所有的查询原则上增加租户的查询条件
 *
 * @author SunJQ
 * @date 2025-04-03
 */
@Component
public class YbParamBuilderUtil {

    /********************* Mapper服务 *******************/
    @Autowired
    private MedicalInsuranceMapper medicalInsuranceMapper;
    /********************* 业务实体服务 *******************/
    @Autowired
    private IEncounterService iEncounterService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IEncounterDiagnosisService iEncounterDiagnosisService;
    @Autowired
    private IConditionService iConditionService;
    @Autowired
    private IConditionDefinitionService iConditionDefinitionService;
    @Autowired
    private IEncounterParticipantService iEncounterParticipantService;
    @Autowired
    private IPractitionerService iPractitionerService;
    @Autowired
    private IOrganizationService iOrganizationService;
    @Autowired
    private IChargeItemService iChargeItemService;
    @Autowired
    private IMedicationDefinitionService iMedicationDefinitionService;
    @Autowired
    private IDeviceDefinitionService iDeviceDefinitionService;
    @Autowired
    private IActivityDefinitionService iActivityDefinitionService;
    @Autowired
    private IContractService iContractService;
    @Autowired
    private IPatientService iPatientService;
    @Autowired
    private IPaymentReconciliationService iPaymentReconciliationService;
    @Autowired
    private IPaymentRecDetailService iPaymentRecDetailService;
    @Autowired
    private IServiceRequestService iServiceRequestService;
    @Autowired
    private IMedicationRequestService iMedicationRequestService;
    @Autowired
    private IDeviceRequestService iDeviceRequestService;
    @Autowired
    private IMedicationService iMedicationService;
    @Autowired
    private IDeviceService iDeviceService;
    @Autowired
    private IMedicationDispenseService iMedicationDispenseService;
    @Autowired
    private IDeviceDispenseService iDeviceDispenseService;
    @Autowired
    private InventoryItemServiceImpl inventoryItemService;
    @Autowired
    private IChargeItemDefinitionService iChargeItemDefinitionService;
    @Autowired
    private IChargeItemDefDetailService iChargeItemDefDetailService;
    @Autowired
    private ISupplyRequestService iSupplyRequestService;
    @Autowired
    private ISupplyDeliveryService iSupplyDeliveryService;
    @Autowired
    private ISupplierService iSupplierService;
    @Autowired
    private IInvoiceService iInvoiceService;
    @Autowired
    private SysUserServiceImpl userService;
    @Autowired
    private IHealthcareServiceService iHealthcareServiceService;
    @Autowired
    private AssignSeqUtil assignSeqUtil;
    /********************* 医保实体服务 *******************/
    // @Autowired
    // private IReadcardService iReadcardService;
    @Autowired
    private ICatalogDrugInfoService iCatalogDrugInfoService;
    @Autowired
    private ICatalogMedicalHerbInfoService iCatalogMedicalHerbInfoService;
    @Autowired
    private IPerinfoService iPerinfoService;
    @Autowired
    private IRegService iRegService;
    @Autowired
    private IClinicPreSettleService iClinicPreSettleService;
    @Autowired
    private IClinicSettleService iClinicSettleService;
    @Autowired
    private IClinicUnSettleService iClinicUnSettleService;

    /**
     * 获取基础读卡实体
     *
     * @param certType 就诊凭证
     * @param certNo 凭证编号
     * @return 读卡基础实体
     */
    public Info1101ReadcardParam getReadCard(String certType, String certNo) {
        YbMdtrtCertType ybCertType = YbMdtrtCertType.getByValue(certType);
        // YbPsnCertType ybPsnCertType = YbPsnCertType.getByValue(psnCertType);
        if (ybCertType == null) {
            throw new ServiceException("未选择就诊凭证类型");
        }
        Info1101ReadcardParam readcard = new Info1101ReadcardParam();
        readcard.setMdtrtCertNo(certNo).setMdtrtCertType(ybCertType.getValue());
        // if (ybPsnCertType != null) {
        // readcard.setPsnCertType(ybPsnCertType.getValue());
        // } else {
        // readcard.setPsnCertType(ybCertType.getValue());
        // }
        return readcard;
    }

    /**
     * 【2201】组装医保挂号参数
     *
     * @return
     */
    public ClinicReg getReg(YbMdtrtCertType ybMdtrtCertType, String busiCardInfo, Long encounterId, YbMedType medType,
        Integer tenantId) {
        // 查询此次就诊信息(此处需要业务上保证下述两个实体一对一关系)
        Encounter encounter = iEncounterService.getById(encounterId);
        Account account =
            accountService.getOne(new LambdaQueryWrapper<Account>().eq(Account::getEncounterId, encounterId)
                .eq(Account::getTenantId, tenantId).ne(Account::getTypeCode, AccountType.PERSONAL_CASH_ACCOUNT)
                .eq(Account::getEncounterFlag, Whether.YES.getValue()));
        if (account == null || encounter == null) {
            throw new ServiceException("未查询到就诊信息");
        }
        Patient patient = iPatientService.getById(encounter.getPatientId());
        if (patient == null) {
            throw new ServiceException("未查询到患者信息");
        }
        YbMdtrtCertType certType = YbMdtrtCertType.getByValue(account.getTypeCode());// 2025/05/28 该值存01/02/03
        if (certType == null) {
            throw new ServiceException("未查询到就诊凭证信息");
        }
        InfoPerson perinfo = iPerinfoService.getOne(new LambdaQueryWrapper<InfoPerson>()
            .eq(InfoPerson::getCertno, patient.getIdCard()).eq(InfoPerson::getTenantId, tenantId)
            .orderByDesc(InfoPerson::getCreateTime).last(YbCommonConstants.sqlConst.LIMIT1));
        if (perinfo == null) {
            throw new ServiceException("未查询到就诊凭证信息");
        }
        // InfoReadcard1101Param readcard = iReadcardService.getOne(new LambdaQueryWrapper<InfoReadcard1101Param>()
        // .eq(InfoReadcard1101Param::getPsnCertType, certType.getValue())
        // .eq(InfoReadcard1101Param::getCertno, account.getNo()).eq(InfoReadcard1101Param::getTenantId, tenantId));
        // Info1101ReadcardParam readcard = JSONObject.parseObject(perinfo.getResult1101(),
        // Info1101ReadcardParam.class);
        // if (readcard == null) {
        // throw new ServiceException("未查询到就诊凭证信息");
        // }
        // 主治医生查询
        EncounterParticipant participant = null;
        Practitioner doctor = null;
        List<EncounterParticipant> list =
            iEncounterParticipantService.list(new LambdaQueryWrapper<EncounterParticipant>()
                .eq(EncounterParticipant::getEncounterId, encounterId).eq(EncounterParticipant::getTenantId, tenantId));
        for (EncounterParticipant encounterParticipant : list) {
            if (ParticipantType.ADMITTER.getCode().equals(encounterParticipant.getTypeCode())) {
                participant = encounterParticipant;
            }
        }
        if (participant == null) {
            throw new ServiceException("未查询到就诊医生信息");
        }
        doctor = iPractitionerService.getById(participant.getPractitionerId());
        if (doctor == null) {
            throw new ServiceException("未查询到就诊医生信息");
        }

        // 查询科室信息
        Organization organization = iOrganizationService.getById(doctor.getOrgId());
        if (organization == null) {
            throw new ServiceException("未查询到就诊科室信息");
        }

        ClinicReg reg = new ClinicReg();
        reg.setPsnNo(perinfo.getPsnNo()).setInsutype(perinfo.getInsutype()).setBegntime(new Date())
            .setMdtrtCertType(ybMdtrtCertType.getValue()).setMdtrtCertNo(busiCardInfo)
            .setIptOtpNo(encounter.getBusNo() + "-" + medType.getValue()).setAtddrNo(doctor.getYbNo())
            .setDrName(doctor.getName()).setDeptCode(organization.getYbNo()).setDeptName(organization.getName())
            .setCaty(organization.getCaty()).setInsuplcAdmdvs(perinfo.getInsuplcAdmdvs())
            .setMedType(medType.getValue());

        return reg;
    }

    /**
     * 【2203】组装就诊信息参数
     *
     * @return
     */
    public Clinic2203MedicalParam getClinicMedical2203Param(Long encounterId, Integer tenantId,
        ClinicReg2201Output reg2201Output) {

        // 查询诊断信息
        EncounterDiagnosis mainEncounterDiagnosis = null;
        List<EncounterDiagnosis> encounterDiagnosis =
            iEncounterDiagnosisService.list(new LambdaQueryWrapper<EncounterDiagnosis>()
                .eq(EncounterDiagnosis::getEncounterId, encounterId).eq(EncounterDiagnosis::getTenantId, tenantId));
        for (EncounterDiagnosis diagnosis : encounterDiagnosis) {
            if (Objects.equals(Whether.YES.getValue(), diagnosis.getMaindiseFlag())) {
                mainEncounterDiagnosis = diagnosis;
                break;
            }
        }
        if (encounterDiagnosis.isEmpty()) {
            throw new ServiceException("未查询到诊断信息");
        }
        if (mainEncounterDiagnosis == null) {
            throw new ServiceException("未查询到主诊断信息");
        }
        // 查询主诊断
        Condition mainCondition = iConditionService.getById(mainEncounterDiagnosis.getConditionId());
        if (mainCondition == null) {
            throw new ServiceException("未查询到主诊断信息");
        }
        // 查询诊断定义信息
        ConditionDefinition conditionDefinition = iConditionDefinitionService.getById(mainCondition.getDefinitionId());

        // 组装诊断信息参数
        List<Clinic2203DiseInfoParam> diseinfo = getDiseinfo(encounterDiagnosis);

        Clinic2203MedicalParam medical2203Param = new Clinic2203MedicalParam();

        medical2203Param.setMdtrtId(reg2201Output.getMdtrtId()).setPsnNo(reg2201Output.getPsnNo())
            .setBegntime(reg2201Output.getBegntime()).setMedType(reg2201Output.getMedType())
            .setMainCondDscr(mainCondition.getDescription()).setInsuplcAdmdvs(reg2201Output.getInsuplcAdmdvs())
            .setDiseCodg(mainCondition.getYbNo()).setDiseName(conditionDefinition.getName()).setDiseinfoList(diseinfo);

        return medical2203Param;
    }

    /**
     * 【2203】组装诊断信息参数
     *
     * @return
     */
    public List<Clinic2203DiseInfoParam> getDiseinfo(List<EncounterDiagnosis> encounterDiagnosisList) {
        List<Clinic2203DiseInfoParam> diseinfos = new ArrayList<>();
        for (EncounterDiagnosis encounterDiagnosis : encounterDiagnosisList) {
            Condition condition = iConditionService.getById(encounterDiagnosis.getConditionId());
            if (condition == null) {
                continue;
            }
            ConditionDefinition conditionDefinition = iConditionDefinitionService.getById(condition.getDefinitionId());
            Practitioner doctor = iPractitionerService.getById(condition.getRecorderId());
            if (doctor == null) {
                continue;
            }
            // 查询科室信息
            Organization organization = iOrganizationService.getById(doctor.getOrgId());
            if (organization == null) {
                throw new ServiceException("未查询到科室信息");
            }

            Clinic2203DiseInfoParam diseinfo = new Clinic2203DiseInfoParam();
            diseinfo.setDiagCode(condition.getYbNo()).setDiagName(conditionDefinition.getName())
                .setDiagDept(organization.getYbNo()).setDiseDorName(doctor.getName()).setDiseDorNo(doctor.getYbNo())
                .setDiagTime(condition.getRecordedDatetime()).setValiFlag(Whether.YES.getValue().toString())
                .setDiagType(conditionDefinition.getTypeCode())
                .setDiagSrtNo(encounterDiagnosis.getDiagSrtNo().toString());

            diseinfos.add(diseinfo);

        }
        return diseinfos;
    }

    /**
     * 【2204】组装费用明细参数
     *
     * @return
     */
    public List<Clinic2204FeeDetailParam> getClinicFeedetailList(Integer tenantId, ClinicReg2201Output reg,
        String chrgBchno, List<Long> chargeItemIds) {

        List<Clinic2204FeeDetailParam> clincFeedetailList = new ArrayList<Clinic2204FeeDetailParam>();
        // 查询收费项
        List<ChargeItem> ChargeItemList = iChargeItemService.list(new LambdaQueryWrapper<ChargeItem>()
            .in(ChargeItem::getId, chargeItemIds).eq(ChargeItem::getTenantId, tenantId));

        // 是否是电子处方
        Whether eFlag = Whether.getByValue(0);

        for (ChargeItem chargeItem : ChargeItemList) {
            Clinic2204FeeDetailParam clinicFeedetail = new Clinic2204FeeDetailParam();
            // 查询项目信息
            String productTable = chargeItem.getProductTable();
            if (CommonConstants.TableName.MED_MEDICATION_DEFINITION.equals(productTable)) {
                MedicationDefinition medication = iMedicationDefinitionService.getById(chargeItem.getProductId());
                if (medication == null) {
                    throw new ServiceException("未查询到药品信息");
                }
                clinicFeedetail.setMedListCodg(medication.getYbNo()).setMedinsListCodg(medication.getBusNo());
            } else if (CommonConstants.TableName.ADM_DEVICE_DEFINITION.equals(productTable)) {
                DeviceDefinition device = iDeviceDefinitionService.getById(chargeItem.getProductId());
                if (device == null) {
                    throw new ServiceException("未查询到药品信息");
                }
                clinicFeedetail.setMedListCodg(device.getYbNo()).setMedinsListCodg(device.getBusNo());
            } else {
                ActivityDefinition activity = iActivityDefinitionService.getById(chargeItem.getProductId());
                if (activity == null) {
                    throw new ServiceException("未查询到药品信息");
                }
                clinicFeedetail.setMedListCodg(activity.getYbNo()).setMedinsListCodg(activity.getBusNo());
            }

            // 开单科室查询
            Organization org = iOrganizationService.getById(chargeItem.getRequestingOrgId());
            if (org == null) {
                throw new ServiceException("未查询到开单科室信息");
            }
            // 开立医生查询
            Practitioner doctor = iPractitionerService.getById(chargeItem.getEntererId());
            if (doctor == null) {
                throw new ServiceException("未查询到开立医生信息");
            }

            // 外购处方标志（文档上没有详细介绍） 2025/04/14经确认，暂定非处方流转传0，处方流转传1
            clinicFeedetail
                .setFeedetlSn(
                    chargeItem.getBusNo() + assignSeqUtil.getSeqByDay(AssignSeqEnum.YB_CLINIC_FEE.getPrefix(), 5))
                .setMdtrtId(reg.getMdtrtId()).setPsnNo(reg.getPsnNo()).setChrgBchno(chrgBchno)
                .setFeeOcurTime(chargeItem.getEnteredDate())
                .setDetItemFeeSumamt(chargeItem.getTotalPrice().doubleValue())
                .setCnt(chargeItem.getQuantityValue().doubleValue()).setPric(chargeItem.getUnitPrice().doubleValue())
                .setBilgDeptCodg(org.getYbNo()).setBilgDeptName(org.getName()).setBilgDrCodg(doctor.getYbNo())
                // 默认无需审批
                .setBilgDrName(doctor.getName()).setHospApprFlag(YbHospApprFlag.NO_APPROVAL_REQUIRED.getValue())
                .setRxCircFlag(eFlag.getValue().toString());

            clincFeedetailList.add(clinicFeedetail);
        }
        return clincFeedetailList;
    }

    /**
     * 【2204】门诊费用明细信息
     *
     * @param tenantId 租户id
     * @return
     */
    public Clinic2204OrderParam getClinicOrder2204Param(Integer tenantId, List<Long> chargeItemIds,
        ClinicReg2201Output reg2201Output) {

        Clinic2204OrderParam clinicOrderPage = new Clinic2204OrderParam();

        // 排番
        clinicOrderPage.setChrgBchno(assignSeqUtil.getSeqByDay(AssignSeqEnum.YB_CLINIC_ORDER.getPrefix(), 12));
        clinicOrderPage.setChrgBchno(clinicOrderPage.getChrgBchno());

        List<Clinic2204FeeDetailParam> clincFeedetailList =
            getClinicFeedetailList(tenantId, reg2201Output, clinicOrderPage.getChrgBchno(), chargeItemIds);

        clinicOrderPage.setFeedetail(clincFeedetailList).setInsuplcAdmdvs(reg2201Output.getInsuplcAdmdvs());
        return clinicOrderPage;
    }

    /**
     * 【2205】费用明细撤销
     * 
     * @return
     */
    public Clinic2205OrderParam getClinic2205OrderParam(Long encounterId, String ChrgBchno) {
        Clinic2205OrderParam clinicOrderPage = new Clinic2205OrderParam();
        Encounter encounter = iEncounterService.getById(encounterId);
        ClinicReg reg =
            iRegService.getOne(new LambdaQueryWrapper<ClinicReg>().eq(ClinicReg::getIptOtpNo, encounter.getBusNo()));
        return clinicOrderPage.setMdtrtId(reg.getMdtrtId()).setPsnNo(reg.getPsnNo()).setChrgBchno(ChrgBchno)
            .setInsuplcAdmdvs(reg.getInsuplcAdmdvs());
    }

    /**
     * 【2206】门诊预结算参数
     *
     * @return
     */
    /**
     * 【2206】门诊预结算参数
     *
     * @param payFee 2204返回的费用总额
     * @param chrgBchno 收费批次号
     * @return
     */
    public Clinic2206OrderParam getClinicOrder2206Param(BigDecimal payFee, ClinicReg2201Output reg, String chrgBchno,
        String busiCardInfo) {

        // 个人结算方式
        YbPsnSetlWay setlWay = YbPsnSetlWay.getByValue("01");

        Clinic2206OrderParam clinicOrder = new Clinic2206OrderParam();
        clinicOrder.setPsnNo(reg.getPsnNo()).setMdtrtCertType(reg.getMdtrtCertType()).setMdtrtCertNo(busiCardInfo)
            .setMedType(reg.getMedType()).setMedfeeSumamt(payFee.doubleValue()).setPsnSetlway(setlWay.getValue())
            .setMdtrtId(reg.getMdtrtId()).setChrgBchno(chrgBchno).setAcctUsedFlag(Whether.YES.getCode())
            .setInsutype(reg.getInsutype()).setInsuplcAdmdvs(reg.getInsuplcAdmdvs());

        return clinicOrder;
    }

    /**
     * 【2207】门诊结算参数
     *
     * @return
     */
    public Clinic2207OrderParam getClinicOrder2207(Long encounterId, Integer tenantId, String psnSetlWay,
        String chrgBchno, String medType) {
        // 查询此次就诊信息(此处需要业务上保证下述两个实体一对一关系)
        Encounter encounter = iEncounterService.getById(encounterId);

        YbMedType ybMedType = YbMedType.getByValue(medType);
        if (ybMedType == null) {
            ybMedType = YbMedType.GENERAL_OUTPATIENT;
        }

        // 院内唯一流水号查询挂号信息
        String busNo = encounter.getBusNo();
        ClinicReg reg = iRegService.getOne(new LambdaQueryWrapper<ClinicReg>().eq(ClinicReg::getIptOtpNo, busNo)
            .eq(ClinicReg::getTenantId, tenantId).eq(ClinicReg::getMedType, ybMedType.getValue()));

        // 个人结算方式
        YbPsnSetlWay setlWay = YbPsnSetlWay.getByValue(psnSetlWay);
        if (setlWay == null) {
            setlWay = YbPsnSetlWay.PSN_SETLWAY01;
        }

        // ClinicSetlinfo2208Param clinicSetlinfo = iClincSetlinfoService.getOne(
        // new LambdaQueryWrapper<ClinicSetlinfo2208Param>().eq(ClinicSetlinfo2208Param::getMdtrtId, reg.getMdtrtId())
        // .eq(ClinicSetlinfo2208Param::getOrderId, chrgBchno));

        ClinicPreSettle clinicPreSettle = iClinicPreSettleService.getOne(new LambdaQueryWrapper<ClinicPreSettle>()
            .eq(ClinicPreSettle::getMdtrtId, reg.getMdtrtId()).eq(ClinicPreSettle::getChrgBchno, chrgBchno));

        Clinic2206OrderResult clinic2206OrderResult =
            JSONObject.parseObject(clinicPreSettle.getResult2206(), Clinic2206OrderResult.class);

        Clinic2207OrderParam clinicOrder = new Clinic2207OrderParam();
        clinicOrder.setPsnNo(reg.getPsnNo()).setMdtrtCertType(reg.getMdtrtCertType())
            // todo：sjq 医疗费用总额需要用ChargeItem的和
            .setMdtrtCertNo(reg.getMdtrtCertNo()).setMedType(reg.getMedType())
            .setMedfeeSumamt(clinic2206OrderResult.getMedfeeSumamt().doubleValue()).setPsnSetlway(setlWay.getValue())
            .setMdtrtId(reg.getMdtrtId()).setChrgBchno(chrgBchno).setAcctUsedFlag(Whether.YES.getCode())
            .setInsutype(reg.getInsutype()).setFulamtOwnpayAmt(clinic2206OrderResult.getFulamtOwnpayAmt().doubleValue())
            .setOverlmtSelfpay(clinic2206OrderResult.getOverlmtSelfpay().doubleValue())
            .setPreselfpayAmt(clinic2206OrderResult.getPreselfpayAmt().doubleValue())
            .setInscpScpAmt(clinic2206OrderResult.getInscpScpAmt().doubleValue());

        return clinicOrder;
    }

    /**
     * 【2208】门诊结算撤销参数
     *
     * @return
     */
    public Clinic2208UnSetlInfoParam getClinicOrder2208(Integer tenantId, String settleId) {
        // 查询此次就诊信息(此处需要业务上保证下述两个实体一对一关系)
        // Encounter encounter = iEncounterService.getById(encounterId);

        ClinicSettle clinicSettle = iClinicSettleService.getBySettleId(settleId);
        if (clinicSettle == null) {
            throw new ServiceException("未查询到医保结算信息");
        }

        // 院内唯一流水号查询挂号信息
        // String busNo = encounter.getBusNo();
        ClinicReg reg =
            iRegService.getOne(new LambdaQueryWrapper<ClinicReg>().eq(ClinicReg::getMdtrtId, clinicSettle.getMdtrtId())
                .eq(ClinicReg::getTenantId, tenantId).eq(ClinicReg::getMdtrtId, clinicSettle.getMdtrtId()));
        if (reg == null) {
            throw new ServiceException("未查询到医保结算信息");
        }
        Clinic2208UnSetlInfoParam clinicSetl = new Clinic2208UnSetlInfoParam();
        clinicSetl.setPsnNo(reg.getPsnNo()).setMdtrtId(clinicSettle.getMdtrtId()).setSetlId(settleId)
            .setInsuplcAdmdvs(reg.getInsuplcAdmdvs());

        return clinicSetl;
    }

    /**
     * 【3201】结算对总账
     *
     * @return
     */
    public Financial3201Param getFinancial3201Param(Settlement3201WebParam settlement3201WebParam, Settlement3201DetailDto reconciliation) {

        // 查询合同信息
        Contract contract = iContractService.getByContractNo(settlement3201WebParam.getContractNo());
        if (contract == null) {
            throw new ServiceException("未查询到合同信息");
        }
        YbClrType clrType = YbClrType.getByValue(settlement3201WebParam.getClrType());
        YbInsuType insuType = YbInsuType.getByValue(settlement3201WebParam.getInsuType().toString());
        if (clrType == null) {
            clrType = YbClrType.OUTPATIENT_CLINIC;
        }
        if (insuType == null) {
            insuType = YbInsuType.YB_INSU_TYPE310;
        }

        Financial3201Param financial3201Param = new Financial3201Param();
        financial3201Param.setInsutype(insuType.getValue()).setClrType(clrType.getValue())
            .setSetlOptins(contract.getAdmVs()).setStmtBegndate(settlement3201WebParam.getStmtBegnDate())
            .setStmtEnddate(settlement3201WebParam.getStmtEndDate())
            .setMedfeeSumamt(reconciliation.getMedFeeSumAmt().doubleValue())
            .setFundPaySumamt(reconciliation.getFundPaySumAmt().doubleValue())
            .setAcctPay(reconciliation.getAcctPay().add(reconciliation.getAcctGjPay()).doubleValue())
            .setFixmedinsSetlCnt(reconciliation.getFixMedInsSetlCnt()).setInsuplcAdmdvs(SecurityUtils.getLoginUser().getOptionJson().getString("admvs"));

        return financial3201Param;
    }

    /**
     * 【3203】清算申请
     *
     * @param financial3203WebParam 前台参数
     * @return 3203参数
     */
    public Financial3203Param getFinancial3203Param(Financial3203WebParam financial3203WebParam) {
        Long orgId = SecurityUtils.getLoginUser().getHospitalId();
        iOrganizationService.getById(orgId);

        YbClrType clrType = YbClrType.getByValue(financial3203WebParam.getClrType());
        if (clrType == null) {
            clrType = YbClrType.OUTPATIENT_CLINIC;
        }

        Financial3203Dto financial3203VO = medicalInsuranceMapper.get3203Param(financial3203WebParam.getBegndate(),
            financial3203WebParam.getEnddate(), SecurityUtils.getLoginUser().getTenantId(),
            financial3203WebParam.getClrType(), YbPayment.SELF_YB_ZH_PAY.getValue(),
            YbPayment.SELF_YB_ZH_GJ_VALUE.getValue(), YbPayment.SELF_CASH_VALUE.getValue(),
            YbPayment.SELF_CASH_VX_VALUE.getValue(), YbPayment.SELF_CASH_ALI_VALUE.getValue(),
            YbPayment.SELF_CASH_UNION_VALUE.getValue(), YbPayment.YB_FUND_PAY.getValue());

        // 数据处理
        financial3203VO
            .setAcctGjPay(financial3203VO.getAcctGjPay() == null ? BigDecimal.ZERO : financial3203VO.getAcctGjPay());
        financial3203VO
            .setAcctPay(financial3203VO.getAcctPay() == null ? BigDecimal.ZERO : financial3203VO.getAcctPay());
        financial3203VO.setFundPaySumAmt(
            financial3203VO.getFundPaySumAmt() == null ? BigDecimal.ZERO : financial3203VO.getFundPaySumAmt());
        financial3203VO.setMedFeeSumAmt(
            financial3203VO.getMedFeeSumAmt() == null ? BigDecimal.ZERO : financial3203VO.getMedFeeSumAmt());
        financial3203VO.setSelfPayCash(
            financial3203VO.getSelfPayCash() == null ? BigDecimal.ZERO : financial3203VO.getSelfPayCash());
        financial3203VO
            .setSelfPayALI(financial3203VO.getSelfPayALI() == null ? BigDecimal.ZERO : financial3203VO.getSelfPayALI());
        financial3203VO
            .setSelfPayVX(financial3203VO.getSelfPayVX() == null ? BigDecimal.ZERO : financial3203VO.getSelfPayVX());
        financial3203VO.setSelfPayUNION(
            financial3203VO.getSelfPayUNION() == null ? BigDecimal.ZERO : financial3203VO.getSelfPayUNION());

        BigDecimal cashSum = financial3203VO.getSelfPayCash().add(financial3203VO.getSelfPayVX())
            .add(financial3203VO.getSelfPayUNION()).add(financial3203VO.getSelfPayALI());

        Financial3203Param financial3203Param = new Financial3203Param();
        financial3203Param.setClrType(financial3203WebParam.getClrType())
            .setMedfeeSumamt(financial3203VO.getMedFeeSumAmt()).setMedSumfee(financial3203VO.getMedSumfee())
            .setCashPayamt(cashSum).setAcctPay(financial3203VO.getAcctPay())
            .setBegndate(financial3203WebParam.getBegndate()).setEnddate(financial3203WebParam.getEnddate())
            .setClrWay(financial3203WebParam.getClrWay())
            .setSetlym(financial3203WebParam.getBegndate().toString().substring(0, 7))
            .setFundAppySum(financial3203VO.getFundPaySumAmt()).setPsntime(financial3203VO.getPsntime());

        return financial3203Param;
    }

    /**
     * 【3203A】清算申请【吉林省】
     * 
     * @param financial3203AWebParam
     * @return
     */
    //public Financial3203AParam getFinancial3203AParam(Financial3203AWebParam financial3203AWebParam) {
    //
    //    YbClrType clrType = YbClrType.getByValue(financial3203AWebParam.getClrType());
    //    if (clrType == null) {
    //        clrType = YbClrType.OUTPATIENT_CLINIC;
    //    }
    //
    //    //Financial3203Dto financial3203VO = medicalInsuranceMapper.get3203Param(financial3203AWebParam.getStmtBegnDate(),
    //    //    financial3203AWebParam.getStmtEndDate(), SecurityUtils.getLoginUser().getTenantId(), clrType.getValue(),
    //    //    YbPayment.SELF_YB_ZH_PAY.getValue(), YbPayment.SELF_YB_ZH_GJ_VALUE.getValue(),
    //    //    YbPayment.SELF_CASH_VALUE.getValue(), YbPayment.SELF_CASH_VX_VALUE.getValue(),
    //    //    YbPayment.SELF_CASH_ALI_VALUE.getValue(), YbPayment.SELF_CASH_UNION_VALUE.getValue(),
    //    //    YbPayment.YB_FUND_PAY.getValue());
    //
    //
    //    // 数据处理
    //    financial3203VO
    //        .setAcctGjPay(financial3203VO.getAcctGjPay() == null ? BigDecimal.ZERO : financial3203VO.getAcctGjPay());
    //    financial3203VO
    //        .setAcctPay(financial3203VO.getAcctPay() == null ? BigDecimal.ZERO : financial3203VO.getAcctPay());
    //    financial3203VO.setFundPaySumAmt(
    //        financial3203VO.getFundPaySumAmt() == null ? BigDecimal.ZERO : financial3203VO.getFundPaySumAmt());
    //    financial3203VO.setMedFeeSumAmt(
    //        financial3203VO.getMedFeeSumAmt() == null ? BigDecimal.ZERO : financial3203VO.getMedFeeSumAmt());
    //    financial3203VO.setSelfPayCash(
    //        financial3203VO.getSelfPayCash() == null ? BigDecimal.ZERO : financial3203VO.getSelfPayCash());
    //    financial3203VO
    //        .setSelfPayALI(financial3203VO.getSelfPayALI() == null ? BigDecimal.ZERO : financial3203VO.getSelfPayALI());
    //    financial3203VO
    //        .setSelfPayVX(financial3203VO.getSelfPayVX() == null ? BigDecimal.ZERO : financial3203VO.getSelfPayVX());
    //    financial3203VO.setSelfPayUNION(
    //        financial3203VO.getSelfPayUNION() == null ? BigDecimal.ZERO : financial3203VO.getSelfPayUNION());
    //
    //    BigDecimal cashSum = financial3203VO.getSelfPayCash().add(financial3203VO.getSelfPayVX())
    //        .add(financial3203VO.getSelfPayUNION()).add(financial3203VO.getSelfPayALI());
    //
    //    Financial3203AParam busMonthSetlApply = new Financial3203AParam();
    //    busMonthSetlApply.setClrType(clrType.getValue()).setMedfeeSumamt()
    //        .setMedSumfee().setCashPayamt()
    //        .setAcctPay()
    //        .setBegndate(DateUtils.dateTime("yyyy-MM-dd", financial3203AWebParam.getStmtBegnDate().toString()))
    //        .setEnddate(DateUtils.dateTime("yyyy-MM-dd", financial3203AWebParam.getStmtEndDate().toString()))
    //        .setClrOptins(financial3203AWebParam.getClrOptins());
    //
    //    return busMonthSetlApply;
    //}

    /**
     * 【3101】明细审核事前分析服务参数
     *
     * @return
     */
    public FsiConsultation3101Param getHealthcareConsultationDetail3101(String syscode, List<Long> encounterId,
        YbPayLoc payLoc, Whether inOutDiagType) {

        Integer tenantId = SecurityUtils.getLoginUser().getTenantId();
        // 查询就诊记录
        List<Encounter> encounters = iEncounterService.list(
            new LambdaQueryWrapper<Encounter>().in(Encounter::getId, encounterId).eq(Encounter::getTenantId, tenantId));

        List<FsiPatient3101Param> patients = new ArrayList<>();
        for (Encounter encounter : encounters) {
            // 查询账户信息
            Account account = accountService.getOne(new LambdaQueryWrapper<Account>()
                .eq(Account::getEncounterId, encounter.getId()).eq(Account::getEncounterFlag, Whether.YES.getValue())
                .orderByDesc(Account::getId).last(YbCommonConstants.sqlConst.LIMIT1));
            // 查询患者信息
            Patient patient = iPatientService.getOne(new LambdaQueryWrapper<Patient>()
                .in(Patient::getId, encounter.getPatientId()).eq(Patient::getTenantId, tenantId)
                .orderByDesc(Patient::getId).last(YbCommonConstants.sqlConst.LIMIT1));

            // 查询就诊信息集合
            List<FsiEncounter3101Param> list = getFsiEncounterList(encounter, patient, tenantId, payLoc, inOutDiagType);

            FsiPatient3101Param patientYb = new FsiPatient3101Param();
            // 患者唯一标识暂定使用id
            patientYb.setPatnId(patient.getId().toString()).setPatnName(patient.getName())
                .setGend(patient.getGenderEnum().toString()).setBrdy(patient.getBirthDate())
                .setPoolarea(account.getYbAreaNo()).setCurrMdtrtId(encounter.getBusNo()).setFsiEncounterDtos(list);

            patients.add(patientYb);
        }

        // 创建实体
        FsiConsultation3101Param healthcareConsultationDetail = new FsiConsultation3101Param();
        healthcareConsultationDetail.setSyscode(syscode).setPatientDtos(patients);

        return healthcareConsultationDetail;
    }

    /**
     * [3101]明细审核事前分析服务参数-就诊信息集合
     * 
     * @return
     */
    private List<FsiEncounter3101Param> getFsiEncounterList(Encounter encounter, Patient patient, Integer tenantId,
        YbPayLoc payLoc, Whether outSetlFlag) {
        // 查询就诊记录
        List<Encounter> encounters = iEncounterService.list(new LambdaQueryWrapper<Encounter>()
            .eq(Encounter::getPatientId, patient.getId()).eq(Encounter::getTenantId, tenantId));

        List<FsiEncounter3101Param> list = new ArrayList<FsiEncounter3101Param>();
        for (Encounter enc : encounters) {
            // 查询机构信息
            Long hospitalIdByOrgId = userService.getHospitalIdByOrgId(enc.getOrganizationId());
            Organization organization = iOrganizationService.getById(hospitalIdByOrgId);
            // 查询医嘱信息
            List<FsiOrder3101Param> fsiOrders = getFsiOrderList(enc, patient, tenantId);
            // 查询手术信息
            List<FsiOperation3101Param> fsiOperations = getFsiOperationList(enc, patient, tenantId);

            YbMedinsType medinsType = YbMedinsType.getByValue(organization.getMedinsType());
            if (medinsType == null) {
                medinsType = YbMedinsType.FIXMEDINS_TYPE1;
            }

            // 查询诊断信息
            List<EncounterDiagnosis> encounterDiagnosisList = iEncounterDiagnosisService
                .list(new LambdaQueryWrapper<EncounterDiagnosis>().eq(EncounterDiagnosis::getEncounterId, enc.getId())
                    .eq(EncounterDiagnosis::getTenantId, enc.getTenantId()));
            EncounterDiagnosis mainEncounterDiagnosis = null;

            for (EncounterDiagnosis encounterDiagnosis : encounterDiagnosisList) {
                if (Whether.YES.getValue().equals(encounterDiagnosis.getMaindiseFlag())) {
                    mainEncounterDiagnosis = encounterDiagnosis;
                }
            }

            Condition condition = iConditionService.getById(mainEncounterDiagnosis.getConditionId());
            ConditionDefinition conditionDefinition = iConditionDefinitionService.getById(condition.getDefinitionId());
            // 查询诊断信息集合
            List<FsiDiagnose3101Param> fsiDiagnoses = getFsiDiagnoseList(encounterDiagnosisList, tenantId);
            // 查询主治医生
            EncounterParticipant encounterParticipant = iEncounterParticipantService.getOne(
                new LambdaQueryWrapper<EncounterParticipant>().eq(EncounterParticipant::getEncounterId, enc.getId())
                    .eq(EncounterParticipant::getTypeCode, ParticipantType.PRINCIPAL_DOCTOR.getCode()));
            Practitioner doctor = iPractitionerService.getOne(new LambdaQueryWrapper<Practitioner>()
                .eq(Practitioner::getUserId, encounterParticipant.getPractitionerId()));
            // 查询科室
            Organization org = iOrganizationService.getById(doctor.getOrgId());
            // 查询医疗类别
            YbClrType clrType = YbClrType.getByValue(enc.getYbClassEnum().toString());
            YbMedMdtrtType ybMedMdtrtType = null;
            switch (clrType) {
                case OUTPATIENT_CLINIC:
                    ybMedMdtrtType = YbMedMdtrtType.OUTPATIENT_CLINIC;
                    break;
                case INPATIENT_CARE:
                    ybMedMdtrtType = YbMedMdtrtType.INPATIENT_CARE;
                    break;
                default:
                    ybMedMdtrtType = YbMedMdtrtType.OTHER;
                    break;
            }
            // 院内唯一流水号查询挂号信息
            String busNo = encounter.getBusNo();
            ClinicReg reg = iRegService.getOne(new LambdaQueryWrapper<ClinicReg>().eq(ClinicReg::getIptOtpNo, busNo)
                .eq(ClinicReg::getTenantId, tenantId));
            // 查询付款实体
            List<PaymentReconciliation> payList = iPaymentReconciliationService.list(
                new LambdaQueryWrapper<PaymentReconciliation>().eq(PaymentReconciliation::getEncounterId, enc.getId())
                    .eq(PaymentReconciliation::getTenantId, tenantId));
            // 自费金额
            BigDecimal ownPay = BigDecimal.ZERO;
            // 自付金额
            BigDecimal selfPay = BigDecimal.ZERO;
            for (PaymentReconciliation paymentReconciliation : payList) {
                // 查询付款详情
                List<PaymentRecDetail> details =
                    iPaymentRecDetailService.list(new LambdaQueryWrapper<PaymentRecDetail>()
                        .eq(PaymentRecDetail::getReconciliationId, paymentReconciliation.getId())
                        .eq(PaymentRecDetail::getTenantId, tenantId));
                for (PaymentRecDetail detail : details) {
                    if (YbPayment.SELF_PAY.getValue().equals(detail.getPayEnum())) {
                        ownPay = ownPay.add(detail.getAmount());
                    }
                    if (YbPayment.SELF_CASH_VALUE.getValue().equals(detail.getPayEnum())
                        || YbPayment.SELF_CASH_VX_VALUE.getValue().equals(detail.getPayEnum())
                        || YbPayment.SELF_CASH_ALI_VALUE.getValue().equals(detail.getPayEnum())
                        || YbPayment.SELF_CASH_UNION_VALUE.getValue().equals(detail.getPayEnum())) {
                        selfPay = selfPay.add(detail.getAmount());
                    }
                }
            }

            FsiEncounter3101Param fsiEncounter = new FsiEncounter3101Param();
            fsiEncounter.setMdtrtId(enc.getBusNo()).setMedinsId(organization.getYbNo())
                .setMedinsName(organization.getName()).setMedinsAdmdvs(organization.getMedinsAdmdvs())
                .setMedinsType(medinsType.getValue()).setMedinsLv(organization.getMedinsLv())
                .setAdmDate(enc.getStartTime()).setDscgDate(enc.getEndTime()).setDscgMainDiseCodg(condition.getYbNo())
                .setDscgMainDiseName(conditionDefinition.getName()).setDrCodg(doctor.getYbNo())
                .setAdmDeptCodg(org.getYbNo()).setAdmDeptName(org.getName())
                // 门诊的入院出院暂定相同，住院的情况应分出一个if
                .setDscgDeptCodg(org.getYbNo()).setDscgDeptName(org.getName())
                .setMedMdtrtType(ybMedMdtrtType.getValue())
                // 生育状态默认未知
                .setMedType(reg.getMedType()).setMedType(YbMatnStas.UNKNOWN.getValue()).setOwnpayAmt(ownPay)
                .setSelfpayAmt(selfPay).setSetlTotlnum(payList.size()).setInsutype(reg.getInsutype())
                .setReimFlag(payLoc.getValue()).setOutSetlFlag(outSetlFlag.getValue().toString());

            list.add(fsiEncounter);
        }

        return list;
    }

    /**
     * [3101]明细审核事前分析服务参数-手术集合
     * 
     * @return
     */
    private List<FsiOperation3101Param> getFsiOperationList(Encounter enc, Patient patient, Integer tenantId) {
        return null;
    }

    /**
     * [3101]明细审核事前分析服务参数-诊断集合
     * 
     * @return
     */
    private List<FsiDiagnose3101Param> getFsiDiagnoseList(List<EncounterDiagnosis> encounterDiagnosisList,
        Integer tenantId) {

        List<FsiDiagnose3101Param> fsiDiagnoses = new ArrayList<FsiDiagnose3101Param>();
        // List<Long> conditionIdList = encounterDiagnosisList.stream()
        // .map(EncounterDiagnosis::getConditionId)
        // .collect(Collectors.toList());
        //
        // List<Condition> conditionList = iConditionService.list(new
        // LambdaQueryWrapper<Condition>().in(Condition::getId, conditionIdList).eq(Condition::getTenantId, tenantId));
        //
        // List<Long> definitionList = conditionList.stream()
        // .map(Condition::getDefinitionId)
        // .collect(Collectors.toList());
        //
        // List<ConditionDefinition> conditionDefinitions = iConditionDefinitionService.list(new
        // LambdaQueryWrapper<ConditionDefinition>().in(ConditionDefinition::getId,
        // definitionList).eq(ConditionDefinition::getTenantId, tenantId));

        Condition condition = null;
        ConditionDefinition conditionDefinition = null;
        int sort = 1;
        for (EncounterDiagnosis encounterDiagnosis : encounterDiagnosisList) {
            FsiDiagnose3101Param fsiDiagnose = new FsiDiagnose3101Param();
            // 出入院诊断及门诊诊断
            if (YbIptDiseTypeCode.DISCHARGE_DIAGNOSIS.getValue()
                .equals(encounterDiagnosis.getIptDiseTypeCode().toString())) {
                fsiDiagnose.setInoutDiseType(YbInOutDiagType.YB_OUT_DIAG_TYPE.getValue());
            } else if (YbIptDiseTypeCode.ADMISSION_DIAGNOSIS.getValue()
                .equals(encounterDiagnosis.getIptDiseTypeCode().toString())) {
                fsiDiagnose.setInoutDiseType(YbInOutDiagType.YB_IN_DIAG_TYPE.getValue());
            } else if (YbIptDiseTypeCode.OUTPATIENT_DIAGNOSIS.getValue()
                .equals(encounterDiagnosis.getIptDiseTypeCode().toString())) {
                //// TODO: sjq 门诊诊断怎么存？
            } else {
                continue;
            }
            condition = iConditionService.getOne(new LambdaQueryWrapper<Condition>()
                .eq(Condition::getId, encounterDiagnosis.getConditionId()).eq(Condition::getTenantId, tenantId));
            if (condition == null) {
                continue;
            } else {
                conditionDefinition = iConditionDefinitionService.getOne(new LambdaQueryWrapper<ConditionDefinition>()
                    .eq(ConditionDefinition::getId, condition.getDefinitionId())
                    .eq(ConditionDefinition::getTenantId, tenantId));
                if (conditionDefinition == null) {
                    continue;
                }
            }
            // 主诊断判断
            if (Whether.YES.getValue().equals(encounterDiagnosis.getMaindiseFlag())) {
                fsiDiagnose.setMaindiseFlag(Whether.YES.getValue().toString());
            }

            fsiDiagnose.setDiseId(condition.getBusNo()).setDiasSrtNo(String.valueOf(sort))
                .setDiseCodg(conditionDefinition.getYbNo()).setDiseName(conditionDefinition.getName())
                .setDiseDate(condition.getRecordedDatetime());

            sort++;

            fsiDiagnoses.add(fsiDiagnose);
        }

        return fsiDiagnoses;
    }

    /**
     * [3101]明细审核事前分析服务参数-处方（医嘱）集合
     * 
     * @return
     */
    private List<FsiOrder3101Param> getFsiOrderList(Encounter enc, Patient patient, Integer tenantId) {
        // 查询医嘱信息
        List<MedicationRequest> medicationRequests =
            iMedicationRequestService.list(new LambdaQueryWrapper<MedicationRequest>()
                .eq(MedicationRequest::getEncounterId, enc.getId()).eq(MedicationRequest::getTenantId, tenantId));

        List<DeviceRequest> deviceRequests = iDeviceRequestService.list(new LambdaQueryWrapper<DeviceRequest>()
            .eq(DeviceRequest::getEncounterId, enc.getId()).eq(DeviceRequest::getTenantId, tenantId));

        List<ServiceRequest> serviceRequests = iServiceRequestService.list(new LambdaQueryWrapper<ServiceRequest>()
            .eq(ServiceRequest::getEncounterId, enc.getId()).eq(ServiceRequest::getTenantId, tenantId));

        for (MedicationRequest medicationRequest : medicationRequests) {
            // 查询药品定义
            Medication medication = iMedicationService.getById(medicationRequest.getDeviceDefId());
            if (medication == null) {
                continue;
            }
            MedicationDefinition medicationDefinition =
                iMedicationDefinitionService.getById(medication.getMedicationDefId());
            if (medicationDefinition == null) {
                continue;
            }
            // 是否是长期医嘱
            Whether whether = Whether.NO;
            if (TherapyTimeType.LONG_TERM.getValue().equals(medicationRequest.getTherapyEnum())) {
                whether = Whether.YES;
            }
            // 查询收费项
            ChargeItem chargeItem = iChargeItemService
                .getOne(new LambdaQueryWrapper<ChargeItem>().eq(ChargeItem::getEncounterId, enc.getId())
                    .eq(ChargeItem::getServiceTable, CommonConstants.TableName.MED_MEDICATION_REQUEST)
                    .eq(ChargeItem::getTenantId, tenantId));
            if (chargeItem == null) {
                continue;
            }
            YbMedChrgItmType medChrgItmType = YbMedChrgItmType.getByCode(chargeItem.getContextEnum());
            if (medChrgItmType == null) {
                continue;
            }
            // 查询医保等级
            YbChrgitmLv chrgitmLv = YbChrgitmLv.getByValue(medicationDefinition.getChrgitmLv());
            if (chrgitmLv == null) {
                chrgitmLv = YbChrgitmLv.SELF_PAY;
            }
            // 查询医保目录价格
            Double price;
            String ybNo = medicationDefinition.getYbNo();
            CatalogDrugInfo drugInfo = iCatalogDrugInfoService.getOne(new LambdaQueryWrapper<CatalogDrugInfo>()
                .eq(CatalogDrugInfo::getMedicalCatalogCode, ybNo).last(YbCommonConstants.sqlConst.LIMIT1));
            if (drugInfo == null) {
                CatalogMedicalHerbInfo herbInfo =
                    iCatalogMedicalHerbInfoService.getOne(new LambdaQueryWrapper<CatalogMedicalHerbInfo>()
                        .eq(CatalogMedicalHerbInfo::getMedicalCatalogCode, ybNo)
                        .last(YbCommonConstants.sqlConst.LIMIT1));
            } else {

            }
            // 查询医院信息
            Long hospitalId = userService.getHospitalIdByOrgId(enc.getOrganizationId());
            Organization org = iOrganizationService.getById(hospitalId);

            FsiOrder3101Param fsiOrder = new FsiOrder3101Param();
            fsiOrder.setRxId(medicationRequest.getBusNo()).setRxno(medicationRequest.getPrescriptionNo())
                .setLongDrordFlag(whether.getCode()).setHilistCode(medicationDefinition.getCategoryCode())
                // 医嘱类别暂定全部使用其他
                .setChrgType(medChrgItmType.getValue()).setDrordBhvr(YbDrordBhvr.OTHER.getValue())
                .setHilistCode(medicationDefinition.getYbNo())
                // 医保目录价格未定义
                .setHilistName(medicationDefinition.getName()).setHilistLv(chrgitmLv.getValue().toString())
                .setHosplistCode(org.getYbNo()).setHosplistName(org.getName())
                .setCnt(new BigDecimal(chargeItem.getQuantityValue().toString())).setPric(chargeItem.getUnitPrice())
                .setSumamt(chargeItem.getTotalPrice());
            // todo：sjq 从第23项后面的值越来越离谱，暂定先停止3101和3103的开发

        }

        return null;
    }

    /**
     * 【3202】医药机构费用结算对明细账
     * 
     * @param settlement3202WebParam 费用结算对明细账前端入参
     * @return 医保参数
     */
    public FinancialSettlement3202Param getFinancialSettlement3202Param(Settlement3202WebParam settlement3202WebParam) {
        // 数据库查询
        Settlement3202Dto reconciliation = medicalInsuranceMapper.get3202Param(settlement3202WebParam.getStmtBegnDate().toString(),
            settlement3202WebParam.getStmtEndDate().toString(), SecurityUtils.getLoginUser().getTenantId(),
            settlement3202WebParam.getOrgId(), settlement3202WebParam.getClrType(),
            settlement3202WebParam.getSetlOptins(), YbPayment.SELF_YB_ZH_PAY.getValue(),
            YbPayment.SELF_YB_ZH_GJ_VALUE.getValue(), YbPayment.SELF_CASH_VALUE.getValue(),
            YbPayment.SELF_CASH_VX_VALUE.getValue(), YbPayment.SELF_CASH_ALI_VALUE.getValue(),
            YbPayment.SELF_CASH_UNION_VALUE.getValue(), YbPayment.YB_FUND_PAY.getValue());
        // 数据处理
        reconciliation
            .setAcctGjPay(reconciliation.getAcctGjPay() == null ? BigDecimal.ZERO : reconciliation.getAcctGjPay());
        reconciliation.setAcctPay(reconciliation.getAcctPay() == null ? BigDecimal.ZERO : reconciliation.getAcctPay());
        reconciliation.setFundPaySumAmt(
            reconciliation.getFundPaySumAmt() == null ? BigDecimal.ZERO : reconciliation.getFundPaySumAmt());
        reconciliation.setMedFeeSumAmt(
            reconciliation.getMedFeeSumAmt() == null ? BigDecimal.ZERO : reconciliation.getMedFeeSumAmt());
        reconciliation.setSelfPayCash(
            reconciliation.getSelfPayCash() == null ? BigDecimal.ZERO : reconciliation.getSelfPayCash());
        reconciliation
            .setSelfPayALI(reconciliation.getSelfPayALI() == null ? BigDecimal.ZERO : reconciliation.getSelfPayALI());
        reconciliation
            .setSelfPayVX(reconciliation.getSelfPayVX() == null ? BigDecimal.ZERO : reconciliation.getSelfPayVX());
        reconciliation.setSelfPayUNION(
            reconciliation.getSelfPayUNION() == null ? BigDecimal.ZERO : reconciliation.getSelfPayUNION());

        BigDecimal cashSum = reconciliation.getSelfPayCash().add(reconciliation.getSelfPayVX())
            .add(reconciliation.getSelfPayUNION()).add(reconciliation.getSelfPayALI());
        // BigDecimal zhSum = reconciliation.getAcctGjPay().add(reconciliation.getAcctPay());
        // 组装参数
        FinancialSettlement3202Param financialSettlement3202Param = new FinancialSettlement3202Param();
        financialSettlement3202Param.setClrType(settlement3202WebParam.getClrType()).setCashPayamt(cashSum)
            .setFileQuryNo(settlement3202WebParam.getFileQuryNo())
            .setFixmedinsSetlCnt(reconciliation.getFixMedInsSetlCnt()).setMedfeeSumamt(reconciliation.getMedFeeSumAmt())
            .setSetlOptins(settlement3202WebParam.getSetlOptins())
            .setStmtBegndate(DateUtils.parseDate(settlement3202WebParam.getStmtBegnDate()))
            .setStmtEnddate(DateUtils.parseDate(settlement3202WebParam.getStmtEndDate()))
            .setFundPaySumamt(reconciliation.getFundPaySumAmt());
        // 返回结果
        return financialSettlement3202Param;
    }

    /**
     * 【3202】对账明细的文件数据
     * 
     * @param paymentIds 付款实体ids
     * @return 查询结果
     */
    public List<Financial3202FileParam> getFinancial3202FileParam(List<Long> paymentIds) {
        List<Financial3202FileParam> list = new ArrayList<>();
        for (Long paymentId : paymentIds) {

            // 查询基础信息
            List<PaymentRecDetail> paymentRecDetails = iPaymentRecDetailService
                .list(new LambdaQueryWrapper<PaymentRecDetail>().eq(PaymentRecDetail::getReconciliationId, paymentId));
            PaymentReconciliation paymentReconciliation = iPaymentReconciliationService.getById(paymentId);
            if (paymentReconciliation == null) {
                throw new ServiceException("未查询到付款信息");
            }
            String ybSettleIds = paymentReconciliation.getYbSettleIds();
            if (StringUtils.isEmpty(ybSettleIds)) {
                continue;
            }
            List<Long> settleIdList =
                Arrays.stream(ybSettleIds.split(",")).map(String::trim).filter(s -> !s.isEmpty()).map(s -> {
                    try {
                        return Long.valueOf(s);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }).filter(Objects::nonNull).collect(Collectors.toList());
            for (Long settleId : settleIdList) {
                ClinicSettle clinicSettle = iClinicSettleService
                    .getOne(new LambdaQueryWrapper<ClinicSettle>().eq(ClinicSettle::getSetlId, settleId));
                if (paymentReconciliation == null || paymentRecDetails.isEmpty() || clinicSettle == null) {
                    continue;
                }
                ClinicReg clinicReg = iRegService
                    .getOne(new LambdaQueryWrapper<ClinicReg>().eq(ClinicReg::getMdtrtId, clinicSettle.getMdtrtId()));

                // 计算金额
                BigDecimal fundPay = BigDecimal.ZERO;
                BigDecimal accountPay = BigDecimal.ZERO;
                for (PaymentRecDetail paymentRecDetail : paymentRecDetails) {
                    if (YbPayment.YB_FUND_PAY.getValue().equals(paymentRecDetail.getPayEnum())) {
                        fundPay = fundPay.add(paymentRecDetail.getAmount());
                    } else if (YbPayment.SELF_YB_ZH_PAY.getValue().equals(paymentRecDetail.getPayEnum())
                        || YbPayment.SELF_YB_ZH_GJ_VALUE.getValue().equals(paymentRecDetail.getPayEnum())) {
                        accountPay = accountPay.add(paymentRecDetail.getAmount());
                    }
                }

                // 反结标志
                String refdSetlFlag = Whether.NO.getValue().toString();
                if (paymentReconciliation.getPaymentEnum().equals(PaymentType.UN_PAY.getValue())) {
                    refdSetlFlag = Whether.YES.getValue().toString();// 1 是退费 2 否退费
                }
                Financial3202FileParam financial3202FileParam = new Financial3202FileParam();
                financial3202FileParam.setSetlId(clinicSettle.getSetlId()).setMdtrtId(clinicSettle.getMdtrtId())
                    .setPsnNo(clinicReg.getPsnNo()).setMedfeeSumamt(paymentReconciliation.getTenderedAmount())
                    .setFundPaySumamt(fundPay).setAcctPay(accountPay).setRefdSetlFlag(refdSetlFlag);
                // 添加集合
                list.add(financial3202FileParam);
            }
        }
        return list;
    }

    /**
     * 【3301】获取医保目录对照入参
     * 
     * @param tableName 对应定义表
     * @param id 主键
     * @return 医保参数
     */
    public MedicalDirectory3301Param getMedicalDirectory3301Param(String tableName, Long id) {
        MedicalDirectory3301Param medicalDirectory3301Param = new MedicalDirectory3301Param();
        if (CommonConstants.TableName.ADM_DEVICE_DEFINITION.equals(tableName)) {
            // 耗材
            DeviceDefinition deviceDefinition = iDeviceDefinitionService.getById(id);
            if (deviceDefinition == null) {
                throw new ServiceException("未查询到药品信息");
            }
            YbListType listType = YbListType.getByValue(deviceDefinition.getCategoryCode());
            if (listType == null) {
                listType = YbListType.MEDICAL_CONSUMABLES;
            }
            medicalDirectory3301Param.setFixmedinsHilistId(deviceDefinition.getBusNo())
                .setFixmedinsHilistName(deviceDefinition.getName()).setListType(listType.getValue())
                .setMedListCodg(deviceDefinition.getYbNo())
                .setBegndate(DateUtils.dateTimeNow(YbCommonConstants.constants.YYYY_MM_DD))
                .setEnddate(YbCommonConstants.constants.END_TIME);
        } else if (CommonConstants.TableName.MED_MEDICATION_DEFINITION.equals(tableName)) {
            // 药品
            MedicationDefinition medicationDefinition = iMedicationDefinitionService.getById(id);
            if (medicationDefinition == null) {
                throw new ServiceException("未查询到药品信息");
            }
            // 项目类型为中成药（1）、西药（2）、外购药品（3）、中草药（4）时查药品表；为耗材（9）时查器材表
            YbListType listType = YbListType.getByValue(medicationDefinition.getCategoryCode());
            if (listType == null) {
                listType = switch (medicationDefinition.getCategoryCode()) {
                    case "1", "2" -> YbListType.WESTERN_AND_CHINESE_PATENT_MEDICINE;
                    case "4" -> YbListType.IMPORTANT_HERBAL_SLICES;
                    default -> throw new ServiceException("未查询到目录类别");
                };
            }
            medicalDirectory3301Param.setFixmedinsHilistId(medicationDefinition.getBusNo())
                .setFixmedinsHilistName(medicationDefinition.getName()).setListType(listType.getValue())
                .setMedListCodg(medicationDefinition.getYbNo())
                .setBegndate(DateUtils.dateTimeNow(YbCommonConstants.constants.YYYY_MM_DD))
                .setEnddate(YbCommonConstants.constants.END_TIME);
        } else if (CommonConstants.TableName.WOR_ACTIVITY_DEFINITION.equals(tableName)) {
            // 医疗项目
            ActivityDefinition activityDefinition = iActivityDefinitionService.getById(id);
            if (activityDefinition == null) {
                throw new ServiceException("未查询到药品信息");
            }
            YbListType listType = YbListType.getByValue(activityDefinition.getCategoryCode());
            if (listType == null) {
                listType = YbListType.MEDICAL_SERVICE_ITEM;
            }
            medicalDirectory3301Param.setFixmedinsHilistId(activityDefinition.getBusNo())
                .setFixmedinsHilistName(activityDefinition.getName()).setListType(listType.getValue())
                .setMedListCodg(activityDefinition.getYbNo())
                .setBegndate(DateUtils.dateTimeNow(YbCommonConstants.constants.YYYY_MM_DD))
                .setEnddate(YbCommonConstants.constants.END_TIME);
        } else if (CommonConstants.TableName.ADM_HEALTHCARE_SERVICE.equals(tableName)) {
            // 医疗服务
            HealthcareService healthcareServiceService = iHealthcareServiceService.getById(id);
            if (healthcareServiceService == null) {
                throw new ServiceException("未查询到药品信息");
            }
            YbListType listType = YbListType.getByValue(healthcareServiceService.getCategoryCode());
            if (listType == null) {
                listType = YbListType.MEDICAL_SERVICE_ITEM;
            }
            medicalDirectory3301Param.setFixmedinsHilistId(String.valueOf(healthcareServiceService.getId()))
                .setFixmedinsHilistName(healthcareServiceService.getName()).setListType(listType.getValue())
                .setMedListCodg(healthcareServiceService.getYbNo())
                .setBegndate(DateUtils.dateTimeNow(YbCommonConstants.constants.YYYY_MM_DD))
                .setEnddate(YbCommonConstants.constants.END_TIME);
        } else {
            throw new ServiceException("未查询到医疗项目");
        }
        return medicalDirectory3301Param;
    }

    /**
     * 【3302】医保目录对照撤销
     *
     * @param tableName 对应定义表
     * @param id 主键
     * @return
     */
    public MedicalDirectory3302Param getMedicalDirectory3302Param(String tableName, Long id) {
        MedicalDirectory3302Param medicalDirectory3302Param = new MedicalDirectory3302Param();
        if (CommonConstants.TableName.ADM_DEVICE_DEFINITION.equals(tableName)) {
            // 耗材
            DeviceDefinition deviceDefinition = iDeviceDefinitionService.getById(id);
            if (deviceDefinition == null) {
                throw new ServiceException("未查询到药品信息");
            }
            YbListType listType = YbListType.getByValue(deviceDefinition.getCategoryCode());
            if (listType == null) {
                listType = YbListType.MEDICAL_CONSUMABLES;
            }
            medicalDirectory3302Param.setFixmedinsHilistId(deviceDefinition.getBusNo())
                .setFixmedinsCode(SecurityUtils.getLoginUser().getOptionJson().getString("fixmedinsCode"))
                .setListType(listType.getValue()).setMedListCodg(deviceDefinition.getYbNo());
        } else if (CommonConstants.TableName.MED_MEDICATION_DEFINITION.equals(tableName)) {
            // 药品
            MedicationDefinition medicationDefinition = iMedicationDefinitionService.getById(id);
            if (medicationDefinition == null) {
                throw new ServiceException("未查询到药品信息");
            }
            // 项目类型为中成药（1）、西药（2）、外购药品（3）、中草药（4）时查药品表；为耗材（9）时查器材表
            YbListType listType = YbListType.getByValue(medicationDefinition.getCategoryCode());
            if (listType == null) {
                listType = switch (medicationDefinition.getCategoryCode()) {
                    case "1", "2" -> YbListType.WESTERN_AND_CHINESE_PATENT_MEDICINE;
                    case "4" -> YbListType.IMPORTANT_HERBAL_SLICES;
                    default -> throw new ServiceException("未查询到目录类别");
                };
            }
            medicalDirectory3302Param.setFixmedinsHilistId(medicationDefinition.getBusNo())
                .setFixmedinsCode(SecurityUtils.getLoginUser().getOptionJson().getString("fixmedinsCode"))
                .setListType(listType.getValue()).setMedListCodg(medicationDefinition.getYbNo());
        } else if (CommonConstants.TableName.WOR_ACTIVITY_DEFINITION.equals(tableName)) {
            // 医疗项目
            ActivityDefinition activityDefinition = iActivityDefinitionService.getById(id);
            if (activityDefinition == null) {
                throw new ServiceException("未查询到药品信息");
            }
            YbListType listType = YbListType.getByValue(activityDefinition.getCategoryCode());
            if (listType == null) {
                listType = YbListType.MEDICAL_SERVICE_ITEM;
            }
            medicalDirectory3302Param.setFixmedinsHilistId(activityDefinition.getBusNo())
                .setFixmedinsCode(SecurityUtils.getLoginUser().getOptionJson().getString("fixmedinsCode"))
                .setListType(listType.getValue()).setMedListCodg(activityDefinition.getYbNo());
        } else {
            // 医疗项目
            HealthcareService healthcareService = iHealthcareServiceService.getById(id);
            if (healthcareService == null) {
                throw new ServiceException("未查询到药品信息");
            }
            YbListType listType = YbListType.getByValue(healthcareService.getCategoryCode());
            if (listType == null) {
                listType = YbListType.MEDICAL_SERVICE_ITEM;
            }
            medicalDirectory3302Param.setFixmedinsHilistId(healthcareService.getId().toString())
                .setFixmedinsCode(SecurityUtils.getLoginUser().getOptionJson().getString("fixmedinsCode"))
                .setListType(listType.getValue()).setMedListCodg(healthcareService.getYbNo());
        }
        return medicalDirectory3302Param;
    }

    /**
     *
     * @param clearing3205AWebParma [3205]前台传参
     * @return 医保入参
     */
    public Clearing3205AParma getClearing3205AParma(Clearing3205AWebParma clearing3205AWebParma) {
        String clrStas = clearing3205AWebParma.getClrStas();
        // 10 已申请20 已受理50 已清算如不填写，默认为10
        if (StringUtils.isEmpty(clrStas)) {
            clrStas = YbClrStas.CLR_STAS10.getValue();
        }
        YbClrStas ybClrStas = YbClrStas.getByValue(clrStas);
        // 年月格式，例如：202310
        // 查询clr_sta为20、50的时候，该值必填
        if (ybClrStas != YbClrStas.CLR_STAS10 && StringUtils.isEmpty(clearing3205AWebParma.getClrYm())) {
            throw new ServiceException("请填写清算状态");
        }
        Clearing3205AParma clearing3205AParma = new Clearing3205AParma();
        clearing3205AParma.setClrOptins(clearing3205AWebParma.getClrOptins())
            .setClrStas(clearing3205AWebParma.getClrStas()).setClrYm(clearing3205AWebParma.getClrYm());
        return clearing3205AParma;
    }

    /**
     * 【3501】库存明细上传
     * 
     * @param id 供应申请id
     * @return 3501医保参数
     */
    public MedicalInventory3501Param getMedicalInventory3501Param(Long id, Integer talentId) {
        MedicalInventory3501Param medicalInventory3501Param = new MedicalInventory3501Param();
        // 查库存
        InventoryItem inventoryItem = inventoryItemService.getById(id);
        if (inventoryItem == null) {
            throw new ServiceException("未查询到库存信息");
        }
        if (CommonConstants.TableName.MED_MEDICATION_DEFINITION.equals(inventoryItem.getItemTable())) {
            // 查询药品
            MedicationDefinition medicationDefinition = iMedicationDefinitionService.getById(inventoryItem.getItemId());
            if (medicationDefinition == null) {
                throw new ServiceException("未查询到药品信息");
            }
            // 查询盘存数据
            SupplyDelivery one = iSupplyDeliveryService.getOne(new LambdaQueryWrapper<SupplyDelivery>()
                .eq(SupplyDelivery::getItemTable, CommonConstants.TableName.MED_MEDICATION_DEFINITION)
                .eq(SupplyDelivery::getItemId, medicationDefinition.getId()).eq(SupplyDelivery::getTenantId, talentId));
            medicalInventory3501Param.setMedListCodg(medicationDefinition.getYbNo())
                .setFixmedinsHilistId(medicationDefinition.getBusNo())
                .setFixmedinsHilistName(CommonConstants.TableName.MED_MEDICATION_DEFINITION)
                .setRxFlag(medicationDefinition.getRxFlag().toString()).setInvdate(one.getOccurrenceEndTime())
                .setInvCnt(inventoryItem.getQuantity()).setFixmedinsBchno(inventoryItem.getLotNumber())
                .setManuDate(inventoryItem.getProductionDate()).setExpyEnd(inventoryItem.getExpirationDate());
        } else if (CommonConstants.TableName.ADM_DEVICE_DEFINITION.equals(inventoryItem.getItemTable())) {
            // 查询器材
            DeviceDefinition deviceDefinition = iDeviceDefinitionService.getById(inventoryItem.getItemId());
            if (deviceDefinition == null) {
                throw new ServiceException("未查询到药品信息");
            }
            // 查询盘存数据
            SupplyDelivery one = iSupplyDeliveryService.getOne(new LambdaQueryWrapper<SupplyDelivery>()
                .eq(SupplyDelivery::getItemTable, CommonConstants.TableName.ADM_DEVICE_DEFINITION)
                .eq(SupplyDelivery::getItemId, deviceDefinition.getId()).eq(SupplyDelivery::getTenantId, talentId));
            medicalInventory3501Param.setMedListCodg(deviceDefinition.getYbNo())
                .setFixmedinsHilistId(deviceDefinition.getBusNo())
                .setFixmedinsHilistName(CommonConstants.TableName.MED_MEDICATION_DEFINITION)
                .setRxFlag(deviceDefinition.getRxFlag().toString()).setInvdate(one.getOccurrenceEndTime())
                .setInvCnt(inventoryItem.getQuantity()).setFixmedinsBchno(inventoryItem.getLotNumber())
                .setManuDate(inventoryItem.getProductionDate()).setExpyEnd(inventoryItem.getExpirationDate());
        } else {
            throw new ServiceException("未查询到信息");
        }
        return medicalInventory3501Param;
    }

    /**
     * 【3502】库存信息变更
     * 
     * @param id 供应发放id
     * @param invChgType 变更类型 参考枚举
     * @param talentId 租户id
     * @return
     */
    public MedicalInventory3502Param getMedicalInventory3502Param(Long id, String invChgType, Integer talentId) {
        MedicalInventory3502Param medicalInventory3502Param = new MedicalInventory3502Param();

        // 查供应发放实体
        SupplyDelivery supplyDelivery = iSupplyDeliveryService.getById(id);
        if (supplyDelivery == null) {
            throw new ServiceException("未查询到供应信息");
        }

        // 库存变更类型
        YbInvChgType chgType = YbInvChgType.getByValue(invChgType);
        if (chgType == null) {
            throw new ServiceException("未查询到付款信息");
        }

        if (CommonConstants.TableName.MED_MEDICATION_DEFINITION.equals(supplyDelivery.getItemTable())) {
            // 查询药品信息
            MedicationDefinition medicationDefinition =
                iMedicationDefinitionService.getById(supplyDelivery.getItemId());
            if (medicationDefinition == null) {
                throw new ServiceException("未查询到药品信息");
            }
            // 查询定价信息
            ChargeItemDefinition chargeItemDefinition =
                iChargeItemDefinitionService.getOne(new LambdaQueryWrapper<ChargeItemDefinition>()
                    .eq(ChargeItemDefinition::getInstanceTable, CommonConstants.TableName.MED_MEDICATION_DEFINITION)
                    .eq(ChargeItemDefinition::getInstanceId, medicationDefinition.getId())
                    .eq(ChargeItemDefinition::getTenantId, talentId));
            if (chargeItemDefinition == null) {
                throw new ServiceException("未查询到定价信息");
            }
            // 查查询定价子表
            ChargeItemDefDetail chargeItemDefDetail =
                iChargeItemDefDetailService.getOne(new LambdaQueryWrapper<ChargeItemDefDetail>()
                    .eq(ChargeItemDefDetail::getDefinitionId, chargeItemDefinition.getId())
                    .eq(ChargeItemDefDetail::getConditionCode, ConditionCode.UNIT.getCode())
                    .eq(ChargeItemDefDetail::getTenantId, talentId).orderByDesc(ChargeItemDefDetail::getConditionValue)
                    .last(YbCommonConstants.sqlConst.LIMIT1));
            if (chargeItemDefDetail == null) {
                throw new ServiceException("未查询到定价信息");
            }
            // 最小单位售卖价格
            BigDecimal price;
            if (medicationDefinition.getPartPercent().compareTo(BigDecimal.ZERO) > 0) {
                price =
                    chargeItemDefDetail.getAmount().divide(medicationDefinition.getPartPercent(), RoundingMode.HALF_UP);
            } else {
                price = chargeItemDefDetail.getAmount().divide(BigDecimal.ONE, RoundingMode.HALF_UP);
            }

            medicalInventory3502Param.setMedListCodg(medicationDefinition.getYbNo()).setInvChgType(chgType.getValue())
                .setFixmedinsHilistId(medicationDefinition.getBusNo())
                .setFixmedinsHilistName(CommonConstants.TableName.MED_MEDICATION_DEFINITION)
                .setRxFlag(medicationDefinition.getRxFlag().toString()).setPric(price)
                .setCnt(supplyDelivery.getQuantity()).setInvChgTime(supplyDelivery.getUpdateTime());
        } else if (CommonConstants.TableName.ADM_DEVICE_DEFINITION.equals(supplyDelivery.getItemTable())) {
            // 查询耗材信息
            DeviceDefinition deviceDefinition = iDeviceDefinitionService.getById(supplyDelivery.getItemId());
            if (deviceDefinition == null) {
                throw new ServiceException("未查询到药品信息");
            }
            // 查询定价信息
            ChargeItemDefinition chargeItemDefinition =
                iChargeItemDefinitionService.getOne(new LambdaQueryWrapper<ChargeItemDefinition>()
                    .eq(ChargeItemDefinition::getInstanceTable, CommonConstants.TableName.ADM_DEVICE_DEFINITION)
                    .eq(ChargeItemDefinition::getInstanceId, deviceDefinition.getId())
                    .eq(ChargeItemDefinition::getTenantId, talentId));
            if (chargeItemDefinition == null) {
                throw new ServiceException("未查询到定价信息");
            }
            // 查查询定价子表
            ChargeItemDefDetail chargeItemDefDetail =
                iChargeItemDefDetailService.getOne(new LambdaQueryWrapper<ChargeItemDefDetail>()
                    .eq(ChargeItemDefDetail::getDefinitionId, chargeItemDefinition.getId())
                    .eq(ChargeItemDefDetail::getConditionCode, ConditionCode.UNIT.getCode())
                    .eq(ChargeItemDefDetail::getTenantId, talentId).orderByDesc(ChargeItemDefDetail::getConditionValue)
                    .last(YbCommonConstants.sqlConst.LIMIT1));
            if (chargeItemDefDetail == null) {
                throw new ServiceException("未查询到定价信息");
            }
            // 最小单位售卖价格
            BigDecimal price;
            if (deviceDefinition.getPartPercent().compareTo(BigDecimal.ZERO) > 0) {
                price = chargeItemDefDetail.getAmount().divide(deviceDefinition.getPartPercent(), RoundingMode.HALF_UP);
            } else {
                price = chargeItemDefDetail.getAmount().divide(BigDecimal.ONE, RoundingMode.HALF_UP);
            }

            medicalInventory3502Param.setMedListCodg(deviceDefinition.getYbNo()).setInvChgType(chgType.getValue())
                .setFixmedinsHilistId(deviceDefinition.getBusNo())
                .setFixmedinsHilistName(CommonConstants.TableName.MED_MEDICATION_DEFINITION)
                .setRxFlag(deviceDefinition.getRxFlag().toString()).setPric(price).setCnt(supplyDelivery.getQuantity())
                .setInvChgTime(supplyDelivery.getUpdateTime());
        } else {
            throw new ServiceException("未查询到信息");
        }
        return medicalInventory3502Param;
    }

    /**
     * 【3503】商品采购
     *
     * @param id 供应发放id
     * @param talentId 租户id
     * @return
     */
    public Medical3503Param getMedical3503Param(Long id, Integer talentId) {
        Medical3503Param medical3503Param = new Medical3503Param();
        // 查供应发放实体
        SupplyDelivery supplyDelivery = iSupplyDeliveryService.getById(id);
        if (supplyDelivery == null) {
            throw new ServiceException("未查询到供应发放信息");
        }
        // 查询发放请求实体
        SupplyRequest supplyRequest = iSupplyRequestService.getOne(new LambdaQueryWrapper<SupplyRequest>()
            .eq(SupplyRequest::getId, supplyDelivery.getRequestId()).eq(SupplyRequest::getTenantId, talentId)
            .eq(SupplyRequest::getTypeEnum, SupplyType.PURCHASE_INVENTORY.getValue()));
        if (supplyRequest == null) {
            throw new ServiceException("未查询到供应信息");
        }
        // 查库存信息
        InventoryItem inventoryItem = inventoryItemService.getOne(new LambdaQueryWrapper<InventoryItem>()
            .eq(InventoryItem::getLocationStoreId, supplyRequest.getSourceLocationStoreId())
            .eq(InventoryItem::getLocationId, supplyRequest.getPurposeLocationId())
            .eq(InventoryItem::getTenantId, talentId));

        // 查询经办人
        Practitioner practitioner = iPractitionerService.getOne(new LambdaQueryWrapper<Practitioner>()
            .eq(Practitioner::getUserId, supplyDelivery.getReceiverId()).eq(Practitioner::getTenantId, talentId));

        if (CommonConstants.TableName.MED_MEDICATION_DEFINITION.equals(supplyDelivery.getItemTable())) {
            // 查询药品信息
            MedicationDefinition medicationDefinition =
                iMedicationDefinitionService.getById(supplyDelivery.getItemId());
            if (medicationDefinition == null) {
                throw new ServiceException("未查询到药品信息");
            }
            // 查询定价信息
            ChargeItemDefinition chargeItemDefinition =
                iChargeItemDefinitionService.getOne(new LambdaQueryWrapper<ChargeItemDefinition>()
                    .eq(ChargeItemDefinition::getInstanceTable, CommonConstants.TableName.MED_MEDICATION_DEFINITION)
                    .eq(ChargeItemDefinition::getInstanceId, medicationDefinition.getId())
                    .eq(ChargeItemDefinition::getTenantId, talentId));
            if (chargeItemDefinition == null) {
                throw new ServiceException("未查询到定价信息");
            }
            // 查查询定价子表
            ChargeItemDefDetail chargeItemDefDetail =
                iChargeItemDefDetailService.getOne(new LambdaQueryWrapper<ChargeItemDefDetail>()
                    .eq(ChargeItemDefDetail::getDefinitionId, chargeItemDefinition.getId())
                    .eq(ChargeItemDefDetail::getConditionCode, ConditionCode.UNIT.getCode())
                    .eq(ChargeItemDefDetail::getTenantId, talentId).orderByDesc(ChargeItemDefDetail::getConditionValue)
                    .last(YbCommonConstants.sqlConst.LIMIT1));
            if (chargeItemDefDetail == null) {
                throw new ServiceException("未查询到定价信息");
            }
            // 查询供应商
            Supplier supplier = iSupplierService.getById(medicationDefinition.getSupplyId());

            medical3503Param.setMedListCodg(medicationDefinition.getYbNo())
                .setFixmedinsBchno(inventoryItem.getLotNumber()).setFixmedinsHilistId(medicationDefinition.getBusNo())
                .setFixmedinsHilistName(CommonConstants.TableName.MED_MEDICATION_DEFINITION)
                .setRxFlag(medicationDefinition.getRxFlag().toString()).setSplerName(supplier.getName())
                .setManuLotnum(supplyDelivery.getLotNumber())
                .setProdentpName(medicationDefinition.getManufacturerText())
                .setAprvno(medicationDefinition.getApprovalNumber()).setManuDate(inventoryItem.getProductionDate())
                .setExpyEnd(inventoryItem.getExpirationDate()).setPurcRetnCnt(supplyDelivery.getQuantity())
                .setPurcRetnStoinTime(supplyDelivery.getOccurrenceTime()).setPurcRetnOpterName(practitioner.getName());
        } else if (CommonConstants.TableName.ADM_DEVICE_DEFINITION.equals(supplyDelivery.getItemTable())) {
            // 查询耗材信息
            DeviceDefinition deviceDefinition = iDeviceDefinitionService.getById(supplyDelivery.getItemId());
            if (deviceDefinition == null) {
                throw new ServiceException("未查询到耗材信息");
            }
            // 查询定价信息
            ChargeItemDefinition chargeItemDefinition =
                iChargeItemDefinitionService.getOne(new LambdaQueryWrapper<ChargeItemDefinition>()
                    .eq(ChargeItemDefinition::getInstanceTable, CommonConstants.TableName.ADM_DEVICE_DEFINITION)
                    .eq(ChargeItemDefinition::getInstanceId, deviceDefinition.getId())
                    .eq(ChargeItemDefinition::getTenantId, talentId));
            if (chargeItemDefinition == null) {
                throw new ServiceException("未查询到定价信息");
            }
            // 查查询定价子表
            ChargeItemDefDetail chargeItemDefDetail =
                iChargeItemDefDetailService.getOne(new LambdaQueryWrapper<ChargeItemDefDetail>()
                    .eq(ChargeItemDefDetail::getDefinitionId, chargeItemDefinition.getId())
                    .eq(ChargeItemDefDetail::getConditionCode, ConditionCode.UNIT.getCode())
                    .eq(ChargeItemDefDetail::getTenantId, talentId).orderByDesc(ChargeItemDefDetail::getConditionValue)
                    .last(YbCommonConstants.sqlConst.LIMIT1));
            if (chargeItemDefDetail == null) {
                throw new ServiceException("未查询到定价信息");
            }
            // 查询供应商
            Supplier supplier = iSupplierService.getById(deviceDefinition.getSupplyId());

            medical3503Param.setMedListCodg(deviceDefinition.getYbNo()).setFixmedinsBchno(inventoryItem.getLotNumber())
                .setFixmedinsHilistId(deviceDefinition.getBusNo())
                .setFixmedinsHilistName(CommonConstants.TableName.ADM_DEVICE_DEFINITION)
                .setRxFlag(deviceDefinition.getRxFlag().toString()).setSplerName(supplier.getName())
                .setManuLotnum(supplyDelivery.getLotNumber()).setProdentpName(deviceDefinition.getManufacturerText())
                .setAprvno(deviceDefinition.getApprovalNumber()).setManuDate(inventoryItem.getProductionDate())
                .setExpyEnd(inventoryItem.getExpirationDate()).setPurcRetnCnt(supplyDelivery.getQuantity())
                .setPurcRetnStoinTime(supplyDelivery.getOccurrenceTime()).setPurcRetnOpterName(practitioner.getName());
        } else {
            throw new ServiceException("未查询到信息");
        }
        return medical3503Param;
    }

    /**
     * 【3504】商品采购退货
     *
     * @param id 供应发放id
     * @param talentId 租户id
     * @return
     */
    public MedicalPurchase3504Param getMedicalPurchase3504Param(Long id, Integer talentId) {

        MedicalPurchase3504Param medicalPurchase3504Param = new MedicalPurchase3504Param();
        // 查供应发放实体
        SupplyDelivery supplyDelivery = iSupplyDeliveryService.getById(id);
        if (supplyDelivery == null) {
            throw new ServiceException("未查询到供应信息");
        }
        // 查询发放请求实体
        SupplyRequest supplyRequest = iSupplyRequestService.getOne(new LambdaQueryWrapper<SupplyRequest>()
            .eq(SupplyRequest::getId, supplyDelivery.getRequestId()).eq(SupplyRequest::getTenantId, talentId)
            .eq(SupplyRequest::getTypeEnum, SupplyType.PRODUCT_RETURN.getValue()));
        if (supplyRequest == null) {
            throw new ServiceException("未查询到供应信息");
        }
        // 查库存信息
        InventoryItem inventoryItem = inventoryItemService.getOne(new LambdaQueryWrapper<InventoryItem>()
            .eq(InventoryItem::getLocationStoreId, supplyRequest.getSourceLocationStoreId())
            .eq(InventoryItem::getLocationId, supplyRequest.getPurposeLocationId())
            .eq(InventoryItem::getTenantId, talentId));

        // 查询经办人
        Practitioner practitioner = iPractitionerService.getOne(new LambdaQueryWrapper<Practitioner>()
            .eq(Practitioner::getUserId, supplyDelivery.getReceiverId()).eq(Practitioner::getTenantId, talentId));

        if (CommonConstants.TableName.MED_MEDICATION_DEFINITION.equals(supplyDelivery.getItemTable())) {
            // 查询药品信息
            MedicationDefinition medicationDefinition =
                iMedicationDefinitionService.getById(supplyDelivery.getItemId());
            if (medicationDefinition == null) {
                throw new ServiceException("未查询到药品信息");
            }
            // 查询定价信息
            ChargeItemDefinition chargeItemDefinition =
                iChargeItemDefinitionService.getOne(new LambdaQueryWrapper<ChargeItemDefinition>()
                    .eq(ChargeItemDefinition::getInstanceTable, CommonConstants.TableName.MED_MEDICATION_DEFINITION)
                    .eq(ChargeItemDefinition::getInstanceId, medicationDefinition.getId())
                    .eq(ChargeItemDefinition::getTenantId, talentId));
            if (chargeItemDefinition == null) {
                throw new ServiceException("未查询到定价信息");
            }
            // 查查询定价子表
            ChargeItemDefDetail chargeItemDefDetail =
                iChargeItemDefDetailService.getOne(new LambdaQueryWrapper<ChargeItemDefDetail>()
                    .eq(ChargeItemDefDetail::getDefinitionId, chargeItemDefinition.getId())
                    .eq(ChargeItemDefDetail::getConditionCode, ConditionCode.UNIT.getCode())
                    .eq(ChargeItemDefDetail::getTenantId, talentId).orderByDesc(ChargeItemDefDetail::getConditionValue)
                    .last(YbCommonConstants.sqlConst.LIMIT1));
            if (chargeItemDefDetail == null) {
                throw new ServiceException("未查询到定价信息");
            }
            // 查询供应商
            Supplier supplier = iSupplierService.getById(medicationDefinition.getSupplyId());
            if (supplier == null) {
                throw new ServiceException("未查询到供应商信息");
            }
            medicalPurchase3504Param.setMedListCodg(medicationDefinition.getYbNo())
                .setFixmedinsBchno(inventoryItem.getLotNumber()).setFixmedinsHilistId(medicationDefinition.getBusNo())
                .setFixmedinsHilistName(CommonConstants.TableName.MED_MEDICATION_DEFINITION)
                .setRxFlag(medicationDefinition.getRxFlag().toString()).setSplerName(supplier.getName())
                .setPurcInvoNo(supplyRequest.getInvoiceNo()).setManuDate(inventoryItem.getProductionDate())
                .setExpyEnd(inventoryItem.getExpirationDate()).setPurcRetnCnt(supplyDelivery.getQuantity())
                .setPurcRetnStoinTime(supplyDelivery.getOccurrenceTime()).setPurcRetnOpterName(practitioner.getName());
        } else if (CommonConstants.TableName.ADM_DEVICE_DEFINITION.equals(supplyDelivery.getItemTable())) {
            // 查询耗材信息
            DeviceDefinition deviceDefinition = iDeviceDefinitionService.getById(supplyDelivery.getItemId());
            if (deviceDefinition == null) {
                throw new ServiceException("未查询到耗材信息");
            }
            // 查询定价信息
            ChargeItemDefinition chargeItemDefinition =
                iChargeItemDefinitionService.getOne(new LambdaQueryWrapper<ChargeItemDefinition>()
                    .eq(ChargeItemDefinition::getInstanceTable, CommonConstants.TableName.ADM_DEVICE_DEFINITION)
                    .eq(ChargeItemDefinition::getInstanceId, deviceDefinition.getId())
                    .eq(ChargeItemDefinition::getTenantId, talentId));
            if (chargeItemDefinition == null) {
                throw new ServiceException("未查询到定价信息");
            }
            // 查查询定价子表
            ChargeItemDefDetail chargeItemDefDetail =
                iChargeItemDefDetailService.getOne(new LambdaQueryWrapper<ChargeItemDefDetail>()
                    .eq(ChargeItemDefDetail::getDefinitionId, chargeItemDefinition.getId())
                    .eq(ChargeItemDefDetail::getConditionCode, ConditionCode.UNIT.getCode())
                    .eq(ChargeItemDefDetail::getTenantId, talentId).orderByDesc(ChargeItemDefDetail::getConditionValue)
                    .last(YbCommonConstants.sqlConst.LIMIT1));
            if (chargeItemDefDetail == null) {
                throw new ServiceException("未查询到定价信息");
            }
            // 查询供应商
            Supplier supplier = iSupplierService.getById(deviceDefinition.getSupplyId());
            if (supplier == null) {
                throw new ServiceException("未查询到供应商信息");
            }
            medicalPurchase3504Param.setMedListCodg(deviceDefinition.getYbNo())
                .setFixmedinsBchno(inventoryItem.getLotNumber()).setFixmedinsHilistId(deviceDefinition.getBusNo())
                .setFixmedinsHilistName(CommonConstants.TableName.ADM_DEVICE_DEFINITION)
                .setRxFlag(deviceDefinition.getRxFlag().toString()).setSplerName(supplier.getName())
                .setManuDate(inventoryItem.getProductionDate()).setExpyEnd(inventoryItem.getExpirationDate())
                .setPurcRetnCnt(supplyDelivery.getQuantity()).setPurcRetnStoinTime(supplyDelivery.getOccurrenceTime())
                .setPurcRetnOpterName(practitioner.getName()).setPurcInvoNo(supplyRequest.getInvoiceNo());
        } else {
            throw new ServiceException("未查询到信息");
        }
        return medicalPurchase3504Param;
    }

    /**
     * 【3505】商品销售
     * 
     * @param id 发放id
     * @param ListType 耗材/药品
     * @param tenantId 租户id
     * @return
     */
    public Medical3505Param getMedical3505Param(Long id, String ListType, Integer tenantId) {
        Medical3505Param medical3505Param = new Medical3505Param();
        // 销售商品的种类
        YbListType type = YbListType.getByValue(ListType);
        if (type == YbListType.MEDICAL_CONSUMABLES) {
            // 耗材的相关处理
            DeviceDispense deviceDispense = iDeviceDispenseService.getById(id);
            if (deviceDispense == null) {
                throw new ServiceException("未查询到耗材信息");
            }
            if (!deviceDispense.getStatusEnum().equals(DispenseStatus.COMPLETED.getValue())) {
                throw new ServiceException("耗材未发放");
            }
            DeviceRequest deviceRequest = iDeviceRequestService.getById(deviceDispense.getDeviceReqId());
            if (deviceRequest == null) {
                throw new ServiceException("未查询到耗材请求信息");
            }
            DeviceDefinition deviceDefinition = iDeviceDefinitionService.getById(deviceDispense.getDeviceDefId());
            if (deviceDefinition == null) {
                throw new ServiceException("未查询到耗材信息");
            }
            // 查询库存信息
            InventoryItem inventoryItem = inventoryItemService.getOne(
                new LambdaQueryWrapper<InventoryItem>().eq(InventoryItem::getLotNumber, deviceDispense.getLotNumber())
                    .eq(InventoryItem::getLocationId, deviceDispense.getLocationId())
                    .eq(InventoryItem::getItemTable, CommonConstants.TableName.ADM_DEVICE_DEFINITION)
                    .eq(InventoryItem::getItemId, deviceDispense.getDeviceDefId())
                    .eq(InventoryItem::getTenantId, tenantId));
            if (inventoryItem == null) {
                throw new ServiceException("未查询到库存信息");
            }
            // 查询供应申请单 2025/04/18经在微信群中确认，仅能通过locid，lotnumber，定义id定位库存记录，无法继续准确追到供应请求
            // SupplyRequest supplyRequest = iSupplyRequestService.getOne(
            // new LambdaQueryWrapper<SupplyRequest>().eq(SupplyRequest::getLotNumber, deviceDispense.getLotNumber())
            // .eq(SupplyRequest::getPurposeLocationId, deviceDispense.getLocationId())
            // .eq(SupplyRequest::getItemTable, CommonConstants.TableName.ADM_DEVICE_DEFINITION)
            // .eq(SupplyRequest::getItemId, deviceDispense.getDeviceDefId())
            // .eq(SupplyRequest::getTenantId, tenantId));
            // if (supplyRequest == null) {
            // return null;
            // }
            // 查询开方医生姓名
            Practitioner practitioner = iPractitionerService.getOne(new LambdaQueryWrapper<Practitioner>()
                .eq(Practitioner::getUserId, deviceRequest.getRequesterId()).eq(Practitioner::getTenantId, tenantId));
            // 查询当前登录用户的姓名(药师)
            Practitioner medDoctor = iPractitionerService.getOne(new LambdaQueryWrapper<Practitioner>()
                .eq(Practitioner::getUserId, SecurityUtils.getUserId()).eq(Practitioner::getTenantId, tenantId));
            // 查询发药人
            Practitioner performer = iPractitionerService.getOne(new LambdaQueryWrapper<Practitioner>()
                .eq(Practitioner::getUserId, deviceDispense.getPerformerId()).eq(Practitioner::getTenantId, tenantId));
            // 查询费用结算信息
            ChargeItem chargeItem = iChargeItemService.getOne(new LambdaQueryWrapper<ChargeItem>()
                .eq(ChargeItem::getServiceTable, CommonConstants.TableName.WOR_DEVICE_REQUEST)
                .eq(ChargeItem::getServiceId, deviceRequest.getId()).eq(ChargeItem::getTenantId, tenantId));
            if (chargeItem == null) {
                throw new ServiceException("未查询到收费项信息");
            }
            // 查询付款信息
            PaymentReconciliation paymentReconciliation =
                iPaymentReconciliationService.getOne(new LambdaQueryWrapper<PaymentReconciliation>()
                    .eq(PaymentReconciliation::getEncounterId, chargeItem.getEncounterId())
                    .eq(PaymentReconciliation::getTenantId, tenantId)
                    .like(PaymentReconciliation::getChargeItemIds, chargeItem.getId())
                    .eq(PaymentReconciliation::getStatusEnum, ChargeItemStatus.BILLED.getValue())
                    .eq(PaymentReconciliation::getPatientId, chargeItem.getPatientId())
                    .eq(PaymentReconciliation::getPaymentEnum, PaymentType.PAY.getValue()));
            if (paymentReconciliation == null) {
                throw new ServiceException("未查询到付款信息");
            }
            // 查询合同实体
            Contract contract = iContractService.getOne(
                new LambdaQueryWrapper<Contract>().eq(Contract::getBusNo, paymentReconciliation.getContractNo()));
            if (contract == null) {
                throw new ServiceException("未查询到合同信息");
            }
            // 查询就诊信息
            Encounter encounter = iEncounterService.getById(deviceDispense.getEncounterId());
            if (encounter == null) {
                throw new ServiceException("未查询到就诊信息");
            }
            // 查询账户信息
            Account account = accountService
                .getOne(new LambdaQueryWrapper<Account>().eq(Account::getEncounterId, chargeItem.getEncounterId())
                    .eq(Account::getTenantId, tenantId).eq(Account::getEncounterFlag, Whether.YES.getValue()));
            if (account == null) {
                throw new ServiceException("未查询到账户信息");
            }
            YbMdtrtCertType mdtrtCertType = YbMdtrtCertType.getByValue(account.getTypeCode());
            if (mdtrtCertType == null) {
                throw new ServiceException("未查询到就诊凭证类型");
            }
            // 查询就诊id
            if (contract.getCategoryEnum().equals(Category.SELF.getValue())
                || contract.getCategoryEnum().equals(Category.PUBLIC.getValue())) {
                medical3505Param.setMdtrtSn(encounter.getBusNo());
            } else {
                ClinicReg one = iRegService
                    .getOne(new LambdaQueryWrapper<ClinicReg>().eq(ClinicReg::getMdtrtCertType, account.getTypeCode())
                        .eq(ClinicReg::getMdtrtCertNo, account.getNo()));
                if (one != null) {
                    medical3505Param.setMdtrtSn(one.getMdtrtId());
                }
            }
            // 判断拆零标志
            Whether flag;
            if (deviceDefinition.getSalesUnitCode().equals(deviceDispense.getUnitCode())) {
                flag = Whether.NO;
            } else {
                flag = Whether.YES;
            }
            // 查询发票信息
            Invoice invoice = iInvoiceService.getById(paymentReconciliation.getInvoiceId());
            if (invoice == null) {
                throw new ServiceException("未查询到发票信息");
            }
            medical3505Param.setMedListCodg(deviceDefinition.getYbNo()).setFixmedinsBchno(inventoryItem.getLotNumber())
                .setFixmedinsHilistId(deviceDefinition.getBusNo())
                .setFixmedinsHilistName(CommonConstants.TableName.ADM_DEVICE_DEFINITION)
                .setPrscDrName(practitioner.getName()).setPharName(medDoctor.getName())
                .setPharPracCertNo(medDoctor.getPharPracCertNo()).setPsnCertType(mdtrtCertType.getValue())
                .setManuLotnum(deviceDispense.getLotNumber()).setManuDate(inventoryItem.getProductionDate())
                .setTrdnFlag(flag.getCode()).setRtalDocno(invoice.getId().toString())
                .setSelRetnCnt(new BigDecimal(deviceDispense.getQuantity()))
                .setSelRetnTime(deviceDispense.getDispenseTime()).setSelRetnOpterName(performer.getName())
                .setRxFlag(deviceDefinition.getRxFlag().toString()).setExpyEnd(inventoryItem.getExpirationDate());

            // 溯源码
            SupplyDelivery supplyDelivery = iSupplyDeliveryService.getOne(
                new LambdaQueryWrapper<SupplyDelivery>().eq(SupplyDelivery::getLotNumber, deviceDispense.getLotNumber())
                    .eq(SupplyDelivery::getItemTable, CommonConstants.TableName.ADM_DEVICE_DEFINITION)
                    .eq(SupplyDelivery::getItemId, deviceDispense.getDeviceDefId()));
            if (supplyDelivery != null) {
                Map map = new HashMap<>();
                map.put("drug_trac_codg", supplyDelivery.getTraceNo());
                // medical3505Param.setDrugtracinfo(JSON.toJSONString(map));
            }

        } else if (type != YbListType.MEDICAL_SERVICE_ITEM) {
            // 药品的相关处理
            MedicationDispense medicationDispense = iMedicationDispenseService.getById(id);
            if (medicationDispense == null) {
                throw new ServiceException("未查询到药品信息");
            }
            if (medicationDispense.getStatusEnum() != DispenseStatus.COMPLETED.getValue()) {
                throw new ServiceException("未发药");
            }
            MedicationRequest medicationRequest = iMedicationRequestService.getById(medicationDispense.getMedReqId());
            if (medicationRequest == null) {
                throw new ServiceException("未查询到药品请求信息");
            }
            MedicationDefinition medicationDefinition =
                iMedicationDefinitionService.getById(medicationDispense.getMedicationId());
            if (medicationDefinition == null) {
                throw new ServiceException("未查询到药品信息");
            }

            // 查询库存信息
            InventoryItem inventoryItem = inventoryItemService.getOne(new LambdaQueryWrapper<InventoryItem>()
                .eq(InventoryItem::getLotNumber, medicationDispense.getLotNumber())
                .eq(InventoryItem::getLocationId, medicationDispense.getLocationId())
                .eq(InventoryItem::getItemTable, CommonConstants.TableName.ADM_DEVICE_DEFINITION)
                .eq(InventoryItem::getItemId, medicationDispense.getMedicationId())
                .eq(InventoryItem::getTenantId, tenantId));
            if (inventoryItem == null) {
                throw new ServiceException("未查询到库存信息");
            }
            // 查询供应申请单 2025/04/18经在微信群中确认，仅能通过locid，lotnumber，定义id定位库存记录，无法继续准确追到供应请求
            // SupplyRequest supplyRequest = iSupplyRequestService.getOne(new LambdaQueryWrapper<SupplyRequest>()
            // .eq(SupplyRequest::getLotNumber, medicationDispense.getLotNumber())
            // .eq(SupplyRequest::getPurposeLocationId, medicationDispense.getLocationId())
            // .eq(SupplyRequest::getItemTable, CommonConstants.TableName.ADM_DEVICE_DEFINITION)
            // .eq(SupplyRequest::getItemId, medicationDispense.getMedicationId())
            // .eq(SupplyRequest::getTenantId, tenantId));
            // if (supplyRequest == null) {
            // return null;
            // }
            // 查询开方医生姓名
            Practitioner practitioner = iPractitionerService.getOne(new LambdaQueryWrapper<Practitioner>()
                .eq(Practitioner::getUserId, medicationRequest.getPractitionerId())
                .eq(Practitioner::getTenantId, tenantId));
            // 查询当前登录用户的姓名(药师)
            Practitioner medDoctor = iPractitionerService.getOne(new LambdaQueryWrapper<Practitioner>()
                .eq(Practitioner::getUserId, SecurityUtils.getUserId()).eq(Practitioner::getTenantId, tenantId));
            // 查询发药人
            Practitioner performer = iPractitionerService.getOne(new LambdaQueryWrapper<Practitioner>()
                .eq(Practitioner::getUserId, medicationDispense.getPractitionerId())
                .eq(Practitioner::getTenantId, tenantId));
            // 查询费用结算信息
            ChargeItem chargeItem = iChargeItemService.getOne(new LambdaQueryWrapper<ChargeItem>()
                .eq(ChargeItem::getServiceTable, CommonConstants.TableName.MED_MEDICATION_REQUEST)
                .eq(ChargeItem::getServiceId, medicationRequest.getId()).eq(ChargeItem::getTenantId, tenantId));
            if (chargeItem == null) {
                throw new ServiceException("未查询到收费信息");
            }
            // 查询付款信息
            PaymentReconciliation paymentReconciliation =
                iPaymentReconciliationService.getOne(new LambdaQueryWrapper<PaymentReconciliation>()
                    .eq(PaymentReconciliation::getEncounterId, chargeItem.getEncounterId())
                    .eq(PaymentReconciliation::getTenantId, tenantId)
                    .like(PaymentReconciliation::getChargeItemIds, chargeItem.getId())
                    .eq(PaymentReconciliation::getStatusEnum, ChargeItemStatus.BILLED.getValue())
                    .eq(PaymentReconciliation::getPatientId, chargeItem.getPatientId())
                    .eq(PaymentReconciliation::getPaymentEnum, PaymentType.PAY.getValue()));
            if (paymentReconciliation == null) {
                throw new ServiceException("未查询到收费信息");
            }
            // 查询合同实体
            Contract contract = iContractService.getOne(
                new LambdaQueryWrapper<Contract>().eq(Contract::getBusNo, paymentReconciliation.getContractNo()));
            if (contract == null) {
                throw new ServiceException("未查询到合同信息");
            }
            // 查询就诊信息
            Encounter encounter = iEncounterService.getById(medicationDispense.getEncounterId());
            if (encounter == null) {
                throw new ServiceException("未查询到就诊信息");
            }
            // 查询账户信息
            Account account = accountService
                .getOne(new LambdaQueryWrapper<Account>().eq(Account::getEncounterId, chargeItem.getEncounterId())
                    .eq(Account::getTenantId, tenantId).eq(Account::getEncounterFlag, Whether.YES.getValue()));
            if (account == null) {
                throw new ServiceException("未查询到账号信息");
            }
            YbMdtrtCertType mdtrtCertType = YbMdtrtCertType.getByValue(account.getTypeCode());
            if (mdtrtCertType == null) {
                throw new ServiceException("未查询到就诊凭证信息");
            }
            // 查询就诊id
            if (contract.getCategoryEnum().equals(Category.SELF.getValue())
                || contract.getCategoryEnum().equals(Category.PUBLIC.getValue())) {
                medical3505Param.setMdtrtSn(encounter.getBusNo());
            } else {
                ClinicReg one = iRegService
                    .getOne(new LambdaQueryWrapper<ClinicReg>().eq(ClinicReg::getMdtrtCertType, account.getTypeCode())
                        .eq(ClinicReg::getMdtrtCertNo, account.getNo()));
                if (one != null) {
                    medical3505Param.setMdtrtSn(one.getMdtrtId());
                }
            }
            // 判断拆零标志
            Whether flag;
            if (medicationDefinition.getDefEncounterUnitCode().equals(medicationDispense.getUnitCode())) {
                flag = Whether.NO;
            } else {
                flag = Whether.YES;
            }
            // 查询发票信息
            Invoice invoice = iInvoiceService.getById(paymentReconciliation.getInvoiceId());
            if (invoice == null) {
                throw new ServiceException("未查询到发票信息");
            }
            // todo：sjq 商品销售的数量是根据是否拆零计算出来的数量吗？
            medical3505Param.setMedListCodg(medicationDefinition.getYbNo())
                .setFixmedinsBchno(inventoryItem.getLotNumber()).setFixmedinsHilistId(medicationDefinition.getBusNo())
                .setFixmedinsHilistName(CommonConstants.TableName.ADM_DEVICE_DEFINITION)
                .setPrscDrName(practitioner.getName()).setPharName(medDoctor.getName())
                .setPharPracCertNo(medDoctor.getPharPracCertNo()).setPsnCertType(mdtrtCertType.getValue())
                .setManuLotnum(medicationDispense.getLotNumber()).setManuDate(inventoryItem.getProductionDate())
                .setTrdnFlag(flag.getCode()).setRtalDocno(invoice.getBusNo())
                .setSelRetnCnt(new BigDecimal(medicationDispense.getQuantity().toString()))
                .setSelRetnTime(medicationDispense.getDispenseTime()).setSelRetnOpterName(performer.getName())
                .setRxFlag(medicationDefinition.getRxFlag().toString()).setExpyEnd(inventoryItem.getExpirationDate());

            // 溯源码
            SupplyDelivery supplyDelivery = iSupplyDeliveryService.getOne(new LambdaQueryWrapper<SupplyDelivery>()
                .eq(SupplyDelivery::getLotNumber, medicationDispense.getLotNumber())
                .eq(SupplyDelivery::getItemTable, CommonConstants.TableName.MED_MEDICATION_DEFINITION)
                .eq(SupplyDelivery::getItemId, medicationDispense.getMedicationId()));
            if (supplyDelivery != null) {
                Map map = new HashMap<>();
                map.put("drug_trac_codg", supplyDelivery.getTraceNo());
                // medical3505Param.setDrugtracinfo(JSON.toJSONString(map));
            }
        }
        return medical3505Param;
    }

    /**
     * 【3506】商品销售退货
     * 
     * @param id 对应发放业务的主键id
     * @param ListType 目录种类
     * @param tenantId 租户id
     * @return 3506参数
     */
    public Medical3506Param getMedical3506Param(Long id, String ListType, Integer tenantId) {
        Medical3506Param medical3506Param = new Medical3506Param();
        // 销售商品的种类
        YbListType type = YbListType.getByValue(ListType);
        if (type == YbListType.MEDICAL_CONSUMABLES) {
            // 耗材的相关处理
            DeviceDispense deviceDispense = iDeviceDispenseService.getById(id);
            if (deviceDispense == null) {
                throw new ServiceException("未查询到耗材信息");
            }
            if (deviceDispense.getStatusEnum() != DispenseStatus.REFUNDED.getValue()) {
                throw new ServiceException("未退耗材");
            }
            DeviceRequest deviceRequest = iDeviceRequestService.getById(deviceDispense.getDeviceReqId());
            if (deviceRequest == null) {
                throw new ServiceException("未查询到耗材请求信息");
            }
            DeviceDefinition deviceDefinition = iDeviceDefinitionService.getById(deviceDispense.getDeviceDefId());
            if (deviceDefinition == null) {
                throw new ServiceException("未查询到耗材信息");
            }
            // 查询库存信息
            InventoryItem inventoryItem = inventoryItemService.getOne(
                new LambdaQueryWrapper<InventoryItem>().eq(InventoryItem::getLotNumber, deviceDispense.getLotNumber())
                    .eq(InventoryItem::getLocationId, deviceDispense.getLocationId())
                    .eq(InventoryItem::getItemTable, CommonConstants.TableName.ADM_DEVICE_DEFINITION)
                    .eq(InventoryItem::getItemId, deviceDispense.getDeviceDefId())
                    .eq(InventoryItem::getTenantId, tenantId));
            if (inventoryItem == null) {
                throw new ServiceException("未查询到库存信息");
            }
            // 查询供应申请单 2025/04/18经在微信群中确认，仅能通过locid，lotnumber，定义id定位库存记录，无法继续准确追到供应请求
            SupplyRequest supplyRequest = iSupplyRequestService.getOne(
                new LambdaQueryWrapper<SupplyRequest>().eq(SupplyRequest::getLotNumber, deviceDispense.getLotNumber())
                    .eq(SupplyRequest::getPurposeLocationId, deviceDispense.getLocationId())
                    .eq(SupplyRequest::getItemTable, CommonConstants.TableName.ADM_DEVICE_DEFINITION)
                    .eq(SupplyRequest::getItemId, deviceDispense.getDeviceDefId())
                    .eq(SupplyRequest::getTenantId, tenantId));
            if (supplyRequest == null) {
                throw new ServiceException("未查询到供应信息");
            }
            // 查询开方医生姓名
            Practitioner practitioner = iPractitionerService.getOne(new LambdaQueryWrapper<Practitioner>()
                .eq(Practitioner::getUserId, deviceRequest.getRequesterId()).eq(Practitioner::getTenantId, tenantId));
            // 查询当前登录用户的姓名(药师)
            Practitioner medDoctor = iPractitionerService.getOne(new LambdaQueryWrapper<Practitioner>()
                .eq(Practitioner::getUserId, SecurityUtils.getUserId()).eq(Practitioner::getTenantId, tenantId));
            // 查询发药人
            Practitioner performer = iPractitionerService.getOne(new LambdaQueryWrapper<Practitioner>()
                .eq(Practitioner::getUserId, deviceDispense.getPerformerId()).eq(Practitioner::getTenantId, tenantId));
            // 查询费用结算信息
            ChargeItem chargeItem = iChargeItemService.getOne(new LambdaQueryWrapper<ChargeItem>()
                .eq(ChargeItem::getServiceTable, CommonConstants.TableName.WOR_DEVICE_REQUEST)
                .eq(ChargeItem::getServiceId, deviceRequest.getId()).eq(ChargeItem::getTenantId, tenantId));
            if (chargeItem == null) {
                throw new ServiceException("未查询到收费信息");
            }
            // 查询付款信息
            PaymentReconciliation paymentReconciliation =
                iPaymentReconciliationService.getOne(new LambdaQueryWrapper<PaymentReconciliation>()
                    .eq(PaymentReconciliation::getEncounterId, chargeItem.getEncounterId())
                    .eq(PaymentReconciliation::getTenantId, tenantId)
                    .like(PaymentReconciliation::getChargeItemIds, chargeItem.getId())
                    .eq(PaymentReconciliation::getStatusEnum, ChargeItemStatus.BILLED.getValue())
                    .eq(PaymentReconciliation::getPatientId, chargeItem.getPatientId())
                    .eq(PaymentReconciliation::getPaymentEnum, PaymentType.PAY.getValue()));
            if (paymentReconciliation == null) {
                throw new ServiceException("未查询到付款信息");
            }
            // 查询合同实体
            Contract contract = iContractService.getOne(
                new LambdaQueryWrapper<Contract>().eq(Contract::getBusNo, paymentReconciliation.getContractNo()));
            if (contract == null) {
                throw new ServiceException("未查询到合同信息");
            }
            // 查询就诊信息
            Encounter encounter = iEncounterService.getById(deviceDispense.getEncounterId());
            if (encounter == null) {
                throw new ServiceException("未查询到就诊信息");
            }
            // 查询账户信息
            Account account = accountService.getOne(new LambdaQueryWrapper<Account>()
                .eq(Account::getEncounterId, chargeItem.getEncounterId()).eq(Account::getTenantId, tenantId));
            if (account == null) {
                throw new ServiceException("未查询到账户信息");
            }
            YbMdtrtCertType mdtrtCertType = YbMdtrtCertType.getByValue(account.getTypeCode());
            if (mdtrtCertType == null) {
                throw new ServiceException("未查询到就诊凭证信息");
            }
            // 查询就诊id
            if (contract.getCategoryEnum().equals(Category.SELF.getValue())
                || contract.getCategoryEnum().equals(Category.PUBLIC.getValue())) {
                medical3506Param.setMdtrtSn(encounter.getBusNo());
            } else {
                ClinicReg one = iRegService
                    .getOne(new LambdaQueryWrapper<ClinicReg>().eq(ClinicReg::getMdtrtCertType, account.getTypeCode())
                        .eq(ClinicReg::getMdtrtCertNo, account.getNo()));
                if (one != null) {
                    medical3506Param.setMdtrtSn(one.getMdtrtId());
                }
            }
            // 判断拆零标志
            Whether flag;
            if (deviceDefinition.getSalesUnitCode().equals(deviceDispense.getUnitCode())) {
                flag = Whether.NO;
            } else {
                flag = Whether.YES;
            }
            // 查询发票信息
            Invoice invoice = iInvoiceService.getById(paymentReconciliation.getInvoiceId());
            if (invoice == null) {
                throw new ServiceException("未查询到发票信息");
            }
            medical3506Param.setMedListCodg(deviceDefinition.getYbNo()).setTrdnFlag(flag.getCode())
                .setFixmedinsBchno(inventoryItem.getLotNumber()).setFixmedinsHilistId(deviceDefinition.getBusNo())
                .setFixmedinsHilistName(CommonConstants.TableName.ADM_DEVICE_DEFINITION)
                .setPsnCertType(mdtrtCertType.getValue()).setManuLotnum(deviceDispense.getLotNumber())
                .setManuDate(inventoryItem.getProductionDate()).setTrdnFlag(flag.getCode())
                .setSelRetnCnt(new BigDecimal(deviceDispense.getQuantity().toString()))
                .setSelRetnTime(deviceDispense.getDispenseTime()).setSelRetnOpterName(performer.getName())
                .setRxFlag(deviceDefinition.getRxFlag().toString()).setExpyEnd(inventoryItem.getExpirationDate());

        } else if (type != YbListType.MEDICAL_SERVICE_ITEM) {
            // 药品的相关处理
            MedicationDispense medicationDispense = iMedicationDispenseService.getById(id);
            if (medicationDispense == null) {
                throw new ServiceException("未查询到药品信息");
            }
            if (medicationDispense.getStatusEnum() != DispenseStatus.REFUNDED.getValue()) {
                throw new ServiceException("未退药");
            }
            MedicationRequest medicationRequest = iMedicationRequestService.getById(medicationDispense.getMedReqId());
            if (medicationRequest == null) {
                throw new ServiceException("未查询到药品信息");
            }
            MedicationDefinition medicationDefinition =
                iMedicationDefinitionService.getById(medicationDispense.getMedicationId());
            if (medicationDefinition == null) {
                throw new ServiceException("未查询到药品信息");
            }

            // 查询库存信息
            InventoryItem inventoryItem = inventoryItemService.getOne(new LambdaQueryWrapper<InventoryItem>()
                .eq(InventoryItem::getLotNumber, medicationDispense.getLotNumber())
                .eq(InventoryItem::getLocationId, medicationDispense.getLocationId())
                .eq(InventoryItem::getItemTable, CommonConstants.TableName.ADM_DEVICE_DEFINITION)
                .eq(InventoryItem::getItemId, medicationDispense.getMedicationId())
                .eq(InventoryItem::getTenantId, tenantId));
            if (inventoryItem == null) {
                throw new ServiceException("未查询到库存信息");
            }
            // 查询供应申请单 2025/04/18经在微信群中确认，仅能通过locid，lotnumber，定义id定位库存记录，无法继续准确追到供应请求
            SupplyRequest supplyRequest = iSupplyRequestService.getOne(new LambdaQueryWrapper<SupplyRequest>()
                .eq(SupplyRequest::getLotNumber, medicationDispense.getLotNumber())
                .eq(SupplyRequest::getPurposeLocationId, medicationDispense.getLocationId())
                .eq(SupplyRequest::getItemTable, CommonConstants.TableName.ADM_DEVICE_DEFINITION)
                .eq(SupplyRequest::getItemId, medicationDispense.getMedicationId())
                .eq(SupplyRequest::getTenantId, tenantId));
            if (supplyRequest == null) {
                throw new ServiceException("未查询到供应信息");
            }
            // 查询开方医生姓名
            Practitioner practitioner = iPractitionerService.getOne(new LambdaQueryWrapper<Practitioner>()
                .eq(Practitioner::getUserId, medicationRequest.getPractitionerId())
                .eq(Practitioner::getTenantId, tenantId));
            // 查询当前登录用户的姓名(药师)
            Practitioner medDoctor = iPractitionerService.getOne(new LambdaQueryWrapper<Practitioner>()
                .eq(Practitioner::getUserId, SecurityUtils.getUserId()).eq(Practitioner::getTenantId, tenantId));
            // 查询发药人
            Practitioner performer = iPractitionerService.getOne(new LambdaQueryWrapper<Practitioner>()
                .eq(Practitioner::getUserId, medicationDispense.getPractitionerId())
                .eq(Practitioner::getTenantId, tenantId));
            // 查询费用结算信息
            ChargeItem chargeItem = iChargeItemService.getOne(new LambdaQueryWrapper<ChargeItem>()
                .eq(ChargeItem::getServiceTable, CommonConstants.TableName.MED_MEDICATION_REQUEST)
                .eq(ChargeItem::getServiceId, medicationRequest.getId()).eq(ChargeItem::getTenantId, tenantId));
            if (chargeItem == null) {
                throw new ServiceException("未查询到收费信息");
            }
            // 查询付款信息
            PaymentReconciliation paymentReconciliation =
                iPaymentReconciliationService.getOne(new LambdaQueryWrapper<PaymentReconciliation>()
                    .eq(PaymentReconciliation::getEncounterId, chargeItem.getEncounterId())
                    .eq(PaymentReconciliation::getTenantId, tenantId)
                    .like(PaymentReconciliation::getChargeItemIds, chargeItem.getId())
                    .eq(PaymentReconciliation::getStatusEnum, ChargeItemStatus.BILLED.getValue())
                    .eq(PaymentReconciliation::getPatientId, chargeItem.getPatientId())
                    .eq(PaymentReconciliation::getPaymentEnum, PaymentType.PAY.getValue()));
            if (paymentReconciliation == null) {
                throw new ServiceException("未查询到付款信息");
            }
            // 查询合同实体
            Contract contract = iContractService.getOne(
                new LambdaQueryWrapper<Contract>().eq(Contract::getBusNo, paymentReconciliation.getContractNo()));
            if (contract == null) {
                throw new ServiceException("未查询到合同信息");
            }
            // 查询就诊信息
            Encounter encounter = iEncounterService.getById(medicationDispense.getEncounterId());
            if (encounter == null) {
                throw new ServiceException("未查询到就诊信息");
            }
            // 查询账户信息
            Account account = accountService
                .getOne(new LambdaQueryWrapper<Account>().eq(Account::getEncounterId, chargeItem.getEncounterId())
                    .eq(Account::getTenantId, tenantId).eq(Account::getEncounterFlag, Whether.YES.getValue()));
            if (account == null) {
                throw new ServiceException("未查询到账户信息");
            }
            YbMdtrtCertType mdtrtCertType = YbMdtrtCertType.getByValue(account.getTypeCode());
            if (mdtrtCertType == null) {
                throw new ServiceException("未查询到就诊凭证信息");
            }
            // 查询就诊id
            if (contract.getCategoryEnum().equals(Category.SELF.getValue())
                || contract.getCategoryEnum().equals(Category.PUBLIC.getValue())) {
                medical3506Param.setMdtrtSn(encounter.getBusNo());
            } else {
                ClinicReg one = iRegService
                    .getOne(new LambdaQueryWrapper<ClinicReg>().eq(ClinicReg::getMdtrtCertType, account.getTypeCode())
                        .eq(ClinicReg::getMdtrtCertNo, account.getNo()));
                if (one != null) {
                    medical3506Param.setMdtrtSn(one.getMdtrtId());
                }
            }
            // 判断拆零标志
            Whether flag;
            if (medicationDefinition.getDefEncounterUnitCode().equals(medicationDispense.getUnitCode())) {
                flag = Whether.NO;
            } else {
                flag = Whether.YES;
            }
            // 查询发票信息
            Invoice invoice = iInvoiceService.getById(paymentReconciliation.getInvoiceId());
            if (invoice == null) {
                throw new ServiceException("未查询到发票信息");
            }
            // todo：sjq 商品销售的数量是根据是否拆零计算出来的数量吗？
            medical3506Param.setMedListCodg(medicationDefinition.getYbNo())
                .setFixmedinsBchno(inventoryItem.getLotNumber()).setFixmedinsHilistId(medicationDefinition.getBusNo())
                .setFixmedinsHilistName(CommonConstants.TableName.ADM_DEVICE_DEFINITION)
                .setPsnCertType(mdtrtCertType.getValue()).setManuLotnum(medicationDispense.getLotNumber())
                .setManuDate(inventoryItem.getProductionDate()).setTrdnFlag(flag.getCode())
                .setSelRetnCnt(new BigDecimal(medicationDispense.getQuantity().toString()))
                .setSelRetnTime(medicationDispense.getDispenseTime()).setSelRetnOpterName(performer.getName())
                .setRxFlag(medicationDefinition.getRxFlag().toString()).setExpyEnd(inventoryItem.getExpirationDate());
        }
        return medical3506Param;
    }

    /**
     * 【3507】商品信息删除参数
     * 
     * @param id 库存项目id
     * @param invDataType 进销存类型
     * @return 3507参数
     */
    public Medical3507Param getMedical3507Param(Long id, String invDataType) {
        InventoryItem inventoryItem = inventoryItemService.getById(id);
        if (inventoryItem == null) {
            throw new ServiceException("未查询到库存信息");
        }
        YbInvDataType dataType = YbInvDataType.getByValue(invDataType);
        if (dataType == null) {
            throw new ServiceException("未查询进销存类型");
        }
        Medical3507Param medical3507Param = new Medical3507Param();
        medical3507Param.setFixmedinsBchno(inventoryItem.getLotNumber()).setInvDataType(dataType.getValue());
        return medical3507Param;
    }

    /**
     * 【9001】签到
     *
     * @param id 员工id
     * @return 9001参数
     */
    public Sign getSignParam(Long id, String mac) {
        Practitioner practitioner = iPractitionerService.getPractitionerByUserId(id);
        if (practitioner == null) {
            return null;
        }

        Sign sign = new Sign();
        sign.setOpterNo(practitioner.getBusNo()).setMac(mac);
        return sign;
    }

    /**
     * 【3209A】查询跨省三方对账未成功数据(吉林省)
     * 
     * @param settlement3209AWebParam 前台参数
     * @return 3209A参数
     */
    public FinancialSettlement3209AParam
        getFinancialSettlement3209AParam(Settlement3209AWebParam settlement3209AWebParam) {
        FinancialSettlement3209AParam financialSettlement3209AParam = new FinancialSettlement3209AParam();
        BeanUtils.copyProperties(settlement3209AWebParam, financialSettlement3209AParam);
        return financialSettlement3209AParam;
    }

    /**
     * 【13203】医药机构费用结算日对账结果查询
     * 
     * @param financial13203WebParam 13203 前台参数
     * @return 13203参数
     */
    public Financial13203Param getFinancial13203Param(Financial13203WebParam financial13203WebParam) {
        Financial13203Param financial13203Param = new Financial13203Param();
        BeanUtils.copyProperties(financial13203WebParam, financial13203Param);
        return financial13203Param;
    }

    /**
     * 基础信息获取
     * 
     * @return 基础信息
     */
    public BaseInfo getBaseInfo(JSONObject jsonObj) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Long hospitalId = loginUser.getHospitalId();
        // Organization hospital = iOrganizationService.getById(hospitalId);
        BaseInfo baseInfo = new BaseInfo();
        if (jsonObj != null && jsonObj.get("insuplc_admdvs") != null) {
            baseInfo.setInsuplcAdmdvs(String.valueOf(jsonObj.get("insuplc_admdvs")));
        }
        if (jsonObj != null && jsonObj.get("decrypt_flag") != null) {
            baseInfo.setDecryptFlag(String.valueOf(jsonObj.get("decrypt_flag")));
        } else {
            baseInfo.setDecryptFlag("1");
        }
        // 电子处方
        JSONObject optionJson = SecurityUtils.getLoginUser().getOptionJson();

        return baseInfo.setUserId(loginUser.getUserId()).setRealname(loginUser.getUsername())
            .setFixmedinsCode(optionJson.getString("fixmedinsCode"))
            .setFixmedinsName(optionJson.getString("fixmedinsName"))
            .setYbClientSecret(optionJson.getString("ybClientSecret")).setYbUsername(optionJson.getString("ybUsername"))
            .setYbPassword(optionJson.getString("ybPassword")).setYbGrantType(optionJson.getString("ybGrantType"))
            .setYbScope(optionJson.getString("ybScope")).setYbCliPrvKey(optionJson.getString("ybCliPrvKey"))
            .setYbClientId(optionJson.getString("ybClientId")).setAdmvs(optionJson.getString("admvs"));
    }

    public BaseInfo getBaseInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Long hospitalId = loginUser.getHospitalId();
        Organization hospital = iOrganizationService.getById(hospitalId);
        BaseInfo baseInfo = new BaseInfo();
        // 电子处方
        JSONObject optionJson = SecurityUtils.getLoginUser().getOptionJson();
        String prePrvKey = optionJson.getString(CommonConstants.Option.APP_PRVKEY);
        String prePubKey = optionJson.getString(CommonConstants.Option.PLAF_PUBKEY);
        String clientId = optionJson.getString(CommonConstants.Option.CLIENT_ID);
        String eleAddress = optionJson.getString(CommonConstants.Option.ELE_ADDRESS);
        String username = optionJson.getString(CommonConstants.Option.USERNAME);
        String password = optionJson.getString(CommonConstants.Option.PASSWORD);
        String scope = optionJson.getString(CommonConstants.Option.SCOPE);
        String grantType = optionJson.getString(CommonConstants.Option.GRANT_TYPE);
        String clientSecret = optionJson.getString(CommonConstants.Option.CLIENT_SECRET);
        String time = optionJson.getString(CommonConstants.Option.TIME);
        String preAppId = optionJson.getString(CommonConstants.Option.PRE_APP_ID);
        String preAppSecret = optionJson.getString(CommonConstants.Option.PRE_APP_SECRET);
        String templatePath = optionJson.getString(CommonConstants.Option.TEMPLATE_PATH);
        String outputPath = optionJson.getString(CommonConstants.Option.OUTPUT_PATH);
        String hospitalSealPath = optionJson.getString(CommonConstants.Option.HOSPITAL_SEAL_PATH);

        return baseInfo.setUserId(loginUser.getUserId()).setRealname(loginUser.getUsername())
            .setAdmvs(hospital.getMedinsAdmdvs())
            .setFixmedinsCode(
                SecurityUtils.getLoginUser().getOptionJson().getString(CommonConstants.Option.FIXMEDINS_CODE))
            .setFixmedinsName(
                SecurityUtils.getLoginUser().getOptionJson().getString(CommonConstants.Option.FIXMEDINS_NAME))
            .setPrePrvKey(prePrvKey).setPrePubKey(prePubKey).setClientId(clientId).setEleAddress(eleAddress)
            .setUsername(username).setPassword(password).setScope(scope).setGrantType(grantType)
            .setClientSecret(clientSecret).setTime(time).setPreAppId(preAppId).setPreAppSecret(preAppSecret)
            .setTemplatePath(templatePath).setOutputPath(outputPath).setHospitalSealPath(hospitalSealPath);

    }

}
