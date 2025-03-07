/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.appservice.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.common.core.domain.R;
import com.openhis.administration.domain.ChargeItemDefApp;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.enums.EventStatus;
import com.openhis.web.inventorymanage.appservice.IInventoryManageService;
import com.openhis.web.inventorymanage.dto.ItemChargeDetailDto;
import com.openhis.web.inventorymanage.dto.SupplyItemDetailDto;
import com.openhis.web.inventorymanage.mapper.InventoryManageMapper;
import com.openhis.workflow.domain.SupplyRequest;

/**
 * 采购入库 impl
 *
 * @author zwh
 * @date 2025-03-05
 */
@Service
public class InventoryManageServiceImpl implements IInventoryManageService {

    @Autowired
    private InventoryManageMapper inventoryManageMapper;

    /**
     * 校验单据是否正确
     *
     * @param supplyRequest 单据信息
     * @return 校验结果
     */
    @Override
    public R<?> verifyInventoryReceipt(SupplyRequest supplyRequest) {

        // // 判断同一物品的批次号是否重复
        // boolean result = supplyRequestMapper
        // .exists(new LambdaQueryWrapper<SupplyRequest>().eq(SupplyRequest::getItemId, supplyRequest.getItemId())
        // .eq(SupplyRequest::getLotNumber, supplyRequest.getLotNumber())
        // .ne(supplyRequest.getId() != null, SupplyRequest::getId, supplyRequest.getId()));
        // if (result) {
        // return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00003, new Object[] {"批次号"}));
        // }
        return R.ok();
    }

    /**
     * 根据单据号获取供应单据及供应项相关详细信息
     *
     * @param busNo 单据号
     * @param itemTable 供应项所在表名
     * @return 供应单据及供应项相关详细信息
     */
    @Override
    public List<SupplyItemDetailDto> getSupplyItemDetail(String busNo, String itemTable) {

        List<SupplyItemDetailDto> supplyItemDetailList;
        // 判断供应项是药品还是耗材
        if (CommonConstants.TableName.MED_MEDICATION_DEFINITION.equals(itemTable)) {
            supplyItemDetailList = inventoryManageMapper.selectSupplyMedDetail(busNo, EventStatus.COMPLETED.getValue());
        } else if (CommonConstants.TableName.ADM_DEVICE.equals(itemTable)) {
            supplyItemDetailList = inventoryManageMapper.selectSupplyDevDetail(busNo, EventStatus.COMPLETED.getValue());
        } else {
            return null;
        }
        return supplyItemDetailList;
    }

    /**
     * 获取物品的价格信息
     *
     * @param itemIdList 物品id
     * @return 价格信息
     */
    @Override
    public List<ItemChargeDetailDto> getItemChargeDetail(List<Long> itemIdList) {
        // todo：未来会移到charge相关的service中
        if (!itemIdList.isEmpty()) {
            return inventoryManageMapper.selectChargeDetail(itemIdList);
        }
        return null;
    }

    /**
     * 入库项价格验证
     *
     * @param agreedList 供应单据
     * @param chargeDetailList 项目价格
     * @return 价格定义子表数据
     */
    @Override
    public List<ChargeItemDefApp> verifyItemCharge(List<SupplyRequest> agreedList,
        List<ItemChargeDetailDto> chargeDetailList) {

        // todo：价格换算的事情后续补上
        List<ChargeItemDefApp> resultList = new ArrayList<>();
        // 创建以项目id为key的map用于匹配对应的供应项目
        Map<Long, ItemChargeDetailDto> chargeDetailMap =
            chargeDetailList.stream().collect(Collectors.toMap(ItemChargeDetailDto::getInstanceId, dto -> dto));

        for (SupplyRequest supplyRequest : agreedList) {
            Long itemId = supplyRequest.getItemId();
            ItemChargeDetailDto chargeDetail = chargeDetailMap.get(itemId);

            if (chargeDetail == null) {
                // 未找到匹配的价格项则跳过
                continue;
            }
            // 比较批号是否一致
            if (!chargeDetail.getConditionLotnumber().equals(supplyRequest.getLotNumber())) {
                // todo:此时之加入了一条单位价格，需不需要进行大单位小单位的换算，增加两条价格
                resultList.add(this.addChargeItemDefApp(supplyRequest, chargeDetail));
            } else {
                // 检查单位是否匹配
                if (chargeDetail.getConditionUnitCode().equals(supplyRequest.getUnitCode())) {
                    // 比较价格是否一致
                    if (chargeDetail.getUnitPrice().compareTo(supplyRequest.getPrice()) != 0) {
                        // todo：此时需不需要增加两条价格，进行单位换算
                        resultList.add(this.addChargeItemDefApp(supplyRequest, chargeDetail));
                    }
                } else {
                    // todo：如果以上换算加入了两条则不存在这种情况，否则需要添加一条批次号相同，单位不同的两条记录（理论上不存在）
                }
            }
        }
        return resultList;
    }

    /**
     * 添加项目定价子表信息
     * 
     * @param supplyRequest 供应申请单
     * @param itemChargeDetail 项目价格信息
     * @return 项目定价子表
     */
    private ChargeItemDefApp addChargeItemDefApp(SupplyRequest supplyRequest, ItemChargeDetailDto itemChargeDetail) {
        ChargeItemDefApp app = new ChargeItemDefApp();
        app.setAmount(supplyRequest.getPrice()).setConditionLotnumber(supplyRequest.getLotNumber())
            .setDefinitionId(itemChargeDetail.getDefinitionId()).setConditionUnitCode(supplyRequest.getUnitCode());
        return app;
    }
}
