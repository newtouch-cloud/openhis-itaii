/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.appservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.DateUtils;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.administration.domain.Location;
import com.openhis.administration.service.ILocationService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.basedatamanage.dto.LocationQueryDto;
import com.openhis.web.inventorymanage.appservice.IProductTransferAppService;
import com.openhis.web.inventorymanage.dto.*;
import com.openhis.web.inventorymanage.mapper.ProductTransferMapper;
import com.openhis.workflow.domain.SupplyRequest;
import com.openhis.workflow.service.ISupplyRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 商品调拨 impl
 *
 * @author zwh
 * @date 2025-03-08
 */
@Service
public class ProductTransferAppServiceImpl implements IProductTransferAppService {

    @Autowired
    private ProductTransferMapper productTransferMapper;

    @Autowired
    private ISupplyRequestService supplyRequestService;

    @Autowired
    private ILocationService locationService;

    @Autowired
    private AssignSeqUtil assignSeqUtil;

    /**
     * 商品调拨页面初始化
     *
     * @return 初始化信息
     */
    @Override
    public R<?> productTransferInit() {

        ProductTransferInitDto initDto = new ProductTransferInitDto();

        // 单据号
        initDto.setBusNo(assignSeqUtil.getSeqByDay(AssignSeqEnum.PURCHASE_NUM.getPrefix(), 12));

        // 入库项目类型
        List<ProductTransferInitDto.categoryListOption> categoryListOptions = Stream.of(ItemType.values())
                .map(itemType -> new ProductTransferInitDto.categoryListOption(itemType.getValue(), itemType.getInfo()))
                .collect(Collectors.toList());
        // 审批状态
        List<ProductTransferInitDto.supplyStatusOption> supplyStatusOptions = Stream.of(SupplyStatus.values())
                .map(supplyStatus -> new ProductTransferInitDto.supplyStatusOption(supplyStatus.getValue(),
                        supplyStatus.getInfo())).collect(Collectors.toList());

        // 获取药房
        List<Location> pharmacyList = locationService.getPharmacyList();
        // 药库列表
        List<Location> cabinetList = locationService.getCabinetList();

        // 将位置列表转为树结构
        List<LocationQueryDto> pharmacyLocationTree = buildTree(pharmacyList);
        List<LocationQueryDto> cabinetLocationTree = buildTree(cabinetList);

        initDto.setCategoryListOptions(categoryListOptions).setSupplyStatusOptions(supplyStatusOptions)
                .setSourceTypeListOptions(pharmacyLocationTree).setPurposeTypeListOptions(cabinetLocationTree);

        return R.ok(initDto);
    }

    /**
     * 将位置列表转换为树结构
     *
     * @param records 位置列表
     * @return tree
     */
    private List<LocationQueryDto> buildTree(List<Location> records) {
        // 按b_no的层级排序，确保父节点先处理
        List<Location> sortedRecords = records.stream()
                .sorted(Comparator.comparingInt(r -> r.getBusNo().split("\\.").length)).collect(Collectors.toList());

        Map<String, LocationQueryDto> nodeMap = new HashMap<>();
        List<LocationQueryDto> tree = new ArrayList<>();

        for (Location record : sortedRecords) {
            String bNo = record.getBusNo();
            String[] parts = bNo.split("\\.");
            LocationQueryDto node = new LocationQueryDto();
            org.springframework.beans.BeanUtils.copyProperties(record, node);
            // 将当前节点加入映射
            nodeMap.put(bNo, node);

            if (parts.length == 1) {
                // 根节点
                tree.add(node);
            } else {
                // 获取父节点的b_no（去掉最后一部分）
                String parentBNo = String.join(".", Arrays.copyOf(parts, parts.length - 1));
                LocationQueryDto parent = nodeMap.get(parentBNo);

                if (parent != null) {
                    parent.getChildren().add(node);
                } else {
                    // 处理父节点不存在的情况（例如数据缺失）
                    // 可根据需求调整为将节点加入根或抛出异常
                    tree.add(node);
                }
            }
        }
        return tree;
    }

    /**
     * 商品调拨单据列表
     *
     * @param supplySearchParam 供应申请查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 商品调拨单据分页列表
     */
    @Override
    public R<?> getPage(SupplySearchParam supplySearchParam, Integer pageNo, Integer pageSize, String searchKey,
        HttpServletRequest request) {

        // 设置模糊查询的字段名
        HashSet<String> searchFields = new HashSet<>();
        searchFields.add(CommonConstants.FieldName.SupplyBusNo);

        // 构建查询条件
        QueryWrapper<SupplySearchParam> queryWrapper =
            HisQueryUtils.buildQueryWrapper(supplySearchParam, searchKey, searchFields, request);
        // 查询商品调拨单据分页列表
        Page<ProductTransferPageDto> transferReceiptPage = productTransferMapper.selectProductTransferPage(
            new Page<>(pageNo, pageSize), queryWrapper, SupplyType.PRODUCT_ALLOCATION.getValue());

        transferReceiptPage.getRecords().forEach(e -> {
            // 单据状态
            e.setStatusEnum_enumText(EnumUtils.getInfoByValue(SupplyStatus.class, e.getStatusEnum()));
        });
        return R.ok(transferReceiptPage);
    }

    /**
     * 商品调拨单据详情
     *
     * @param busNo 单据号
     * @return 商品调拨单据详情
     */
    @Override
    public R<?> getDetail(String busNo) {
        List<ProductTransferDetailDto> productTransferDetailList = productTransferMapper.selectDetail(busNo);
        if (productTransferDetailList.isEmpty()) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00006, null));
        }
        return R.ok(productTransferDetailList);
    }

    /**
     * 添加/编辑商品调拨单据
     *
     * @param productTransferDto 商品调拨单据
     * @return 编辑结果
     */
    @Override
    public R<?> addOrEditTransferReceipt(ProductTransferDto productTransferDto) {

        // 初始化单据信息
        SupplyRequest supplyRequest = new SupplyRequest();
        BeanUtils.copyProperties(productTransferDto, supplyRequest);

        if (productTransferDto.getId() != null) {
            // 更新单据信息
            supplyRequestService.updateById(supplyRequest);
        } else {
            // 生成商品调拨单据
            supplyRequest
                // 单据分类：库存供应
                .setCategoryEnum(SupplyCategory.STOCK_SUPPLY.getValue())
                // 单据类型：商品调拨
                .setTypeEnum(SupplyType.PRODUCT_ALLOCATION.getValue())
                // 申请时间
                .setApplyTime(DateUtils.getNowDate());
            supplyRequestService.save(supplyRequest);
        }
        // 返回单据id
        return R.ok(supplyRequest.getId(), null);
    }

    /**
     * 删除单据
     *
     * @param supplyRequestId 供应请求id
     * @return 操作结果
     */
    @Override
    public R<?> deleteReceipt(Long supplyRequestId) {
        // 删除单据
        boolean result = supplyRequestService.removeById(supplyRequestId);
        return result ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null))
            : R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 提交审批
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @Override
    public R<?> submitApproval(String busNo) {
        // 单据提交审核
        boolean result = supplyRequestService.submitApproval(busNo);
        return result ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null))
            : R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 撤回审批
     *
     * @param busNo 单据号
     * @return 操作结果
     */
    @Override
    public R<?> withdrawApproval(String busNo) {
        // 撤回审核
        boolean result = supplyRequestService.withdrawApproval(busNo);
        return result ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null))
            : R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }
}
