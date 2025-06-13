/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.ybmanage.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.core.common.utils.SecurityUtils;
import com.openhis.common.constant.CommonConstants;
import com.openhis.workflow.domain.ElepMedicationRequest;
import com.openhis.workflow.service.IElepMedicationRequestService;
import com.openhis.ybcatalog.domain.CatalogDrugInfo;
import com.openhis.ybcatalog.service.ICatalogDrugInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.core.common.utils.DateUtils;
import com.openhis.administration.domain.*;
import com.openhis.administration.service.*;
import com.openhis.clinical.domain.Condition;
import com.openhis.clinical.domain.ConditionDefinition;
import com.openhis.clinical.service.IConditionDefinitionService;
import com.openhis.clinical.service.IConditionService;
import com.openhis.common.enums.*;
import com.openhis.common.enums.ybenums.*;
import com.openhis.medication.domain.Medication;
import com.openhis.medication.domain.MedicationDefinition;
import com.openhis.medication.service.IMedicationDefinitionService;
import com.openhis.medication.service.IMedicationService;
import com.openhis.yb.domain.ClinicReg;
import com.openhis.yb.service.IRegService;
import com.openhis.ybelep.domain.*;
import com.openhis.ybelep.service.IElepVeriPrescriptionInfoService;
import com.openhis.ybelep.service.IElepVeriVisitInfoService;

/**
 * 医保接口调用工具
 *
 * @author yuxj
 * @date 2025-04-17
 */
@Component
public class YbEleParamBuilderUtil {

    /********************* 业务实体服务 *******************/

    // 就诊管理服务
    @Autowired
    IEncounterService iEncounterService;
    @Autowired
    IElepMedicationRequestService elepMedicationRequestService;
    // 就诊管理
    @Autowired
    IAccountService accountService;
    @Autowired
    IMedicationDefinitionService medicationDefinitionService;
    @Autowired
    ICatalogDrugInfoService catalogDrugInfoService;
    @Autowired
    IEncounterDiagnosisService encounterDiagnosisService;
    @Autowired
    IEncounterService encounterService;
    @Autowired
    IPatientService patientService;
    @Autowired
    IOrganizationService organizationService;
    @Autowired
    IPractitionerService practitionerService;
    @Autowired
    IPractitionerRoleService practitionerRoleService;
    @Autowired
    IConditionService conditionService;
    @Autowired
    IConditionDefinitionService conditionDefinitionService;

    /********************* 医保实体服务 *******************/
    @Autowired
    IRegService iRegService;
    @Autowired
    IElepVeriPrescriptionInfoService eleVerPreInfoService;
    @Autowired
    IElepVeriVisitInfoService eleVerVisInfoService;

