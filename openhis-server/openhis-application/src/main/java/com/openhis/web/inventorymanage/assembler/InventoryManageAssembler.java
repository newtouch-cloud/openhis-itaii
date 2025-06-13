/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.assembler;

import java.util.ArrayList;
import java.util.List;

import com.openhis.web.inventorymanage.dto.SupplyItemDetailDto;
import com.openhis.workflow.domain.InventoryItem;

/**
 * 入库管理dto转换器
 *
 * @author zwh
 * @date 2025-03-05
 */
public class InventoryManageAssembler {

    /**
     * 将供应项目的详细信息装配为库存项目
     *
     * @param supplyItemDetailList 供应项目的详细信息
     * @return 库存项目
     */
    public static List<InventoryItem> assembleInventoryItem(List<SupplyItemDetailDto> supplyItemDetailList) {

        List<InventoryItem> inventoryItemList = new ArrayList<>();
        for (SupplyItemDetailDto supplyItemDetailDto : supplyItemDetailList) {

            InventoryItem inventoryItem = new InventoryItem();
            inventoryItem.setItemId(supplyItemDetailDto.getItemId())
                // 入库项目所在表
                .setItemTable(supplyItemDetailDto.getItemTable())
                // 入库项目
                .setItemId(supplyItemDetailDto.getItemId())
                // 入库项目类型
                .setCategoryCode(supplyItemDetailDto.getItemCategory())
                // 批号
                .setLotNumber(supplyItemDetailDto.getLotNumber())
                // 追溯码
                .setTraceNo(supplyItemDetailDto.getTraceNo())
                // 供应商
                .setSupplierId(supplyItemDetailDto.getSupplierId())
                // 仓库
                .setLocationId(supplyItemDetailDto.getPurposeLocationId())
                // 库位
                .setLocationStoreId(supplyItemDetailDto.getPurposeLocationStoreId())
                // 过期日期
                .setExpirationDate(supplyItemDetailDto.getEndTime())
                // 生产日期
                .setProductionDate(supplyItemDetailDto.getStartTime())
                // 项目名
                .setName(supplyItemDetailDto.getName())
                // 拼音码
                .setPyStr(supplyItemDetailDto.getPyStr())
                // 五笔码
                .setWbStr(supplyItemDetailDto.getWbStr())
                // 最小单位
                .setUnitCode(supplyItemDetailDto.getMinUnitCode())
                // 采购价格
                .setPrice(supplyItemDetailDto.getPrice());

            if (supplyItemDetailDto.getItemUnit().equals(supplyItemDetailDto.getUnitCode())) {
                inventoryItem
                    // 拆零数量（拆零比×包装数量）
                    .setQuantity(supplyItemDetailDto.getPartPercent().multiply(supplyItemDetailDto.getItemQuantity()));
            } else if (supplyItemDetailDto.getItemUnit().equals(supplyItemDetailDto.getMinUnitCode())) {
                inventoryItem
                    // 拆零数量
                    .setQuantity(supplyItemDetailDto.getItemQuantity());
            }

            inventoryItemList.add(inventoryItem);
        }

        return inventoryItemList;
    }
}
