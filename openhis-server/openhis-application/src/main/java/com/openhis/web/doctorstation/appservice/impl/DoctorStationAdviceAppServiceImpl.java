package com.openhis.web.doctorstation.appservice.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
import com.openhis.administration.service.IChargeItemService;
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
                CommonConstants.TableName.WOR_ACTIVITY_DEFINITION, queryWrapper);
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
            .getChildCharge(ConditionCode.UNIT_PRODUCT_BATCH_NUM.getInfo(), chargeItemDefinitionIdList);
        // 费用定价主表信息
        List<AdvicePriceDto> mainCharge = doctorStationAdviceAppMapper.getMainCharge(chargeItemDefinitionIdList);
        // 药品和耗材
        List<AdviceBaseDto> medicationAndDeviceList = adviceBaseDtoList.stream()
            .filter(e -> CommonConstants.TableName.MED_MEDICATION_DEFINITION.equals(e.getAdviceTableName())
                || CommonConstants.TableName.ADM_DEVICE_DEFINITION.equals(e.getAdviceTableName()))
            .collect(Collectors.toList());
        // 药品和耗材-赋值
        String unitCode = ""; // 包装单位
        String minUnitCode; // 小单位
        Long chargeItemDefinitionId; // 费用定价主表ID
        for (AdviceBaseDto baseDto : medicationAndDeviceList) {
            // 每一条医嘱的库存集合信息
            List<AdviceInventoryDto> inventoryList =
                adviceInventory.stream().filter(e -> baseDto.getAdviceDefinitionId().equals(e.getItemId())
                    && baseDto.getAdviceTableName().equals(e.getItemTable())).collect(Collectors.toList());
            // 库存信息
            baseDto.setInventoryList(inventoryList);

            unitCode = baseDto.getUnitCode();
            minUnitCode = baseDto.getMinUnitCode();
            chargeItemDefinitionId = baseDto.getChargeItemDefinitionId();

            List<AdvicePriceDto> priceDtoList = new ArrayList<>();
            // 库存信息里取 单位,产品批号 去匹配价格
            for (AdviceInventoryDto adviceInventoryDto : inventoryList) {
                Long finalChargeItemDefinitionId = chargeItemDefinitionId;
                String finalUnitCode = unitCode;
                String finalMinUnitCode = minUnitCode;
                // 匹配包装单位
                List<AdvicePriceDto> advicePrice1 = childCharge.stream()
                    .filter(e -> e.getDefinitionId().equals(finalChargeItemDefinitionId) && e.getConditionValue()
                        .equals(String.format(CommonConstants.Common.COMMA_FORMAT, finalUnitCode,
                            adviceInventoryDto.getLotNumber())))
                    .peek(e -> e.setUnitCode(finalUnitCode)) // 设置 unitCode
                    .collect(Collectors.toList());
                // 匹配最小单位
                List<AdvicePriceDto> advicePrice2 = childCharge.stream()
                    .filter(e -> e.getDefinitionId().equals(finalChargeItemDefinitionId) && e.getConditionValue()
                        .equals(String.format(CommonConstants.Common.COMMA_FORMAT, finalMinUnitCode,
                            adviceInventoryDto.getLotNumber())))
                    .peek(e -> e.setUnitCode(finalMinUnitCode)) // 设置 unitCode
                    .collect(Collectors.toList());
                priceDtoList.addAll(advicePrice1);
                priceDtoList.addAll(advicePrice2);
            }
            // 价格信息
            baseDto.setPriceList(priceDtoList);
        }
        // 诊疗
        List<AdviceBaseDto> activityList = adviceBaseDtoList.stream()
            .filter(e -> CommonConstants.TableName.WOR_ACTIVITY_DEFINITION.equals(e.getAdviceTableName()))
            .collect(Collectors.toList());
        // 诊疗-赋值
        for (AdviceBaseDto baseDto : activityList) {
            // 活动类型
            baseDto.setActivityType_enumText(EnumUtils.getInfoByValue(ActivityType.class, baseDto.getActivityType()));
            List<AdvicePriceDto> priceList =
                mainCharge.stream().filter(e -> baseDto.getChargeItemDefinitionId().equals(e.getDefinitionId()))
                    .collect(Collectors.toList());
            // 价格信息
            baseDto.setPriceList(priceList);
        }
        return adviceBaseInfo;
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
        // 保存药品请求
        List<MedicationRequest> medicationRequestList = new ArrayList<>();
        MedicationRequest medicationRequest;
        for (AdviceSaveDto adviceSaveDto : medicineList) {
            medicationRequest = new MedicationRequest();
            medicationRequest.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.MEDICATION_RES_NO.getPrefix(), 8));
            medicationRequest.setPrescriptionNo(prescriptionNo);
            medicationRequest.setQuantity(adviceSaveDto.getQuantity());
            medicationRequest.setExecuteNum(adviceSaveDto.getExecuteNum());
            medicationRequest.setUnitCode(adviceSaveDto.getUnitCode());
            medicationRequest.setLotNumber(adviceSaveDto.getLotNumber());
            medicationRequest.setStatusEnum(adviceSaveDto.getStatusEnum());
            medicationRequest.setCategoryEnum(adviceSaveDto.getCategoryEnum());
            medicationRequest.setMedicationId(adviceSaveDto.getAdviceDefinitionId());// 医嘱定义id
            medicationRequest.setPatientId(adviceSaveDto.getPatientId());
            medicationRequest.setPractitionerId(adviceSaveDto.getPractitionerId());
            medicationRequest.setOrgId(adviceSaveDto.getOrgId());
            medicationRequest.setLocationId(adviceSaveDto.getLocationId());
            medicationRequest.setEncounterId(adviceSaveDto.getEncounterId());
            medicationRequest.setTherapyEnum(adviceSaveDto.getTherapyEnum());
            medicationRequest.setMethodCode(adviceSaveDto.getMethodCode());
            medicationRequest.setRateCode(adviceSaveDto.getRateCode());
            medicationRequest.setDose(adviceSaveDto.getDose());
            medicationRequest.setDoseUnitCode(adviceSaveDto.getDoseUnitCode());
            medicationRequest.setSkinTestFlag(adviceSaveDto.getSkinTestFlag()); // 皮试标记
            medicationRequest.setGroupId(adviceSaveDto.getGroupId());// 分组id
            // medicationRequest.setPackageId(adviceSaveDto.getPackageId());

            medicationRequestList.add(medicationRequest);
        }
        iMedicationRequestService.saveBatch(medicationRequestList);

        // 保存耗材请求
        List<DeviceRequest> deviceRequestList = new ArrayList<>();
        DeviceRequest deviceRequest;
        for (AdviceSaveDto adviceSaveDto : deviceList) {
            deviceRequest = new DeviceRequest();
            deviceRequest.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.DEVICE_RES_NO.getPrefix(), 8));
            // deviceRequest.setPrescriptionNo(prescriptionNo); // 耗材不需要处方号
            deviceRequest.setQuantity(adviceSaveDto.getQuantity());
            deviceRequest.setUnitCode(adviceSaveDto.getUnitCode());
            deviceRequest.setLotNumber(adviceSaveDto.getLotNumber());
            deviceRequest.setStatusEnum(adviceSaveDto.getStatusEnum());
            deviceRequest.setCategoryEnum(adviceSaveDto.getCategoryEnum());
            deviceRequest.setDeviceDefId(adviceSaveDto.getAdviceDefinitionId());// 耗材定义id
            deviceRequest.setPatientId(adviceSaveDto.getPatientId());
            deviceRequest.setRequesterId(adviceSaveDto.getPractitionerId());
            deviceRequest.setOrgId(adviceSaveDto.getOrgId());
            deviceRequest.setLocationId(adviceSaveDto.getLocationId());
            deviceRequest.setEncounterId(adviceSaveDto.getEncounterId());
            // deviceRequest.setPackageId(adviceSaveDto.getPackageId());
            // deviceRequest.setActivityId(adviceSaveDto.getActivityId());

            deviceRequestList.add(deviceRequest);
        }
        iDeviceRequestService.saveBatch(deviceRequestList);

        // 保存诊疗项目请求
        List<ServiceRequest> serviceRequestList = new ArrayList<>();
        ServiceRequest serviceRequest;
        for (AdviceSaveDto adviceSaveDto : activityList) {
            serviceRequest = new ServiceRequest();
            serviceRequest.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.SERVICE_RES_NO.getPrefix(), 8));
            // serviceRequest.setPrescriptionNo(prescriptionNo); // 诊疗不需要处方号
            serviceRequest.setBasedOnTable(CommonConstants.TableName.WOR_ACTIVITY_DEFINITION);
            serviceRequest.setBasedOnId(adviceSaveDto.getAdviceDefinitionId());
            serviceRequest.setQuantity(adviceSaveDto.getQuantity());
            serviceRequest.setUnitCode(adviceSaveDto.getUnitCode());
            serviceRequest.setStatusEnum(adviceSaveDto.getStatusEnum());
            serviceRequest.setCategoryEnum(adviceSaveDto.getCategoryEnum());
            serviceRequest.setCategoryEnum(adviceSaveDto.getCategoryEnum());
            serviceRequest.setActivityId(adviceSaveDto.getAdviceDefinitionId());// 诊疗定义id
            serviceRequest.setPatientId(adviceSaveDto.getPatientId());
            serviceRequest.setRequesterId(adviceSaveDto.getPractitionerId());
            serviceRequest.setEncounterId(adviceSaveDto.getEncounterId());

            serviceRequestList.add(serviceRequest);
        }
        iServiceRequestService.saveBatch(serviceRequestList);

        // 保存费用项管理
        List<ChargeItem> chargeItemList = new ArrayList<>();
        ChargeItem chargeItem;
        for (AdviceSaveDto adviceSaveDto : adviceSaveList) {
            chargeItem = new ChargeItem();
            chargeItem.setStatusEnum(ChargeItemStatus.PLANNED.getValue());
            chargeItem.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.CHARGE_ITEM_NO.getPrefix(), 8));
            chargeItem.setPrescriptionNo(prescriptionNo);
            chargeItem.setPatientId(adviceSaveDto.getPatientId());
            chargeItem.setContextEnum(adviceSaveDto.getAdviceType());
            chargeItem.setEncounterId(adviceSaveDto.getEncounterId());
            chargeItem.setQuantityValue(adviceSaveDto.getQuantity()); // 数量
            chargeItem.setQuantityUnit(adviceSaveDto.getUnitCode()); // 单位
            chargeItem.setUnitPrice(adviceSaveDto.getUnitPrice()); // 单价
            chargeItem
                .setTotalPrice((new BigDecimal(adviceSaveDto.getQuantity()).multiply(adviceSaveDto.getUnitPrice()))
                    .setScale(4, RoundingMode.HALF_UP)); // 总价
            chargeItem.setDefinitionId(adviceSaveDto.getDefinitionId());
            chargeItem.setDefDetailId(adviceSaveDto.getDefinitionDetailId());

            chargeItemList.add(chargeItem);
        }
        iChargeItemService.saveBatch(chargeItemList);
        // TODO: 此处调用请求发放接口

        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"门诊医嘱"}));
    }

}
