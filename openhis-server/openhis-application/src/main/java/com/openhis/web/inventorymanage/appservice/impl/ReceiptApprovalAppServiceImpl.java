/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.appservice.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.common.core.domain.R;
import com.core.common.core.domain.model.LoginUser;
import com.core.common.utils.DateUtils;
import com.core.common.utils.MessageUtils;
import com.core.framework.web.service.TokenService;
import com.openhis.administration.domain.ChargeItem;
import com.openhis.administration.domain.ChargeItemDefDetail;
import com.openhis.administration.service.IChargeItemDefDetailService;
import com.openhis.administration.service.IChargeItemService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.EventStatus;
import com.openhis.web.inventorymanage.appservice.IReceiptApprovalAppService;
import com.openhis.web.inventorymanage.assembler.InventoryManageAssembler;
import com.openhis.web.inventorymanage.dto.ItemChargeDetailDto;
import com.openhis.web.inventorymanage.dto.SupplyItemDetailDto;
import com.openhis.web.inventorymanage.mapper.ReceiptApprovalMapper;
import com.openhis.workflow.domain.InventoryItem;
import com.openhis.workflow.domain.SupplyDelivery;
import com.openhis.workflow.domain.SupplyRequest;
import com.openhis.workflow.service.IInventoryItemService;
import com.openhis.workflow.service.ISupplyDeliveryService;
import com.openhis.workflow.service.ISupplyRequestService;

/**
 * 单据审批 impl
 *
 * @author zwh
 * @date 2025-03-05
 */
@Service
public class ReceiptApprovalAppServiceImpl implements IReceiptApprovalAppService {

