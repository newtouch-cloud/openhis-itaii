package com.openhis.web.pharmacymanage.appservice.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.exception.ServiceException;
import com.core.common.utils.AgeCalculatorUtil;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.SecurityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openhis.administration.domain.Account;
import com.openhis.administration.domain.ChargeItem;
import com.openhis.administration.domain.EncounterDiagnosis;
import com.openhis.administration.domain.Organization;
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
import com.openhis.web.pharmacymanage.appservice.ReturnMedicineAppService;
import com.openhis.web.pharmacymanage.dto.*;
import com.openhis.web.pharmacymanage.mapper.ReturnMedicineMapper;
import com.openhis.workflow.domain.DeviceDispense;
import com.openhis.workflow.domain.InventoryItem;
import com.openhis.workflow.service.IDeviceDispenseService;
import com.openhis.workflow.service.IDeviceRequestService;
import com.openhis.workflow.service.IInventoryItemService;
import com.openhis.yb.domain.ClinicSettle;
import com.openhis.yb.dto.Medical3506Param;
import com.openhis.yb.service.IClinicSettleService;
import com.openhis.yb.service.IRegService;
import com.openhis.yb.service.YbManager;

/**
 * 退药管理 应用实现类
 *
 * @author yangmo
 * @date 2025/4/4
 */
@Service
public class ReturnMedicineAppServiceImpl implements ReturnMedicineAppService {

    @Autowired
    private IOrganizationService iOrganizationService;

    @Autowired
    private IInventoryItemService iInventoryItemService;

    @Autowired
    private ReturnMedicineMapper returnMedicineMapper;

    @Autowired
    private IMedicationRequestService medicationRequestService;

    @Autowired
    private IMedicationDispenseService medicationDispenseService;

    @Autowired
    private IDeviceDispenseService deviceDispenseService;

    @Autowired
    private IDeviceRequestService deviceRequestService;

    @Autowired
    private AssignSeqUtil assignSeqUtil;

    @Autowired
    private YbManager ybService;

    @Autowired
    private IChargeItemService iChargeItemService;

    @Autowired
    private IPaymentReconciliationService iPaymentReconciliationService;

    @Autowired
    private IContractService iContractService;

    @Autowired
    private IClinicSettleService clinicSettleService;

    @Autowired
    private IEncounterDiagnosisService encounterDiagnosisService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IRegService iRegService;

    /**
     * 获取页面初始化信息
     *
     * @return 初始化信息
     */
    @Override
    public R<?> init() {

        ReturnMedicineInitDto initDto = new ReturnMedicineInitDto();

        // 获取科室下拉选列表
        List<Organization> organizationList =
            iOrganizationService.getList(OrganizationType.DEPARTMENT.getValue(), OrganizationClass.CLINIC.getValue());
        List<ReturnMedicineInitDto.DepartmentOption> organizationOptions = organizationList.stream().map(
            organization -> new ReturnMedicineInitDto.DepartmentOption(organization.getId(), organization.getName()))
            .collect(Collectors.toList());

        // 发药状态
        List<ReturnMedicineInitDto.RefundStatusOption> refundStatusOptions = new ArrayList<>();
        refundStatusOptions.add(new ReturnMedicineInitDto.RefundStatusOption(RequestStatus.IN_REFUND.getValue(),
            RequestStatus.IN_REFUND.getInfo()));
        refundStatusOptions.add(new ReturnMedicineInitDto.RefundStatusOption(RequestStatus.COMPLETED.getValue(),
            RequestStatus.COMPLETED.getInfo()));
        initDto.setDepartmentOptions(organizationOptions).setRefundStatusOptions(refundStatusOptions);
        return R.ok(initDto);
    }

    /**
     * 查询退药患者分页列表
     *
     * @param encounterInfoPageDto 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 退药患者分页列表
     */
    @Override
    public R<?> getReturnMedicinePatientPage(EncounterInfoPageDto encounterInfoPageDto, String searchKey,
        Integer pageNo, Integer pageSize, HttpServletRequest request) {
        // 构建查询条件
        QueryWrapper<EncounterInfoPageDto> queryWrapper =
            HisQueryUtils.buildQueryWrapper(encounterInfoPageDto, searchKey,
                new HashSet<>(Arrays.asList(CommonConstants.FieldName.PatientName, CommonConstants.FieldName.idCard,
                    CommonConstants.FieldName.PatientPyStr, CommonConstants.FieldName.PatientWbStr)),
                request);
        // 查询退药患者分页列表
        Page<EncounterInfoPageDto> encounterInfoPage =
            returnMedicineMapper.selectEncounterInfoListPage(new Page<>(pageNo, pageSize), queryWrapper);
        encounterInfoPage.getRecords().forEach(encounterInfo -> {
            // 性别
            encounterInfo.setGenderEnum_enumText(
                EnumUtils.getInfoByValue(AdministrativeGender.class, encounterInfo.getGenderEnum()));
            // 年龄
            encounterInfo.setAge(AgeCalculatorUtil.getAge(encounterInfo.getBirthDate()));
            // 退药状态
            encounterInfo
                .setRefundEnum_enumText(EnumUtils.getInfoByValue(RequestStatus.class, encounterInfo.getRefundEnum()));
        });
        return R.ok(encounterInfoPage);
    }