    /**
     * 获取处方信息
     *
     * @param medicationRequest 处方信息
     * @param patient           患者信息
     * @param tenantId          租户Id
     * @return 处方信息
     */
    public PreCheckPrescription getEleVeriPrescriptionInfo(ElepMedicationRequest medicationRequest, Patient patient,
                                                           Integer tenantId) {

        // 声明处方信息对象
        PreCheckPrescription elepVeriPrescriptionInfo = new PreCheckPrescription();
        elepVeriPrescriptionInfo.setMdtrtCertType("02")
                .setMdtrtCertNo(patient.getIdCard())
                // 01-定点医疗机构就诊
                .setBizTypeCode("01").setHospRxno(medicationRequest.getPrescriptionNo())
                .setRxTypeCode(medicationRequest.getRxTypeCode().toString()).setPrscTime(medicationRequest.getIssueTime())
                // 处方有效天数
                .setValiDays(medicationRequest.getValidityDays().toString())
                // 计算截止时间
                .setValiEndTime(DateUtils.addDays(medicationRequest.getIssueTime(), medicationRequest.getValidityDays()));

        // 就诊凭证类型为03”时,填写社会保障卡卡号
        if (elepVeriPrescriptionInfo.getMdtrtCertType() == YbMdtrtCertType.MDTRT_CERT_TYPE03.getValue()) {

            Account account = accountService.getOne(
                    new LambdaQueryWrapper<Account>().eq(Account::getEncounterId, medicationRequest.getEncounterId())
                            .eq(Account::getTypeCode, AccountType.SOCIAL_SECURITY_CARD.getValue())
                            .eq(Account::getTenantId, tenantId));
            if (account == null) {
                return null;
            }
            // 设置社会保障卡号
            elepVeriPrescriptionInfo.setCardSn(account.getNo());
        }

        // todo 药品类目数（剂数）：西药、中成药时为药品的类目数量
        // 西药、中成药对应的处方类别list
        List<Integer> westernOrChineseList = new ArrayList<>();
        westernOrChineseList.add(Integer.parseInt(YbRxItemTypeCode.WESTERN_MEDICINE.getValue()));
        westernOrChineseList.add(Integer.parseInt(YbRxItemTypeCode.CHINESE_PATENT_MEDICINE.getValue()));

        // 调用 count 方法
        Long countWesternOrChinese = elepMedicationRequestService
                .selectWesternOrChineseCount(medicationRequest.getPrescriptionNo(), westernOrChineseList, tenantId);
        // 西药、中成药时为药品的类目数量
        elepVeriPrescriptionInfo.setRxDrugCnt(countWesternOrChinese.toString());

        return elepVeriPrescriptionInfo;
    }

    /**
     * 获取处方明细信息
     *
     * @param prescriptionNo 处方号
     * @param tenantId       租户Id
     * @return 处方明细信息
     */
    public List<ElepVeriPrescriptionDetail> getEleVeriPrescriptionDetail(String prescriptionNo, Integer tenantId) {

        // 查询该处方所有中药饮片
        List<ElepMedicationRequest> materialObjs = elepMedicationRequestService.list(new LambdaQueryWrapper<ElepMedicationRequest>()
                .eq(ElepMedicationRequest::getPrescriptionNo, prescriptionNo).eq(ElepMedicationRequest::getTenantId, tenantId));
        // 未查到返回空
        if (materialObjs == null) {
            return null;
        }

        List<ElepVeriPrescriptionDetail> eleDetList = new ArrayList<>();

        // 遍历 materialObjs 列表
        for (ElepMedicationRequest materialObj : materialObjs) {
            CatalogDrugInfo mObj = catalogDrugInfoService.getOne(new LambdaQueryWrapper<CatalogDrugInfo>()
                    .eq(CatalogDrugInfo::getMedicalCatalogCode, materialObj.getMedicationId()).orderByDesc(CatalogDrugInfo::getCreatedAt).last("LIMIT 1"));
            // 未查到返回空
            if (mObj == null) {
                return null;
            }

            ElepVeriPrescriptionDetail eleObj = new ElepVeriPrescriptionDetail();
            eleObj.setMedListCodg(mObj.getMedicalCatalogCode())
                    .setDrugGenname(mObj.getDrugGenericName()).setDrugDosform(mObj.getDrugForm())
                    .setDrugSpec(mObj.getDrugSpecification()).setMedcBegntime(materialObj.getEffectiveDoseStart())
                    .setMedcEndtime(materialObj.getEffectiveDoseEnd())
                    .setMedcDays(materialObj.getDispensePerDuration().toString())
                    .setDrugDosunt(mObj.getMinPricingUnit()).setDrugCnt(materialObj.getQuantity().toString())
                    // todo 医院审批标志，配合目录的限制使用标志使用（目前吉林省不启用）,暂时先写死
                    .setHospApprFlag("0")
                    // 院内内部处方号
                    .setPrescriptionNo(prescriptionNo)
                    .setRxItemTypeCode(materialObj.getRxTypeCode().toString())
                    .setRxItemTypeName(PrescriptionType.getByValue(materialObj.getRxTypeCode()).getInfo())
                    .setMedcWayCodg(materialObj.getMedRoute())
                    .setMedcWayDscr(mObj.getDosage())
                    .setSinDoscnt(materialObj.getMedDosage().toString()).setSinDosunt(materialObj.getMedDosageUnitCode())
                    .setUsedFrquCodg(materialObj.getMedFrequency())
                    .setUsedFrquName(YbUsedFrqu.getByValue(materialObj.getMedFrequency()).getDescription());

            eleDetList.add(eleObj);
        }

        return eleDetList;
    }

