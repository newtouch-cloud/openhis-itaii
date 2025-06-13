package com.openhis.web.doctorstation.appservice.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.exception.ServiceException;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.SecurityUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openhis.administration.domain.ChargeItem;
import com.openhis.administration.service.IChargeItemService;
import com.openhis.administration.service.IOrganizationLocationService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.medication.domain.MedicationRequest;
import com.openhis.medication.service.IMedicationDispenseService;
import com.openhis.medication.service.IMedicationRequestService;
import com.openhis.web.chargemanage.mapper.OutpatientRegistrationAppMapper;
import com.openhis.web.datadictionary.dto.ActivityChildJsonDto;
import com.openhis.web.doctorstation.appservice.IDoctorStationAdviceAppService;
import com.openhis.web.doctorstation.dto.*;
import com.openhis.web.doctorstation.mapper.DoctorStationAdviceAppMapper;
import com.openhis.web.doctorstation.utils.AdviceUtils;
import com.openhis.web.doctorstation.utils.PrescriptionUtils;
import com.openhis.web.personalization.dto.ActivityDeviceDto;
import com.openhis.workflow.domain.ActivityDefinition;
import com.openhis.workflow.domain.DeviceRequest;
import com.openhis.workflow.domain.ServiceRequest;
import com.openhis.workflow.service.IActivityDefinitionService;
import com.openhis.workflow.service.IDeviceDispenseService;
import com.openhis.workflow.service.IDeviceRequestService;
import com.openhis.workflow.service.IServiceRequestService;

/**
 * 医生站-医嘱/处方 应用实现类
 */
@Service
public class DoctorStationAdviceAppServiceImpl implements IDoctorStationAdviceAppService {

    @Resource
    AssignSeqUtil assignSeqUtil;

    @Resource
    DoctorStationAdviceAppMapper doctorStationAdviceAppMapper;

    @Resource
    IMedicationRequestService iMedicationRequestService;

    @Resource
    IDeviceRequestService iDeviceRequestService;

    @Resource
    IServiceRequestService iServiceRequestService;

    @Resource
    IChargeItemService iChargeItemService;

    @Resource
    IOrganizationLocationService iOrganizationLocationService;

    @Resource
    IMedicationDispenseService iMedicationDispenseService;

    @Resource
    IDeviceDispenseService iDeviceDispenseService;

    @Resource
    PrescriptionUtils prescriptionUtils;

    @Resource
    AdviceUtils adviceUtils;

    @Resource
    IActivityDefinitionService iActivityDefinitionService;

    @Resource
    IDoctorStationAdviceAppService iDoctorStationAdviceAppService;

    @Resource
    OutpatientRegistrationAppMapper outpatientRegistrationAppMapper;

    /**
     * 查询医嘱信息
     *
     * @param adviceBaseDto 查询条件
     * @param searchKey 模糊查询关键字
     * @param locationId 药房id
     * @param adviceDefinitionIdParamList 医嘱定义id参数集合
     * @param organizationId 患者挂号对应的科室id
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @param pricingFlag 划价标记
     * @return 医嘱信息
     */
    @Override
    public IPage<AdviceBaseDto> getAdviceBaseInfo(AdviceBaseDto adviceBaseDto, String searchKey, Long locationId,
        List<Long> adviceDefinitionIdParamList, Long organizationId, Integer pageNo, Integer pageSize,
        Integer pricingFlag) {
        // 构建查询条件
        QueryWrapper<AdviceBaseDto> queryWrapper = HisQueryUtils.buildQueryWrapper(adviceBaseDto, searchKey,
            new HashSet<>(Arrays.asList("advice_name", "py_str", "wb_str")), null);
        IPage<AdviceBaseDto> adviceBaseInfo = doctorStationAdviceAppMapper.getAdviceBaseInfo(
            new Page<>(pageNo, pageSize), PublicationStatus.ACTIVE.getValue(), organizationId,
            CommonConstants.TableName.MED_MEDICATION_DEFINITION, CommonConstants.TableName.ADM_DEVICE_DEFINITION,
            CommonConstants.TableName.WOR_ACTIVITY_DEFINITION, DeviceCategory.SINGLE_USE.getCode(), pricingFlag,
            adviceDefinitionIdParamList, queryWrapper);
        List<AdviceBaseDto> adviceBaseDtoList = adviceBaseInfo.getRecords();
        // 医嘱定义ID集合
        List<Long> adviceDefinitionIdList =
            adviceBaseDtoList.stream().map(AdviceBaseDto::getAdviceDefinitionId).collect(Collectors.toList());
        // 费用定价主表ID集合
        List<Long> chargeItemDefinitionIdList =
            adviceBaseDtoList.stream().map(AdviceBaseDto::getChargeItemDefinitionId).collect(Collectors.toList());
        // 医嘱库存集合
        List<AdviceInventoryDto> adviceInventoryList =
            doctorStationAdviceAppMapper.getAdviceInventory(locationId, adviceDefinitionIdList,
                CommonConstants.SqlCondition.ABOUT_INVENTORY_TABLE_STR, PublicationStatus.ACTIVE.getValue());
        // 待发放个数信息
        List<AdviceInventoryDto> adviceDraftInventoryList =
            doctorStationAdviceAppMapper.getAdviceDraftInventory(CommonConstants.TableName.MED_MEDICATION_DEFINITION,
                CommonConstants.TableName.ADM_DEVICE_DEFINITION, DispenseStatus.DRAFT.getValue());
        // 预减库存
        List<AdviceInventoryDto> adviceInventory =
            adviceUtils.subtractInventory(adviceInventoryList, adviceDraftInventoryList);
        // 费用定价子表信息
        List<AdvicePriceDto> childCharge =
            doctorStationAdviceAppMapper.getChildCharge(ConditionCode.LOT_NUMBER.getCode(), chargeItemDefinitionIdList);
        // 费用定价主表信息
        List<AdvicePriceDto> mainCharge = doctorStationAdviceAppMapper.getMainCharge(chargeItemDefinitionIdList);
        String unitCode = ""; // 包装单位
        Long chargeItemDefinitionId; // 费用定价主表ID
        for (AdviceBaseDto baseDto : adviceBaseDtoList) {
            switch (baseDto.getAdviceTableName()) {
                case CommonConstants.TableName.MED_MEDICATION_DEFINITION: // 药品
                    // 是否皮试
                    baseDto
                        .setSkinTestFlag_enumText(EnumUtils.getInfoByValue(Whether.class, baseDto.getSkinTestFlag()));
                    // 是否为注射药物
                    baseDto.setInjectFlag_enumText(EnumUtils.getInfoByValue(Whether.class, baseDto.getInjectFlag()));
                case CommonConstants.TableName.ADM_DEVICE_DEFINITION: // 耗材
                    // 每一条医嘱的库存集合信息 , 包装单位库存前端计算
                    List<
                        AdviceInventoryDto> inventoryList =
                            adviceInventory.stream()
                                .filter(e -> baseDto.getAdviceDefinitionId().equals(e.getItemId())
                                    && baseDto.getAdviceTableName().equals(e.getItemTable()))
                                .collect(Collectors.toList());
                    // 库存信息
                    baseDto.setInventoryList(inventoryList);
                    // 如果有库存信息,设置默认产品批号
                    if (!inventoryList.isEmpty()) {
                        baseDto.setDefaultLotNumber(inventoryList.get(0).getLotNumber());
                    }
                    unitCode = baseDto.getUnitCode();
                    chargeItemDefinitionId = baseDto.getChargeItemDefinitionId();
                    List<AdvicePriceDto> priceDtoList = new ArrayList<>();
                    // 库存信息里取 命中条件 去匹配价格
                    for (AdviceInventoryDto adviceInventoryDto : inventoryList) {
                        Long finalChargeItemDefinitionId = chargeItemDefinitionId;
                        String finalUnitCode = unitCode;
                        // 匹配包装单位
                        List<AdvicePriceDto> advicePrice = childCharge.stream()
                            .filter(e -> e.getDefinitionId().equals(finalChargeItemDefinitionId)
                                && e.getConditionValue().equals(adviceInventoryDto.getLotNumber()))
                            .peek(e -> e.setUnitCode(finalUnitCode)) // 设置 unitCode
                            .collect(Collectors.toList());
                        priceDtoList.addAll(advicePrice);
                    }
                    // 价格信息
                    baseDto.setPriceList(priceDtoList);
                    break;
                case CommonConstants.TableName.WOR_ACTIVITY_DEFINITION: // 诊疗
                    List<AdvicePriceDto> priceList =
                        mainCharge.stream().filter(e -> baseDto.getChargeItemDefinitionId().equals(e.getDefinitionId()))
                            .collect(Collectors.toList());
                    // 价格信息
                    baseDto.setPriceList(priceList);
                    // 活动类型
                    baseDto.setActivityType_enumText(
                        EnumUtils.getInfoByValue(ActivityType.class, baseDto.getActivityType()));
                    break;
                default:
                    break;
            }
        }
        return adviceBaseInfo;
    }