    /**
     * 查询退药信息
     *
     * @param encounterId 就诊ID
     * @param refundStatus 退药id
     * @return 退药信息
     */
    @Override
    public R<?> getReturnMedicineInfo(Long encounterId, Integer refundStatus) {

        // // 患者基本信息查询
        // PrescriptionPatientInfoDto prescriptionPatientInfoDto =
        // returnMedicineMapper.selectPrescriptionPatientInfo(encounterId);
        // // 年龄
        // prescriptionPatientInfoDto.setAge(AgeCalculatorUtil.getAge(prescriptionPatientInfoDto.getBirthDate()));
        // // 性别
        // prescriptionPatientInfoDto.setGenderEnum_enumText(
        // EnumUtils.getInfoByValue(AdministrativeGender.class, prescriptionPatientInfoDto.getGenderEnum()));
        // // 合同类型
        // if (StringUtils.isNotNull(prescriptionPatientInfoDto.getCategoryEnum())) {
        // prescriptionPatientInfoDto.setCategoryEnum_enumText(
        // EnumUtils.getInfoByValue(FinCategory.class, prescriptionPatientInfoDto.getCategoryEnum()));
        // }

        // 获取退药信息
        List<ReturnMedicineInfoDto> returnMedicineInfoList = returnMedicineMapper.selectReturnMedicineInfo(encounterId,
            CommonConstants.TableName.WOR_DEVICE_REQUEST, CommonConstants.TableName.MED_MEDICATION_REQUEST,
            ItemType.MEDICINE.getValue(), ItemType.DEVICE.getValue(), refundStatus, RequestStatus.IN_REFUND.getValue(),
            RequestStatus.COMPLETED.getValue());
        returnMedicineInfoList.forEach(returnMedicineInfoDto -> {
            // 退药状态
            returnMedicineInfoDto.setRefundEnum_enumText(
                EnumUtils.getInfoByValue(DispenseStatus.class, returnMedicineInfoDto.getRefundEnum()));
            // 退药请求状态
            returnMedicineInfoDto.setReqStatus_enumText(
                EnumUtils.getInfoByValue(RequestStatus.class, returnMedicineInfoDto.getReqStatus()));
        });

        return R.ok(returnMedicineInfoList);
    }