    /**
     * 获取就诊信息和诊断信息
     *
     * @param medicationRequest 处方信息
     * @param clinicReg         医保挂号保存的信息
     * @param tenantId          租户Id
     * @return 处方明细信息
     */
    public PreCheckPrescription getEleVeriVisitAndDiagnosisInfo(ElepMedicationRequest medicationRequest,
                                                                ClinicReg clinicReg, Integer tenantId) {
        // 电子处方上传预核验信息
        PreCheckPrescription preCheckPrescription = new PreCheckPrescription();

        // 获取就诊诊断信息
        EncounterDiagnosis encDiagObjs =
                encounterDiagnosisService.getOne(new LambdaQueryWrapper<EncounterDiagnosis>()
                        .eq(EncounterDiagnosis::getEncounterId, medicationRequest.getEncounterId())
                        .eq(EncounterDiagnosis::getTenantId, tenantId).eq(EncounterDiagnosis::getTenantId, tenantId).eq(EncounterDiagnosis::getMaindiseFlag, Whether.YES.getValue()));
        // 就诊管理
        Encounter encounter = encounterService.getOne(new LambdaQueryWrapper<Encounter>()
                .eq(Encounter::getId, medicationRequest.getEncounterId()));
        // 患者信息
        Patient patient = patientService.getOne(new LambdaQueryWrapper<Patient>()
                .eq(Patient::getId, encounter.getPatientId()).eq(Patient::getTenantId, tenantId));
        // 医生所属科室
        Organization organization = organizationService.getOne(new LambdaQueryWrapper<Organization>()
                .eq(Organization::getId, medicationRequest.getOrgId()).eq(Organization::getTenantId, tenantId));
        // 就诊诊断所属科室
        Organization orgDis = organizationService.getOne(new LambdaQueryWrapper<Organization>()
                .eq(Organization::getId, encounter.getOrganizationId()).eq(Organization::getTenantId, tenantId));
        // 医生信息
        Practitioner practitioner = practitionerService.getOne(new LambdaQueryWrapper<Practitioner>()
                .eq(Practitioner::getId, medicationRequest.getPrescribingDrId()).eq(Practitioner::getTenantId, tenantId));
        // 医生详细信息
        Practitioner praRole = practitionerService.getOne(new LambdaQueryWrapper<Practitioner>()
                .eq(Practitioner::getId, medicationRequest.getPrescribingDrId())
                .eq(Practitioner::getTenantId, tenantId));
        // 医生所属科室信息
        Organization orgDor = organizationService.getOne(new LambdaQueryWrapper<Organization>()
                .eq(Organization::getId, praRole.getOrgId()).eq(Organization::getTenantId, tenantId));
        if (encDiagObjs == null || encounter == null || patient == null || organization == null || orgDis == null
                || practitioner == null || praRole == null || orgDor == null) {
            return null;
        }
        // 门诊/住院判断
        String otpIptFlag = "";
        if (encounter.getClassEnum() == EncounterClass.AMB.getValue()) {
            otpIptFlag = YbEncounterClass.AMB.getValue();
        } else if (encounter.getClassEnum() == EncounterClass.IMP.getValue()) {
            otpIptFlag = YbEncounterClass.IMP.getValue();
        }

        String fixmedinsCode = SecurityUtils.getLoginUser().getOptionJson().getString(CommonConstants.Option.FIXMEDINS_CODE);
        String fixmedinsName = SecurityUtils.getLoginUser().getOptionJson().getString(CommonConstants.Option.FIXMEDINS_NAME);

        // 1-输入-就诊信息
        ElepVeriVisitInfo eleInfo = new ElepVeriVisitInfo();
        // 诊断信息
        Condition condition = conditionService.getOne(new LambdaQueryWrapper<Condition>()
                .eq(Condition::getId, encDiagObjs.getConditionId()).eq(Condition::getTenantId, tenantId));
        // 诊断定义
        ConditionDefinition cdObj = conditionDefinitionService.getOne(new LambdaQueryWrapper<ConditionDefinition>()
                .eq(ConditionDefinition::getId, condition.getDefinitionId())
                .eq(ConditionDefinition::getTenantId, tenantId));
        if (condition == null || cdObj == null) {
            return null;
        }
        // 以挂号时间为截至时间，计算年龄
        BigDecimal age = calculateAge(patient.getBirthDate(), clinicReg.getBegntime());
        eleInfo.setFixmedinsCode(fixmedinsCode).setFixmedinsName(fixmedinsName).setMdtrtId(clinicReg.getMdtrtId())
                .setIptOtpNo(clinicReg.getIptOtpNo()).setOtpIptFlag(otpIptFlag)
                .setPsnNo(clinicReg.getPsnNo()).setPatnName(patient.getName()).setCertno(patient.getIdCard())
                // todo 目前默认都是身份证:01
                .setPsnCertType(YbIdDocumentType.RESIDENT_ID_CARD.getValue()).setPatnAge(age.toString())
                .setPrscDeptName(organization.getName()).setPrscDeptCode(organization.getYbNo())
                .setDrCode(practitioner.getYbNo()).setPrscDrName(praRole.getName())
                .setDrProfttlCodg(practitioner.getDrProfttlCode())
                .setDrProfttlName(YbDoctorTitle.getByValue(practitioner.getDrProfttlCode()).getDescription())
                .setDrDeptCode(orgDor.getYbNo()).setDrDeptName(orgDor.getName()).setMdtrtTime(encounter.getReceptionTime())
                // 院内内部处方号
                .setPrescriptionNo(medicationRequest.getPrescriptionNo());
        if (clinicReg.getMedType() == null) {
            eleInfo.setMedType("11");
        } else {

            eleInfo.setMedType(clinicReg.getMedType());
        }

        // 性别
        if (patient.getGenderEnum() == AdministrativeGender.FEMALE.getValue()) {
            eleInfo.setGend(YbGender.FEMALE.getValue());
        } else if (patient.getGenderEnum() == AdministrativeGender.MALE.getValue()) {
            eleInfo.setGend(YbGender.MALE.getValue());
        } else {
            eleInfo.setGend(YbGender.UNKNOWN.getValue());
        }
        // 特殊病种标志 2025/05/24 该字段不为空是，特殊病种给yes
        if (encDiagObjs.getMedTypeCode() != null) {
            eleInfo.setSpDiseFlag(Whether.YES.getCode());
        } else {
            eleInfo.setSpDiseFlag(Whether.NO.getCode());
        }
        // 主诊断标记
        if (encDiagObjs.getMaindiseFlag() == Whether.YES.getValue()) {
            eleInfo.setMaindiagCode(cdObj.getYbNo()).setMaindiagName(cdObj.getName());
        }

        // 2-输入-诊断信息
        ElepVeriDiagnosisInfo eleVerDisInfo = new ElepVeriDiagnosisInfo();

        eleVerDisInfo.setDiagType(cdObj.getTypeCode()).setDiagSrtNo(encDiagObjs.getDiagSrtNo().toString())
                .setDiagCode(cdObj.getYbNo()).setDiagName(cdObj.getName()).setDiagDept(orgDis.getName())
                .setDiagDrNo(practitioner.getYbNo()).setDiagDrName(praRole.getName())
                .setDiagTime(encounter.getReceptionTime())
                // 院内内部处方号
                .setPrescriptionNo(medicationRequest.getPrescriptionNo());
        // 主诊断标记
        if (encDiagObjs.getMaindiseFlag() == Whether.YES.getValue()) {
            eleVerDisInfo.setMaindiagFlag(Whether.YES.getValue().toString());
        } else {
            eleVerDisInfo.setMaindiagFlag(Whether.NO.getValue().toString());
        }

        preCheckPrescription.setMdtrtinfo(eleInfo).setDiseinfo(eleVerDisInfo);

        return preCheckPrescription;

    }