    /**
     * 门诊保存/签发医嘱
     *
     * @param adviceSaveParam 医嘱表单信息
     * @param adviceOpType 医嘱操作类型
     * @return 结果
     */
    @Override
    public R<?> saveAdvice(AdviceSaveParam adviceSaveParam, String adviceOpType) {
        // 患者挂号对应的科室id
        Long organizationId = adviceSaveParam.getOrganizationId();
        // 医嘱分类信息
        List<AdviceSaveDto> adviceSaveList = adviceSaveParam.getAdviceSaveList();
        // 药品
        List<AdviceSaveDto> medicineList = adviceSaveList.stream()
            .filter(e -> ItemType.MEDICINE.getValue().equals(e.getAdviceType())).collect(Collectors.toList());
        // 耗材
        List<AdviceSaveDto> deviceList = adviceSaveList.stream()
            .filter(e -> ItemType.DEVICE.getValue().equals(e.getAdviceType())).collect(Collectors.toList());
        // 诊疗活动
        List<AdviceSaveDto> activityList = adviceSaveList.stream()
            .filter(e -> ItemType.ACTIVITY.getValue().equals(e.getAdviceType())).collect(Collectors.toList());

        /**
         * 保存时,校验库存
         */
        if (AdviceOpType.SAVE_ADVICE.getCode().equals(adviceOpType)) {
            List<AdviceSaveDto> needCheckList =
                adviceSaveList.stream().filter(e -> !DbOpType.DELETE.getCode().equals(e.getDbOpType())
                    && !ItemType.ACTIVITY.getValue().equals(e.getAdviceType())).collect(Collectors.toList());
            // 校验库存
            String tipRes = adviceUtils.checkInventory(needCheckList);
            if (tipRes != null) {
                return R.fail(null, tipRes);
            }
        }
        // 当前时间
        Date curDate = new Date();

        /**
         * 处理药品请求
         */
        this.handMedication(medicineList, curDate, adviceOpType, organizationId);

        /**
         * 处理诊疗项目请求
         */
        this.handService(activityList, curDate, adviceOpType, organizationId);

        /**
         * 处理耗材请求
         */
        this.handDevice(deviceList, curDate, adviceOpType);

        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"门诊医嘱"}));
    }

    /**
     * 处理药品
     */
    private void handMedication(List<AdviceSaveDto> medicineList, Date curDate, String adviceOpType,
        Long organizationId) {
        // 当前登录账号的科室id
        Long orgId = SecurityUtils.getLoginUser().getOrgId();
        // 保存操作
        boolean is_save = AdviceOpType.SAVE_ADVICE.getCode().equals(adviceOpType);
        // 签发操作
        boolean is_sign = AdviceOpType.SIGN_ADVICE.getCode().equals(adviceOpType);
        // 保存药品请求
        MedicationRequest medicationRequest;
        // 声明费用项
        ChargeItem chargeItem;
        // 新增 + 修改
        List<AdviceSaveDto> insertOrUpdateList =
            medicineList.stream().filter(e -> (DbOpType.INSERT.getCode().equals(e.getDbOpType())
                || DbOpType.UPDATE.getCode().equals(e.getDbOpType()))).collect(Collectors.toList());
        // 删除
        List<AdviceSaveDto> deleteList = medicineList.stream()
            .filter(e -> DbOpType.DELETE.getCode().equals(e.getDbOpType())).collect(Collectors.toList());
        for (AdviceSaveDto adviceSaveDto : deleteList) {
            iMedicationRequestService.removeById(adviceSaveDto.getRequestId());
            // 删除已经产生的药品发放信息
            iMedicationDispenseService.deleteMedicationDispense(adviceSaveDto.getRequestId());
            // 删除费用项
            iChargeItemService.deleteByServiceTableAndId(CommonConstants.TableName.MED_MEDICATION_REQUEST,
                adviceSaveDto.getRequestId());
            // 删除基于这个药品生成的需要执行的诊疗请求
            iServiceRequestService.remove(new LambdaQueryWrapper<ServiceRequest>()
                .eq(ServiceRequest::getId, adviceSaveDto.getRequestId()).isNotNull(ServiceRequest::getBasedOnTable)
                .eq(ServiceRequest::getStatusEnum, RequestStatus.IN_PROGRESS.getValue()));
        }
        // 当前时间毫秒值
        Long currentTimeMillis = System.currentTimeMillis();
        // 签发时
        if (is_sign) {
            // 对组号进行二次处理
            Long i = 1L;
            for (AdviceSaveDto adviceSaveDto : insertOrUpdateList) {
                // 输液标记
                if (Whether.YES.getValue().equals(adviceSaveDto.getInjectFlag())) {
                    if (adviceSaveDto.getGroupId() == null) {
                        adviceSaveDto.setGroupId(currentTimeMillis + i);
                    } else {
                        adviceSaveDto.setGroupId(currentTimeMillis + adviceSaveDto.getGroupId());
                    }
                }
                i = i++;
            }
            // 生成处方号
            prescriptionUtils.generatePrescriptionNumbers(insertOrUpdateList);
        }

        for (AdviceSaveDto adviceSaveDto : insertOrUpdateList) {
            medicationRequest = new MedicationRequest();
            medicationRequest.setId(adviceSaveDto.getRequestId()); // 主键id
            medicationRequest.setStatusEnum(is_save ? RequestStatus.DRAFT.getValue() : RequestStatus.ACTIVE.getValue()); // 请求状态
            medicationRequest.setPrescriptionNo(adviceSaveDto.getPrescriptionNo()); // 处方号
            medicationRequest.setGroupId(adviceSaveDto.getGroupId()); // 组号
            // 只有保存时才处理的字段属性
            if (is_save) {
                medicationRequest.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.MEDICATION_RES_NO.getPrefix(), 10));
                medicationRequest.setQuantity(adviceSaveDto.getQuantity()); // 请求数量
                medicationRequest.setExecuteNum(adviceSaveDto.getExecuteNum()); // 执行次数
                medicationRequest.setUnitCode(adviceSaveDto.getUnitCode()); // 请求单位编码
                medicationRequest.setLotNumber(adviceSaveDto.getLotNumber()); // 产品批号
                medicationRequest.setCategoryEnum(adviceSaveDto.getCategoryEnum()); // 请求类型,默认-门诊
                medicationRequest.setMedicationId(adviceSaveDto.getAdviceDefinitionId());// 医嘱定义id
                medicationRequest.setPatientId(adviceSaveDto.getPatientId()); // 患者
                medicationRequest.setPractitionerId(adviceSaveDto.getPractitionerId()); // 开方医生
                medicationRequest.setOrgId(adviceSaveDto.getFounderOrgId()); // 开方人科室
                medicationRequest.setReqAuthoredTime(curDate); // 请求开始时间
                // 发放药房
                medicationRequest.setPerformLocation(adviceSaveDto.getLocationId());
                medicationRequest.setEncounterId(adviceSaveDto.getEncounterId()); // 就诊id
                medicationRequest.setConditionId(adviceSaveDto.getConditionId()); // 诊断id
                medicationRequest.setEncounterDiagnosisId(adviceSaveDto.getEncounterDiagnosisId()); // 就诊诊断id
                medicationRequest.setTherapyEnum(adviceSaveDto.getTherapyEnum()); // 治疗类型,默认-临时
                medicationRequest.setMethodCode(adviceSaveDto.getMethodCode()); // 用法
                medicationRequest.setRateCode(adviceSaveDto.getRateCode()); // 用药频次
                medicationRequest.setDose(adviceSaveDto.getDose()); // 单次剂量
                medicationRequest.setDoseUnitCode(adviceSaveDto.getDoseUnitCode()); // 剂量单位
                medicationRequest.setDispensePerDuration(adviceSaveDto.getDispensePerDuration()); // 每次发药供应天数
                medicationRequest.setSkinTestFlag(adviceSaveDto.getSkinTestFlag()); // 皮试标记
                medicationRequest.setPackageId(adviceSaveDto.getPackageId()); // 组套id
                medicationRequest.setContentJson(adviceSaveDto.getContentJson()); // 请求内容json
                medicationRequest.setYbClassEnum(adviceSaveDto.getYbClassEnum());// 类别医保编码
                medicationRequest.setSkinTestFlag(adviceSaveDto.getSkinTestFlag()); // 皮试标志
                medicationRequest.setInfusionFlag(adviceSaveDto.getInjectFlag()); // 输液标志

            }
            iMedicationRequestService.saveOrUpdate(medicationRequest);
            // 第一次保存时,如果药品请求为皮试或输液,自动开立对应绑定的诊疗请求
            if (adviceSaveDto.getRequestId() == null) {
                if (is_save && (Whether.YES.getValue().equals(adviceSaveDto.getSkinTestFlag())
                    || Whether.YES.getValue().equals(adviceSaveDto.getInjectFlag()))) {
                    // 查询已经存在的当前数据
                    medicationRequest = iMedicationRequestService.getById(medicationRequest.getId());
                    this.generateActRequestByMedicationFlag(medicationRequest, organizationId, adviceSaveDto);
                }
            }
            // 保存时 处理药品发放 和 保存药品费用项
            if (is_save) {
                // 处理药品发放
                iMedicationDispenseService.handleMedicationDispense(medicationRequest, adviceSaveDto.getDbOpType());
                // 保存药品费用项
                chargeItem = new ChargeItem();
                chargeItem.setId(adviceSaveDto.getChargeItemId()); // 费用项id
                chargeItem.setStatusEnum(ChargeItemStatus.PLANNED.getValue()); // 默认-待收费
                chargeItem.setBusNo(AssignSeqEnum.CHARGE_ITEM_NO.getPrefix().concat(medicationRequest.getBusNo()));
                chargeItem.setPrescriptionNo(adviceSaveDto.getPrescriptionNo()); // 处方号
                chargeItem.setPatientId(adviceSaveDto.getPatientId()); // 患者
                chargeItem.setContextEnum(adviceSaveDto.getAdviceType()); // 类型
                chargeItem.setEncounterId(adviceSaveDto.getEncounterId()); // 就诊id
                chargeItem.setDefinitionId(adviceSaveDto.getDefinitionId()); // 费用定价ID
                chargeItem.setDefDetailId(adviceSaveDto.getDefinitionDetailId()); // 定价子表主键
                chargeItem.setEntererId(adviceSaveDto.getPractitionerId());// 开立人ID
                chargeItem.setRequestingOrgId(orgId); // 开立科室
                chargeItem.setEnteredDate(curDate); // 开立时间
                chargeItem.setServiceTable(CommonConstants.TableName.MED_MEDICATION_REQUEST);// 医疗服务类型
                chargeItem.setServiceId(medicationRequest.getId()); // 医疗服务ID
                chargeItem.setProductTable(adviceSaveDto.getAdviceTableName());// 产品所在表
                chargeItem.setProductId(adviceSaveDto.getAdviceDefinitionId());// 收费项id
                chargeItem.setAccountId(adviceSaveDto.getAccountId());// 关联账户ID
                chargeItem.setConditionId(adviceSaveDto.getConditionId()); // 诊断id
                chargeItem.setEncounterDiagnosisId(adviceSaveDto.getEncounterDiagnosisId()); // 就诊诊断id

                chargeItem.setQuantityValue(adviceSaveDto.getQuantity()); // 数量
                chargeItem.setQuantityUnit(adviceSaveDto.getUnitCode()); // 单位
                chargeItem.setUnitPrice(adviceSaveDto.getUnitPrice()); // 单价
                chargeItem.setTotalPrice(adviceSaveDto.getTotalPrice()); // 总价

                iChargeItemService.saveOrUpdate(chargeItem);
            }
        }
        // 签发时,如果是输液药品,生成仅用于执行的 "静脉输液" request(同一个组号生成一个)
        if (is_sign) {
            List<AdviceSaveDto> filteredList = insertOrUpdateList.stream()
                .filter(dto -> Whether.YES.getValue().equals(dto.getInjectFlag()) && dto.getGroupId() != null) // 过滤条件：输液且groupId不为null
                .collect(Collectors.groupingBy(AdviceSaveDto::getGroupId)) // 按groupId分组
                .values().stream().map(group -> group.get(0)).collect(Collectors.toList()); // 取每组中的第一个元素
            AdviceBaseDto adviceBaseDto;
            // 静脉输液的诊疗定义id
            Long intravenousInfusionDefinitionId = iActivityDefinitionService
                .getAppointActivityDefinitionId(CommonConstants.BusinessName.INTRAVENOUS_INFUSION);
            // 对应的诊疗医嘱信息
            adviceBaseDto = new AdviceBaseDto();
            adviceBaseDto.setAdviceDefinitionId(intravenousInfusionDefinitionId); // 医嘱定义id
            AdviceBaseDto activityAdviceBaseDto = iDoctorStationAdviceAppService
                .getAdviceBaseInfo(adviceBaseDto, null, null, null, organizationId, 1, 1, Whether.NO.getValue())
                .getRecords().get(0);

            ServiceRequest serviceRequest;
            for (AdviceSaveDto adviceSaveDto : filteredList) {
                Long groupId = adviceSaveDto.getGroupId();// 组号
                Integer quantity = adviceSaveDto.getExecuteNum();// 执行次数
                // 生成诊疗请求
                serviceRequest = new ServiceRequest();
                serviceRequest.setStatusEnum(RequestStatus.IN_PROGRESS.getValue());// 请求状态,待执行
                serviceRequest.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.SERVICE_RES_NO.getPrefix(), 10));
                serviceRequest.setQuantity(quantity); // 请求数量
                serviceRequest.setUnitCode(activityAdviceBaseDto.getUnitCode()); // 请求单位编码
                serviceRequest.setCategoryEnum(EncounterClass.AMB.getValue()); // 请求类型,默认-门诊
                serviceRequest.setActivityId(activityAdviceBaseDto.getAdviceDefinitionId());// 诊疗定义id
                serviceRequest.setPatientId(adviceSaveDto.getPatientId()); // 患者
                serviceRequest.setRequesterId(SecurityUtils.getLoginUser().getPractitionerId()); // 开方医生
                serviceRequest.setEncounterId(adviceSaveDto.getEncounterId()); // 就诊id
                serviceRequest.setAuthoredTime(curDate); // 请求签发时间
                serviceRequest.setOrgId(organizationId); // 执行科室
                serviceRequest.setBasedOnTable(CommonConstants.TableName.MED_MEDICATION_REQUEST);
                serviceRequest.setBasedOnId(adviceSaveDto.getRequestId());
                serviceRequest.setConditionId(adviceSaveDto.getConditionId()); // 诊断id
                serviceRequest.setEncounterDiagnosisId(adviceSaveDto.getEncounterDiagnosisId()); // 就诊诊断id
                serviceRequest.setGroupId(groupId);// 组号
                iServiceRequestService.save(serviceRequest);
            }
        }
    }

    /**
     * 药品请求为皮试或输液,开立对应的请求及账单
     */
    private void generateActRequestByMedicationFlag(MedicationRequest medicationRequest, Long organizationId,
        AdviceSaveDto adviceSaveDto) {
        // 当前登录账号的科室id
        Long orgId = SecurityUtils.getLoginUser().getOrgId();
        // 皮试
        boolean isSkinTest = Whether.YES.getValue().equals(medicationRequest.getSkinTestFlag());
        // 输液
        boolean isInfusion = Whether.YES.getValue().equals(medicationRequest.getInfusionFlag());
        // 当前时间
        Date curDate = new Date();
        AdviceBaseDto adviceBaseDto;
        ServiceRequest serviceRequest;
        ServiceRequest serviceRequestUseExe;
        AdviceBaseDto activityAdviceBaseDto;
        Integer quantity;
        ChargeItem chargeItem;
        DeviceRequest deviceRequest;
        // 生成皮试检查对应的请求和账单;生成皮试检查这个诊疗自身的请求,以及皮试检查诊疗绑定的耗材(包括发放)
        if (isSkinTest) {
            quantity = 1; // 请求数量
            // 皮试检查的诊疗定义id
            Long skinTestInspectionDefinitionId = iActivityDefinitionService
                .getAppointActivityDefinitionId(CommonConstants.BusinessName.SKIN_TEST_INSPECTION);
            if (skinTestInspectionDefinitionId != null) {
                adviceBaseDto = new AdviceBaseDto();
                adviceBaseDto.setAdviceDefinitionId(skinTestInspectionDefinitionId); // 医嘱定义id
                // 对应的诊疗医嘱信息
                activityAdviceBaseDto = iDoctorStationAdviceAppService
                    .getAdviceBaseInfo(adviceBaseDto, null, null, null, organizationId, 1, 1, Whether.NO.getValue())
                    .getRecords().get(0);
                if (activityAdviceBaseDto != null) {
                    // 费用定价
                    AdvicePriceDto advicePriceDto = activityAdviceBaseDto.getPriceList().get(0);
                    if (advicePriceDto != null) {
                        // 生成诊疗请求,用于执行
                        serviceRequestUseExe = new ServiceRequest();
                        serviceRequestUseExe.setStatusEnum(RequestStatus.IN_PROGRESS.getValue());// 请求状态,待执行
                        serviceRequestUseExe
                            .setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.SERVICE_RES_NO.getPrefix(), 10));
                        serviceRequestUseExe.setQuantity(quantity); // 请求数量
                        serviceRequestUseExe.setUnitCode(activityAdviceBaseDto.getUnitCode()); // 请求单位编码
                        serviceRequestUseExe.setCategoryEnum(EncounterClass.AMB.getValue()); // 请求类型,默认-门诊
                        serviceRequestUseExe.setActivityId(activityAdviceBaseDto.getAdviceDefinitionId());// 诊疗定义id
                        serviceRequestUseExe.setPatientId(medicationRequest.getPatientId()); // 患者
                        serviceRequestUseExe.setRequesterId(SecurityUtils.getLoginUser().getPractitionerId()); // 开方医生
                        serviceRequestUseExe.setEncounterId(medicationRequest.getEncounterId()); // 就诊id
                        serviceRequestUseExe.setAuthoredTime(curDate); // 请求签发时间
                        serviceRequestUseExe.setOrgId(organizationId); // 执行科室
                        serviceRequestUseExe.setBasedOnTable(CommonConstants.TableName.MED_MEDICATION_REQUEST);
                        serviceRequestUseExe.setBasedOnId(medicationRequest.getId());
                        serviceRequestUseExe.setConditionId(adviceSaveDto.getConditionId()); // 诊断id
                        serviceRequestUseExe.setEncounterDiagnosisId(adviceSaveDto.getEncounterDiagnosisId()); // 就诊诊断id
                        iServiceRequestService.save(serviceRequestUseExe);

                        // 生成诊疗请求,用于收费
                        serviceRequest = new ServiceRequest();
                        serviceRequest.setStatusEnum(RequestStatus.DRAFT.getValue());// 请求状态,默认已发送
                        serviceRequest.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.SERVICE_RES_NO.getPrefix(), 10));
                        serviceRequest.setQuantity(quantity); // 请求数量
                        serviceRequest.setUnitCode(activityAdviceBaseDto.getUnitCode()); // 请求单位编码
                        serviceRequest.setCategoryEnum(EncounterClass.AMB.getValue()); // 请求类型,默认-门诊
                        serviceRequest.setActivityId(activityAdviceBaseDto.getAdviceDefinitionId());// 诊疗定义id
                        serviceRequest.setPatientId(medicationRequest.getPatientId()); // 患者
                        serviceRequest.setRequesterId(SecurityUtils.getLoginUser().getPractitionerId()); // 开方医生
                        serviceRequest.setEncounterId(medicationRequest.getEncounterId()); // 就诊id
                        serviceRequest.setAuthoredTime(curDate); // 请求签发时间
                        serviceRequest.setOrgId(organizationId); // 执行科室
                        serviceRequest.setConditionId(adviceSaveDto.getConditionId()); // 诊断id
                        serviceRequest.setEncounterDiagnosisId(adviceSaveDto.getEncounterDiagnosisId()); // 就诊诊断id
                        iServiceRequestService.save(serviceRequest);
                        // 生成账单
                        chargeItem = new ChargeItem();
                        chargeItem.setGenerateSourceEnum(ChargeItemGenerateSource.MEDICAL_ORDER_BINDING.getValue()); // 账单生成来源
                        chargeItem.setStatusEnum(ChargeItemStatus.PLANNED.getValue()); // 待收费
                        chargeItem.setBusNo(AssignSeqEnum.CHARGE_ITEM_NO.getPrefix().concat(serviceRequest.getBusNo()));
                        chargeItem.setPatientId(medicationRequest.getPatientId()); // 患者
                        chargeItem.setContextEnum(ChargeItemContext.ACTIVITY.getValue()); // 类型
                        chargeItem.setEncounterId(medicationRequest.getEncounterId()); // 就诊id
                        chargeItem.setDefinitionId(advicePriceDto.getDefinitionId()); // 费用定价ID
                        chargeItem.setEntererId(SecurityUtils.getLoginUser().getPractitionerId());// 开立人ID
                        chargeItem.setRequestingOrgId(orgId); // 开立科室
                        chargeItem.setEnteredDate(curDate); // 开立时间
                        chargeItem.setServiceTable(CommonConstants.TableName.WOR_SERVICE_REQUEST);// 医疗服务类型
                        chargeItem.setServiceId(serviceRequest.getId()); // 医疗服务ID
                        chargeItem.setProductTable(activityAdviceBaseDto.getAdviceTableName());// 产品所在表
                        chargeItem.setProductId(activityAdviceBaseDto.getAdviceDefinitionId());// 收费项id
                        chargeItem.setAccountId(adviceSaveDto.getAccountId());// 关联账户ID
                        chargeItem.setConditionId(adviceSaveDto.getConditionId()); // 诊断id
                        chargeItem.setEncounterDiagnosisId(adviceSaveDto.getEncounterDiagnosisId()); // 就诊诊断id

                        chargeItem.setQuantityValue(quantity); // 数量
                        chargeItem.setQuantityUnit(activityAdviceBaseDto.getUnitCode()); // 单位
                        chargeItem.setUnitPrice(advicePriceDto.getPrice()); // 单价
                        // 计算总价,保留4位小数
                        BigDecimal qty = new BigDecimal(quantity);
                        chargeItem
                            .setTotalPrice(qty.multiply(advicePriceDto.getPrice()).setScale(4, RoundingMode.HALF_UP)); // 总价
                        iChargeItemService.save(chargeItem);
                    }
                }
                // 查询皮试检查绑定的耗材id及请求数量
                List<ActivityDeviceDto> tmpActivityList = outpatientRegistrationAppMapper.getTmpActivityList(
                    String.valueOf(skinTestInspectionDefinitionId), CommonConstants.TableName.ADM_DEVICE_DEFINITION);
                for (ActivityDeviceDto activityDeviceDto : tmpActivityList) {
                    quantity = activityDeviceDto.getQuantity(); // 请求数量
                    adviceBaseDto = new AdviceBaseDto();
                    adviceBaseDto.setAdviceDefinitionId(activityDeviceDto.getDevActId());
                    // 对应的耗材医嘱信息
                    activityAdviceBaseDto = iDoctorStationAdviceAppService
                        .getAdviceBaseInfo(adviceBaseDto, null, null, null, organizationId, 1, 1, Whether.NO.getValue())
                        .getRecords().get(0);
                    // 库存信息
                    AdviceInventoryDto adviceInventoryDto = activityAdviceBaseDto.getInventoryList().get(0);
                    // 价格信息
                    AdvicePriceDto advicePriceDto = activityAdviceBaseDto.getPriceList().get(0);
                    if (adviceInventoryDto != null && advicePriceDto != null) {
                        // 耗材请求
                        deviceRequest = new DeviceRequest();
                        deviceRequest.setStatusEnum(RequestStatus.DRAFT.getValue()); // 请求状态,已发送
                        deviceRequest.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.DEVICE_RES_NO.getPrefix(), 10));
                        deviceRequest.setQuantity(quantity); // 请求数量
                        deviceRequest.setUnitCode(activityAdviceBaseDto.getUnitCode()); // 请求单位编码
                        deviceRequest.setLotNumber(adviceInventoryDto.getLotNumber());// 产品批号
                        deviceRequest.setCategoryEnum(EncounterClass.AMB.getValue()); // 请求类型,默认-门诊
                        deviceRequest.setDeviceDefId(activityAdviceBaseDto.getAdviceDefinitionId());// 耗材定义id
                        deviceRequest.setPatientId(medicationRequest.getPatientId()); // 患者
                        deviceRequest.setRequesterId(SecurityUtils.getLoginUser().getPractitionerId()); // 开方医生
                        deviceRequest.setOrgId(organizationId);// 开方人科室
                        deviceRequest.setReqAuthoredTime(curDate); // 请求开始时间
                        // 发放耗材房
                        deviceRequest.setPerformLocation(activityAdviceBaseDto.getLocationId());
                        deviceRequest.setEncounterId(medicationRequest.getEncounterId()); // 就诊id
                        deviceRequest.setConditionId(adviceSaveDto.getConditionId()); // 诊断id
                        deviceRequest.setEncounterDiagnosisId(adviceSaveDto.getEncounterDiagnosisId()); // 就诊诊断id
                        iDeviceRequestService.save(deviceRequest);
                        // 耗材发放
                        iDeviceDispenseService.handleDeviceDispense(deviceRequest, DbOpType.INSERT.getCode());
                        // 耗材账单
                        chargeItem = new ChargeItem();
                        chargeItem.setGenerateSourceEnum(ChargeItemGenerateSource.MEDICAL_ORDER_BINDING.getValue()); // 账单生成来源
                        chargeItem.setStatusEnum(ChargeItemStatus.PLANNED.getValue()); // 默认-待收费
                        chargeItem.setBusNo(AssignSeqEnum.CHARGE_ITEM_NO.getPrefix().concat(deviceRequest.getBusNo()));
                        chargeItem.setPatientId(medicationRequest.getPatientId()); // 患者
                        chargeItem.setContextEnum(ChargeItemContext.DEVICE.getValue()); // 类型
                        chargeItem.setEncounterId(medicationRequest.getEncounterId()); // 就诊id
                        chargeItem.setDefinitionId(advicePriceDto.getDefinitionId()); // 费用定价ID
                        chargeItem.setDefDetailId(advicePriceDto.getDefinitionDetailId()); // 定价子表主键
                        chargeItem.setEntererId(SecurityUtils.getLoginUser().getPractitionerId());// 开立人ID
                        chargeItem.setEnteredDate(curDate); // 开立时间
                        chargeItem.setServiceTable(CommonConstants.TableName.WOR_DEVICE_REQUEST);// 医疗服务类型
                        chargeItem.setServiceId(deviceRequest.getId()); // 医疗服务ID
                        chargeItem.setProductTable(activityAdviceBaseDto.getAdviceTableName());// 产品所在表
                        chargeItem.setProductId(activityAdviceBaseDto.getAdviceDefinitionId());// 收费项id
                        chargeItem.setAccountId(adviceSaveDto.getAccountId());// 关联账户ID
                        chargeItem.setConditionId(adviceSaveDto.getConditionId()); // 诊断id
                        chargeItem.setEncounterDiagnosisId(adviceSaveDto.getEncounterDiagnosisId()); // 就诊诊断id

                        chargeItem.setQuantityValue(quantity); // 数量
                        chargeItem.setQuantityUnit(activityAdviceBaseDto.getUnitCode()); // 单位
                        chargeItem.setUnitPrice(advicePriceDto.getPrice()); // 单价
                        // 计算总价,保留4位小数
                        BigDecimal qty = new BigDecimal(quantity);
                        chargeItem
                            .setTotalPrice(qty.multiply(advicePriceDto.getPrice()).setScale(4, RoundingMode.HALF_UP)); // 总价
                        iChargeItemService.save(chargeItem);
                    }
                }
            }
        }
        // 生成用法(输液)绑定的诊疗对应的请求和账单
        if (isInfusion) {
            // 分组id
            Long groupId = medicationRequest.getGroupId();
            // 执行次数
            Integer executeNum = medicationRequest.getExecuteNum();
            // 用法
            String methodCode = medicationRequest.getMethodCode();
            // 查询用法绑定的诊疗信息
            List<ActivityDeviceDto> tmpActivityList = outpatientRegistrationAppMapper.getTmpActivityList(methodCode,
                CommonConstants.TableName.WOR_ACTIVITY_DEFINITION);
            for (ActivityDeviceDto activityDeviceDto : tmpActivityList) {
                quantity = activityDeviceDto.getQuantity() * executeNum; // 请求数量
                adviceBaseDto = new AdviceBaseDto();
                adviceBaseDto.setAdviceDefinitionId(activityDeviceDto.getDevActId());
                // 对应的诊疗医嘱信息
                activityAdviceBaseDto = iDoctorStationAdviceAppService
                    .getAdviceBaseInfo(adviceBaseDto, null, null, null, organizationId, 1, 1, Whether.NO.getValue())
                    .getRecords().get(0);
                if (activityAdviceBaseDto != null) {
                    // 费用定价
                    AdvicePriceDto advicePriceDto = activityAdviceBaseDto.getPriceList().get(0);
                    if (advicePriceDto != null) {
                        // 同一个组号,只生成一次请求和账单
                        ServiceRequest one = iServiceRequestService
                            .getOne(new LambdaQueryWrapper<ServiceRequest>().eq(ServiceRequest::getGroupId, groupId));
                        if (one == null) {
                            // 生成诊疗请求
                            serviceRequest = new ServiceRequest();
                            serviceRequest.setStatusEnum(RequestStatus.DRAFT.getValue());// 请求状态,已发送
                            serviceRequest.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.SERVICE_RES_NO.getPrefix(), 10));
                            serviceRequest.setQuantity(quantity); // 请求数量
                            serviceRequest.setUnitCode(activityAdviceBaseDto.getUnitCode()); // 请求单位编码
                            serviceRequest.setCategoryEnum(EncounterClass.AMB.getValue()); // 请求类型,默认-门诊
                            serviceRequest.setActivityId(activityAdviceBaseDto.getAdviceDefinitionId());// 诊疗定义id
                            serviceRequest.setPatientId(medicationRequest.getPatientId()); // 患者
                            serviceRequest.setRequesterId(SecurityUtils.getLoginUser().getPractitionerId()); // 开方医生
                            serviceRequest.setEncounterId(medicationRequest.getEncounterId()); // 就诊id
                            serviceRequest.setAuthoredTime(curDate); // 请求签发时间
                            serviceRequest.setOrgId(organizationId); // 执行科室
                            serviceRequest.setGroupId(groupId); // 分组id
                            serviceRequest.setConditionId(adviceSaveDto.getConditionId()); // 诊断id
                            serviceRequest.setEncounterDiagnosisId(adviceSaveDto.getEncounterDiagnosisId()); // 就诊诊断id
                            iServiceRequestService.save(serviceRequest);

                            chargeItem = new ChargeItem();
                            chargeItem.setGenerateSourceEnum(ChargeItemGenerateSource.MEDICAL_ORDER_BINDING.getValue()); // 账单生成来源
                            chargeItem.setStatusEnum(ChargeItemStatus.PLANNED.getValue()); // 待收费
                            chargeItem
                                .setBusNo(AssignSeqEnum.CHARGE_ITEM_NO.getPrefix().concat(serviceRequest.getBusNo()));
                            chargeItem.setPatientId(medicationRequest.getPatientId()); // 患者
                            chargeItem.setContextEnum(ChargeItemContext.ACTIVITY.getValue()); // 类型
                            chargeItem.setEncounterId(medicationRequest.getEncounterId()); // 就诊id
                            chargeItem.setDefinitionId(advicePriceDto.getDefinitionId()); // 费用定价ID
                            chargeItem.setEntererId(SecurityUtils.getLoginUser().getPractitionerId());// 开立人ID
                            chargeItem.setRequestingOrgId(orgId); // 开立科室
                            chargeItem.setEnteredDate(curDate); // 开立时间
                            chargeItem.setServiceTable(CommonConstants.TableName.WOR_SERVICE_REQUEST);// 医疗服务类型
                            chargeItem.setServiceId(serviceRequest.getId()); // 医疗服务ID
                            chargeItem.setProductTable(activityAdviceBaseDto.getAdviceTableName());// 产品所在表
                            chargeItem.setProductId(activityAdviceBaseDto.getAdviceDefinitionId());// 收费项id
                            chargeItem.setAccountId(adviceSaveDto.getAccountId());// 关联账户ID
                            chargeItem.setConditionId(adviceSaveDto.getConditionId()); // 诊断id
                            chargeItem.setEncounterDiagnosisId(adviceSaveDto.getEncounterDiagnosisId()); // 就诊诊断id

                            chargeItem.setQuantityValue(quantity); // 数量
                            chargeItem.setQuantityUnit(activityAdviceBaseDto.getUnitCode()); // 单位
                            chargeItem.setUnitPrice(advicePriceDto.getPrice()); // 单价
                            // 计算总价,保留4位小数
                            BigDecimal qty = new BigDecimal(quantity);
                            chargeItem.setTotalPrice(
                                qty.multiply(advicePriceDto.getPrice()).setScale(4, RoundingMode.HALF_UP)); // 总价
                            iChargeItemService.saveOrUpdate(chargeItem);
                        }
                    }
                }
            }
        }
    }

    /**
     * 处理耗材
     */
    private void handDevice(List<AdviceSaveDto> deviceList, Date curDate, String adviceOpType) {
        // 当前登录账号的科室id
        Long orgId = SecurityUtils.getLoginUser().getOrgId();
        // 保存操作
        boolean is_save = AdviceOpType.SAVE_ADVICE.getCode().equals(adviceOpType);
        // 签发操作
        // boolean is_sign = AdviceOpType.SIGN_ADVICE.getCode().equals(adviceOpType);
        DeviceRequest deviceRequest;
        // 声明费用项
        ChargeItem chargeItem;
        // 新增 + 修改
        List<AdviceSaveDto> insertOrUpdateList =
            deviceList.stream().filter(e -> (DbOpType.INSERT.getCode().equals(e.getDbOpType())
                || DbOpType.UPDATE.getCode().equals(e.getDbOpType()))).collect(Collectors.toList());
        // 删除
        List<AdviceSaveDto> deleteList = deviceList.stream()
            .filter(e -> DbOpType.DELETE.getCode().equals(e.getDbOpType())).collect(Collectors.toList());
        for (AdviceSaveDto adviceSaveDto : deleteList) {
            iDeviceRequestService.removeById(adviceSaveDto.getRequestId());
            // 删除已经产生的耗材发放信息
            iDeviceDispenseService.deleteDeviceDispense(adviceSaveDto.getRequestId());
            // 删除费用项
            iChargeItemService.deleteByServiceTableAndId(CommonConstants.TableName.WOR_DEVICE_REQUEST,
                adviceSaveDto.getRequestId());
        }

        for (AdviceSaveDto adviceSaveDto : insertOrUpdateList) {
            deviceRequest = new DeviceRequest();
            deviceRequest.setId(adviceSaveDto.getRequestId()); // 主键id
            deviceRequest.setStatusEnum(is_save ? RequestStatus.DRAFT.getValue() : RequestStatus.ACTIVE.getValue()); // 请求状态
            // 只有保存时才处理的字段属性
            if (is_save) {
                deviceRequest.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.DEVICE_RES_NO.getPrefix(), 10));
                deviceRequest.setQuantity(adviceSaveDto.getQuantity()); // 请求数量
                deviceRequest.setUnitCode(adviceSaveDto.getUnitCode()); // 请求单位编码
                deviceRequest.setLotNumber(adviceSaveDto.getLotNumber());// 产品批号

                deviceRequest.setCategoryEnum(adviceSaveDto.getCategoryEnum()); // 请求类型,默认-门诊
                deviceRequest.setDeviceDefId(adviceSaveDto.getAdviceDefinitionId());// 耗材定义id
                deviceRequest.setPatientId(adviceSaveDto.getPatientId()); // 患者
                deviceRequest.setRequesterId(adviceSaveDto.getPractitionerId()); // 开方医生
                deviceRequest.setOrgId(adviceSaveDto.getFounderOrgId());// 开方人科室
                deviceRequest.setReqAuthoredTime(curDate); // 请求开始时间
                // 发放耗材房
                deviceRequest.setPerformLocation(adviceSaveDto.getLocationId());
                deviceRequest.setEncounterId(adviceSaveDto.getEncounterId()); // 就诊id
                deviceRequest.setPackageId(adviceSaveDto.getPackageId()); // 组套id
                // deviceRequest.setActivityId(adviceSaveDto.getActivityId());
                deviceRequest.setContentJson(adviceSaveDto.getContentJson()); // 请求内容json
                deviceRequest.setYbClassEnum(adviceSaveDto.getYbClassEnum());// 类别医保编码
                deviceRequest.setConditionId(adviceSaveDto.getConditionId()); // 诊断id
                deviceRequest.setEncounterDiagnosisId(adviceSaveDto.getEncounterDiagnosisId()); // 就诊诊断id
            }

            iDeviceRequestService.saveOrUpdate(deviceRequest);

            // 保存时 处理耗材发放 和 保存耗材费用项
            if (is_save) {
                // 处理耗材发放
                iDeviceDispenseService.handleDeviceDispense(deviceRequest, adviceSaveDto.getDbOpType());
                // 保存耗材费用项
                chargeItem = new ChargeItem();
                chargeItem.setId(adviceSaveDto.getChargeItemId()); // 费用项id
                chargeItem.setStatusEnum(ChargeItemStatus.PLANNED.getValue()); // 默认-待收费
                chargeItem.setBusNo(AssignSeqEnum.CHARGE_ITEM_NO.getPrefix().concat(deviceRequest.getBusNo()));
                chargeItem.setPatientId(adviceSaveDto.getPatientId()); // 患者
                chargeItem.setContextEnum(adviceSaveDto.getAdviceType()); // 类型
                chargeItem.setEncounterId(adviceSaveDto.getEncounterId()); // 就诊id
                chargeItem.setDefinitionId(adviceSaveDto.getDefinitionId()); // 费用定价ID
                chargeItem.setDefDetailId(adviceSaveDto.getDefinitionDetailId()); // 定价子表主键
                chargeItem.setEntererId(adviceSaveDto.getPractitionerId());// 开立人ID
                chargeItem.setRequestingOrgId(orgId); // 开立科室
                chargeItem.setEnteredDate(curDate); // 开立时间
                chargeItem.setServiceTable(CommonConstants.TableName.WOR_DEVICE_REQUEST);// 医疗服务类型
                chargeItem.setServiceId(deviceRequest.getId()); // 医疗服务ID
                chargeItem.setProductTable(adviceSaveDto.getAdviceTableName());// 产品所在表
                chargeItem.setProductId(adviceSaveDto.getAdviceDefinitionId());// 收费项id
                chargeItem.setAccountId(adviceSaveDto.getAccountId());// 关联账户ID
                chargeItem.setConditionId(adviceSaveDto.getConditionId()); // 诊断id
                chargeItem.setEncounterDiagnosisId(adviceSaveDto.getEncounterDiagnosisId()); // 就诊诊断id

                chargeItem.setQuantityValue(adviceSaveDto.getQuantity()); // 数量
                chargeItem.setQuantityUnit(adviceSaveDto.getUnitCode()); // 单位
                chargeItem.setUnitPrice(adviceSaveDto.getUnitPrice()); // 单价
                chargeItem.setTotalPrice(adviceSaveDto.getTotalPrice()); // 总价

                iChargeItemService.saveOrUpdate(chargeItem);
            }
        }
    }

    /**
     * 处理诊疗
     */
    private void handService(List<AdviceSaveDto> activityList, Date curDate, String adviceOpType, Long organizationId) {
        // 当前登录账号的科室id
        Long orgId = SecurityUtils.getLoginUser().getOrgId();
        // 保存操作
        boolean is_save = AdviceOpType.SAVE_ADVICE.getCode().equals(adviceOpType);
        // 签发操作
        boolean is_sign = AdviceOpType.SIGN_ADVICE.getCode().equals(adviceOpType);
        ServiceRequest serviceRequest;
        // 声明费用项
        ChargeItem chargeItem;
        // 新增 + 修改
        List<AdviceSaveDto> insertOrUpdateList =
            activityList.stream().filter(e -> (DbOpType.INSERT.getCode().equals(e.getDbOpType())
                || DbOpType.UPDATE.getCode().equals(e.getDbOpType()))).collect(Collectors.toList());
        // 删除
        List<AdviceSaveDto> deleteList = activityList.stream()
            .filter(e -> DbOpType.DELETE.getCode().equals(e.getDbOpType())).collect(Collectors.toList());
        for (AdviceSaveDto adviceSaveDto : deleteList) {
            iServiceRequestService.removeById(adviceSaveDto.getRequestId());
            // 删除费用项
            iChargeItemService.deleteByServiceTableAndId(CommonConstants.TableName.WOR_SERVICE_REQUEST,
                adviceSaveDto.getRequestId());
        }

        for (AdviceSaveDto adviceSaveDto : insertOrUpdateList) {
            serviceRequest = new ServiceRequest();
            serviceRequest.setId(adviceSaveDto.getRequestId()); // 主键id
            serviceRequest.setStatusEnum(is_save ? RequestStatus.DRAFT.getValue() : RequestStatus.ACTIVE.getValue());// 请求状态
            // 只有保存时才处理的字段属性
            if (is_save) {
                serviceRequest.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.SERVICE_RES_NO.getPrefix(), 10));
                serviceRequest.setQuantity(adviceSaveDto.getQuantity()); // 请求数量
                serviceRequest.setUnitCode(adviceSaveDto.getUnitCode()); // 请求单位编码

                serviceRequest.setCategoryEnum(adviceSaveDto.getCategoryEnum()); // 请求类型,默认-门诊
                serviceRequest.setActivityId(adviceSaveDto.getAdviceDefinitionId());// 诊疗定义id
                serviceRequest.setPatientId(adviceSaveDto.getPatientId()); // 患者
                serviceRequest.setRequesterId(adviceSaveDto.getPractitionerId()); // 开方医生
                serviceRequest.setEncounterId(adviceSaveDto.getEncounterId()); // 就诊id
                serviceRequest.setAuthoredTime(curDate); // 请求签发时间
                // 执行科室
                serviceRequest.setOrgId(adviceSaveDto.getPositionId());
                serviceRequest.setContentJson(adviceSaveDto.getContentJson()); // 请求内容json
                serviceRequest.setYbClassEnum(adviceSaveDto.getYbClassEnum());// 类别医保编码
                serviceRequest.setConditionId(adviceSaveDto.getConditionId()); // 诊断id
                serviceRequest.setEncounterDiagnosisId(adviceSaveDto.getEncounterDiagnosisId()); // 就诊诊断id
            }

            iServiceRequestService.saveOrUpdate(serviceRequest);

            // 保存时保存诊疗费用项
            if (is_save) {
                chargeItem = new ChargeItem();
                chargeItem.setId(adviceSaveDto.getChargeItemId()); // 费用项id
                chargeItem.setStatusEnum(ChargeItemStatus.PLANNED.getValue()); // 默认-待收费
                chargeItem.setBusNo(AssignSeqEnum.CHARGE_ITEM_NO.getPrefix().concat(serviceRequest.getBusNo()));
                chargeItem.setPatientId(adviceSaveDto.getPatientId()); // 患者
                chargeItem.setContextEnum(adviceSaveDto.getAdviceType()); // 类型
                chargeItem.setEncounterId(adviceSaveDto.getEncounterId()); // 就诊id
                chargeItem.setDefinitionId(adviceSaveDto.getDefinitionId()); // 费用定价ID
                chargeItem.setDefDetailId(adviceSaveDto.getDefinitionDetailId()); // 定价子表主键
                chargeItem.setEntererId(adviceSaveDto.getPractitionerId());// 开立人ID
                chargeItem.setEnteredDate(curDate); // 开立时间
                chargeItem.setServiceTable(CommonConstants.TableName.WOR_SERVICE_REQUEST);// 医疗服务类型
                chargeItem.setServiceId(serviceRequest.getId()); // 医疗服务ID
                chargeItem.setProductTable(adviceSaveDto.getAdviceTableName());// 产品所在表
                chargeItem.setProductId(adviceSaveDto.getAdviceDefinitionId());// 收费项id
                chargeItem.setAccountId(adviceSaveDto.getAccountId());// 关联账户ID
                chargeItem.setRequestingOrgId(orgId); // 开立科室
                chargeItem.setConditionId(adviceSaveDto.getConditionId()); // 诊断id
                chargeItem.setEncounterDiagnosisId(adviceSaveDto.getEncounterDiagnosisId()); // 就诊诊断id
                chargeItem.setQuantityValue(adviceSaveDto.getQuantity()); // 数量
                chargeItem.setQuantityUnit(adviceSaveDto.getUnitCode()); // 单位
                chargeItem.setUnitPrice(adviceSaveDto.getUnitPrice()); // 单价
                chargeItem.setTotalPrice(adviceSaveDto.getTotalPrice()); // 总价

                iChargeItemService.saveOrUpdate(chargeItem);
            }
            // 第一次保存时,处理诊疗套餐的子项信息
            if (adviceSaveDto.getRequestId() == null) {
                ActivityDefinition activityDefinition =
                    iActivityDefinitionService.getById(adviceSaveDto.getAdviceDefinitionId());
                String childrenJson = activityDefinition.getChildrenJson();
                if (is_save && childrenJson != null) {
                    serviceRequest = iServiceRequestService.getById(adviceSaveDto.getRequestId());
                    this.handleActivityChild(serviceRequest, childrenJson, organizationId, adviceSaveDto);
                }
            }
        }
    }

    /**
     * 处理诊疗子项信息
     * 
     * @param mainServiceRequest 主项的诊疗请求
     * @param childrenJson 诊疗子项JSON
     * @param organizationId 患者挂号对应的科室id
     * @param adviceSaveDto 医嘱保存信息
     */
    private void handleActivityChild(ServiceRequest mainServiceRequest, String childrenJson, Long organizationId,
        AdviceSaveDto adviceSaveDto) {
        // 当前登录账号的科室id
        Long orgId = SecurityUtils.getLoginUser().getOrgId();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 将JSON字符串转换为List<ActivityChildJsonDto>
            List<ActivityChildJsonDto> activityChildJsonDtoList =
                objectMapper.readValue(childrenJson, new TypeReference<>() {});
            // 创建子项的诊疗请求
            Long adviceDefinitionId; // 诊疗医嘱定义ID
            Integer quantity; // 子项请求数量
            List<ChargeItem> chargeItemList = new ArrayList<>(); // 子项账单集合
            ServiceRequest serviceRequest;
            ChargeItem chargeItem;
            AdviceBaseDto adviceBaseDto;
            // 当前时间
            Date curDate = new Date();
            for (ActivityChildJsonDto activityChildJsonDto : activityChildJsonDtoList) {
                adviceDefinitionId = activityChildJsonDto.getAdviceDefinitionId();
                quantity = activityChildJsonDto.getChildrenRequestNum();
                adviceBaseDto = new AdviceBaseDto();
                adviceBaseDto.setAdviceDefinitionId(adviceDefinitionId);
                // 对应的子项诊疗医嘱信息
                AdviceBaseDto activityAdviceBaseDto = iDoctorStationAdviceAppService
                    .getAdviceBaseInfo(adviceBaseDto, null, null, null, organizationId, 1, 1, Whether.NO.getValue())
                    .getRecords().get(0);
                if (activityAdviceBaseDto != null) {
                    // 费用定价
                    AdvicePriceDto advicePriceDto = activityAdviceBaseDto.getPriceList().get(0);
                    if (advicePriceDto != null) {
                        // 生成诊疗请求
                        serviceRequest = new ServiceRequest();
                        serviceRequest.setStatusEnum(RequestStatus.DRAFT.getValue());// 请求状态,默认已完成
                        serviceRequest.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.SERVICE_RES_NO.getPrefix(), 10));
                        serviceRequest.setQuantity(quantity); // 请求数量
                        serviceRequest.setUnitCode(activityAdviceBaseDto.getUnitCode()); // 请求单位编码
                        serviceRequest.setCategoryEnum(EncounterClass.AMB.getValue()); // 请求类型,默认-门诊
                        serviceRequest.setActivityId(activityAdviceBaseDto.getAdviceDefinitionId());// 诊疗定义id
                        serviceRequest.setPatientId(mainServiceRequest.getPatientId()); // 患者
                        serviceRequest.setRequesterId(SecurityUtils.getLoginUser().getPractitionerId()); // 开方医生
                        serviceRequest.setEncounterId(mainServiceRequest.getEncounterId()); // 就诊id
                        serviceRequest.setAuthoredTime(curDate); // 请求签发时间
                        serviceRequest.setOrgId(organizationId); // 执行科室

                        iServiceRequestService.save(serviceRequest);

                        // 生成账单
                        chargeItem = new ChargeItem();
                        chargeItem.setGenerateSourceEnum(ChargeItemGenerateSource.MEDICAL_ORDER_BINDING.getValue());
                        chargeItem.setStatusEnum(ChargeItemStatus.PLANNED.getValue()); // 待收费
                        chargeItem.setBusNo(AssignSeqEnum.CHARGE_ITEM_NO.getPrefix().concat(serviceRequest.getBusNo()));
                        chargeItem.setPatientId(mainServiceRequest.getPatientId()); // 患者
                        chargeItem.setContextEnum(ChargeItemContext.ACTIVITY.getValue()); // 类型
                        chargeItem.setEncounterId(mainServiceRequest.getEncounterId()); // 就诊id
                        chargeItem.setDefinitionId(advicePriceDto.getDefinitionId()); // 费用定价ID
                        chargeItem.setEntererId(SecurityUtils.getLoginUser().getPractitionerId());// 开立人ID
                        chargeItem.setRequestingOrgId(orgId); // 开立科室
                        chargeItem.setEnteredDate(curDate); // 开立时间
                        chargeItem.setServiceTable(CommonConstants.TableName.WOR_SERVICE_REQUEST);// 医疗服务类型
                        chargeItem.setServiceId(serviceRequest.getId()); // 医疗服务ID
                        chargeItem.setProductTable(activityAdviceBaseDto.getAdviceTableName());// 产品所在表
                        chargeItem.setProductId(activityAdviceBaseDto.getAdviceDefinitionId());// 收费项id
                        chargeItem.setAccountId(adviceSaveDto.getAccountId());// 关联账户ID

                        chargeItem.setQuantityValue(quantity); // 数量
                        chargeItem.setQuantityUnit(activityAdviceBaseDto.getUnitCode()); // 单位
                        chargeItem.setUnitPrice(advicePriceDto.getPrice()); // 单价
                        // 计算总价,保留4位小数
                        BigDecimal qty = new BigDecimal(quantity);
                        chargeItem
                            .setTotalPrice(qty.multiply(advicePriceDto.getPrice()).setScale(4, RoundingMode.HALF_UP)); // 总价

                        chargeItemList.add(chargeItem);
                    }
                }
            }
            // 更新主项账单的 children_json 字段
            String chargeItemChildrenJson = objectMapper.writeValueAsString(chargeItemList);
            ChargeItem mainChargeItem = new ChargeItem();
            mainChargeItem.setId(adviceSaveDto.getChargeItemId()); // 主键
            mainChargeItem.setChildrenJson(chargeItemChildrenJson); // 子项json
            iChargeItemService.updateById(mainChargeItem);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询医嘱请求数据
     *
     * @param encounterId 就诊id
     * @return 医嘱请求数据
     */
    @Override
    public R<?> getRequestBaseInfo(Long encounterId) {
        // 当前账号的参与者id
        Long practitionerId = SecurityUtils.getLoginUser().getPractitionerId();
        // 医嘱请求数据
        List<RequestBaseDto> requestBaseInfo = doctorStationAdviceAppMapper.getRequestBaseInfo(encounterId, null,
            CommonConstants.TableName.MED_MEDICATION_REQUEST, CommonConstants.TableName.WOR_DEVICE_REQUEST,
            CommonConstants.TableName.WOR_SERVICE_REQUEST, practitionerId, Whether.NO.getCode());
        for (RequestBaseDto requestBaseDto : requestBaseInfo) {
            // 请求状态
            requestBaseDto
                .setStatusEnum_enumText(EnumUtils.getInfoByValue(RequestStatus.class, requestBaseDto.getStatusEnum()));
            // 是否皮试
            requestBaseDto
                .setSkinTestFlag_enumText(EnumUtils.getInfoByValue(Whether.class, requestBaseDto.getSkinTestFlag()));
            // 是否为注射药物
            requestBaseDto
                .setInjectFlag_enumText(EnumUtils.getInfoByValue(Whether.class, requestBaseDto.getInjectFlag()));
            // 收费状态
            requestBaseDto.setChargeStatus_enumText(
                EnumUtils.getInfoByValue(ChargeItemStatus.class, requestBaseDto.getChargeStatus()));
        }
        return R.ok(requestBaseInfo);
    }

    /**
     * 门诊签退医嘱
     *
     * @param requestIdList 请求id列表
     * @return 结果
     */
    @Override
    public R<?> signOffAdvice(List<Long> requestIdList) {
        // 根据请求编号列表查询收费项目信息
        List<ChargeItem> chargeItemList = iChargeItemService.getChargeItemInfoByReqId(requestIdList);

        if (chargeItemList != null && !chargeItemList.isEmpty()) {
            for (ChargeItem chargeItem : chargeItemList) {
                if (ChargeItemStatus.BILLED.getValue().equals(chargeItem.getStatusEnum())) {
                    throw new ServiceException("已收费的项目无法签退，请刷新页面后重试");
                }
            }
            // 分别获取各个请求id列表
            List<Long> medReqIdList = new ArrayList<>();
            List<Long> devReqIdList = new ArrayList<>();
            List<Long> serReqIdList = new ArrayList<>();

            chargeItemList.forEach(item -> {
                switch (item.getServiceTable()) {
                    case CommonConstants.TableName.MED_MEDICATION_REQUEST -> medReqIdList.add(item.getServiceId());
                    case CommonConstants.TableName.WOR_DEVICE_REQUEST -> devReqIdList.add(item.getServiceId());
                    case CommonConstants.TableName.WOR_SERVICE_REQUEST -> serReqIdList.add(item.getServiceId());
                }
            });
            List<Long> chargeItemIdList = chargeItemList.stream().map(ChargeItem::getId).collect(Collectors.toList());
            // 根据id更新收费项目状态
            iChargeItemService.updatePaymentStatus(chargeItemIdList, ChargeItemStatus.PLANNED.getValue());// 撤回后需要繼續帶簽發
            if (!medReqIdList.isEmpty()) {
                // 根据请求id更新请求状态
                iMedicationRequestService.updateDraftStatusBatch(medReqIdList);
            }
            if (!devReqIdList.isEmpty()) {
                // 根据请求id更新请求状态
                iDeviceRequestService.updateDraftStatusBatch(devReqIdList);
            }
            if (!serReqIdList.isEmpty()) {
                // 根据请求id更新请求状态
                iServiceRequestService.updateDraftStatusBatch(serReqIdList);
            }
        } else {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00006, null));
        }
        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null));
    }

    /**
     * 查询历史医嘱请求数据
     *
     * @param patientId 病人id
     * @param encounterId 就诊id
     * @return 历史医嘱请求数据
     */
    @Override
    public R<?> getRequestHistoryInfo(Long patientId, Long encounterId) {
        // 当前账号的参与者id
        Long practitionerId = SecurityUtils.getLoginUser().getPractitionerId();
        // 医嘱请求数据
        List<RequestBaseDto> requestBaseInfo = doctorStationAdviceAppMapper.getRequestBaseInfo(encounterId, patientId,
            CommonConstants.TableName.MED_MEDICATION_REQUEST, CommonConstants.TableName.WOR_DEVICE_REQUEST,
            CommonConstants.TableName.WOR_SERVICE_REQUEST, practitionerId, Whether.YES.getCode());
        for (RequestBaseDto requestBaseDto : requestBaseInfo) {
            // 请求状态
            requestBaseDto
                .setStatusEnum_enumText(EnumUtils.getInfoByValue(RequestStatus.class, requestBaseDto.getStatusEnum()));
            // 是否皮试
            requestBaseDto
                .setSkinTestFlag_enumText(EnumUtils.getInfoByValue(Whether.class, requestBaseDto.getSkinTestFlag()));
            // 是否为注射药物
            requestBaseDto
                .setInjectFlag_enumText(EnumUtils.getInfoByValue(Whether.class, requestBaseDto.getInjectFlag()));
            // 收费状态
            requestBaseDto.setChargeStatus_enumText(
                EnumUtils.getInfoByValue(ChargeItemStatus.class, requestBaseDto.getChargeStatus()));
        }
        return R.ok(requestBaseInfo);
    }

    /**
     * 更新组号
     *
     * @param updateGroupIdParam 更新组号参数
     * @return 结果
     */
    @Override
    public void updateGroupId(UpdateGroupIdParam updateGroupIdParam) {
        List<UpdateGroupDto> groupList = updateGroupIdParam.getGroupList();
        List<Long> idsToSetNull = groupList.stream().filter(dto -> dto.getGroupId() == null)
            .map(UpdateGroupDto::getRequestId).collect(Collectors.toList());

        if (!idsToSetNull.isEmpty()) {
            // 创建更新条件
            UpdateWrapper<MedicationRequest> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("group_id", null).in("id", idsToSetNull);

            // 执行更新
            iMedicationRequestService.update(updateWrapper);
        }

        // 处理非null的情况
        List<MedicationRequest> medicationRequestList = groupList.stream().filter(dto -> dto.getGroupId() != null)
            .map(dto -> new MedicationRequest().setId(dto.getRequestId()).setGroupId(dto.getGroupId()))
            .collect(Collectors.toList());

        if (!medicationRequestList.isEmpty()) {
            iMedicationRequestService.saveOrUpdateBatch(medicationRequestList);
        }
    }

    /**
     * 查询就诊费用性质
     *
     * @param encounterId 就诊id
     * @return 就诊费用性质
     */
    @Override
    public R<?> getEncounterContract(Long encounterId) {
        return R.ok(doctorStationAdviceAppMapper.getEncounterContract(encounterId));
    }

}
