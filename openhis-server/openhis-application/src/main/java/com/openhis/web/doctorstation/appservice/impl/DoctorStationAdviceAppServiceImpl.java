package com.openhis.web.doctorstation.appservice.impl;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.MessageUtils;
import com.openhis.administration.domain.ChargeItem;
import com.openhis.administration.domain.OrganizationLocation;
import com.openhis.administration.service.IChargeItemService;
import com.openhis.administration.service.IOrganizationLocationService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.medication.domain.MedicationRequest;
import com.openhis.medication.service.IMedicationRequestService;
import com.openhis.web.doctorstation.appservice.IDoctorStationAdviceAppService;
import com.openhis.web.doctorstation.dto.*;
import com.openhis.web.doctorstation.mapper.DoctorStationAdviceAppMapper;
import com.openhis.workflow.domain.DeviceRequest;
import com.openhis.workflow.domain.ServiceRequest;
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

    /**
     * 查询医嘱信息
     *
     * @param adviceBaseDto 查询条件
     * @param searchKey 模糊查询关键字
     * @param locationId 药房id
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 医嘱信息
     */
    @Override
    public IPage<AdviceBaseDto> getAdviceBaseInfo(AdviceBaseDto adviceBaseDto, String searchKey, Long locationId,
        Integer pageNo, Integer pageSize) {
        // 构建查询条件
        QueryWrapper<AdviceBaseDto> queryWrapper = HisQueryUtils.buildQueryWrapper(adviceBaseDto, searchKey,
            new HashSet<>(Arrays.asList("advice_name", "py_str", "wb_str")), null);
        IPage<AdviceBaseDto> adviceBaseInfo =
            doctorStationAdviceAppMapper.getAdviceBaseInfo(new Page<>(pageNo, pageSize),
                CommonConstants.TableName.MED_MEDICATION_DEFINITION, CommonConstants.TableName.ADM_DEVICE_DEFINITION,
                CommonConstants.TableName.WOR_ACTIVITY_DEFINITION, DeviceCategory.SINGLE_USE.getCode(), queryWrapper);
        List<AdviceBaseDto> adviceBaseDtoList = adviceBaseInfo.getRecords();
        // 医嘱定义ID集合
        List<Long> adviceDefinitionIdList =
            adviceBaseDtoList.stream().map(AdviceBaseDto::getAdviceDefinitionId).collect(Collectors.toList());
        // 费用定价主表ID集合
        List<Long> chargeItemDefinitionIdList =
            adviceBaseDtoList.stream().map(AdviceBaseDto::getChargeItemDefinitionId).collect(Collectors.toList());
        // 医嘱库存集合
        List<AdviceInventoryDto> adviceInventory = doctorStationAdviceAppMapper.getAdviceInventory(locationId,
            adviceDefinitionIdList, CommonConstants.SqlCondition.ABOUT_INVENTORY_TABLE_STR);
        // TODO: 预减库存待处理

        // 费用定价子表信息
        List<AdvicePriceDto> childCharge = doctorStationAdviceAppMapper
            .getChildCharge(ConditionCode.UNIT_PRODUCT_BATCH_NUM.getCode(), chargeItemDefinitionIdList);
        // 费用定价主表信息
        List<AdvicePriceDto> mainCharge = doctorStationAdviceAppMapper.getMainCharge(chargeItemDefinitionIdList);

        String unitCode = ""; // 包装单位
        Long chargeItemDefinitionId; // 费用定价主表ID
        for (AdviceBaseDto baseDto : adviceBaseDtoList) {
            // 药品和耗材
            if (CommonConstants.TableName.MED_MEDICATION_DEFINITION.equals(baseDto.getAdviceTableName())
                || CommonConstants.TableName.ADM_DEVICE_DEFINITION.equals(baseDto.getAdviceTableName())) {
                // 每一条医嘱的库存集合信息 , 包装单位库存前端计算
                List<AdviceInventoryDto> inventoryList =
                    adviceInventory.stream().filter(e -> baseDto.getAdviceDefinitionId().equals(e.getItemId())
                        && baseDto.getAdviceTableName().equals(e.getItemTable())).collect(Collectors.toList());
                // 库存信息
                baseDto.setInventoryList(inventoryList);
                unitCode = baseDto.getUnitCode();
                chargeItemDefinitionId = baseDto.getChargeItemDefinitionId();
                List<AdvicePriceDto> priceDtoList = new ArrayList<>();
                // 库存信息里取 命中条件 去匹配价格
                for (AdviceInventoryDto adviceInventoryDto : inventoryList) {
                    Long finalChargeItemDefinitionId = chargeItemDefinitionId;
                    String finalUnitCode = unitCode;
                    // 匹配包装单位
                    List<AdvicePriceDto> advicePrice1 = childCharge.stream()
                        .filter(e -> e.getDefinitionId().equals(finalChargeItemDefinitionId)
                            && e.getConditionValue().equals(adviceInventoryDto.getLotNumber()))
                        .peek(e -> e.setUnitCode(finalUnitCode)) // 设置 unitCode
                        .collect(Collectors.toList());
                    priceDtoList.addAll(advicePrice1);
                }
                // 价格信息
                baseDto.setPriceList(priceDtoList);
            }
            // 诊疗活动
            else if (CommonConstants.TableName.WOR_ACTIVITY_DEFINITION.equals(baseDto.getAdviceTableName())) {
                List<AdvicePriceDto> priceList =
                    mainCharge.stream().filter(e -> baseDto.getChargeItemDefinitionId().equals(e.getDefinitionId()))
                        .collect(Collectors.toList());
                // 价格信息
                baseDto.setPriceList(priceList);
                // 活动类型
                baseDto
                    .setActivityType_enumText(EnumUtils.getInfoByValue(ActivityType.class, baseDto.getActivityType()));
            }
        }

        return adviceBaseInfo;

        // 下面的注释不要删除 2025.03.27
        // // 药品和耗材
        // List<AdviceBaseDto> medicationAndDeviceList = adviceBaseDtoList.stream()
        // .filter(e -> CommonConstants.TableName.MED_MEDICATION_DEFINITION.equals(e.getAdviceTableName())
        // || CommonConstants.TableName.ADM_DEVICE_DEFINITION.equals(e.getAdviceTableName()))
        // .collect(Collectors.toList());
        // // 药品和耗材-赋值
        // String unitCode = ""; // 包装单位
        // String minUnitCode; // 小单位
        // Long chargeItemDefinitionId; // 费用定价主表ID
        // for (AdviceBaseDto baseDto : medicationAndDeviceList) {
        // // 每一条医嘱的库存集合信息
        // List<AdviceInventoryDto> inventoryList =
        // adviceInventory.stream().filter(e -> baseDto.getAdviceDefinitionId().equals(e.getItemId())
        // && baseDto.getAdviceTableName().equals(e.getItemTable())).collect(Collectors.toList());
        // // 库存信息
        // baseDto.setInventoryList(inventoryList);
        //
        // unitCode = baseDto.getUnitCode();
        // minUnitCode = baseDto.getMinUnitCode();
        // chargeItemDefinitionId = baseDto.getChargeItemDefinitionId();
        //
        // List<AdvicePriceDto> priceDtoList = new ArrayList<>();
        // // 库存信息里取 单位,产品批号 去匹配价格
        // for (AdviceInventoryDto adviceInventoryDto : inventoryList) {
        // Long finalChargeItemDefinitionId = chargeItemDefinitionId;
        // String finalUnitCode = unitCode;
        // String finalMinUnitCode = minUnitCode;
        // // 匹配包装单位
        // List<AdvicePriceDto> advicePrice1 = childCharge.stream()
        // .filter(e -> e.getDefinitionId().equals(finalChargeItemDefinitionId) && e.getConditionValue()
        // .equals(String.format(CommonConstants.Common.COMMA_FORMAT, finalUnitCode,
        // adviceInventoryDto.getLotNumber())))
        // .peek(e -> e.setUnitCode(finalUnitCode)) // 设置 unitCode
        // .collect(Collectors.toList());
        // // 匹配最小单位
        // List<AdvicePriceDto> advicePrice2 = childCharge.stream()
        // .filter(e -> e.getDefinitionId().equals(finalChargeItemDefinitionId) && e.getConditionValue()
        // .equals(String.format(CommonConstants.Common.COMMA_FORMAT, finalMinUnitCode,
        // adviceInventoryDto.getLotNumber())))
        // .peek(e -> e.setUnitCode(finalMinUnitCode)) // 设置 unitCode
        // .collect(Collectors.toList());
        // priceDtoList.addAll(advicePrice1);
        // priceDtoList.addAll(advicePrice2);
        // }
        // // 价格信息
        // baseDto.setPriceList(priceDtoList);
        // }
        // // 诊疗
        // List<AdviceBaseDto> activityList = adviceBaseDtoList.stream()
        // .filter(e -> CommonConstants.TableName.WOR_ACTIVITY_DEFINITION.equals(e.getAdviceTableName()))
        // .collect(Collectors.toList());
        // // 诊疗-赋值
        // for (AdviceBaseDto baseDto : activityList) {
        // // 活动类型
        // baseDto.setActivityType_enumText(EnumUtils.getInfoByValue(ActivityType.class, baseDto.getActivityType()));
        // List<AdvicePriceDto> priceList =
        // mainCharge.stream().filter(e -> baseDto.getChargeItemDefinitionId().equals(e.getDefinitionId()))
        // .collect(Collectors.toList());
        // // 价格信息
        // baseDto.setPriceList(priceList);
        // }

    }

    /**
     * 门诊保存医嘱
     *
     * @param adviceSaveParam 医嘱表单信息
     * @return 结果
     */
    @Override
    public R<?> saveAdvice(AdviceSaveParam adviceSaveParam) {
        // 医嘱分类信息
        List<AdviceSaveDto> adviceSaveList = adviceSaveParam.getAdviceSaveList();
        // TODO: 保存医嘱时规则校验;待做
        // 药品
        List<AdviceSaveDto> medicineList = adviceSaveList.stream()
            .filter(e -> ItemType.MEDICINE.getValue().equals(e.getAdviceType())).collect(Collectors.toList());
        // 耗材
        List<AdviceSaveDto> deviceList = adviceSaveList.stream()
            .filter(e -> ItemType.DEVICE.getValue().equals(e.getAdviceType())).collect(Collectors.toList());
        // 诊疗活动
        List<AdviceSaveDto> activityList = adviceSaveList.stream()
            .filter(e -> ItemType.ACTIVITY.getValue().equals(e.getAdviceType())).collect(Collectors.toList());
        // 生成处方号 , 只有开了药品才有处方号
        String prescriptionNo = "";
        if (medicineList.size() > 0) {
            // TODO: 药品分方;待做
            prescriptionNo = assignSeqUtil.getSeq(AssignSeqEnum.PRESCRIPTION_NO.getPrefix(), 8);
        }
        Date curDate = new Date();
        // 声明费用项
        ChargeItem chargeItem;
        // 保存药品请求
        MedicationRequest medicationRequest;
        for (AdviceSaveDto adviceSaveDto : medicineList) {
            medicationRequest = new MedicationRequest();
            medicationRequest.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.MEDICATION_RES_NO.getPrefix(), 10));
            medicationRequest.setPrescriptionNo(prescriptionNo); // 处方号
            medicationRequest.setQuantity(adviceSaveDto.getQuantity()); // 请求数量
            medicationRequest.setExecuteNum(adviceSaveDto.getExecuteNum()); // 执行次数
            medicationRequest.setUnitCode(adviceSaveDto.getUnitCode()); // 请求单位编码
            medicationRequest.setLotNumber(adviceSaveDto.getLotNumber()); // 产品批号
            medicationRequest.setStatusEnum(adviceSaveDto.getStatusEnum()); // 请求状态,默认-待发送
            medicationRequest.setCategoryEnum(adviceSaveDto.getCategoryEnum()); // 请求类型,默认-门诊
            medicationRequest.setMedicationId(adviceSaveDto.getAdviceDefinitionId());// 医嘱定义id
            medicationRequest.setPatientId(adviceSaveDto.getPatientId()); // 患者
            medicationRequest.setPractitionerId(adviceSaveDto.getPractitionerId()); // 开方医生
            medicationRequest.setOrgId(adviceSaveDto.getOrgId()); // 开方人科室
            // 查询机构位置关系
            OrganizationLocation orgLocByOrgIdAndCategoryCode = iOrganizationLocationService
                .getOrgLocByOrgIdAndCategoryCode(adviceSaveDto.getOrgId(), adviceSaveDto.getCategoryCode());
            if (orgLocByOrgIdAndCategoryCode != null) {
                // 发放药房
                medicationRequest.setPerformLocation(orgLocByOrgIdAndCategoryCode.getDefLocationId());
            }
            // medicationRequest.setLocationId(adviceSaveDto.getLocationId()); // 请求发起的位置
            medicationRequest.setEncounterId(adviceSaveDto.getEncounterId()); // 就诊id
            medicationRequest.setConditionId(adviceSaveDto.getConditionId()); // 诊断id
            medicationRequest.setTherapyEnum(adviceSaveDto.getTherapyEnum()); // 治疗类型,默认-临时
            medicationRequest.setMethodCode(adviceSaveDto.getMethodCode()); // 用法
            medicationRequest.setRateCode(adviceSaveDto.getRateCode()); // 用药频次
            medicationRequest.setDose(adviceSaveDto.getDose()); // 单次剂量
            medicationRequest.setDoseUnitCode(adviceSaveDto.getDoseUnitCode()); // 剂量单位
            medicationRequest.setDispensePerDuration(adviceSaveDto.getDispensePerDuration()); // 每次发药供应天数
            medicationRequest.setSkinTestFlag(adviceSaveDto.getSkinTestFlag()); // 皮试标记
            medicationRequest.setGroupId(adviceSaveDto.getGroupId());// 分组id
            // medicationRequest.setPackageId(adviceSaveDto.getPackageId()); // 组套id

            iMedicationRequestService.save(medicationRequest);

            // 保存药品费用项
            chargeItem = new ChargeItem();
            chargeItem.setStatusEnum(ChargeItemStatus.PLANNED.getValue()); // 默认-待收费
            chargeItem.setBusNo(AssignSeqEnum.CHARGE_ITEM_NO.getPrefix().concat(medicationRequest.getBusNo()));
            chargeItem.setPrescriptionNo(prescriptionNo); // 处方号
            chargeItem.setPatientId(adviceSaveDto.getPatientId()); // 患者
            chargeItem.setContextEnum(adviceSaveDto.getAdviceType()); // 类型
            chargeItem.setEncounterId(adviceSaveDto.getEncounterId()); // 就诊id
            chargeItem.setDefinitionId(adviceSaveDto.getDefinitionId()); // 费用定价ID
            chargeItem.setDefDetailId(adviceSaveDto.getDefinitionDetailId()); // 定价子表主键
            chargeItem.setEntererId(adviceSaveDto.getPractitionerId());// 开立人ID
            chargeItem.setEnteredDate(curDate); // 开立时间
            chargeItem.setServiceTable("med_medication_request");// 医疗服务类型
            chargeItem.setServiceId(medicationRequest.getId()); // 医疗服务ID
            chargeItem.setProductTable(adviceSaveDto.getAdviceTableName());// 产品所在表
            chargeItem.setProductId(adviceSaveDto.getAdviceDefinitionId());// 收费项id
            chargeItem.setAccountId(adviceSaveDto.getAccountId());// 关联账户ID

            chargeItem.setQuantityValue(adviceSaveDto.getQuantity()); // 数量
            chargeItem.setQuantityUnit(adviceSaveDto.getUnitCode()); // 单位
            chargeItem.setUnitPrice(adviceSaveDto.getUnitPrice()); // 单价
            chargeItem.setTotalPrice(adviceSaveDto.getTotalPrice()); // 总价

            iChargeItemService.save(chargeItem);
        }

        // 保存耗材请求
        DeviceRequest deviceRequest;
        for (AdviceSaveDto adviceSaveDto : deviceList) {
            deviceRequest = new DeviceRequest();
            deviceRequest.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.DEVICE_RES_NO.getPrefix(), 10));
            // deviceRequest.setPrescriptionNo(prescriptionNo); // 耗材不需要处方号
            deviceRequest.setQuantity(adviceSaveDto.getQuantity()); // 请求数量
            deviceRequest.setUnitCode(adviceSaveDto.getUnitCode()); // 请求单位编码
            deviceRequest.setLotNumber(adviceSaveDto.getLotNumber());// 产品批号
            deviceRequest.setStatusEnum(adviceSaveDto.getStatusEnum()); // 请求状态,默认-待发送
            deviceRequest.setCategoryEnum(adviceSaveDto.getCategoryEnum()); // 请求类型,默认-门诊
            deviceRequest.setDeviceDefId(adviceSaveDto.getAdviceDefinitionId());// 耗材定义id
            deviceRequest.setPatientId(adviceSaveDto.getPatientId()); // 患者
            deviceRequest.setRequesterId(adviceSaveDto.getPractitionerId()); // 开方医生
            deviceRequest.setOrgId(adviceSaveDto.getOrgId());// 开方人科室
            // 查询机构位置关系
            OrganizationLocation orgLocByOrgIdAndCategoryCode = iOrganizationLocationService
                .getOrgLocByOrgIdAndCategoryCode(adviceSaveDto.getOrgId(), adviceSaveDto.getCategoryCode());
            if (orgLocByOrgIdAndCategoryCode != null) {
                // 发放耗材房
                deviceRequest.setPerformLocation(orgLocByOrgIdAndCategoryCode.getDefLocationId());
            }
            // deviceRequest.setLocationId(adviceSaveDto.getLocationId()); 请求发起的位置
            deviceRequest.setEncounterId(adviceSaveDto.getEncounterId()); // 就诊id
            // deviceRequest.setPackageId(adviceSaveDto.getPackageId()); // 组套id
            // deviceRequest.setActivityId(adviceSaveDto.getActivityId());

            iDeviceRequestService.save(deviceRequest);

            // 保存耗材费用项
            chargeItem = new ChargeItem();
            chargeItem.setStatusEnum(ChargeItemStatus.PLANNED.getValue()); // 默认-待收费
            chargeItem.setBusNo(AssignSeqEnum.CHARGE_ITEM_NO.getPrefix().concat(deviceRequest.getBusNo()));
            chargeItem.setPatientId(adviceSaveDto.getPatientId()); // 患者
            chargeItem.setContextEnum(adviceSaveDto.getAdviceType()); // 类型
            chargeItem.setEncounterId(adviceSaveDto.getEncounterId()); // 就诊id
            chargeItem.setDefinitionId(adviceSaveDto.getDefinitionId()); // 费用定价ID
            chargeItem.setDefDetailId(adviceSaveDto.getDefinitionDetailId()); // 定价子表主键
            chargeItem.setEntererId(adviceSaveDto.getPractitionerId());// 开立人ID
            chargeItem.setEnteredDate(curDate); // 开立时间
            chargeItem.setServiceTable("wor_device_request");// 医疗服务类型
            chargeItem.setServiceId(deviceRequest.getId()); // 医疗服务ID
            chargeItem.setProductTable(adviceSaveDto.getAdviceTableName());// 产品所在表
            chargeItem.setProductId(adviceSaveDto.getAdviceDefinitionId());// 收费项id
            chargeItem.setAccountId(adviceSaveDto.getAccountId());// 关联账户ID

            chargeItem.setQuantityValue(adviceSaveDto.getQuantity()); // 数量
            chargeItem.setQuantityUnit(adviceSaveDto.getUnitCode()); // 单位
            chargeItem.setUnitPrice(adviceSaveDto.getUnitPrice()); // 单价
            chargeItem.setTotalPrice(adviceSaveDto.getTotalPrice()); // 总价

            iChargeItemService.save(chargeItem);
        }

        // 保存诊疗项目请求
        ServiceRequest serviceRequest;
        for (AdviceSaveDto adviceSaveDto : activityList) {
            serviceRequest = new ServiceRequest();
            serviceRequest.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.SERVICE_RES_NO.getPrefix(), 10));
            // serviceRequest.setPrescriptionNo(prescriptionNo); // 诊疗不需要处方号
            serviceRequest.setBasedOnTable(CommonConstants.TableName.WOR_ACTIVITY_DEFINITION);
            serviceRequest.setBasedOnId(adviceSaveDto.getAdviceDefinitionId());
            serviceRequest.setQuantity(adviceSaveDto.getQuantity()); // 请求数量
            serviceRequest.setUnitCode(adviceSaveDto.getUnitCode()); // 请求单位编码
            serviceRequest.setStatusEnum(adviceSaveDto.getStatusEnum());// 请求状态,默认-待发送
            serviceRequest.setCategoryEnum(adviceSaveDto.getCategoryEnum()); // 请求类型,默认-门诊
            serviceRequest.setActivityId(adviceSaveDto.getAdviceDefinitionId());// 诊疗定义id
            serviceRequest.setPatientId(adviceSaveDto.getPatientId()); // 患者
            serviceRequest.setRequesterId(adviceSaveDto.getPractitionerId()); // 开方医生
            serviceRequest.setEncounterId(adviceSaveDto.getEncounterId()); // 就诊id

            iServiceRequestService.save(serviceRequest);

            // 保存诊疗费用项
            chargeItem = new ChargeItem();
            chargeItem.setStatusEnum(ChargeItemStatus.PLANNED.getValue()); // 默认-待收费
            chargeItem.setBusNo(AssignSeqEnum.CHARGE_ITEM_NO.getPrefix().concat(serviceRequest.getBusNo()));
            chargeItem.setPatientId(adviceSaveDto.getPatientId()); // 患者
            chargeItem.setContextEnum(adviceSaveDto.getAdviceType()); // 类型
            chargeItem.setEncounterId(adviceSaveDto.getEncounterId()); // 就诊id
            chargeItem.setDefinitionId(adviceSaveDto.getDefinitionId()); // 费用定价ID
            chargeItem.setDefDetailId(adviceSaveDto.getDefinitionDetailId()); // 定价子表主键
            chargeItem.setEntererId(adviceSaveDto.getPractitionerId());// 开立人ID
            chargeItem.setEnteredDate(curDate); // 开立时间
            chargeItem.setServiceTable("wor_service_request");// 医疗服务类型
            chargeItem.setServiceId(serviceRequest.getId()); // 医疗服务ID
            chargeItem.setProductTable(adviceSaveDto.getAdviceTableName());// 产品所在表
            chargeItem.setProductId(adviceSaveDto.getAdviceDefinitionId());// 收费项id
            chargeItem.setAccountId(adviceSaveDto.getAccountId());// 关联账户ID

            chargeItem.setQuantityValue(adviceSaveDto.getQuantity()); // 数量
            chargeItem.setQuantityUnit(adviceSaveDto.getUnitCode()); // 单位
            chargeItem.setUnitPrice(adviceSaveDto.getUnitPrice()); // 单价
            chargeItem.setTotalPrice(adviceSaveDto.getTotalPrice()); // 总价

            iChargeItemService.save(chargeItem);
        }

        // TODO: 此处调用请求发放接口

        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"门诊医嘱"}));
    }

}