    /**
     * 做成电子处方医保电子签名入参
     *
     * @param pcpResult      电子处方上传预核验的相响应参数
     * @param practitionerId 审方药师Id
     * @param checkDate      审方时间
     * @param tenantId       租户Id
     * @return 处方信息
     */
    public ElepSignatureInput getEleSignatureInput(ElepVeriPrescriptionOutput pcpResult, Long practitionerId,
                                                   Date checkDate, Integer tenantId) {

        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String targetDateStr = targetFormat.format(checkDate);

        JSONObject optionJson = SecurityUtils.getLoginUser().getOptionJson();
        String outputPath = optionJson.getString(CommonConstants.Option.OUTPUT_PATH);
        String fixmedinsCode = optionJson.getString(CommonConstants.Option.FIXMEDINS_CODE);
        String fixmedinsName = optionJson.getString(CommonConstants.Option.FIXMEDINS_NAME);

        // 审方药师信息
        Practitioner practitioner = practitionerService.getOne(new LambdaQueryWrapper<Practitioner>()
                .eq(Practitioner::getUserId, practitionerId).eq(Practitioner::getTenantId, tenantId));
        // 审方药师所属科室信息
        Organization orgDor = organizationService.getOne(new LambdaQueryWrapper<Organization>()
                .eq(Organization::getId, practitioner.getOrgId()).eq(Organization::getTenantId, tenantId));

        // 电子处方上传预核验-输入-输入-就诊信息
        ElepVeriVisitInfo eleVerVisInfo = eleVerVisInfoService.getOne(new LambdaQueryWrapper<ElepVeriVisitInfo>()
                .eq(ElepVeriVisitInfo::getPrescriptionNo, pcpResult.getPrescriptionNo()));

        if (practitioner == null || orgDor == null || eleVerVisInfo == null) {
            return null;
        }
        // 原始待签名处方信息
        JSONObject cpdata = new JSONObject();
        // 1 rxTraceCode 处方追溯码 字符型 20 Y
        cpdata.put("rxTraceCode", pcpResult.getRxTraceCode());
        // 2 hiRxno 医保处方编号 字符型 30 Y
        cpdata.put("hiRxno", pcpResult.getHiRxno());
        // 3 mdtrtId 医保就诊 ID 字符型 30 Y
        cpdata.put("mdtrtId", eleVerVisInfo.getMdtrtId());
        // 4 patnName 患者姓名 字符型 40 Y
        cpdata.put("patnName", eleVerVisInfo.getPatnName());
        // 5 psnCertType 人员证件类型 字符型 6 Y Y //todo 默认身份证:01
        cpdata.put("psnCertType", YbIdDocumentType.RESIDENT_ID_CARD.getValue());
        // 6 certno 证件号码 字符型 50 Y
        cpdata.put("certno", eleVerVisInfo.getCertno());
        // 7 fixmedinsName 定点医疗机构名称 字符型 200 Y
        cpdata.put("fixmedinsName", fixmedinsName);
        // 8 fixmedinsCode 定点医疗机构编号 字符型 20 Y
        cpdata.put("fixmedinsCode", fixmedinsCode);
        // 9 drCode 开方医保医师代码 字符型 20 Y
        cpdata.put("drCode", eleVerVisInfo.getDrCode());
        // 10 prscDrName 开方医师姓名 字符型 50 Y
        cpdata.put("prscDrName", eleVerVisInfo.getPrscDrName());
        // 11 pharDeptName 审方药师科室名称 字符型 50 Y
        cpdata.put("pharDeptName", orgDor.getName());
        // 12 pharDeptCode 审方药师科室编号 字符型 30 Y
        cpdata.put("pharDeptCode", orgDor.getYbNo());
        // 13 pharProfttlCodg 审方药师职称编码 字符型 20 Y N
        cpdata.put("pharProfttlCodg", practitioner.getDrProfttlCode() == null ? "" : practitioner.getDrProfttlCode());
        // 14 pharProfttlName 审方药师职称名称 字符型 20
        cpdata.put("pharProfttlName", practitioner.getDrProfttlCode() == null ? ""
                : YbPharmacistTitle.getByValue(practitioner.getDrProfttlCode()).getDescription());
        // 15 pharCode 审方医保药师代码 字符型 20 Y
        cpdata.put("pharCode", practitioner.getYbNo());
        // 16 pharCertType 审方药师证件类型 字符型 6 Y N
        cpdata.put("pharCertType", "");
        // 17 pharCertno 审方药师证件号码 字符型 50 N
        cpdata.put("pharCertno", "");
        // 18 pharName 审方药师姓名 字符型 50 Y
        cpdata.put("pharName", practitioner.getName());
        // 19 pharPracCertNo 审方药师执业资格证号 字符型 50
        cpdata.put("pharPracCertNo", "");
        // 20 pharChkTime 医疗机构药师审方时间 日期时间型 Y yyyy-MM-dd HH:mm:ss
        cpdata.put("pharChkTime", targetDateStr);
        // 使用 TreeMap 对键进行排序
        TreeMap<String, Object> sortedMap = new TreeMap<>();
        for (String key : cpdata.keySet()) {
            sortedMap.put(key, cpdata.get(key));
        }
        // 重新构建排序后的 JSONObject
        JSONObject sortedCpdata = new JSONObject(sortedMap);

        // 将 cpdata 转换为 JSON 字符串
        String jsonString = sortedCpdata.toString();

        // 将 JSON 字符串编码为 Base64 字符串
        String base64EncodedString = Base64.getEncoder().encodeToString(jsonString.getBytes());

        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 定义格式（yyyyMMdd）
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        // 格式化日期
        String formattedDate = currentDate.format(formatter);


        String folderPath = outputPath + formattedDate + "\\"; // Windows 绝对路径，注意末尾的反斜杠
        String fileName = pcpResult.getPrescriptionNo() + ".pdf"; // 文件名由处方号拼接而成
        // 拼接完整的文件路径
        String filePath = folderPath + fileName;
        // 调用方法读取文件内容并获取 Base64 字符值
        String base64Content = fileToBase64(filePath);

        ElepSignatureInput eleSinIn = new ElepSignatureInput();
        eleSinIn.setFixmedinsCode(fixmedinsCode).setOriginalValue(base64EncodedString).setOriginalRxFile(base64Content)
                // 医保处方编号
                .setHiRxno(pcpResult.getHiRxno()).setPrescriptionNo(pcpResult.getPrescriptionNo());

        return eleSinIn;

    }

