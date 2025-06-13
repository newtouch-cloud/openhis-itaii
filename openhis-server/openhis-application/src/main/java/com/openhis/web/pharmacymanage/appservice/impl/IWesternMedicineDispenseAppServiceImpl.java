package com.openhis.web.pharmacymanage.appservice.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.core.domain.entity.SysDictData;
import com.core.common.exception.ServiceException;
import com.core.common.utils.*;
import com.core.system.mapper.SysDictDataMapper;
import com.openhis.administration.domain.*;
import com.openhis.administration.service.IAccountService;
import com.openhis.administration.service.IChargeItemService;
import com.openhis.administration.service.IEncounterDiagnosisService;
import com.openhis.administration.service.IOrganizationService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
import com.openhis.common.enums.ybenums.YbMdtrtCertType;
import com.openhis.common.enums.ybenums.YbRxFlag;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.financial.domain.Contract;
import com.openhis.financial.domain.PaymentReconciliation;
import com.openhis.financial.service.IContractService;
import com.openhis.financial.service.IPaymentReconciliationService;
import com.openhis.medication.domain.MedicationDispense;
import com.openhis.medication.service.IMedicationDispenseService;
import com.openhis.medication.service.IMedicationRequestService;
import com.openhis.web.pharmacymanage.appservice.IWesternMedicineDispenseAppService;
import com.openhis.web.pharmacymanage.dto.*;
import com.openhis.web.pharmacymanage.mapper.ReturnMedicineMapper;
import com.openhis.web.pharmacymanage.mapper.WesternMedicineDispenseMapper;
import com.openhis.workflow.domain.InventoryItem;
import com.openhis.workflow.service.IInventoryItemService;
import com.openhis.yb.domain.ClinicSettle;
import com.openhis.yb.dto.Medical3505Param;
import com.openhis.yb.service.IClinicSettleService;
import com.openhis.yb.service.YbManager;

/**
 * 西药发放 应用实现类
 *
 * @author wangyang
 * @date 2025/3/14
 */
@Service
public class IWesternMedicineDispenseAppServiceImpl implements IWesternMedicineDispenseAppService {

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private IMedicationDispenseService medicationDispenseService;

    @Autowired
    private IInventoryItemService iInventoryItemService;

    @Autowired
    private IMedicationRequestService medicationRequestService;

    @Autowired
    private WesternMedicineDispenseMapper westernMedicineDispenseMapper;

    @Autowired
    private ReturnMedicineMapper returnMedicineMapper;

    @Autowired
    private YbManager ybService;

    @Autowired
    private IChargeItemService iChargeItemService;

    @Autowired
    private IPaymentReconciliationService iPaymentReconciliationService;

    @Autowired
    private IContractService iContractService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IClinicSettleService clinicSettleService;

    @Autowired
    private IEncounterDiagnosisService encounterDiagnosisService;

    @Resource
    private SysDictDataMapper sysDictDataMapper;

    /**
     * 获取页面初始化信息
     *
     * @return 初始化信息
     */
    @Override
    public R<?> init() {

        DispenseInitDto initDto = new DispenseInitDto();

        // 获取科室下拉选列表
        List<Organization> organizationList =
            organizationService.getList(OrganizationType.DEPARTMENT.getValue(), OrganizationClass.CLINIC.getValue());
        List<DispenseInitDto.DepartmentOption> organizationOptions = organizationList.stream()
            .map(organization -> new DispenseInitDto.DepartmentOption(organization.getId(), organization.getName()))
            .collect(Collectors.toList());

        // 获取配药人下拉选列表
        List<Practitioner> preparerDoctorList =
            westernMedicineDispenseMapper.getPreparerDoctorList(PractitionerRoles.PHARMACIST.getCode());
        List<DispenseInitDto.PreparerDoctorOption> preparerDoctorOptions = preparerDoctorList.stream()
            .map(practitioner -> new DispenseInitDto.PreparerDoctorOption(practitioner.getId(), practitioner.getName()))
            .collect(Collectors.toList());

        // 未发药原因下拉选列表
        List<DispenseInitDto.NotPerformedReasonOption> notPerformedReasonOptions =
            Stream.of(NotPerformedReasonEnum.values())
                .map(notPerformedReason -> new DispenseInitDto.NotPerformedReasonOption(notPerformedReason.getValue(),
                    notPerformedReason.getInfo()))
                .collect(Collectors.toList());

        // 发药状态
        List<DispenseInitDto.DispenseStatusOption> dispenseStatusOptions = new ArrayList<>();
        dispenseStatusOptions.add(new DispenseInitDto.DispenseStatusOption(DispenseStatus.IN_PROGRESS.getValue(),
            DispenseStatus.IN_PROGRESS.getInfo()));
        dispenseStatusOptions.add(new DispenseInitDto.DispenseStatusOption(DispenseStatus.COMPLETED.getValue(),
            DispenseStatus.COMPLETED.getInfo()));

        initDto.setDepartmentOptions(organizationOptions).setNotPerformedReasonOptions(notPerformedReasonOptions)
            .setDispenseStatusOptions(dispenseStatusOptions).setPreparerDoctorOptions(preparerDoctorOptions);
        return R.ok(initDto);
    }

