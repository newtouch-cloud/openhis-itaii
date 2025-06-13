package com.openhis.web.doctorstation.utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.openhis.common.constant.CommonConstants;
import com.openhis.common.enums.DispenseStatus;
import com.openhis.common.enums.PublicationStatus;
import com.openhis.web.doctorstation.dto.AdviceInventoryDto;
import com.openhis.web.doctorstation.dto.AdviceSaveDto;
import com.openhis.web.doctorstation.mapper.DoctorStationAdviceAppMapper;

/**
 * 医嘱工具类
 */
@Component
public class AdviceUtils {

    @Resource
    DoctorStationAdviceAppMapper doctorStationAdviceAppMapper;

    /**
     * 校验库存
     *
     * @param adviceSaveList 医嘱信息
     * @return 提示信息
     */
    public String checkInventory(List<AdviceSaveDto> adviceSaveList) {
        // 医嘱定义ID集合
        List<Long> adviceDefinitionIdList =
            adviceSaveList.stream().map(AdviceSaveDto::getAdviceDefinitionId).collect(Collectors.toList());
        // 医嘱库存集合
        List<AdviceInventoryDto> adviceInventoryList =
            doctorStationAdviceAppMapper.getAdviceInventory(null, adviceDefinitionIdList,
                CommonConstants.SqlCondition.ABOUT_INVENTORY_TABLE_STR, PublicationStatus.ACTIVE.getValue());
        // 待发放个数信息
        List<AdviceInventoryDto> adviceDraftInventoryList =
            doctorStationAdviceAppMapper.getAdviceDraftInventory(CommonConstants.TableName.MED_MEDICATION_DEFINITION,
                CommonConstants.TableName.ADM_DEVICE_DEFINITION, DispenseStatus.DRAFT.getValue());
        // 预减库存
        List<AdviceInventoryDto> adviceInventory =
            this.subtractInventory(adviceInventoryList, adviceDraftInventoryList);
        // 检查库存
        for (AdviceSaveDto saveDto : adviceSaveList) {
            boolean matched = false;
            for (AdviceInventoryDto inventoryDto : adviceInventory) {
                // 匹配条件：adviceDefinitionId, adviceTableName, locationId, lotNumber 同时相等
                if (saveDto.getAdviceDefinitionId().equals(inventoryDto.getItemId())
                    && saveDto.getAdviceTableName().equals(inventoryDto.getItemTable())
                    && saveDto.getLocationId().equals(inventoryDto.getLocationId())
                    && saveDto.getLotNumber().equals(inventoryDto.getLotNumber())) {
                    matched = true;
                    // 检查库存是否充足
                    BigDecimal minUnitQuantity = saveDto.getMinUnitQuantity();
                    Integer chineseHerbsDoseQuantity = saveDto.getChineseHerbsDoseQuantity(); // 中药付数
                    // 中草药医嘱的情况
                    if (chineseHerbsDoseQuantity != null && chineseHerbsDoseQuantity > 0) {
                        minUnitQuantity =
                            minUnitQuantity.multiply(BigDecimal.valueOf(chineseHerbsDoseQuantity.longValue()));
                    }
                    if (minUnitQuantity.compareTo(inventoryDto.getQuantity()) > 0) {
                        return saveDto.getAdviceName() + "在" + inventoryDto.getLocationName() + "库存不足";
                    }
                    break;
                }
            }
            // 如果没有匹配到库存
            if (!matched) {
                return saveDto.getAdviceName() + "未匹配到库存信息";
            }
        }
        return null; // 校验通过
    }

    /**
     * 预减库存
     *
     * @param adviceInventoryList 库存表数据
     * @param adviceDraftInventoryList 待发放数据
     * @return 预减库存
     */
    public List<AdviceInventoryDto> subtractInventory(List<AdviceInventoryDto> adviceInventoryList,
        List<AdviceInventoryDto> adviceDraftInventoryList) {
        // 1. 将 draft 列表转换为 Map，合并重复键的 quantity
        Map<String,
            AdviceInventoryDto> draftMap = adviceDraftInventoryList.stream()
                .collect(Collectors.toMap(
                    draft -> String.format(CommonConstants.Common.SS_DD_FORMAT, draft.getItemTable(),
                        draft.getLotNumber(), draft.getItemId(), draft.getLocationId()),
                    draft -> draft, (existing, replacement) -> {
                        // 合并重复键的 quantity
                        BigDecimal existingQty =
                            existing.getQuantity() != null ? existing.getQuantity() : BigDecimal.ZERO;
                        BigDecimal replacementQty =
                            replacement.getQuantity() != null ? replacement.getQuantity() : BigDecimal.ZERO;
                        existing.setQuantity(existingQty.add(replacementQty));
                        return existing;
                    }));
        // 2. 遍历原始库存列表，匹配并扣减
        return adviceInventoryList.stream().map(inventory -> {
            String key = String.format(CommonConstants.Common.SS_DD_FORMAT, inventory.getItemTable(),
                inventory.getLotNumber(), inventory.getItemId(), inventory.getLocationId());
            AdviceInventoryDto draft = draftMap.get(key);
            if (draft != null) {
                BigDecimal draftQty = draft.getQuantity() != null ? draft.getQuantity() : BigDecimal.ZERO;
                BigDecimal newQty = inventory.getQuantity().subtract(draftQty);
                inventory.setQuantity(newQty.compareTo(BigDecimal.ZERO) >= 0 ? newQty : BigDecimal.ZERO);
            }
            return inventory;
        }).collect(Collectors.toList());
    }

}