    /**
     * 做成电子处方上传入参
     *
     * @param pcpResult      电子处方上传预核验的相响应参数
     * @param esResult       电子处方医保电子签名响应出参
     * @param practitionerId 审方药师Id
     * @param checkDate      审方时间
     * @param tenantId       租户Id
     * @return 电子处方上传入参
     */
    public ElepUploadInput getEleUploadInput(ElepVeriPrescriptionOutput pcpResult, ElepSignatureOutput esResult,
                                             Long practitionerId, Date checkDate, Integer tenantId) {
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String targetDateStr = targetFormat.format(checkDate);

        // 审方药师信息
        Practitioner practitioner = practitionerService.getOne(new LambdaQueryWrapper<Practitioner>()
                .eq(Practitioner::getUserId, practitionerId).eq(Practitioner::getTenantId, tenantId));
        // 审方药师所属科室信息
        Organization orgDor = organizationService.getOne(new LambdaQueryWrapper<Organization>()
                .eq(Organization::getId, practitioner.getOrgId()).eq(Organization::getTenantId, tenantId));

        // 电子处方上传预核验-输入-输入-就诊信息
        ElepVeriVisitInfo eleVerVisInfo = eleVerVisInfoService.getOne(new LambdaQueryWrapper<ElepVeriVisitInfo>()
                .eq(ElepVeriVisitInfo::getPrescriptionNo, pcpResult.getPrescriptionNo()));

        if (practitioner == null || orgDor == null || eleVerVisInfo == null) {
            return null;
        }

        ElepUploadInput eleUploadInput = new ElepUploadInput();
        eleUploadInput.setRxTraceCode(pcpResult.getRxTraceCode()).setHiRxno(pcpResult.getHiRxno())
                .setMdtrtId(eleVerVisInfo.getMdtrtId()).setPatnName(eleVerVisInfo.getPatnName())
                // todo 默认身份类型是身份证：01
                .setPsnCertType(YbIdDocumentType.RESIDENT_ID_CARD.getValue()).setCertno(eleVerVisInfo.getCertno()).setPatnName(eleVerVisInfo.getPatnName())
                .setFixmedinsCode(eleVerVisInfo.getFixmedinsCode()).setFixmedinsName(eleVerVisInfo.getFixmedinsName())
                .setDrCode(eleVerVisInfo.getDrCode()).setPrscDrName(eleVerVisInfo.getPrscDrName())
                .setPharDeptCode(orgDor.getYbNo()).setPharDeptName(orgDor.getName()).setPharCode(practitioner.getYbNo())
                .setPharName(practitioner.getName()).setPharChkTime(targetDateStr).setRxFile(esResult.getRxFile())
                .setSignDigest(esResult.getSignDigest()).setPharProfttlCodg(practitioner.getDrProfttlCode()).setPharProfttlName(YbPharmacistTitle.getByValue(practitioner.getDrProfttlCode()).getDescription());

        return eleUploadInput;

    }