    /**
     * 分页查询发药病人列表
     *
     * @param encounterInfoPageDto 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 发药病人列表
     */
    @Override
    public R<?> getEncounterInfoListPage(EncounterInfoPageDto encounterInfoPageDto, String searchKey, Integer pageNo,
        Integer pageSize, HttpServletRequest request) {
        // 发药状态初始化
        Integer statusEnum = encounterInfoPageDto.getStatusEnum();
        encounterInfoPageDto.setStatusEnum(null);
        // 构建查询条件
        QueryWrapper<EncounterInfoPageDto> queryWrapper =
            HisQueryUtils.buildQueryWrapper(encounterInfoPageDto, searchKey,
                new HashSet<>(Arrays.asList(CommonConstants.FieldName.PatientName, CommonConstants.FieldName.idCard,
                    CommonConstants.FieldName.PatientPyStr, CommonConstants.FieldName.PatientWbStr)),
                request);
        // 查询就诊病人列表
        Page<EncounterInfoPageDto> encounterInfoPage =
            westernMedicineDispenseMapper.selectEncounterInfoListPage(new Page<>(pageNo, pageSize), queryWrapper,
                statusEnum, DispenseStatus.IN_PROGRESS.getValue(), DispenseStatus.COMPLETED.getValue(),
                DispenseStatus.PREPARATION.getValue(), DispenseStatus.PREPARED.getValue());
        encounterInfoPage.getRecords().forEach(encounterInfo -> {
            // 性别
            encounterInfo.setGenderEnum_enumText(
                EnumUtils.getInfoByValue(AdministrativeGender.class, encounterInfo.getGenderEnum()));
            // 发药状态
            encounterInfo
                .setStatusEnum_enumText(EnumUtils.getInfoByValue(DispenseStatus.class, encounterInfo.getStatusEnum()));
        });
        return R.ok(encounterInfoPage);
    }

