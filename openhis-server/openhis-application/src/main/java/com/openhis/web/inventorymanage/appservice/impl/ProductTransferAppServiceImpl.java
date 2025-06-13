/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.appservice.impl;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import com.openhis.web.chargemanage.dto.EncounterPatientPaymentDto;
import com.openhis.web.common.dto.InventoryItemDto;
import com.openhis.web.common.dto.UnitDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.DateUtils;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.SecurityUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.administration.domain.Location;
import com.openhis.administration.service.ILocationService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.basedatamanage.dto.LocationDto;
import com.openhis.web.inventorymanage.appservice.IProductTransferAppService;
import com.openhis.web.inventorymanage.dto.*;
import com.openhis.web.inventorymanage.mapper.ProductTransferMapper;
import com.openhis.workflow.domain.SupplyRequest;
import com.openhis.workflow.service.ISupplyRequestService;

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
        // 入库项目类型
        List<ProductTransferInitDto.categoryListOption> categoryListOptions = Stream.of(ItemType.values())
            .map(itemType -> new ProductTransferInitDto.categoryListOption(itemType.getValue(), itemType.getInfo()))
            .collect(Collectors.toList());
        // 审批状态
        List<ProductTransferInitDto.supplyStatusOption> supplyStatusOptions = Stream.of(SupplyStatus.values())
            .map(supplyStatus -> new ProductTransferInitDto.supplyStatusOption(supplyStatus.getValue(),
                supplyStatus.getInfo()))
            .collect(Collectors.toList());

        // 单据类型
        List<ProductTransferInitDto.supplyTypeOption> supplyTypeOptions = new ArrayList<>();
        supplyTypeOptions.add(new ProductTransferInitDto.supplyTypeOption(SupplyType.PRODUCT_TRANSFER.getValue(),
            SupplyType.PRODUCT_TRANSFER.getInfo()));
        supplyTypeOptions.add(new ProductTransferInitDto.supplyTypeOption(SupplyType.PRODUCT_BATCH_TRANSFER.getValue(),
            SupplyType.PRODUCT_BATCH_TRANSFER.getInfo()));

        // 获取药房
        List<Location> pharmacyList = locationService.getPharmacyList();
        // 药库列表
        List<Location> cabinetList = locationService.getCabinetList();

        // 将位置列表转为树结构
        List<LocationDto> pharmacyLocationTree = buildTree(pharmacyList);
        List<LocationDto> cabinetLocationTree = buildTree(cabinetList);

        initDto.setCategoryListOptions(categoryListOptions).setSupplyStatusOptions(supplyStatusOptions)
            .setSourceTypeListOptions(pharmacyLocationTree).setPurposeTypeListOptions(cabinetLocationTree)
            .setSupplyTypeOptions(supplyTypeOptions);

        return R.ok(initDto);
    }

    /**
     * 商品调拨单据编号初始化
     *
     * @return 初始化信息
     */
    @Override
    public R<?> productTransferNoInit() {
        ProductTransferInitDto initDto = new ProductTransferInitDto();
        // 单据号
        initDto.setBusNo(assignSeqUtil.getSeqByDay(AssignSeqEnum.TRANSFER_NUM.getPrefix(), 10));
        return R.ok(initDto);
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
        Page<ProductTransferPageDto> transferReceiptPage =
            productTransferMapper.selectProductTransferPage(new Page<>(pageNo, pageSize), queryWrapper,
                SupplyType.PRODUCT_TRANSFER.getValue(), SupplyType.PRODUCT_BATCH_TRANSFER.getValue());

        transferReceiptPage.getRecords().forEach(e -> {
            // 单据状态
            e.setStatusEnum_enumText(EnumUtils.getInfoByValue(SupplyStatus.class, e.getStatusEnum()));
            // 单据类型
            e.setTypeEnum_enumText(EnumUtils.getInfoByValue(SupplyType.class, e.getTypeEnum()));
        });
        return R.ok(transferReceiptPage);
    }

    /**
     * 生成商品批量调拨单据
     *
     * @param batchTransferSearchParam 生成批量调拨单查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 批量调拨单据
     */
    @Override
    public R<?> createBatchTransfer(BatchTransferSearchParam batchTransferSearchParam, Integer pageNo, Integer pageSize,
        HttpServletRequest request) {

        // 源仓库id
        Long sourceLocationId = batchTransferSearchParam.getSourceLocationId();
        batchTransferSearchParam.setSourceLocationId(null);
        // 目的仓库id
        Long purposeLocationId = batchTransferSearchParam.getPurposeLocationId();
        batchTransferSearchParam.setPurposeLocationId(null);

        // 构建查询条件
        QueryWrapper<SupplySearchParam> queryWrapper =
            HisQueryUtils.buildQueryWrapper(batchTransferSearchParam, null, null, request);
        Page<ProductTransferDetailDto> productTransferDetailList = productTransferMapper.selectBatchTransferDetail(
            new Page<>(pageNo, pageSize), queryWrapper, sourceLocationId, purposeLocationId,
            CommonConstants.TableName.MED_MEDICATION_DEFINITION, CommonConstants.TableName.ADM_DEVICE_DEFINITION,
            ItemType.MEDICINE.getValue(), ItemType.DEVICE.getValue(), PublicationStatus.ACTIVE.getValue());

        productTransferDetailList.getRecords().forEach(e -> {
            List<UnitDto> unitList = new ArrayList<>();
            UnitDto unitDto = new UnitDto();
            // 单位列表
            unitDto.setUnitCode(e.getUnitCode()).setMinUnitCode(e.getMinUnitCode());
            unitList.add(unitDto);
            e.setUnitList(unitList);

        });

        return R.ok(productTransferDetailList);
    }

    /**
     * 保存商品批量调拨单据
     *
     * @param productTransferDtoList 商品批量调拨单据
     * @return 操作结果
     */
    @Override
    public R<?> addOrEditBatchTransferReceipt(List<ProductTransferDto> productTransferDtoList, Boolean flag) {

        List<String> idList = new ArrayList<>();
        if (flag) {
            // 批量保存按钮
            // 单据号取得
            List<String> busNoList =
                productTransferDtoList.stream().map(ProductTransferDto::getBusNo).collect(Collectors.toList());
            // 请求id取得
            List<SupplyRequest> requestList = supplyRequestService.getSupplyByBusNo(busNoList.get(0));
            if (!requestList.isEmpty()) {
                List<Long> requestIdList = requestList.stream().map(SupplyRequest::getId).collect(Collectors.toList());
                // 单据信息删除
                supplyRequestService.removeByIds(requestIdList);
            }

            // 生成批量调拨单据
            List<SupplyRequest> supplyRequestList = new ArrayList<>();
            for (ProductTransferDto productTransferDto : productTransferDtoList) {
                // 初始化单据信息
                SupplyRequest supplyRequest = new SupplyRequest();
                BeanUtils.copyProperties(productTransferDto, supplyRequest);
                // 生成商品批量调拨单据
                supplyRequest
                    // id
                    .setId(null)
                    // 单据分类：库存供应
                    .setCategoryEnum(SupplyCategory.STOCK_SUPPLY.getValue())
                    // 单据类型：商品批量调拨
                    .setTypeEnum(SupplyType.PRODUCT_BATCH_TRANSFER.getValue())
                    // 制单人
                    .setApplicantId(SecurityUtils.getLoginUser().getPractitionerId())
                    // 申请时间
                    .setApplyTime(DateUtils.getNowDate());
                supplyRequestList.add(supplyRequest);
            }
            supplyRequestService.saveOrUpdateBatch(supplyRequestList);
            // 请求id取得
            List<SupplyRequest> supplyRequestIdList = supplyRequestService.getSupplyByBusNo(busNoList.get(0));
            // 返回请求id列表
            List<Long> requestIdList =
                supplyRequestIdList.stream().map(SupplyRequest::getId).collect(Collectors.toList());
            for (Long list : requestIdList) {
                idList.add(list.toString());
            }
        } else {
            // 单独保存按钮
            for (ProductTransferDto productTransferDto : productTransferDtoList) {
                // 初始化单据信息
                SupplyRequest supplyRequest = new SupplyRequest();
                BeanUtils.copyProperties(productTransferDto, supplyRequest);

                if (productTransferDto.getId() != null) {
                    // 更新单据信息
                    supplyRequestService.updateById(supplyRequest);
                } else {
                    // 生成商品批量调拨单据
                    supplyRequest
                        // 单据分类：库存供应
                        .setCategoryEnum(SupplyCategory.STOCK_SUPPLY.getValue())
                        // 单据类型：商品批量调拨
                        .setTypeEnum(SupplyType.PRODUCT_BATCH_TRANSFER.getValue())
                        // 制单人
                        .setApplicantId(SecurityUtils.getLoginUser().getPractitionerId())
                        // 申请时间
                        .setApplyTime(DateUtils.getNowDate());
                    supplyRequestService.save(supplyRequest);
                }
                // 返回单据id
                return R.ok(supplyRequest.getId().toString(), null);
            }
        }
        // 返回单据id
        return R.ok(idList, null);
    }

    /**
     * 商品调拨单据详情
     *
     * @param busNo 单据号
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return 商品调拨单据详情
     */
    @Override
    public R<?> getDetail(String busNo, Integer pageNo, Integer pageSize) {

        Page<ProductTransferDetailDto> productTransferDetailList = productTransferMapper.selectDetail(
            new Page<>(pageNo, pageSize), busNo, CommonConstants.TableName.MED_MEDICATION_DEFINITION,
            CommonConstants.TableName.ADM_DEVICE_DEFINITION, ItemType.MEDICINE.getValue(), ItemType.DEVICE.getValue());

        productTransferDetailList.getRecords().forEach(e -> {
            List<UnitDto> unitList = new ArrayList<>();
            UnitDto unitDto = new UnitDto();
            // 单位列表
            unitDto.setUnitCode(e.getUnitCode()).setMinUnitCode(e.getMinUnitCode());
            unitList.add(unitDto);
            e.setUnitList(unitList);

        });

        return R.ok(productTransferDetailList);
    }

    /**
     * 添加/编辑商品调拨单据(批量)
     *
     * @param productTransferDtoList 商品调拨单据
     * @return 编辑结果
     */
    @Override
    public R<?> addOrEditTransferReceipt(List<ProductTransferDto> productTransferDtoList) {

        List<String> idList = new ArrayList<>();

        // 单据号取得
        List<String> busNoList =
                productTransferDtoList.stream().map(ProductTransferDto::getBusNo).collect(Collectors.toList());
        // 请求数据取得
        List<SupplyRequest> requestList = supplyRequestService.getSupplyByBusNo(busNoList.get(0));
        if (!requestList.isEmpty()) {
            // 请求id取得
            List<Long> requestIdList = requestList.stream().map(SupplyRequest::getId).collect(Collectors.toList());
            // 单据信息删除
            supplyRequestService.removeByIds(requestIdList);
        }

        List<SupplyRequest> supplyRequestList = new ArrayList<>();
        for (ProductTransferDto productTransferDto : productTransferDtoList) {
            // 初始化单据信息
            SupplyRequest supplyRequest = new SupplyRequest();
            BeanUtils.copyProperties(productTransferDto, supplyRequest);
            // 生成商品调拨单据
            supplyRequest
                    // id
                    .setId(null)
                    // 单据分类：库存供应
                    .setCategoryEnum(SupplyCategory.STOCK_SUPPLY.getValue())
                    // 单据类型：商品调拨
                    .setTypeEnum(SupplyType.PRODUCT_TRANSFER.getValue())
                    // 制单人
                    .setApplicantId(SecurityUtils.getLoginUser().getPractitionerId())
                    // 申请时间
                    .setApplyTime(DateUtils.getNowDate());
            supplyRequestList.add(supplyRequest);

        }

        // 保存
        supplyRequestService.saveOrUpdateBatch(supplyRequestList);

        // 请求id取得
        List<SupplyRequest> supplyRequestIdList = supplyRequestService.getSupplyByBusNo(busNoList.get(0));
        // 返回请求id列表
        List<Long> requestIdList =
                supplyRequestIdList.stream().map(SupplyRequest::getId).collect(Collectors.toList());
        for (Long list : requestIdList) {
            idList.add(list.toString());
        }

        // 返回请求id
        return R.ok(idList, null);
    }

    /**
     * 删除单据
     *
     * @param supplyRequestIds 供应请求id
     * @return 操作结果
     */
    @Override
    public R<?> deleteReceipt(List<Long> supplyRequestIds) {
        // 删除单据
        boolean result = supplyRequestService.removeByIds(supplyRequestIds);
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

    /**
     * 将位置列表转换为树结构
     *
     * @param records 位置列表
     * @return tree
     */
    private List<LocationDto> buildTree(List<Location> records) {
        // 按b_no的层级排序，确保父节点先处理
        List<Location> sortedRecords = records.stream()
            .sorted(Comparator.comparingInt(r -> r.getBusNo().split("\\.").length)).collect(Collectors.toList());

        Map<String, LocationDto> nodeMap = new HashMap<>();
        List<LocationDto> tree = new ArrayList<>();

        for (Location record : sortedRecords) {
            String bNo = record.getBusNo();
            String[] parts = bNo.split("\\.");
            LocationDto node = new LocationDto();
            org.springframework.beans.BeanUtils.copyProperties(record, node);
            // 将当前节点加入映射
            nodeMap.put(bNo, node);

            if (parts.length == 1) {
                // 根节点
                tree.add(node);
            } else {
                // 获取父节点的b_no（去掉最后一部分）
                String parentBNo = String.join(".", Arrays.copyOf(parts, parts.length - 1));
                LocationDto parent = nodeMap.get(parentBNo);

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
}