    /**
     * 做成电子处方撤销入参
     *
     * @param pcpResult      电子处方上传预核验的相响应参数
     * @param euResult       电子处方上传响应出参
     * @param practitionerId 撤销药师Id
     * @param description    撤销原因
     * @param revokeDate     撤销时间
     * @param tenantId       租户Id
     * @return 电子处方撤销入参
     */
    public ElepRevokeInput getElepRevokeInput(ElepVeriPrescriptionOutput pcpResult, ElepUploadOutput euResult,
                                              Long practitionerId, String description, Date revokeDate, Integer tenantId) {

        // 撤销时药师信息
        Practitioner practitioner = practitionerService.getOne(new LambdaQueryWrapper<Practitioner>()
                .eq(Practitioner::getUserId, practitionerId).eq(Practitioner::getTenantId, tenantId));
        // 撤销时药师所属科室信息
        Organization orgDor = organizationService.getOne(new LambdaQueryWrapper<Organization>()
                .eq(Organization::getId, practitioner.getOrgId()).eq(Organization::getTenantId, tenantId));

        // 电子处方上传预核验-输入-输入-就诊信息
        ElepVeriVisitInfo eleVerVisInfo = eleVerVisInfoService.getOne(new LambdaQueryWrapper<ElepVeriVisitInfo>()
                .eq(ElepVeriVisitInfo::getPrescriptionNo, pcpResult.getPrescriptionNo()));

        if (practitioner == null || orgDor == null || eleVerVisInfo == null) {
            return null;
        }

        ElepRevokeInput eleRevokeInput = new ElepRevokeInput();
        eleRevokeInput.setHiRxno(pcpResult.getHiRxno()).setFixmedinsCode(eleVerVisInfo.getFixmedinsCode())
                .setDrCode(practitioner.getYbNo()).setUndoDrName(practitioner.getName())
                // todo 默认身份类型是身份证：01
                .setUndoDrCertno(practitioner.getPharPracCertNo())
                .setUndoDrCertType(YbIdDocumentType.RESIDENT_ID_CARD.getValue()).setUndoRea(description).setUndoTime(revokeDate);

        return eleRevokeInput;
    }