    /**
     * 查询处方单列表
     *
     * @param encounterId 就诊号
     * @param dispenseStatus 发药状态
     * @return 处方单列表
     */
    @Override
    public R<?> getPrescriptionInfo(Long encounterId, Integer dispenseStatus) {

        // 患者基本信息查询
        PrescriptionPatientInfoDto prescriptionPatientInfoDto =
            westernMedicineDispenseMapper.selectPrescriptionPatientInfo(encounterId);
        // 年龄
        prescriptionPatientInfoDto.setAge(AgeCalculatorUtil.getAge(prescriptionPatientInfoDto.getBirthDate()));
        // 性别
        prescriptionPatientInfoDto.setGenderEnum_enumText(
            EnumUtils.getInfoByValue(AdministrativeGender.class, prescriptionPatientInfoDto.getGenderEnum()));
        // 合同类型
        if (StringUtils.isNotNull(prescriptionPatientInfoDto.getCategoryEnum())) {
            prescriptionPatientInfoDto.setCategoryEnum_enumText(
                EnumUtils.getInfoByValue(FinCategory.class, prescriptionPatientInfoDto.getCategoryEnum()));
        }

        // 处方单信息查询
        List<PrescriptionMedicineInfoDto> prescriptionMedicineInfoList =
            westernMedicineDispenseMapper.selectPrescriptionMedicineInfoList(encounterId,
                DispenseStatus.IN_PROGRESS.getValue(), DispenseStatus.COMPLETED.getValue(),
                DispenseStatus.PREPARATION.getValue(), DispenseStatus.PREPARED.getValue(), dispenseStatus);
        // 查询字典值
        List<SysDictData> unitCodeList = sysDictDataMapper.selectDictDataByType("unit_code");
        Map<String, String> unitCodeLabel = unitCodeList.stream().collect(
            Collectors.toMap(SysDictData::getDictValue, SysDictData::getDictLabel, (oldValue, newValue) -> oldValue // 如果有重复键，保留旧值
            ));
        List<SysDictData> methodCodeList = sysDictDataMapper.selectDictDataByType("method_code");
        Map<String, String> methodCodeLabel = methodCodeList.stream().collect(
            Collectors.toMap(SysDictData::getDictValue, SysDictData::getDictLabel, (oldValue, newValue) -> oldValue // 如果有重复键，保留旧值
            ));
        prescriptionMedicineInfoList.forEach(prescriptionMedicineInfo -> {
            // 发药状态
            prescriptionMedicineInfo.setStatusEnum_enumText(
                EnumUtils.getInfoByValue(DispenseStatus.class, prescriptionMedicineInfo.getStatusEnum()));
            // 匹配剂量单位字典文本
            String doseUnitCode = prescriptionMedicineInfo.getDoseUnitCode();
            String doseUnitLabel = unitCodeLabel.getOrDefault(doseUnitCode, "");
            prescriptionMedicineInfo.setDoseUnitCode_dictText(doseUnitLabel);
            // 匹配单位字典文本
            String unitCode = prescriptionMedicineInfo.getUnitCode();
            String unitLabel = unitCodeLabel.getOrDefault(unitCode, "");
            prescriptionMedicineInfo.setUnitCode_dictText(unitLabel);
            // 匹配用法字典文本
            String methodCode = prescriptionMedicineInfo.getMethodCode();
            String methodLabel = methodCodeLabel.getOrDefault(methodCode, "");
            prescriptionMedicineInfo.setMethodCode_dictText(methodLabel);
        });

        // 计算合计金额
        if (!prescriptionMedicineInfoList.isEmpty()) {
            double totalPrice = 0d;
            for (PrescriptionMedicineInfoDto item : prescriptionMedicineInfoList) {
                totalPrice += item.getTotalPrice().doubleValue();
            }
            prescriptionPatientInfoDto.setTotalPrice(totalPrice);
        }
        PrescriptionInfoDto prescriptionInfoDto = new PrescriptionInfoDto();
        prescriptionInfoDto.setPrescriptionPatientInfoDto(prescriptionPatientInfoDto);
        prescriptionInfoDto.setPrescriptionMedicineInfoDtoList(prescriptionMedicineInfoList);
        return R.ok(prescriptionInfoDto);
    }