    @Autowired
    private ISupplyRequestService supplyRequestService;
    @Autowired
    private ISupplyDeliveryService supplyDeliveryService;
    @Autowired
    private IChargeItemDefDetailService chargeItemDefAppService;
    @Autowired
    private IInventoryItemService inventoryItemService;
    @Autowired
    private IChargeItemService chargeItemService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ReceiptApprovalMapper receiptApprovalMapper;

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
            supplyItemDetailList = receiptApprovalMapper.selectSupplyMedDetail(busNo, EventStatus.COMPLETED.getValue());
        } else if (CommonConstants.TableName.ADM_DEVICE.equals(itemTable)) {
            supplyItemDetailList = receiptApprovalMapper.selectSupplyDevDetail(busNo, EventStatus.COMPLETED.getValue());
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
            return receiptApprovalMapper.selectChargeDetail(itemIdList);
        }
        return null;
    }

    /**
     * 审批通过
     *
     * @param busNo 单据号
     * @param request 请求数据
     * @return 操作结果
     */
    @Override
    public R<?> approved(String busNo, HttpServletRequest request) {
        // 获取登录者的信息
        LoginUser loginUser = tokenService.getLoginUser(request);
        // 获取当前时间
        Date now = DateUtils.getNowDate();

        // 审批单据并返回单据详情
        List<SupplyRequest> agreedList = supplyRequestService.agreeRequest(busNo, loginUser, now);
        if (agreedList.isEmpty()) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }
        // 根据单据，发放物品
        List<SupplyDelivery> deliveredList = supplyDeliveryService.createCompletedSupplyDelivery(agreedList, now);
        if (deliveredList.isEmpty()) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }

        // 获取供应项目所在表
        String itemTable = supplyRequestService.getItemTable(agreedList);
        // 获取供应的物品
        List<Long> itemIdList = supplyRequestService.getItem(agreedList);
        // 获取项目价格相关信息
        List<ItemChargeDetailDto> chargeDetailList = this.getItemChargeDetail(itemIdList);
        List<ChargeItemDefDetail> chargeItemDefDetailList = new ArrayList<>();
        if (!chargeDetailList.isEmpty()) {
            // 生成与之前定价不同的定价子表数据
            chargeItemDefDetailList = this.creatChargeItemDetail(agreedList, chargeDetailList);
        }
        if (!chargeItemDefDetailList.isEmpty()) {
            for (ChargeItemDefDetail chargeItemDefDetail : chargeItemDefDetailList) {
                // 增加项目定价子表数据
                chargeItemDefAppService.addChargeItemDefApp(chargeItemDefDetail);
            }
        }

        // 查询供应项目的详细信息
        List<SupplyItemDetailDto> supplyItemDetailList = this.getSupplyItemDetail(busNo, itemTable);
        if (!supplyItemDetailList.isEmpty()) {
            // 将供应项目的详细信息装配为库存项目和采购账单
            Pair<List<ChargeItem>, List<InventoryItem>> listPair =
                InventoryManageAssembler.assembleChargeAndInventory(supplyItemDetailList, now, loginUser);
            // 创建已结算的采购财务流水
            chargeItemService.createBilledPurchaseCharge(listPair.getLeft());
            // 入库
            inventoryItemService.stockIn(listPair.getRight());
        }
        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null));
    }

    /**
     * 审批驳回
     *
     * @param busNo 单据号
     * @param request 请求数据
     * @return 操作结果
     */
    @Override
    public R<?> reject(String busNo, HttpServletRequest request) {
        // 获取当前时间
        Date now = DateUtils.getNowDate();
        // 获取登录者的信息
        LoginUser loginUser = tokenService.getLoginUser(request);
        // 驳回单据
        boolean result = supplyRequestService.rejectRequest(busNo, loginUser, now);
        return result ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null))
            : R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 入库项价格验证
     *
     * @param agreedList 供应单据
     * @param chargeDetailList 项目价格
     * @return 价格定义子表数据
     */
    private List<ChargeItemDefDetail> creatChargeItemDetail(List<SupplyRequest> agreedList,
        List<ItemChargeDetailDto> chargeDetailList) {
        List<ChargeItemDefDetail> resultList = new ArrayList<>();

        // 将各个项目的定价信息按项目id分组
        Map<Long, List<ItemChargeDetailDto>> chargeDetailGroup =
            chargeDetailList.stream().collect(Collectors.groupingBy(ItemChargeDetailDto::getInstanceId));

        for (SupplyRequest supplyRequest : agreedList) {
            Long itemId = supplyRequest.getItemId();
            // 通过供应项目id匹配项目定价
            List<ItemChargeDetailDto> details = chargeDetailGroup.get(itemId);
            if (details == null)
                continue;

            // 生成请求的命中值
            String lotUnitCondition =
                String.format(CommonConstants.Common.COMMA, supplyRequest.getLotNumber(), supplyRequest.getUnitCode());

            for (ItemChargeDetailDto detail : details) {

                // 将字符串按逗号分割
                String[] parts = detail.getConditionValue().split(",", 2);

                // 判断是否有至少两部分，且单位部分与入库单位相等
                if (parts.length > 1 && parts[1].trim().equals(supplyRequest.getUnitCode())) {
                    // 判断请求中的命中值是否命中了定价的条件
                    boolean isConditionMatched = lotUnitCondition.equals(detail.getConditionValue());

                    // 如果未命中或命中价格不同，则新增数据
                    if (!isConditionMatched || supplyRequest.getPrice().compareTo(detail.getUnitPrice()) != 0) {
                        // 判断入库单位是大单位还是小单位
                        if (supplyRequest.getUnitCode().equals(detail.getUnitCode())) {
                            resultList.add(this.addChargeItemDefApp(
                                String.format(CommonConstants.Common.COMMA, supplyRequest.getLotNumber(),
                                    detail.getMinUnitCode()),
                                supplyRequest.getMinSellPrice(), detail.getDefinitionId()));
                            resultList.add(this.addChargeItemDefApp(lotUnitCondition, supplyRequest.getSellPrice(),
                                detail.getDefinitionId()));
                        } else {
                            resultList
                                .add(this.addChargeItemDefApp(
                                    String.format(CommonConstants.Common.COMMA, supplyRequest.getLotNumber(),
                                        detail.getUnitCode()),
                                    supplyRequest.getMinSellPrice(), detail.getDefinitionId()));
                            resultList.add(this.addChargeItemDefApp(lotUnitCondition, supplyRequest.getMinSellPrice(),
                                detail.getDefinitionId()));
                        }
                    }
                }
            }
        }
        return resultList;
    }

    /**
     * 添加项目定价子表信息
     * 
     * @param conditionValue 命中值
     * @param sellPrice 售价
     * @param definitionId 主表id
     * @return 子表信息
     */
    private ChargeItemDefDetail addChargeItemDefApp(String conditionValue, BigDecimal sellPrice, Long definitionId) {
        ChargeItemDefDetail chargeItemDefDetail = new ChargeItemDefDetail();
        // todo：命中条件或建成字典枚举，此处为批次号，单位
        chargeItemDefDetail
            // 命中值
            .setConditionValue(conditionValue)
            // 主表id
            .setDefinitionId(definitionId)
            // 售价
            .setAmount(sellPrice);
        return chargeItemDefDetail;
    }
}