    /**
     * 做成电子处方信息查询入参
     *
     * @param pcpResult 电子处方上传预核验的相响应参数
     * @return 电子处方信息查询入参
     */
    public ElepQuerPrescriptionInput getEleQueryPrescriptionInput(ElepVeriPrescriptionOutput pcpResult) {

        // 电子处方上传预核验-输入-输入-就诊信息
        ElepVeriVisitInfo eleVerVisInfo = eleVerVisInfoService.getOne(new LambdaQueryWrapper<ElepVeriVisitInfo>()
                .eq(ElepVeriVisitInfo::getPrescriptionNo, pcpResult.getPrescriptionNo()));

        if (eleVerVisInfo == null) {
            return null;
        }

        ElepQuerPrescriptionInput eleQueryPreObj = new ElepQuerPrescriptionInput();
        eleQueryPreObj.setFixmedinsCode(eleVerVisInfo.getFixmedinsCode()).setHiRxno(pcpResult.getHiRxno())
                .setMdtrtId(eleVerVisInfo.getMdtrtId()).setPsnName(eleVerVisInfo.getPatnName())
                // todo 默认身份类型是身份证：01
                .setPsnCertType(YbIdDocumentType.RESIDENT_ID_CARD.getValue()).setCertno(eleVerVisInfo.getCertno());

        return eleQueryPreObj;
    }