    /**
     * 配药
     *
     * @param dispenseMedicineList 配药信息
     * @return 处理结果
     */
    @Override
    public R<?> medicinePrepare(List<DispenseMedicineDto> dispenseMedicineList) {
        // 配药人检查
        if (dispenseMedicineList.get(0).getPreparerId() == null
            || dispenseMedicineList.get(0).getPreparerId().equals(SecurityUtils.getLoginUser().getPractitionerId())) {
            return R.fail("请选择调配药师");
        }
        // 获取发药单id列表
        List<Long> medDispenseIdList =
            dispenseMedicineList.stream().map(DispenseMedicineDto::getDispenseId).collect(Collectors.toList());
        // 获取库存信息
        List<InventoryDto> inventoryList = returnMedicineMapper.selectInventoryInfoList(null, medDispenseIdList,
            CommonConstants.TableName.MED_MEDICATION_DEFINITION, CommonConstants.TableName.ADM_DEVICE_DEFINITION);
        if (!inventoryList.isEmpty()) {
            if (inventoryList.stream().map(InventoryDto::getInventoryStatusEnum)
                .allMatch(x -> x.equals(PublicationStatus.RETIRED.getValue()))) {
                // 库存停供
                return R.fail("当前处方中的药品库存已停供");
            }
            for (InventoryDto inventoryDto : inventoryList) {
                if (PublicationStatus.ACTIVE.getValue().equals(inventoryDto.getInventoryStatusEnum())) {
                    // 库存数量判定
                    if (inventoryDto.getDispenseUnit().equals(inventoryDto.getInventoryUnitCode())) {
                        // 当前库存数量(拆零单位)=当前库存数量(拆零单位)-请求数量
                        BigDecimal quantity = inventoryDto.getInventoryQuantity().subtract(inventoryDto.getQuantity());
                        // 库存数量判定
                        if (quantity.compareTo(BigDecimal.ZERO) < 0) {
                            // 库存数量不足
                            return R.fail(inventoryDto.getItemName() + "当前库存数量不足");
                        }
                    } else {
                        // 当前库存数量(拆零单位)=当前库存数量(拆零单位)-拆零数量（拆零比×请求数量）
                        BigDecimal quantity = inventoryDto.getInventoryQuantity()
                            .subtract(inventoryDto.getPartPercent().multiply(inventoryDto.getQuantity()));
                        // 库存数量判定
                        if (quantity.compareTo(BigDecimal.ZERO) < 0) {
                            // 库存数量不足
                            return R.fail(inventoryDto.getItemName() + "当前库存数量不足");
                        }
                    }
                }
            }
        }
        // 药品发药信息查询
        List<MedicationDispense> dispenseList = medicationDispenseService
            .list(new LambdaQueryWrapper<MedicationDispense>().in(MedicationDispense::getId, medDispenseIdList));
        if (dispenseList != null) {
            if (dispenseList.stream().map(MedicationDispense::getStatusEnum)
                .anyMatch(x -> x.equals(DispenseStatus.PREPARED.getValue()))) {
                throw new ServiceException("药品已配药，请勿重复配药");
            } else {
                for (MedicationDispense medicationDispense : dispenseList) {
                    // 药品发放状态
                    medicationDispense.setStatusEnum(DispenseStatus.PREPARED.getValue());
                    // 状态变更时间
                    medicationDispense.setStatusChangedTime(DateUtils.getNowDate());
                    // 配药人
                    medicationDispense.setPreparerId(dispenseMedicineList.get(0).getPreparerId());
                    // 配药时间
                    medicationDispense.setPrepareTime(DateUtils.getNowDate());
                    for (DispenseMedicineDto dispenseMedicineDto : dispenseMedicineList) {
                        if (medicationDispense.getId().equals(dispenseMedicineDto.getDispenseId()))
                            // 药品追溯码
                            medicationDispense.setTraceNo(dispenseMedicineDto.getTraceNo());
                    }
                }
                // 药品发放更新
                medicationDispenseService.updateBatchById(dispenseList);
            }
        } else {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }
        return R.ok(MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null));
    }

    /**
     * 处方单核对发药
     *
     * @param dispenseMedicineList 发药信息
     * @return 处理结果
     */
    @Override
    public R<?> medicineDispense(List<DispenseMedicineDto> dispenseMedicineList) {
        List<InventoryItem> inventoryItemList = new ArrayList<>();
        // 获取发药单id列表
        List<Long> medDispenseIdList =
            dispenseMedicineList.stream().map(DispenseMedicineDto::getDispenseId).collect(Collectors.toList());
        // 获取药品申请id列表
        List<Long> medRequestIdList =
            dispenseMedicineList.stream().map(DispenseMedicineDto::getRequestId).collect(Collectors.toList());
        // 获取库存信息
        List<InventoryDto> inventoryList = returnMedicineMapper.selectInventoryInfoList(null, medDispenseIdList,
            CommonConstants.TableName.MED_MEDICATION_DEFINITION, CommonConstants.TableName.ADM_DEVICE_DEFINITION);
        if (!inventoryList.isEmpty()) {
            if (inventoryList.stream().map(InventoryDto::getInventoryStatusEnum)
                .allMatch(x -> x.equals(PublicationStatus.RETIRED.getValue()))) {
                // 库存停供
                return R.fail("当前处方中的药品库存已停供");
            }
            for (InventoryDto inventoryDto : inventoryList) {
                if (PublicationStatus.ACTIVE.getValue().equals(inventoryDto.getInventoryStatusEnum())) {
                    InventoryItem inventoryItem = new InventoryItem();
                    // 库存数量判定
                    if (inventoryDto.getDispenseUnit().equals(inventoryDto.getInventoryUnitCode())) {
                        // 当前库存数量(拆零单位)=当前库存数量(拆零单位)-请求数量
                        BigDecimal quantity = inventoryDto.getInventoryQuantity().subtract(inventoryDto.getQuantity());
                        // 库存数量判定
                        if (quantity.compareTo(BigDecimal.ZERO) < 0) {
                            // 库存数量不足
                            return R.fail(inventoryDto.getItemName() + "当前库存数量不足");
                        } else {
                            inventoryItem.setId(inventoryDto.getInventoryId()).setQuantity(quantity);
                        }
                    } else {
                        // 当前库存数量(拆零单位)=当前库存数量(拆零单位)-拆零数量（拆零比×请求数量）
                        BigDecimal quantity = inventoryDto.getInventoryQuantity()
                            .subtract(inventoryDto.getPartPercent().multiply(inventoryDto.getQuantity()));
                        // 库存数量判定
                        if (quantity.compareTo(BigDecimal.ZERO) < 0) {
                            // 库存数量不足
                            return R.fail(inventoryDto.getItemName() + "当前库存数量不足");
                        } else {
                            inventoryItem.setId(inventoryDto.getInventoryId()).setQuantity(quantity);
                        }
                    }
                    inventoryItemList.add(inventoryItem);
                }
            }
        }
        // 药品发药信息查询
        List<MedicationDispense> dispenseList = medicationDispenseService
            .list(new LambdaQueryWrapper<MedicationDispense>().in(MedicationDispense::getId, medDispenseIdList));
        if (dispenseList != null) {
            if (dispenseList.stream().map(MedicationDispense::getStatusEnum)
                .anyMatch(x -> x.equals(DispenseStatus.COMPLETED.getValue()))) {
                throw new ServiceException("药品已发药，请勿重复发药");
            } else {
                for (MedicationDispense medicationDispense : dispenseList) {
                    // 药品发放状态
                    medicationDispense.setStatusEnum(DispenseStatus.COMPLETED.getValue());
                    // 状态变更时间
                    medicationDispense.setStatusChangedTime(DateUtils.getNowDate());
                    // 发药数量
                    medicationDispense.setDispenseQuantity(medicationDispense.getQuantity());
                    // 发药时间
                    medicationDispense.setDispenseTime(DateUtils.getNowDate());
                    // 发药人
                    medicationDispense.setPractitionerId(SecurityUtils.getLoginUser().getPractitionerId());
                }
                // 药品发放更新
                medicationDispenseService.updateBatchById(dispenseList);
            }
        } else {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }
        // 库存更新
        iInventoryItemService.updateBatchById(inventoryItemList);
        // 更新请求状态为已完成
        medicationRequestService.updateCompletedStatusBatch(medRequestIdList);
        // 返回信息
        String returnMsg = null;
        // 调用医保商品销售接口
        String ybSwitch = SecurityUtils.getLoginUser().getOptionJson().getString(CommonConstants.Option.YB_SWITCH); // 医保开关
        if (Whether.YES.getCode().equals(ybSwitch)) {
            List<String> uploadFailedNoList = this.ybMedicineIntegrated(medDispenseIdList);
            if (uploadFailedNoList != null) {
                returnMsg = "3505商品销售上传错误，错误项目编码" + uploadFailedNoList;
            } else {
                returnMsg = "3505商品销售上传成功";
            }
        }
        return R.ok(returnMsg, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null));
    }

    /**
     * 作废
     *
     * @param dispenseMedicineList 作废信息
     * @return 处理结果
     */
    @Override
    public R<?> medicineCancel(List<DispenseMedicineDto> dispenseMedicineList) {
        // 获取发药单id列表
        List<Long> medDispenseIdList =
            dispenseMedicineList.stream().map(DispenseMedicineDto::getDispenseId).collect(Collectors.toList());
        // 药品发药信息查询
        List<MedicationDispense> dispenseList = medicationDispenseService
            .list(new LambdaQueryWrapper<MedicationDispense>().in(MedicationDispense::getId, medDispenseIdList));
        if (dispenseList != null) {
            for (MedicationDispense medicationDispense : dispenseList) {
                // 药品发放状态
                medicationDispense.setStatusEnum(DispenseStatus.DECLINED.getValue());
                // 未发药原因
                medicationDispense.setNotPerformedReasonEnum(dispenseMedicineList.get(0).getNotPerformedReasonEnum());
                // 状态变更时间
                medicationDispense.setStatusChangedTime(DateUtils.getNowDate());
            }
            // 药品发放更新
            medicationDispenseService.updateBatchById(dispenseList);
        } else {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }
        return R.ok(MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null));
    }

    /**
     * 医保发药相关进销存接口
     *
     * @param medDispenseIdList 发药id列表
     * @return 上传失败的编号集合
     */
    public List<String> ybMedicineIntegrated(List<Long> medDispenseIdList) {
        List<String> uploadFailedNoList = new ArrayList<>();
        R<?> result;
        if (!medDispenseIdList.isEmpty()) {
            // 查询发药项目相关信息
            List<DispenseInventoryDto> dispenseInventoryList = returnMedicineMapper.selectReturnItemDetail(null,
                medDispenseIdList, CommonConstants.TableName.MED_MEDICATION_DEFINITION,
                CommonConstants.TableName.ADM_DEVICE_DEFINITION);
            for (DispenseInventoryDto dispenseInventoryDto : dispenseInventoryList) {
                if (dispenseInventoryDto.getYbNo() == null) {
                    continue;
                }
                result = ybService.merchandise(getMedical3505Param(dispenseInventoryDto));
                if (result.getCode() != 200) {
                    uploadFailedNoList.add(dispenseInventoryDto.getDispenseNo());
                }
            }
        }
        return uploadFailedNoList;
    }

    private Medical3505Param getMedical3505Param(DispenseInventoryDto dispenseInventoryDto) {
        Medical3505Param medical3505Param = new Medical3505Param();
        // 查询费用结算信息
        ChargeItem chargeItem = iChargeItemService.getOne(new LambdaQueryWrapper<ChargeItem>()
            .eq(ChargeItem::getServiceTable, CommonConstants.TableName.MED_MEDICATION_REQUEST)
            .eq(ChargeItem::getServiceId, dispenseInventoryDto.getMedReqId())
            .eq(ChargeItem::getTenantId, SecurityUtils.getLoginUser().getTenantId()));
        if (chargeItem == null) {
            throw new ServiceException("未查找到收费项");
        }
        // 查询就诊诊断信息
        EncounterDiagnosis encounterDiagnosis = encounterDiagnosisService.getById(chargeItem.getEncounterDiagnosisId());
        if (encounterDiagnosis == null) {
            throw new ServiceException("未查找到就诊诊断信息");
        }
        // 查询付款信息
        PaymentReconciliation paymentReconciliation =
            iPaymentReconciliationService.getOne(new LambdaQueryWrapper<PaymentReconciliation>()
                .eq(PaymentReconciliation::getEncounterId, dispenseInventoryDto.getEncounterId())
                .eq(PaymentReconciliation::getTenantId, SecurityUtils.getLoginUser().getTenantId())
                .like(PaymentReconciliation::getChargeItemIds, chargeItem.getId())
                .eq(PaymentReconciliation::getStatusEnum, PaymentStatus.SUCCESS.getValue())
                .eq(PaymentReconciliation::getPatientId, dispenseInventoryDto.getPatientId())
                .eq(PaymentReconciliation::getPaymentEnum, PaymentType.PAY.getValue()));
        if (paymentReconciliation == null) {
            throw new ServiceException("未查找到收费信息");
        }
        // 查询账户信息
        Account account = accountService
            .getOne(new LambdaQueryWrapper<Account>().eq(Account::getEncounterId, dispenseInventoryDto.getEncounterId())
                .eq(Account::getEncounterFlag, Whether.YES.getValue()));
        if (account == null) {
            throw new ServiceException("未查找到账户信息");
        }
        // 查询合同实体
        Contract contract =
            iContractService.getOne(new LambdaQueryWrapper<Contract>().eq(Contract::getBusNo, account.getContractNo()));
        if (contract == null) {
            throw new ServiceException("未查找到合同信息");
        }
        YbMdtrtCertType mdtrtCertType;
        if (AccountType.PERSONAL_CASH_ACCOUNT.getCode().equals(account.getTypeCode())) {
            mdtrtCertType = YbMdtrtCertType.MDTRT_CERT_TYPE02;
        } else {
            mdtrtCertType = YbMdtrtCertType.getByValue(account.getTypeCode());// 2025/05/28 该值存01/02/03
        }
        if (mdtrtCertType == null) {
            throw new ServiceException("未查找到就诊凭证信息");
        }
        // 查询就诊id
        if (contract.getCategoryEnum().equals(Category.SELF.getValue())
            || contract.getCategoryEnum().equals(Category.PUBLIC.getValue())) {
            medical3505Param.setMdtrtSn(dispenseInventoryDto.getEncounterNo()).setMdtrtSetlType("2");
        } else {
            // todo：虽然是医保挂号，但是用什么结算的暂时未知先不做修改
            ClinicSettle clinicSettle = clinicSettleService.getOne(new LambdaQueryWrapper<ClinicSettle>()
                .in(ClinicSettle::getSetlId, List.of(paymentReconciliation.getYbSettleIds()))
                .eq(ClinicSettle::getMedType, encounterDiagnosis.getMedTypeCode()).last(" LIMIT 1"));
            if (clinicSettle != null) {
                medical3505Param.setMdtrtSn(clinicSettle.getMdtrtId()).setMdtrtSetlType("1");
            }
        }
        // // 查询发票信息
        // Invoice invoice = iInvoiceService.getById(paymentReconciliation.getInvoiceId());
        // if (invoice == null) {
        // throw new ServiceException("未查找到就诊发票信息");
        // }
        // 转换为JSON
        JSONArray medicalTraceNo = new JSONArray();
        // 获取追溯码信息
        if (dispenseInventoryDto.getTraceNo() != null) {
            List<String> traceNoList =
                Arrays.stream(dispenseInventoryDto.getTraceNo().split(CommonConstants.Common.COMMA)).map(String::trim)
                    .filter(s -> !s.isEmpty()).collect(Collectors.toList());
            for (String traceNo : traceNoList) {
                Map<String, String> traceNoMap = new HashMap<>();
                traceNoMap.put("drug_trac_codg", traceNo);
                medicalTraceNo.add(traceNoMap);
            }
        }
        medical3505Param.setMedListCodg(dispenseInventoryDto.getYbNo())
            .setFixmedinsBchno(dispenseInventoryDto.getLotNumber())
            .setFixmedinsHilistId(dispenseInventoryDto.getMedicationNo())
            .setFixmedinsHilistName(CommonConstants.TableName.ADM_DEVICE_DEFINITION)
            .setPrscDrName(dispenseInventoryDto.getPractitionerName())
            .setPharName(dispenseInventoryDto.getDispenseName())
            .setPharPracCertNo(dispenseInventoryDto.getPharPracCertNo()).setPsnCertType(mdtrtCertType.getValue())
            .setManuLotnum(dispenseInventoryDto.getLotNumber()).setManuDate(dispenseInventoryDto.getProductionDate())
            .setRtalDocno(dispenseInventoryDto.getDispenseNo())
            .setSelRetnCnt(new BigDecimal(dispenseInventoryDto.getDispenseQuantity().toString()))
            .setSelRetnTime(dispenseInventoryDto.getDispenseTime())
            .setSelRetnOpterName(dispenseInventoryDto.getPreparerName())
            .setExpyEnd(dispenseInventoryDto.getExpirationDate()).setStooutNo(dispenseInventoryDto.getDispenseNo())
            .setDrugtracinfo(medicalTraceNo);
        if (dispenseInventoryDto.getInventoryUnitCode().equals(dispenseInventoryDto.getDispenseUnitCode())) {
            medical3505Param.setTrdnFlag(Whether.YES.getCode());
        } else {
            medical3505Param.setTrdnFlag(Whether.NO.getCode());
        }
        if (YbRxFlag.IMPORTANT_HERBAL_SLICES.getCode() == dispenseInventoryDto.getRxFlag()) {
            medical3505Param.setRxFlag(YbRxFlag.IMPORTANT_HERBAL_SLICES.getName());
        } else if (YbRxFlag.WESTERN_AND_CHINESE_PATENT_MEDICINE.getCode() == dispenseInventoryDto.getRxFlag()) {
            medical3505Param.setRxFlag(YbRxFlag.WESTERN_AND_CHINESE_PATENT_MEDICINE.getName());
        } else if (YbRxFlag.SELF_PREPARED_MEDICATION.getCode() == dispenseInventoryDto.getRxFlag()) {
            medical3505Param.setRxFlag(YbRxFlag.WESTERN_AND_CHINESE_PATENT_MEDICINE.getName());
        }
        return medical3505Param;

    }
}
