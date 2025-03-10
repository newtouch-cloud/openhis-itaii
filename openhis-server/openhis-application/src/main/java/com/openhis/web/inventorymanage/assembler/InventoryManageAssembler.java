/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.assembler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.core.common.core.domain.model.LoginUser;
import com.openhis.administration.domain.ChargeItem;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.enums.ChargeItemContext;
import com.openhis.common.enums.ChargeItemStatus;
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
     * 将供应项目的详细信息装配为库存项目和采购账单
     * 
     * @param supplyItemDetailList 供应项目的详细信息
     * @param now 当前时间
     * @param loginUser 登陆者信息
     * @return 库存项目和采购账单
     */
    public static Pair<List<ChargeItem>, List<InventoryItem>>
        assembleChargeAndInventory(List<SupplyItemDetailDto> supplyItemDetailList, Date now, LoginUser loginUser) {

        List<ChargeItem> chargeItemList = new ArrayList<>();
        List<InventoryItem> inventoryItemList = new ArrayList<>();
        for (SupplyItemDetailDto supplyItemDetailDto : supplyItemDetailList) {

            ChargeItem chargeItem = new ChargeItem();
            chargeItem
                // 收费项所在表：供应物品所在表
                .setProductTable(supplyItemDetailDto.getItemTable())
                // 收费项id：供应物品id
                .setProductId(supplyItemDetailDto.getItemId())
                // 物品数量
                .setQuantityValue(supplyItemDetailDto.getItemQuantity().longValue())
                // 物品单位
                .setQuantityUnit(supplyItemDetailDto.getItemUnit())
                // 原价
                .setBaseAmount(supplyItemDetailDto.getBaseAmount())
                // 总价
                .setTotalPrice(supplyItemDetailDto.getTotalPrice())
                // 单价
                .setUnitPrice(supplyItemDetailDto.getPrice())
                // 价格定义id
                .setDefinitionId(supplyItemDetailDto.getDefinitionId())
                // 价格定义子表id
                .setDefDetailId(supplyItemDetailDto.getDefDetailId())
                // 执行人：审批人
                .setPerformerId(supplyItemDetailDto.getApproverId())
                // 执行科室：发放目的仓库
                .setPerformingOrgId(supplyItemDetailDto.getPurposeLocationId())
                // 收费来源id：供应服务id
                .setServiceId(supplyItemDetailDto.getServiceId())
                // 收费来源表：发放请求
                .setServiceTable(CommonConstants.TableName.WOR_SUPPLY_REQUEST)
                // 收费状态：已结算
                .setStatusEnum(ChargeItemStatus.BILLED.getValue())
                // 收费类型：采购
                .setContextEnum(ChargeItemContext.PRESCRIPTION.getValue())
                // 发生时间
                .setOccurrenceTime(now)
                // 开立人
                .setEntererId(loginUser.getUserId())
                // 开立时间
                .setEnteredDate(now);

            InventoryItem inventoryItem = new InventoryItem();
            inventoryItem.setItemId(supplyItemDetailDto.getItemId())
                // 入库项目所在表
                .setItemTable(supplyItemDetailDto.getItemTable())
                // 入库项目
                .setItemId(supplyItemDetailDto.getItemId())
                // 入库项目类型
                .setCategoryEnum(Integer.valueOf(supplyItemDetailDto.getItemCategory()))
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
                // 包装单位
                .setBaseUnitCode(supplyItemDetailDto.getUnitCode())
                // 最小单位
                .setMinUnitCode(supplyItemDetailDto.getMinUnitCode());

            chargeItemList.add(chargeItem);
            inventoryItemList.add(inventoryItem);
        }

        return Pair.of(chargeItemList, inventoryItemList);
    }
}