    /**
     * 做成电子处方信息查询入参
     *
     * @param pcpResult 电子处方上传预核验的相响应参数
     * @return 电子处方信息查询入参
     */
    public ElepMedresultInput getEleMedResultInput(ElepVeriPrescriptionOutput pcpResult) {

        // 电子处方上传预核验-输入-输入-就诊信息
        ElepVeriVisitInfo eleVerVisInfo = eleVerVisInfoService.getOne(new LambdaQueryWrapper<ElepVeriVisitInfo>()
                .eq(ElepVeriVisitInfo::getPrescriptionNo, pcpResult.getPrescriptionNo()));

        if (eleVerVisInfo == null) {
            return null;
        }

        ElepMedresultInput eleQueryPreObj = new ElepMedresultInput();
        eleQueryPreObj.setHiRxno(pcpResult.getHiRxno()).setFixmedinsCode(eleVerVisInfo.getFixmedinsCode())
                .setMdtrtId(eleVerVisInfo.getMdtrtId()).setPsnName(eleVerVisInfo.getPatnName())
                // todo 默认身份类型是身份证：01
                .setPsnCertType(YbIdDocumentType.RESIDENT_ID_CARD.getValue()).setCertno(eleVerVisInfo.getCertno());

        return eleQueryPreObj;
    }

    /**
     * 获取BigDecimal类型的年龄
     *
     * @param birthDate 出生日期
     * @param beginTime 计算起始日期
     * @return 年龄
     */
    public static BigDecimal calculateAge(Date birthDate, Date beginTime) {
        // 验证输入参数是否为空
        if (Objects.isNull(birthDate)) {
            System.out.println("出生年月日不能为空！");
            return null;
        }
        // 验证输入参数是否为空
        if (Objects.isNull(beginTime)) {
            beginTime = DateUtils.getNowDate();
        }

        // 将 Date 转换为 LocalDate
        LocalDate localBirthDate = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localBeginTime = beginTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // 计算出生日期到起始日期之间的年份差异
        Period period = Period.between(localBirthDate, localBeginTime);

        int years = period.getYears();

        // 检查是否已经过了生日，如果没有过，则年份减一
        boolean hasBirthdayPassed = !localBirthDate.plusYears(years).isAfter(localBeginTime);
        if (!hasBirthdayPassed) {
            years--;
        }

        return BigDecimal.valueOf(years);
    }

    /**
     * 读取文件内容并获取其 Base64 字符值
     *
     * @param filePath 文件路径
     * @return 文件内容的 Base64 字符值
     */
    public static String fileToBase64(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("文件不存在！");
            return null;
        }

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            byte[] fileContent = new byte[(int) file.length()];
            fileInputStream.read(fileContent);
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