    /**
     * 退药处理
     *
     * @param medicineReturnList 退药清单
     * @return 处理结果
     */
    @Override
    public R<?> medicineReturn(List<ReturnMedicineDto> medicineReturnList) {
        // 分别处理退药与退耗材的请求
        List<ReturnMedicineDto> returnMedicineList = new ArrayList<>();
        List<ReturnMedicineDto> returnDeviceList = new ArrayList<>();
        medicineReturnList.forEach(item -> {
            switch (item.getTableName()) {
                case CommonConstants.TableName.MED_MEDICATION_REQUEST -> returnMedicineList
                    .add(new ReturnMedicineDto().setDispenseId(item.getDispenseId()).setRequestId(item.getRequestId()));
                case CommonConstants.TableName.WOR_DEVICE_REQUEST -> returnDeviceList
                    .add(new ReturnMedicineDto().setDispenseId(item.getDispenseId()).setRequestId(item.getRequestId()));
            }
        });
        // 获取药品发放id列表
        List<Long> medDispenseIdList =
            returnMedicineList.stream().map(ReturnMedicineDto::getDispenseId).collect(Collectors.toList());
        // 获取药品退药请求id列表
        List<Long> medRequestIdList =
            returnMedicineList.stream().map(ReturnMedicineDto::getRequestId).collect(Collectors.toList());

        // 药品已退药信息查询
        List<MedicationDispense> refundList = medicationDispenseService
            .list(new LambdaQueryWrapper<MedicationDispense>().in(MedicationDispense::getMedReqId, medRequestIdList));
        if (refundList != null) {
            if (refundList.stream().map(MedicationDispense::getStatusEnum)
                .anyMatch(x -> x.equals(DispenseStatus.REFUNDED.getValue()))) {
                throw new ServiceException("药品已退药，请勿重复退药");
            }
        }
        // 退药单列表
        List<MedicationDispense> medicationRefundList = new ArrayList<>();
        if (!medDispenseIdList.isEmpty()) {
            // 药品已发放信息查询
            List<MedicationDispense> medicationDispenseList = medicationDispenseService
                .list(new LambdaQueryWrapper<MedicationDispense>().in(MedicationDispense::getId, medDispenseIdList));
            // 根据发药单对应生成退药单
            for (MedicationDispense medicationDispense : medicationDispenseList) {
                // 退药的药品请求id
                for (ReturnMedicineDto returnMedicineDto : returnMedicineList) {
                    if (returnMedicineDto.getDispenseId().equals(medicationDispense.getId())) {
                        medicationDispense.setMedReqId(returnMedicineDto.getRequestId())
                            .setTraceNo(returnMedicineDto.getTraceNo());
                    }
                }
                // 药品发放编码
                medicationDispense.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.MEDICATION_DIS_NO.getPrefix(), 10));
                // 退药状态
                medicationDispense.setStatusEnum(DispenseStatus.REFUNDED.getValue());
                medicationDispense.setId(null);
                medicationRefundList.add(medicationDispense);
            }
            medicationDispenseService.saveBatch(medicationRefundList);
            // 药品退药请求状态变更(待退药→已完成)
            medicationRequestService.updateCompletedStatusBatch(medRequestIdList);
        }
        // 获取耗材发放id列表
        List<Long> devDispenseIdList =
            returnDeviceList.stream().map(ReturnMedicineDto::getDispenseId).collect(Collectors.toList());
        // 获取退耗材请求id列表
        List<Long> devRequestIdList =
            returnDeviceList.stream().map(ReturnMedicineDto::getRequestId).collect(Collectors.toList());
        // 退耗材单列表
        List<DeviceDispense> devRefundList = new ArrayList<>();
        if (!devDispenseIdList.isEmpty()) {
            // 耗材已发放信息查询
            List<DeviceDispense> deviceDispenseList = deviceDispenseService
                .list(new LambdaQueryWrapper<DeviceDispense>().in(DeviceDispense::getId, devDispenseIdList));
            // 根据发药单对应生成退药单
            for (DeviceDispense deviceDispense : deviceDispenseList) {
                if (DispenseStatus.REFUNDED.getValue().equals(deviceDispense.getStatusEnum())) {
                    throw new ServiceException("耗材已退药，请勿重复退药");
                }
                deviceDispense.setId(null);
                // 耗材发放编码
                deviceDispense.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.DEVICE_DIS_NO.getPrefix(), 10));
                // 退药的药品请求id
                for (ReturnMedicineDto returnDeviceDto : returnDeviceList) {
                    if (returnDeviceDto.getDispenseId().equals(deviceDispense.getId())) {
                        deviceDispense.setDeviceReqId(returnDeviceDto.getRequestId())
                            .setTraceNo(returnDeviceDto.getTraceNo());
                    }
                }
                // 退药状态
                deviceDispense.setStatusEnum(DispenseStatus.REFUNDED.getValue());
                devRefundList.add(deviceDispense);
            }
            deviceDispenseService.saveBatch(devRefundList);
            // 药品退药请求状态变更(待退药→已完成)
            deviceRequestService.updateCompletedStatusBatch(devRequestIdList);
        }

        List<InventoryItem> inventoryItemList = new ArrayList<>();
        // 获取库存信息
        List<InventoryDto> inventoryList =
            returnMedicineMapper.selectInventoryInfoList(devDispenseIdList, medDispenseIdList,
                CommonConstants.TableName.MED_MEDICATION_DEFINITION, CommonConstants.TableName.ADM_DEVICE_DEFINITION);

        // 设置库存数量
        for (InventoryDto inventory : inventoryList) {
            // 库存表项目设定
            InventoryItem inventoryItem = new InventoryItem();
            // id
            inventoryItem.setId(inventory.getInventoryId());
            // 库存数量
            if (inventory.getDispenseUnit().equals(inventory.getInventoryUnitCode())) {
                // 当前库存数量(拆零单位)=当前库存数量(拆零单位)+待退数量
                inventoryItem.setQuantity(inventory.getInventoryQuantity().add(inventory.getDispenseQuantity()));
            } else {
                // 当前库存数量(拆零单位)=当前库存数量(拆零单位)+退药数量（拆零比×待退数量）
                inventoryItem.setQuantity(inventory.getInventoryQuantity()
                    .add(inventory.getPartPercent().multiply(inventory.getDispenseQuantity())));
            }
            inventoryItemList.add(inventoryItem);
        }
        // 库存更新
        iInventoryItemService.updateBatchById(inventoryItemList);
        // 返回信息
        String returnMsg = null;
        // 调用医保商品销售退货接口
        String ybSwitch = SecurityUtils.getLoginUser().getOptionJson().getString(CommonConstants.Option.YB_SWITCH); // 医保开关
        if (Whether.YES.getCode().equals(ybSwitch)) {
            if (!medicationRefundList.isEmpty() || !devRefundList.isEmpty()) {
                List<String> uploadFailedNoList = this.ybReturnIntegrated(medDispenseIdList, devDispenseIdList);
                if (uploadFailedNoList != null) {
                    returnMsg = "3506商品销售退货上传错误，错误项目编码" + uploadFailedNoList;
                } else {
                    returnMsg = "3506商品销售退货上传成功";
                }
            }
        }
        // 返回退药成功信息
        return R.ok(returnMsg, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, new Object[] {"退药"}));
    }

    /**
     * 医保退药相关进销存接口
     *
     * @param dispenseMedIdList 退药id列表
     * @param dispenseDevIdList 退耗材id列表
     * @return 上传失败的编号集合
     */
    public List<String> ybReturnIntegrated(List<Long> dispenseMedIdList, List<Long> dispenseDevIdList) {
        List<String> uploadFailedNoList = new ArrayList<>();
        R<?> result;
        if (!dispenseMedIdList.isEmpty() || !dispenseDevIdList.isEmpty()) {
            // 查询退药项目相关信息
            // 因暂时无发药材的需求，不查询退耗材的信息
            List<DispenseInventoryDto> dispenseInventoryList = returnMedicineMapper.selectReturnItemDetail(
                dispenseDevIdList, dispenseMedIdList, CommonConstants.TableName.MED_MEDICATION_DEFINITION,
                CommonConstants.TableName.ADM_DEVICE_DEFINITION);
            for (DispenseInventoryDto dispenseInventoryDto : dispenseInventoryList) {
                if (dispenseInventoryDto.getYbNo() == null) {
                    continue;
                }
                result = ybService.cancelMerchandise(getMedical3505Result(dispenseInventoryDto));
                if (result.getCode() != 200) {
                    uploadFailedNoList.add(dispenseInventoryDto.getDispenseNo());
                }
            }
        }
        return uploadFailedNoList;
    }

    private Medical3506Param getMedical3505Result(DispenseInventoryDto dispenseInventoryDto) {
        Medical3506Param medical3506Param = new Medical3506Param();
        ObjectMapper mapper = new ObjectMapper();
        // 查询费用结算信息
        ChargeItem chargeItem = iChargeItemService.getOne(new LambdaQueryWrapper<ChargeItem>()
            .eq(ChargeItem::getServiceTable, CommonConstants.TableName.MED_MEDICATION_REQUEST)
            .eq(ChargeItem::getServiceId, dispenseInventoryDto.getMedReqId())
            .eq(ChargeItem::getTenantId, SecurityUtils.getLoginUser().getTenantId()));
        if (chargeItem == null) {
            throw new ServiceException("未查询到收费项");
        }
        // 查询就诊诊断信息
        EncounterDiagnosis encounterDiagnosis = encounterDiagnosisService.getById(chargeItem.getEncounterDiagnosisId());
        if (encounterDiagnosis == null) {
            throw new ServiceException("未查找到就诊诊断信息");
        }
        // 查询付款信息
        PaymentReconciliation paymentReconciliation =
            iPaymentReconciliationService.getOne(new LambdaQueryWrapper<PaymentReconciliation>()
                .eq(PaymentReconciliation::getEncounterId, chargeItem.getEncounterId())
                .like(PaymentReconciliation::getChargeItemIds, chargeItem.getId())
                .eq(PaymentReconciliation::getStatusEnum, PaymentStatus.SUCCESS.getValue())
                .eq(PaymentReconciliation::getPatientId, chargeItem.getPatientId())
                .eq(PaymentReconciliation::getPaymentEnum, PaymentType.PAY.getValue()));
        if (paymentReconciliation == null) {
            throw new ServiceException("未查询到收费");
        }

        // 查询账户信息
        Account account = accountService
            .getOne(new LambdaQueryWrapper<Account>().eq(Account::getEncounterId, chargeItem.getEncounterId())
                .eq(Account::getEncounterFlag, Whether.YES.getValue()));
        if (account == null) {
            throw new ServiceException("未查询到账户");
        }
        // 查询合同实体
        Contract contract =
            iContractService.getOne(new LambdaQueryWrapper<Contract>().eq(Contract::getBusNo, account.getContractNo()));
        if (contract == null) {
            throw new ServiceException("未查询到合同信息");
        }
        YbMdtrtCertType mdtrtCertType;
        if (AccountType.PERSONAL_CASH_ACCOUNT.getCode().equals(account.getTypeCode())) {
            mdtrtCertType = YbMdtrtCertType.MDTRT_CERT_TYPE02;
        } else {
            mdtrtCertType = YbMdtrtCertType.getByValue(account.getTypeCode());// 2025/05/28 该值存01/02/03
        }
        if (mdtrtCertType == null) {
            throw new ServiceException("未查询到电子凭证");
        }
        // 查询就诊id
        if (contract.getCategoryEnum().equals(Category.SELF.getValue())
            || contract.getCategoryEnum().equals(Category.PUBLIC.getValue())) {
            medical3506Param.setMdtrtSn(dispenseInventoryDto.getEncounterNo());
        } else {
            ClinicSettle clinicSettle = clinicSettleService.getOne(new LambdaQueryWrapper<ClinicSettle>()
                .in(ClinicSettle::getSetlId, List.of(paymentReconciliation.getYbSettleIds()))
                .eq(ClinicSettle::getMedType, encounterDiagnosis.getMedTypeCode()).last(" LIMIT 1"));
            if (clinicSettle != null) {
                medical3506Param.setMdtrtSn(clinicSettle.getMdtrtId());
            }
        }
        // // 查询发票信息
        // Invoice invoice = iInvoiceService.getById(paymentReconciliation.getInvoiceId());
        // if (invoice == null) {
        // throw new ServiceException("未查询到发票信息");
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
        medical3506Param.setMedListCodg(dispenseInventoryDto.getYbNo())
            .setFixmedinsBchno(dispenseInventoryDto.getLotNumber())
            .setFixmedinsHilistId(dispenseInventoryDto.getMedicationNo())
            .setFixmedinsHilistName(CommonConstants.TableName.ADM_DEVICE_DEFINITION)
            .setPsnCertType(mdtrtCertType.getValue()).setManuLotnum(dispenseInventoryDto.getLotNumber())
            .setManuDate(dispenseInventoryDto.getProductionDate())
            .setSelRetnCnt(new BigDecimal(dispenseInventoryDto.getDispenseQuantity().toString()))
            .setSelRetnTime(dispenseInventoryDto.getDispenseTime())
            .setSelRetnOpterName(dispenseInventoryDto.getPreparerName())
            .setExpyEnd(dispenseInventoryDto.getExpirationDate())
            .setMedinsProdSelNo(dispenseInventoryDto.getDispenseNo()).setDrugtracinfo(medicalTraceNo);
        if (dispenseInventoryDto.getInventoryUnitCode().equals(dispenseInventoryDto.getDispenseUnitCode())) {
            medical3506Param.setTrdnFlag(Whether.YES.getCode());
        } else {
            medical3506Param.setTrdnFlag(Whether.NO.getCode());
        }
        if (YbRxFlag.IMPORTANT_HERBAL_SLICES.getCode() == dispenseInventoryDto.getRxFlag()) {
            medical3506Param.setRxFlag(YbRxFlag.IMPORTANT_HERBAL_SLICES.getName());
        } else if (YbRxFlag.WESTERN_AND_CHINESE_PATENT_MEDICINE.getCode() == dispenseInventoryDto.getRxFlag()) {
            medical3506Param.setRxFlag(YbRxFlag.WESTERN_AND_CHINESE_PATENT_MEDICINE.getName());
        } else if (YbRxFlag.SELF_PREPARED_MEDICATION.getCode() == dispenseInventoryDto.getRxFlag()) {
            medical3506Param.setRxFlag(YbRxFlag.WESTERN_AND_CHINESE_PATENT_MEDICINE.getName());
        }
        return medical3506Param;
    }
}
