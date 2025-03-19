package com.openhis.web.doctorstation.appservice.impl;

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
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.enums.ActivityType;
import com.openhis.common.enums.ConditionCode;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.doctorstation.appservice.IDoctorStationAdviceAppService;
import com.openhis.web.doctorstation.dto.AdviceBaseDto;
import com.openhis.web.doctorstation.dto.AdviceInventoryDto;
import com.openhis.web.doctorstation.dto.AdvicePriceDto;
import com.openhis.web.doctorstation.mapper.DoctorStationAdviceAppMapper;

/**
 * 医生站-医嘱/处方 应用实现类
 */
@Service
public class DoctorStationAdviceAppServiceImpl implements IDoctorStationAdviceAppService {

    @Resource
    DoctorStationAdviceAppMapper doctorStationAdviceAppMapper;

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

}
