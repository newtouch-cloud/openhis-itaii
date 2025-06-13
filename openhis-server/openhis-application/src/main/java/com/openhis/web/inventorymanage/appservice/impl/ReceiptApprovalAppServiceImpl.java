/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.appservice.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.exception.ServiceException;
import com.core.common.utils.DateUtils;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.SecurityUtils;
import com.openhis.administration.domain.ChargeItemDefDetail;
import com.openhis.administration.service.IChargeItemDefDetailService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
import com.openhis.common.enums.ybenums.YbInvChgType;
import com.openhis.common.enums.ybenums.YbRxFlag;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.medication.domain.MedicationDispense;
import com.openhis.medication.service.IMedicationDispenseService;
import com.openhis.web.inventorymanage.appservice.IReceiptApprovalAppService;
import com.openhis.web.inventorymanage.assembler.InventoryManageAssembler;
import com.openhis.web.inventorymanage.dto.*;
import com.openhis.web.inventorymanage.mapper.ReceiptApprovalMapper;
import com.openhis.workflow.domain.DeviceDispense;
import com.openhis.workflow.domain.InventoryItem;
import com.openhis.workflow.domain.SupplyDelivery;
import com.openhis.workflow.domain.SupplyRequest;
import com.openhis.workflow.service.IDeviceDispenseService;
import com.openhis.workflow.service.IInventoryItemService;
import com.openhis.workflow.service.ISupplyDeliveryService;
import com.openhis.workflow.service.ISupplyRequestService;
import com.openhis.yb.dto.Medical3503Param;
import com.openhis.yb.dto.MedicalInventory3501Param;
import com.openhis.yb.dto.MedicalInventory3502Param;
import com.openhis.yb.dto.MedicalPurchase3504Param;
import com.openhis.yb.service.YbManager;

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
    private ReceiptApprovalMapper receiptApprovalMapper;
    @Autowired
    private YbManager ybService;
    @Autowired
    private IMedicationDispenseService medicationDispenseService;
    @Autowired
    private IDeviceDispenseService deviceDispenseService;
    @Autowired
    private IReceiptApprovalAppService receiptApprovalAppService;

    /**
     * 单据审批页面初始化
     *
     * @return 初始化信息
     */
    @Override
    public R<?> receiptApprovalInit() {
        ReceiptInitDto initDto = new ReceiptInitDto();
        // 单据类型
        List<ReceiptInitDto.supplyTypeOption> supplyTypeOptions = Stream.of(SupplyType.values())
            .map(supplyType -> new ReceiptInitDto.supplyTypeOption(supplyType.getValue(), supplyType.getInfo()))
            .collect(Collectors.toList());
        // 审批状态
        List<ReceiptInitDto.supplyStatusOption> supplyStatusOptions = Stream.of(SupplyStatus.values())
            .map(supplyStatus -> new ReceiptInitDto.supplyStatusOption(supplyStatus.getValue(), supplyStatus.getInfo()))
            .collect(Collectors.toList());
        initDto.setSupplyTypeOptions(supplyTypeOptions).setSupplyStatusOptions(supplyStatusOptions);

        return R.ok(initDto);
    }

    /**
     * 审批单据分页列表
     *
     * @param receiptSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 审批单据分页列表
     */
    @Override
    public R<?> getPage(ReceiptApprovalSearchParam receiptSearchParam, Integer pageNo, Integer pageSize,
        String searchKey, HttpServletRequest request) {
        // 设置模糊查询的字段名
        HashSet<String> searchFields = new HashSet<>();
        searchFields.add(CommonConstants.FieldName.SupplyBusNo);

        // 构建查询条件
        QueryWrapper<ReceiptApprovalSearchParam> queryWrapper =
            HisQueryUtils.buildQueryWrapper(receiptSearchParam, searchKey, searchFields, request);
        // 查询单据分页列表
        Page<ReceiptPageDto> receiptPage =
            receiptApprovalMapper.selectReceiptPage(new Page<>(pageNo, pageSize), queryWrapper,
                SupplyStatus.APPROVAL.getValue(), SupplyStatus.AGREE.getValue(), SupplyStatus.REJECT.getValue());

        receiptPage.getRecords().forEach(e -> {
            // 单据状态
            e.setStatusEnum_enumText(EnumUtils.getInfoByValue(SupplyStatus.class, e.getStatusEnum()));
            // 单据类型
            e.setTypeEnum_enumText(EnumUtils.getInfoByValue(SupplyType.class, e.getTypeEnum()));
        });
        return R.ok(receiptPage);
    }

    /**
     * 根据单据号获取供应单据及供应项相关详细信息
     *
     * @param busNo 单据号
     * @return 供应单据及供应项相关详细信息
     */
    @Override
    public List<SupplyItemDetailDto> getSupplyItemDetail(String busNo) {
        return receiptApprovalMapper.selectSupplyDetail(busNo, EventStatus.COMPLETED.getValue());
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
     * 入库单据审批通过
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @Override
    public R<?> purchaseInventoryApproved(String busNo) {
        // 获取当前时间
        Date now = DateUtils.getNowDate();
        // 审批单据并返回单据详情
        List<SupplyRequest> agreedList = supplyRequestService.agreeRequest(busNo, now);
        if (agreedList.isEmpty()) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }
        // 获取审批通过后的供应请求id列表
        List<Long> supplyReqIdList = agreedList.stream().map(SupplyRequest::getId).collect(Collectors.toList());

        // 校验(已经审批通过的单号(发放状态是已完成),不能再重复审批通过)
        boolean validation = supplyDeliveryService.supplyDeliveryValidation(supplyReqIdList);
        if (validation) {
            throw new ServiceException("请勿重复审批");
        }

        // 根据单据，发放物品
        List<SupplyDelivery> deliveredList = supplyDeliveryService.createCompletedSupplyDelivery(agreedList, now);
        if (deliveredList.isEmpty()) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }

        // 获取供应的物品
        List<Long> itemIdList = supplyRequestService.getItem(agreedList);
        // 获取项目价格相关信息
        List<ItemChargeDetailDto> chargeDetailList = this.getItemChargeDetail(itemIdList);
        List<ChargeItemDefDetail> chargeItemDefDetailList;
        if (!chargeDetailList.isEmpty()) {
            // 生成与之前定价不同的定价子表数据
            chargeItemDefDetailList = this.creatChargeItemDetail(agreedList, chargeDetailList);
            if (!chargeItemDefDetailList.isEmpty()) {
                for (ChargeItemDefDetail chargeItemDefDetail : chargeItemDefDetailList) {
                    // 增加项目定价子表数据
                    chargeItemDefAppService.addChargeItemDefApp(chargeItemDefDetail);
                }
            }
        }
        // 查询供应项目的详细信息
        List<SupplyItemDetailDto> supplyItemDetailList = this.getSupplyItemDetail(busNo);
        // 返回信息
        String returnMsg = null;
        if (!supplyItemDetailList.isEmpty()) {
            InventoryItem inventoryItemPurpose = null;
            // 新增库存信息
            List<SupplyItemDetailDto> supplyList = new ArrayList<>();

            for (SupplyItemDetailDto supplyItemDetailDto : supplyItemDetailList) {

                // 根据项目id,产品批号，仓库id 查询仓库库存表信息
                List<InventoryItem> inventoryItemList = inventoryItemService.selectInventoryByItemId(
                    supplyItemDetailDto.getItemId(), supplyItemDetailDto.getLotNumber(),
                    supplyItemDetailDto.getPurposeLocationId(), SecurityUtils.getLoginUser().getTenantId());

                if (!inventoryItemList.isEmpty()) {
                    inventoryItemPurpose = inventoryItemList.get(0);
                }

                if (inventoryItemPurpose == null) {
                    // 新增库存信息
                    supplyList.add(supplyItemDetailDto);
                } else {
                    // 包装数量（常规单位库存数量） 更新库存数量方法中没用到
                    BigDecimal baseQuantity = inventoryItemPurpose.getQuantity();
                    // 最小数量（最小单位库存数量）
                    BigDecimal minQuantity = inventoryItemPurpose.getQuantity();

                    // 计算盘点后库存数量，结果取小单位
                    // 供应申请的物品计量单位与包装单位相同
                    if (supplyItemDetailDto.getItemUnit().equals(supplyItemDetailDto.getUnitCode())) {
                        // 源仓库库存+(调拨数量*拆零比)
                        minQuantity = minQuantity
                            .add(supplyItemDetailDto.getPartPercent().multiply(supplyItemDetailDto.getItemQuantity()));

                    } else if (supplyItemDetailDto.getItemUnit().equals(supplyItemDetailDto.getMinUnitCode())) {
                        // 供应申请的物品计量单位与最小单位相同
                        // 源仓库库存+调拨数量
                        minQuantity = minQuantity.add(supplyItemDetailDto.getItemQuantity());
                    }
                    // 更新源仓库库存数量
                    Boolean aBoolean = inventoryItemService.updateInventoryQuantity(inventoryItemPurpose.getId(),
                        baseQuantity, minQuantity, now);

                    if (!aBoolean) {
                        return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
                    }
                }
            }

            if (!supplyList.isEmpty()) {
                // 将供应项目的详细信息装配为库存项目
                List<InventoryItem> inventoryItemList = InventoryManageAssembler.assembleInventoryItem(supplyList);
                // 入库
                inventoryItemService.stockIn(inventoryItemList);
            }

            // 调用医保商品采购接口
            String ybSwitch = SecurityUtils.getLoginUser().getOptionJson().getString(CommonConstants.Option.YB_SWITCH); // 医保开关
            if (Whether.YES.getCode().equals(ybSwitch)) {
                List<String> uploadFailedNoList =
                    this.ybInventoryIntegrated(supplyItemDetailList, YbInvChgType.PURCHASE_IN, false, now);
                if (uploadFailedNoList != null && !uploadFailedNoList.isEmpty()) {
                    returnMsg = "3503商品采购上传错误，错误项目编码" + uploadFailedNoList;
                } else {
                    returnMsg = "3503商品采购上传成功";
                }
            }
        }
        return R.ok(returnMsg, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null));
    }

    /**
     * 商品盘点审批通过
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @Override
    public R<?> productStocktakingApproved(String busNo) {
        // 获取当前时间
        Date now = DateUtils.getNowDate();
        // 审批单据并返回单据详情
        List<SupplyRequest> agreedList = supplyRequestService.agreeRequest(busNo, now);
        if (agreedList.isEmpty()) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }
        // 根据单据，生成供应发放单
        List<SupplyDelivery> deliveredList = supplyDeliveryService.createCompletedSupplyDelivery(agreedList, now);
        if (deliveredList.isEmpty()) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }

        // 查询供应项目的详细信息
        List<SupplyItemDetailDto> supplyItemDetailList = this.getSupplyItemDetail(busNo);
        // 首次盘存列表
        List<SupplyItemDetailDto> firstSupplyItemDetailList = new ArrayList<>();
        // 药品/耗材发放列表
        List<MedicationDispense> medicationDispenses = null;
        List<DeviceDispense> deviceDispenses = null;
        // 返回信息
        String stocktakingReturnMsg = null;
        String changeReturnMsg = null;
        if (!supplyItemDetailList.isEmpty()) {
            // 获取盘盈列表（itemQuantity > 0）
            List<SupplyItemDetailDto> positiveList = supplyItemDetailList.stream()
                .filter(item -> item.getItemQuantity() != null && item.getItemQuantity().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toList());
            // 获取盘亏列表（itemQuantity < 0）
            List<SupplyItemDetailDto> negativeList = supplyItemDetailList.stream()
                .filter(item -> item.getItemQuantity() != null && item.getItemQuantity().compareTo(BigDecimal.ZERO) < 0)
                .collect(Collectors.toList());

            for (SupplyItemDetailDto supplyItemDetailDto : supplyItemDetailList) {
                // 根据id，产品批号，仓库 查询库存表信息
                List<InventoryItem> inventoryItemList = inventoryItemService.selectInventoryByItemId(
                    supplyItemDetailDto.getItemId(), supplyItemDetailDto.getLotNumber(),
                    supplyItemDetailDto.getPurposeLocationId(), SecurityUtils.getLoginUser().getTenantId());
                // 查看该批号的药品/耗材是否发放过(用于证明是否首次盘存)
                if (CommonConstants.TableName.MED_MEDICATION_DEFINITION.equals(supplyItemDetailDto.getItemTable())) {
                    medicationDispenses = medicationDispenseService.list(new LambdaQueryWrapper<MedicationDispense>()
                        .eq(MedicationDispense::getLotNumber, supplyItemDetailDto.getLotNumber()));
                    if (medicationDispenses.isEmpty()) {
                        firstSupplyItemDetailList.add(supplyItemDetailDto);
                    }
                } else if (CommonConstants.TableName.ADM_DEVICE_DEFINITION.equals(supplyItemDetailDto.getItemTable())) {
                    deviceDispenses = deviceDispenseService.list(new LambdaQueryWrapper<DeviceDispense>()
                        .in(DeviceDispense::getLotNumber, supplyItemDetailDto.getLotNumber()));
                    if (deviceDispenses.isEmpty()) {
                        firstSupplyItemDetailList.add(supplyItemDetailDto);
                    }
                }
                if (!inventoryItemList.isEmpty()) {
                    InventoryItem inventoryItem = inventoryItemList.get(0);
                    // 包装数量（常规单位库存数量） 更新库存数量方法中没用到
                    BigDecimal baseQuantity = inventoryItem.getQuantity();
                    // 最小数量（最小单位库存数量）
                    BigDecimal minQuantity = inventoryItem.getQuantity();

                    // 计算盘点后库存数量，结果取小单位
                    // 供应申请的物品计量单位与包装单位相同
                    if (supplyItemDetailDto.getItemUnit().equals(supplyItemDetailDto.getUnitCode())) {
                        minQuantity = minQuantity
                            .add(supplyItemDetailDto.getPartPercent().multiply(supplyItemDetailDto.getItemQuantity()));
                        // 供应申请的物品计量单位与最小单位相同
                    } else if (supplyItemDetailDto.getItemUnit().equals(supplyItemDetailDto.getMinUnitCode())) {
                        minQuantity = minQuantity.add(supplyItemDetailDto.getItemQuantity());
                    }
                    // 更新库存数量
                    Boolean aBoolean = inventoryItemService.updateInventoryQuantity(inventoryItem.getId(), baseQuantity,
                        minQuantity, now);
                    if (!aBoolean) {
                        return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
                    }
                }

                String ybSwitch =
                    SecurityUtils.getLoginUser().getOptionJson().getString(CommonConstants.Option.YB_SWITCH); // 医保开关
                if (Whether.YES.getCode().equals(ybSwitch)) {
                    // 如果首次盘点信息不为空
                    if (!firstSupplyItemDetailList.isEmpty()) {
                        // 调用医保盘存接口，盘盈
                        List<String> uploadFailedNoList = this.ybInventoryIntegrated(firstSupplyItemDetailList,
                            YbInvChgType.INVENTORY_GAIN, true, now);
                        if (uploadFailedNoList != null && !uploadFailedNoList.isEmpty()) {
                            stocktakingReturnMsg = "3501盘存上传错误，错误项目编码" + uploadFailedNoList;
                        }
                    }
                    List<String> uploadFailedGainList = null;
                    List<String> uploadFailedLossList = null;
                    if (!positiveList.isEmpty()) {
                        // 调用医保库存变更接口，盘盈
                        uploadFailedGainList =
                            this.ybInventoryIntegrated(positiveList, YbInvChgType.INVENTORY_GAIN, false, now);
                    }
                    if (!negativeList.isEmpty()) {
                        // 调用医保库存变更接口，盘亏
                        uploadFailedLossList =
                            this.ybInventoryIntegrated(negativeList, YbInvChgType.INVENTORY_LOSS, false, now);
                    }
                    if (uploadFailedGainList != null || uploadFailedLossList != null) {
                        changeReturnMsg = "3502库存变更上传错误，错误项目编码" + uploadFailedGainList + uploadFailedLossList;
                    }
                }
            }
        }
        return R.ok(stocktakingReturnMsg + changeReturnMsg,
            MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null));
    }

    /**
     * 商品调拨审批通过
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @Override
    public R<?> productTransferApproved(String busNo) {

        // 获取当前时间
        Date now = DateUtils.getNowDate();

        // 审批单据并返回单据详情
        List<SupplyRequest> agreedList = supplyRequestService.agreeRequest(busNo, now);
        if (agreedList.isEmpty()) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }
        // 根据单据，发放物品
        List<SupplyDelivery> deliveredList = supplyDeliveryService.createCompletedSupplyDelivery(agreedList, now);
        if (deliveredList.isEmpty()) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }

        // 查询供应项目的详细信息
        List<SupplyItemDetailDto> supplyItemDetailList = this.getSupplyItemDetail(busNo);

        if (!supplyItemDetailList.isEmpty()) {

            // 新增库存信息
            List<SupplyItemDetailDto> supplylList = new ArrayList<>();

            for (SupplyItemDetailDto supplyItemDetailDto : supplyItemDetailList) {

                // 根据项目id,产品批号，源仓库id 查询源仓库库存表信息
                List<InventoryItem> inventoryItemList = inventoryItemService.selectInventoryByItemId(
                    supplyItemDetailDto.getItemId(), supplyItemDetailDto.getLotNumber(),
                    supplyItemDetailDto.getSourceLocationId(), SecurityUtils.getLoginUser().getTenantId());
                InventoryItem inventoryItemSource = new InventoryItem();
                if (!inventoryItemList.isEmpty()) {
                    inventoryItemSource = inventoryItemList.get(0);
                    // 包装数量（常规单位库存数量） 更新库存数量方法中没用到
                    BigDecimal baseQuantity = inventoryItemSource.getQuantity();
                    // 最小数量（最小单位库存数量）
                    BigDecimal minQuantity = inventoryItemSource.getQuantity();

                    // 计算盘点后库存数量，结果取小单位
                    // 供应申请的物品计量单位与包装单位相同
                    if (supplyItemDetailDto.getItemUnit().equals(supplyItemDetailDto.getUnitCode())) {
                        if (minQuantity.compareTo(supplyItemDetailDto.getItemQuantity()) < 0) {
                            // 库存数量不足
                            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Inventory.M00002, null));
                        } else {
                            // 源仓库库存-(调拨数量*拆零比)
                            minQuantity = minQuantity.subtract(
                                supplyItemDetailDto.getPartPercent().multiply(supplyItemDetailDto.getItemQuantity()));
                        }
                    } else if (supplyItemDetailDto.getItemUnit().equals(supplyItemDetailDto.getMinUnitCode())) {
                        if (minQuantity.compareTo(supplyItemDetailDto.getItemQuantity()) < 0) {
                            // 库存数量不足
                            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Inventory.M00002, null));
                        } else {
                            // 供应申请的物品计量单位与最小单位相同
                            // 源仓库库存-调拨数量
                            minQuantity = minQuantity.subtract(supplyItemDetailDto.getItemQuantity());
                        }
                    }
                    // 更新源仓库库存数量
                    Boolean aBoolean = inventoryItemService.updateInventoryQuantity(inventoryItemSource.getId(),
                        baseQuantity, minQuantity, now);
                    if (!aBoolean) {
                        return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
                    }
                }

                // 根据项目id,产品批号，目的仓库id 查询目的仓库库存表信息
                List<InventoryItem> inventoryItemPurposeList = inventoryItemService.selectInventoryByItemId(
                    supplyItemDetailDto.getItemId(), supplyItemDetailDto.getLotNumber(),
                    supplyItemDetailDto.getPurposeLocationId(), SecurityUtils.getLoginUser().getTenantId());
                InventoryItem inventoryItemPurpose = null;
                if (!inventoryItemPurposeList.isEmpty()) {
                    inventoryItemPurpose = inventoryItemPurposeList.get(0);
                }

                if (inventoryItemPurpose == null) {
                    // 供应申请的物品计量单位与最小单位相同
                    if (supplyItemDetailDto.getItemUnit().equals(supplyItemDetailDto.getMinUnitCode())) {
                        // 采购单价=单价*拆零比
                        supplyItemDetailDto
                            .setPrice(supplyItemDetailDto.getPrice().multiply(supplyItemDetailDto.getPartPercent()));
                    }
                    // 新增库存信息
                    supplylList.add(supplyItemDetailDto);
                } else {
                    // 包装数量（常规单位库存数量） 更新库存数量方法中没用到
                    BigDecimal baseQuantity = inventoryItemPurpose.getQuantity();
                    // 最小数量（最小单位库存数量）
                    BigDecimal minQuantity = inventoryItemPurpose.getQuantity();

                    // 计算盘点后库存数量，结果取小单位
                    // 供应申请的物品计量单位与包装单位相同
                    if (supplyItemDetailDto.getItemUnit().equals(supplyItemDetailDto.getUnitCode())) {
                        // 源仓库库存+(调拨数量*拆零比)
                        minQuantity = minQuantity
                            .add(supplyItemDetailDto.getPartPercent().multiply(supplyItemDetailDto.getItemQuantity()));
                    } else if (supplyItemDetailDto.getItemUnit().equals(supplyItemDetailDto.getMinUnitCode())) {
                        // 供应申请的物品计量单位与最小单位相同
                        // 源仓库库存+调拨数量
                        minQuantity = minQuantity.add(supplyItemDetailDto.getItemQuantity());
                    }
                    // 更新目的仓库库存数量
                    Boolean bBoolean = inventoryItemService.updateInventoryQuantity(inventoryItemPurpose.getId(),
                        baseQuantity, minQuantity, now);

                    if (!bBoolean) {
                        return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
                    }
                }
            }

            if (!supplylList.isEmpty()) {
                // 将供应项目的详细信息装配为库存项目
                List<InventoryItem> inventoryItemList = InventoryManageAssembler.assembleInventoryItem(supplylList);
                // 入库
                inventoryItemService.stockIn(inventoryItemList);
            }
        }
        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null));

    }

    /**
     * 采购退货审批通过
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @Override
    public R<?> purchaseReturnApproved(String busNo) {
        // 获取当前时间
        Date now = DateUtils.getNowDate();
        // 审批单据并返回单据详情
        List<SupplyRequest> agreedList = supplyRequestService.agreeRequest(busNo, now);
        if (agreedList.isEmpty()) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }

        // 根据单据，发放物品
        List<SupplyDelivery> deliveredList = supplyDeliveryService.createCompletedSupplyDelivery(agreedList, now);
        if (deliveredList.isEmpty()) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }

        // 查询供应项目的详细信息
        List<SupplyItemDetailDto> supplyItemDetailList = this.getSupplyItemDetail(busNo);
        // 返回信息
        String returnMsg = null;
        if (!supplyItemDetailList.isEmpty()) {
            for (SupplyItemDetailDto supplyItemDetailDto : supplyItemDetailList) {

                // 根据项目id,产品批号，目的仓库id 查询仓库库存表信息
                List<InventoryItem> inventoryItemList = inventoryItemService.selectInventoryByItemId(
                    supplyItemDetailDto.getItemId(), supplyItemDetailDto.getLotNumber(),
                    supplyItemDetailDto.getPurposeLocationId(), SecurityUtils.getLoginUser().getTenantId());
                InventoryItem inventoryItemSource;
                if (!inventoryItemList.isEmpty()) {
                    inventoryItemSource = inventoryItemList.get(0);

                    // 包装数量（常规单位库存数量） 更新库存数量方法中没用到
                    BigDecimal baseQuantity = inventoryItemSource.getQuantity();
                    // 最小数量（最小单位库存数量）
                    BigDecimal minQuantity = inventoryItemSource.getQuantity();

                    // 计算退货后库存数量，结果取小单位
                    // 供应申请的物品计量单位与包装单位相同
                    if (supplyItemDetailDto.getItemUnit().equals(supplyItemDetailDto.getUnitCode())) {
                        if (minQuantity.compareTo(supplyItemDetailDto.getItemQuantity()) < 0) {
                            // 库存数量不足
                            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Inventory.M00002, null));
                        } else {
                            // 仓库库存-(退货数量*拆零比)
                            minQuantity = minQuantity.subtract(
                                supplyItemDetailDto.getPartPercent().multiply(supplyItemDetailDto.getItemQuantity()));
                        }
                    } else if (supplyItemDetailDto.getItemUnit().equals(supplyItemDetailDto.getMinUnitCode())) {
                        if (minQuantity.compareTo(supplyItemDetailDto.getItemQuantity()) < 0) {
                            // 库存数量不足
                            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Inventory.M00002, null));
                        } else {
                            // 供应申请的物品计量单位与最小单位相同
                            // 仓库库存-退货数量
                            minQuantity = minQuantity.subtract(supplyItemDetailDto.getItemQuantity());
                        }
                    }

                    // 更新仓库库存数量
                    Boolean aBoolean = inventoryItemService.updateInventoryQuantity(inventoryItemSource.getId(),
                        baseQuantity, minQuantity, now);

                    if (!aBoolean) {
                        return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
                    }

                    // 退货数量等于库存数量时，删除库存数据
                    if (minQuantity.compareTo(BigDecimal.ZERO) == 0) {
                        inventoryItemService.removeById(inventoryItemSource.getId());
                    }
                }
            }

            // 调用医保采购退货接口
            String ybSwitch = SecurityUtils.getLoginUser().getOptionJson().getString(CommonConstants.Option.YB_SWITCH); // 医保开关
            if (Whether.YES.getCode().equals(ybSwitch)) {
                List<String> uploadFailedNoList =
                    this.ybInventoryIntegrated(supplyItemDetailList, YbInvChgType.RETURN_OUT, false, now);
                if (uploadFailedNoList != null && !uploadFailedNoList.isEmpty()) {
                    returnMsg = "3504采购退货上传错误，错误项目编码" + uploadFailedNoList;
                }
            }
        }
        return R.ok(returnMsg, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null));
    }

    /**
     * 报损单审批通过
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @Override
    public R<?> lossReportApproved(String busNo) {
        // 获取当前时间
        Date now = DateUtils.getNowDate();
        // 审批单据并返回单据详情
        List<SupplyRequest> agreedList = supplyRequestService.agreeRequest(busNo, now);
        if (agreedList.isEmpty()) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }
        // 根据单据，生成供应发放单
        List<SupplyDelivery> deliveredList = supplyDeliveryService.createCompletedSupplyDelivery(agreedList, now);
        if (deliveredList.isEmpty()) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }
        // 返回信息
        String returnMsg = null;
        // 查询供应项目的详细信息
        List<SupplyItemDetailDto> supplyItemDetailList = this.getSupplyItemDetail(busNo);
        if (!supplyItemDetailList.isEmpty()) {
            for (SupplyItemDetailDto supplyItemDetailDto : supplyItemDetailList) {
                // 根据id，产品批号，仓库 查询库存表信息
                List<InventoryItem> inventoryItemList = inventoryItemService.selectInventoryByItemId(
                    supplyItemDetailDto.getItemId(), supplyItemDetailDto.getLotNumber(),
                    supplyItemDetailDto.getPurposeLocationId(), SecurityUtils.getLoginUser().getTenantId());
                InventoryItem inventoryItem = new InventoryItem();
                if (!inventoryItemList.isEmpty()) {
                    inventoryItem = inventoryItemList.get(0);

                    // 包装数量（常规单位库存数量） 更新库存数量方法中没用到
                    BigDecimal baseQuantity = inventoryItem.getQuantity();
                    // 最小数量（最小单位库存数量）
                    BigDecimal minQuantity = inventoryItem.getQuantity();

                    // 计算报损后库存数量，结果取小单位
                    // 供应申请的物品计量单位与包装单位相同
                    if (supplyItemDetailDto.getItemUnit().equals(supplyItemDetailDto.getUnitCode())) {
                        if (minQuantity.compareTo(supplyItemDetailDto.getItemQuantity()) < 0) {
                            // 库存数量不足
                            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Inventory.M00002, null));
                        } else {
                            // 目的仓库库存-(报损数量*拆零比)
                            minQuantity = minQuantity.subtract(
                                supplyItemDetailDto.getPartPercent().multiply(supplyItemDetailDto.getItemQuantity()));
                        }
                    } else if (supplyItemDetailDto.getItemUnit().equals(supplyItemDetailDto.getMinUnitCode())) {
                        if (minQuantity.compareTo(supplyItemDetailDto.getItemQuantity()) < 0) {
                            // 库存数量不足
                            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Inventory.M00002, null));
                        } else {
                            // 供应申请的物品计量单位与最小单位相同
                            // 目的仓库库存-报损数量
                            minQuantity = minQuantity.subtract(supplyItemDetailDto.getItemQuantity());
                        }
                    }
                    // 更新库存数量
                    Boolean aBoolean = inventoryItemService.updateInventoryQuantity(inventoryItem.getId(), baseQuantity,
                        minQuantity, now);
                    if (!aBoolean) {
                        return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
                    }
                }
            }
            // 调用医保库存变更接口
            String ybSwitch = SecurityUtils.getLoginUser().getOptionJson().getString(CommonConstants.Option.YB_SWITCH); // 医保开关
            if (Whether.YES.getCode().equals(ybSwitch)) {
                List<String> uploadFailedNoList =
                    this.ybInventoryIntegrated(supplyItemDetailList, YbInvChgType.DESTRUCTION, false, now);
                if (uploadFailedNoList != null && !uploadFailedNoList.isEmpty()) {
                    returnMsg = "3502库存变更上传错误，错误项目编码" + uploadFailedNoList;
                }
            }
        }
        return R.ok(returnMsg, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null));
    }

    /**
     * 领用出库审批通过
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @Override
    public R<?> requisitionIssueApproved(String busNo) {
        // 获取当前时间
        Date now = DateUtils.getNowDate();
        // 审批单据并返回单据详情
        List<SupplyRequest> agreedList = supplyRequestService.agreeRequest(busNo, now);
        if (agreedList.isEmpty()) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }
        // 根据单据，发放物品
        List<SupplyDelivery> deliveredList = supplyDeliveryService.createCompletedSupplyDelivery(agreedList, now);
        if (deliveredList.isEmpty()) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }

        // 查询供应项目的详细信息
        List<SupplyItemDetailDto> supplyItemDetailList = this.getSupplyItemDetail(busNo);

        if (!supplyItemDetailList.isEmpty()) {

            for (SupplyItemDetailDto supplyItemDetailDto : supplyItemDetailList) {

                // 根据项目id,产品批号，源仓库id 查询源仓库库存表信息
                List<InventoryItem> inventoryItemList = inventoryItemService.selectInventoryByItemId(
                    supplyItemDetailDto.getItemId(), supplyItemDetailDto.getLotNumber(),
                    supplyItemDetailDto.getSourceLocationId(), SecurityUtils.getLoginUser().getTenantId());
                InventoryItem inventoryItemSource = new InventoryItem();
                if (!inventoryItemList.isEmpty()) {
                    inventoryItemSource = inventoryItemList.get(0);

                    // 包装数量（常规单位库存数量） 更新库存数量方法中没用到
                    BigDecimal baseQuantity = inventoryItemSource.getQuantity();
                    // 最小数量（最小单位库存数量）
                    BigDecimal minQuantity = inventoryItemSource.getQuantity();

                    // 供应申请的物品计量单位与包装单位相同
                    if (supplyItemDetailDto.getItemUnit().equals(supplyItemDetailDto.getUnitCode())) {
                        if (minQuantity.compareTo(supplyItemDetailDto.getItemQuantity()) < 0) {
                            // 库存数量不足
                            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Inventory.M00002, null));
                        } else {
                            // 源仓库库存-(领用数量*拆零比)
                            minQuantity = minQuantity.subtract(
                                supplyItemDetailDto.getPartPercent().multiply(supplyItemDetailDto.getItemQuantity()));
                        }
                    } else if (supplyItemDetailDto.getItemUnit().equals(supplyItemDetailDto.getMinUnitCode())) {
                        if (minQuantity.compareTo(supplyItemDetailDto.getItemQuantity()) < 0) {
                            // 库存数量不足
                            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Inventory.M00002, null));
                        } else {
                            // 供应申请的物品计量单位与最小单位相同
                            // 源仓库库存-领用数量
                            minQuantity = minQuantity.subtract(supplyItemDetailDto.getItemQuantity());
                        }
                    }
                    // 更新源仓库库存数量
                    Boolean aBoolean = inventoryItemService.updateInventoryQuantity(inventoryItemSource.getId(),
                        baseQuantity, minQuantity, now);

                    if (!aBoolean) {
                        return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
                    }
                }
            }
        }
        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null));

    }

    /**
     * 领用退库审批通过
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @Override
    public R<?> returnIssueApproved(String busNo) {
        // 获取当前时间
        Date now = DateUtils.getNowDate();
        // 审批单据并返回单据详情
        List<SupplyRequest> agreedList = supplyRequestService.agreeRequest(busNo, now);
        if (agreedList.isEmpty()) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }
        // 根据单据，发放物品
        List<SupplyDelivery> deliveredList = supplyDeliveryService.createCompletedSupplyDelivery(agreedList, now);
        if (deliveredList.isEmpty()) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }

        // 查询供应项目的详细信息
        List<SupplyItemDetailDto> supplyItemDetailList = this.getSupplyItemDetail(busNo);

        if (!supplyItemDetailList.isEmpty()) {

            // 新增库存信息
            List<SupplyItemDetailDto> supplylList = new ArrayList<>();

            for (SupplyItemDetailDto supplyItemDetailDto : supplyItemDetailList) {

                // 根据项目id,产品批号，目的仓库id 查询目的仓库库存表信息
                List<InventoryItem> inventoryItemList = inventoryItemService.selectInventoryByItemId(
                    supplyItemDetailDto.getItemId(), supplyItemDetailDto.getLotNumber(),
                    supplyItemDetailDto.getPurposeLocationId(), SecurityUtils.getLoginUser().getTenantId());
                InventoryItem inventoryItemPurpose = null;
                if (!inventoryItemList.isEmpty()) {
                    inventoryItemPurpose = inventoryItemList.get(0);
                }

                if (inventoryItemPurpose == null) {
                    // 新增库存信息
                    supplylList.add(supplyItemDetailDto);
                } else {
                    // 包装数量（常规单位库存数量） 更新库存数量方法中没用到
                    BigDecimal baseQuantity = inventoryItemPurpose.getQuantity();
                    // 最小数量（最小单位库存数量）
                    BigDecimal minQuantity = inventoryItemPurpose.getQuantity();

                    // 计算盘点后库存数量，结果取小单位
                    // 供应申请的物品计量单位与包装单位相同
                    if (supplyItemDetailDto.getItemUnit().equals(supplyItemDetailDto.getUnitCode())) {
                        // 源仓库库存+(退库数量*拆零比)
                        minQuantity = minQuantity
                            .add(supplyItemDetailDto.getPartPercent().multiply(supplyItemDetailDto.getItemQuantity()));
                    } else if (supplyItemDetailDto.getItemUnit().equals(supplyItemDetailDto.getMinUnitCode())) {
                        // 供应申请的物品计量单位与最小单位相同
                        // 源仓库库存+退库数量
                        minQuantity = minQuantity.add(supplyItemDetailDto.getItemQuantity());
                    }
                    // 更新目的仓库库存数量
                    Boolean bBoolean = inventoryItemService.updateInventoryQuantity(inventoryItemPurpose.getId(),
                        baseQuantity, minQuantity, now);
                    if (!bBoolean) {
                        return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
                    }
                }
            }

            if (!supplylList.isEmpty()) {
                // 将供应项目的详细信息装配为库存项目
                List<InventoryItem> inventoryItemList = InventoryManageAssembler.assembleInventoryItem(supplylList);
                // 入库
                inventoryItemService.stockIn(inventoryItemList);
            }

        }
        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null));

    }

    /**
     * 审批驳回
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @Override
    public R<?> reject(String busNo) {
        // 驳回单据
        boolean result = supplyRequestService.rejectRequest(busNo);
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

            boolean shouldSkip = false;
            for (ItemChargeDetailDto detail : details) {
                // 如果入库项的批次与定价中的批次相同
                if (detail.getConditionValue().equals(supplyRequest.getLotNumber())) {
                    // 无论价格是否相同都跳过这条记录
                    shouldSkip = true;
                    break;
                }
            }
            if (shouldSkip) {
                continue; // 跳过当前supplyRequest，不加入结果集
            }
            // 新增对应项目的价格定义子表
            resultList.add(this.addChargeItemDefApp(supplyRequest.getLotNumber(), supplyRequest.getPrice(),
                details.get(0).getDefinitionId()));
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
        chargeItemDefDetail
            // 命中条件：批号
            .setConditionCode(ConditionCode.LOT_NUMBER.getCode())
            // 命中值
            .setConditionValue(conditionValue)
            // 主表id
            .setDefinitionId(definitionId)
            // 售价
            .setAmount(sellPrice);
        return chargeItemDefDetail;
    }

    /**
     * 医保库存相关进销存接口
     *
     * @param supplyItemDetailList 供应申请项目详细信息
     * @param ybInvChgType 库存变更类型
     * @param firstFlag 首次盘存标识
     * @param now 库存变更时间
     * @return 上传失败的id集合
     */
    // @Async
    // public List<String> ybInventoryIntegrated(List<SupplyItemDetailDto> supplyItemDetailList,
    // YbInvChgType ybInvChgType, Boolean firstFlag, Date now) {
    // List<String> uploadFailedNoList = new ArrayList<>();
    // R<?> result;
    // R<?> firstResult = R.ok();
    // for (SupplyItemDetailDto supplyItemDetailDto : supplyItemDetailList) {
    // switch (ybInvChgType) {
    // case ALLOCATION_IN:
    // case ALLOCATION_OUT:
    // case INVENTORY_GAIN:
    // case INVENTORY_LOSS:
    // case DESTRUCTION:
    // case OTHER_IN:
    // case OTHER_OUT:
    // case DONATION_IN:
    // case DONATION_RETURN_OUT:
    // if (firstFlag) {
    // firstResult = ybService.uploadInventoryCount(supplyItemDetailDto, now);
    // }
    // result = ybService.updateInventoryCount(supplyItemDetailDto, now, ybInvChgType.getDescription());
    // if (result.getCode() != 200 || firstResult.getCode() != 200) {
    // uploadFailedNoList.add(supplyItemDetailDto.getItemBusNo());
    // }
    // break;
    // case PURCHASE_IN:
    // result = ybService.procurement(supplyItemDetailDto, now);
    // if (result.getCode() != 200) {
    // uploadFailedNoList.add(supplyItemDetailDto.getItemBusNo());
    // }
    // case RETURN_OUT:
    // result = ybService.cancelProcurement(supplyItemDetailDto, now);
    // if (result.getCode() != 200) {
    // uploadFailedNoList.add(supplyItemDetailDto.getItemBusNo());
    // }
    // break;
    // default:
    // throw new IllegalArgumentException("未知的库存变更类型: " + ybInvChgType);
    // }
    // }
    // return CompletableFuture.completedFuture(uploadFailedNoList);
    // }

    /**
     * 医保库存相关进销存接口
     *
     * @param supplyItemDetailList 供应申请项目详细信息
     * @param ybInvChgType 库存变更类型
     * @param firstFlag 首次盘存标识
     * @param now 库存变更时间
     * @return 上传失败的id集合
     */
    private List<String> ybInventoryIntegrated(List<SupplyItemDetailDto> supplyItemDetailList,
        YbInvChgType ybInvChgType, Boolean firstFlag, Date now) {
        List<String> uploadFailedNoList = new ArrayList<>();
        R<?> result;
        R<?> firstResult = R.ok();
        for (SupplyItemDetailDto supplyItemDetailDto : supplyItemDetailList) {
            if (supplyItemDetailDto.getYbNo() == null) {
                continue;
            }
            switch (ybInvChgType) {
                case ALLOCATION_IN:
                case ALLOCATION_OUT:
                case INVENTORY_GAIN:
                case INVENTORY_LOSS:
                case DESTRUCTION:
                case OTHER_IN:
                case OTHER_OUT:
                case DONATION_IN:
                case DONATION_RETURN_OUT:
                    if (firstFlag) {
                        firstResult =
                            ybService.uploadInventoryCount(getMedicalInventory3501Param(supplyItemDetailDto, now), now);
                    }
                    result = ybService.updateInventoryCount(
                        getMedicalInventory3502Param(supplyItemDetailDto, now, ybInvChgType.getValue()));
                    if (result.getCode() != 200 || firstResult.getCode() != 200) {
                        uploadFailedNoList.add(supplyItemDetailDto.getItemBusNo());
                    }
                    break;
                case PURCHASE_IN:
                    result = ybService.procurement(getMedical3503Param(supplyItemDetailDto, now));
                    if (result.getCode() != 200) {
                        uploadFailedNoList.add(supplyItemDetailDto.getItemBusNo());
                    }
                    break;
                case RETURN_OUT:
                    result = ybService.cancelProcurement(getMedicalPurchase3504Param(supplyItemDetailDto, now));
                    if (result.getCode() != 200) {
                        uploadFailedNoList.add(supplyItemDetailDto.getItemBusNo());
                    }
                    break;
                default:
                    throw new IllegalArgumentException("未知的库存变更类型: " + ybInvChgType);
            }
        }
        return uploadFailedNoList;
    }

    private MedicalPurchase3504Param getMedicalPurchase3504Param(SupplyItemDetailDto supplyItemDetailDto, Date now) {
        MedicalPurchase3504Param medicalPurchase3504Param = new MedicalPurchase3504Param();
        medicalPurchase3504Param.setMedListCodg(supplyItemDetailDto.getYbNo())
            .setFixmedinsBchno(supplyItemDetailDto.getLotNumber())
            .setFixmedinsHilistId(supplyItemDetailDto.getItemBusNo())
            .setFixmedinsHilistName(supplyItemDetailDto.getItemTable())
            .setSplerName(supplyItemDetailDto.getSupplierName()).setPurcInvoNo(supplyItemDetailDto.getInvoiceNo())
            .setManuDate(supplyItemDetailDto.getStartTime()).setExpyEnd(supplyItemDetailDto.getEndTime())
            .setPurcRetnCnt(supplyItemDetailDto.getItemQuantity()).setPurcRetnStoinTime(now)
            .setPurcRetnOpterName(supplyItemDetailDto.getPractitionerName());
        if (YbRxFlag.IMPORTANT_HERBAL_SLICES.getCode() == supplyItemDetailDto.getRxFlag()) {
            medicalPurchase3504Param.setRxFlag(YbRxFlag.IMPORTANT_HERBAL_SLICES.getName());
        } else if (YbRxFlag.WESTERN_AND_CHINESE_PATENT_MEDICINE.getCode() == supplyItemDetailDto.getRxFlag()) {
            medicalPurchase3504Param.setRxFlag(YbRxFlag.WESTERN_AND_CHINESE_PATENT_MEDICINE.getName());
        } else if (YbRxFlag.SELF_PREPARED_MEDICATION.getCode() == supplyItemDetailDto.getRxFlag()) {
            medicalPurchase3504Param.setRxFlag(YbRxFlag.WESTERN_AND_CHINESE_PATENT_MEDICINE.getName());
        }
        return medicalPurchase3504Param;
    }

    private Medical3503Param getMedical3503Param(SupplyItemDetailDto supplyItemDetailDto, Date now) {
        Medical3503Param medical3503Param = new Medical3503Param();
        medical3503Param.setMedListCodg(supplyItemDetailDto.getYbNo())
            .setFixmedinsBchno(supplyItemDetailDto.getLotNumber())
            .setFixmedinsHilistId(supplyItemDetailDto.getItemBusNo())
            .setFixmedinsHilistName(supplyItemDetailDto.getItemTable())
            .setSplerName(supplyItemDetailDto.getSupplierName()).setManuLotnum(supplyItemDetailDto.getLotNumber())
            .setProdentpName(supplyItemDetailDto.getManufacturerText())
            .setAprvno(supplyItemDetailDto.getApprovalNumber()).setManuDate(supplyItemDetailDto.getStartTime())
            .setExpyEnd(supplyItemDetailDto.getEndTime()).setPurcRetnCnt(supplyItemDetailDto.getItemQuantity())
            .setPurcRetnStoinTime(now).setPurcRetnOpterName(supplyItemDetailDto.getPractitionerName());
        if (YbRxFlag.IMPORTANT_HERBAL_SLICES.getCode() == supplyItemDetailDto.getRxFlag()) {
            medical3503Param.setRxFlag(YbRxFlag.IMPORTANT_HERBAL_SLICES.getName());
        } else if (YbRxFlag.WESTERN_AND_CHINESE_PATENT_MEDICINE.getCode() == supplyItemDetailDto.getRxFlag()) {
            medical3503Param.setRxFlag(YbRxFlag.WESTERN_AND_CHINESE_PATENT_MEDICINE.getName());
        } else if (YbRxFlag.SELF_PREPARED_MEDICATION.getCode() == supplyItemDetailDto.getRxFlag()) {
            medical3503Param.setRxFlag(YbRxFlag.WESTERN_AND_CHINESE_PATENT_MEDICINE.getName());
        }

        return medical3503Param;
    }

    private MedicalInventory3502Param getMedicalInventory3502Param(SupplyItemDetailDto supplyItemDetailDto, Date now,
        String ybInvChgType) {
        MedicalInventory3502Param medicalInventory3502Param = new MedicalInventory3502Param();
        // 查库存信息
        List<InventoryItem> inventoryItemList =
            inventoryItemService.selectInventoryByItemId(supplyItemDetailDto.getItemId(),
                supplyItemDetailDto.getLotNumber(), null, SecurityUtils.getLoginUser().getTenantId());
        // 查询商品价格信息
        List<ItemChargeDetailDto> itemChargeDetailList =
            receiptApprovalAppService.getItemChargeDetail(List.of(supplyItemDetailDto.getItemId()));
        if (!inventoryItemList.isEmpty() && !itemChargeDetailList.isEmpty()) {
            // 获取该项目所有的数量（最小单位）
            BigDecimal totalQuantity = inventoryItemList.stream()
                .map(item -> item.getQuantity() != null ? item.getQuantity() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            // 最小单位售卖价格
            BigDecimal minPrice = BigDecimal.ZERO;
            Optional<BigDecimal> price = itemChargeDetailList.stream()
                .filter(x -> x.getConditionValue().equals(supplyItemDetailDto.getLotNumber()))
                .map(ItemChargeDetailDto::getSellPrice).findFirst();
            if (price.isPresent()) {
                if (supplyItemDetailDto.getPartPercent().compareTo(BigDecimal.ZERO) > 0) {
                    minPrice = price.get().divide(supplyItemDetailDto.getPartPercent(), RoundingMode.HALF_UP);
                }
            }
            // 转换为JSON
            JSONArray medicalTraceNo = new JSONArray();
            // 获取追溯码信息
            if (supplyItemDetailDto.getTraceNo() != null) {
                List<String> traceNoList =
                    Arrays.stream(supplyItemDetailDto.getTraceNo().split(CommonConstants.Common.COMMA))
                        .map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
                for (String traceNo : traceNoList) {
                    Map<String, String> traceNoMap = new HashMap<>();
                    traceNoMap.put("drug_trac_codg", traceNo);
                    medicalTraceNo.add(traceNoMap);
                }
            }
            medicalInventory3502Param.setMedListCodg(supplyItemDetailDto.getYbNo()).setInvChgType(ybInvChgType)
                .setFixmedinsHilistId(supplyItemDetailDto.getItemBusNo())
                .setFixmedinsHilistName(supplyItemDetailDto.getItemTable())
                .setFixmedinsBchno(supplyItemDetailDto.getLotNumber()).setPric(minPrice).setCnt(totalQuantity)
                .setInvChgTime(now).setDrugtracinfo(medicalTraceNo);
            if (YbRxFlag.IMPORTANT_HERBAL_SLICES.getCode() == supplyItemDetailDto.getRxFlag()) {
                medicalInventory3502Param.setRxFlag(YbRxFlag.IMPORTANT_HERBAL_SLICES.getName());
            } else if (YbRxFlag.WESTERN_AND_CHINESE_PATENT_MEDICINE.getCode() == supplyItemDetailDto.getRxFlag()) {
                medicalInventory3502Param.setRxFlag(YbRxFlag.WESTERN_AND_CHINESE_PATENT_MEDICINE.getName());
            } else if (YbRxFlag.SELF_PREPARED_MEDICATION.getCode() == supplyItemDetailDto.getRxFlag()) {
                medicalInventory3502Param.setRxFlag(YbRxFlag.WESTERN_AND_CHINESE_PATENT_MEDICINE.getName());
            }
        }
        return medicalInventory3502Param;
    }

    private MedicalInventory3501Param getMedicalInventory3501Param(SupplyItemDetailDto supplyItemDetailDto, Date date) {
        MedicalInventory3501Param medicalInventory3501Param = new MedicalInventory3501Param();
        // 查库存信息
        List<InventoryItem> inventoryItemList =
            inventoryItemService.selectInventoryByItemId(supplyItemDetailDto.getItemId(),
                supplyItemDetailDto.getLotNumber(), null, SecurityUtils.getLoginUser().getTenantId());
        if (!inventoryItemList.isEmpty()) {
            // 获取该项目所有的数量（最小单位）
            BigDecimal totalQuantity = inventoryItemList.stream()
                .map(item -> item.getQuantity() != null ? item.getQuantity() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            medicalInventory3501Param.setMedListCodg(supplyItemDetailDto.getYbNo())
                .setFixmedinsHilistId(supplyItemDetailDto.getItemBusNo())
                .setFixmedinsHilistName(supplyItemDetailDto.getItemTable()).setInvdate(date).setInvCnt(totalQuantity)
                .setFixmedinsBchno(supplyItemDetailDto.getLotNumber()).setManuDate(supplyItemDetailDto.getStartTime())
                .setExpyEnd(supplyItemDetailDto.getEndTime());
            if (YbRxFlag.IMPORTANT_HERBAL_SLICES.getCode() == supplyItemDetailDto.getRxFlag()) {
                medicalInventory3501Param.setRxFlag(YbRxFlag.IMPORTANT_HERBAL_SLICES.getName());
            } else if (YbRxFlag.WESTERN_AND_CHINESE_PATENT_MEDICINE.getCode() == supplyItemDetailDto.getRxFlag()) {
                medicalInventory3501Param.setRxFlag(YbRxFlag.WESTERN_AND_CHINESE_PATENT_MEDICINE.getName());
            } else if (YbRxFlag.SELF_PREPARED_MEDICATION.getCode() == supplyItemDetailDto.getRxFlag()) {
                medicalInventory3501Param.setRxFlag(YbRxFlag.WESTERN_AND_CHINESE_PATENT_MEDICINE.getName());
            }
        }
        return medicalInventory3501Param;
    }
}
