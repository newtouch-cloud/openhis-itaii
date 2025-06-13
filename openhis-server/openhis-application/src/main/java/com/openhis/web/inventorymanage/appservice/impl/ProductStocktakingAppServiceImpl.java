/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.appservice.impl;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.core.domain.model.LoginUser;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.DateUtils;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.SecurityUtils;
import com.openhis.administration.domain.Location;
import com.openhis.administration.domain.Practitioner;
import com.openhis.administration.service.ILocationService;
import com.openhis.administration.service.IPractitionerService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.basedatamanage.dto.LocationDto;
import com.openhis.web.common.dto.UnitDto;
import com.openhis.web.inventorymanage.appservice.IProductStocktakingAppService;
import com.openhis.web.inventorymanage.dto.*;
import com.openhis.web.inventorymanage.mapper.ProductStocktakingMapper;
import com.openhis.workflow.domain.SupplyRequest;
import com.openhis.workflow.service.ISupplyRequestService;

/**
 * 商品盘点 impl
 *
 * @author zwh
 * @date 2025-03-11
 */
@Service
public class ProductStocktakingAppServiceImpl implements IProductStocktakingAppService {
    @Resource
    private ProductStocktakingMapper productStocktakingMapper;
    @Resource
    private ISupplyRequestService supplyRequestService;

    @Resource
    private IPractitionerService practitionerService;

    @Resource
    private AssignSeqUtil assignSeqUtil;

    @Autowired
    private ILocationService locationService;

    /**
     * 商品盘点页面初始化
     *
     * @return 初始化信息
     */
    @Override
    public R<?> productStocktakingInit() {

        ProductStocktakingInitDto initDto = new ProductStocktakingInitDto();

        // 查询制单人列表
        List<Practitioner> applicantList = practitionerService.getList();
        // 制单人信息
        List<ProductStocktakingInitDto.applicantListOption> applicantListOptions = applicantList.stream()
            .map(applicant -> new ProductStocktakingInitDto.applicantListOption(applicant.getId(), applicant.getName()))
            .collect(Collectors.toList());
        // 审批状态
        List<ProductStocktakingInitDto.supplyStatusOption> supplyStatusOptions = Stream.of(SupplyStatus.values())
            .map(supplyStatus -> new ProductStocktakingInitDto.supplyStatusOption(supplyStatus.getValue(),
                supplyStatus.getInfo()))
            .collect(Collectors.toList());

        // 药品类型
        List<ProductStocktakingInitDto.categoryListOption> categoryListOptions = Stream.of(ItemType.values())
            .map(itemType -> new ProductStocktakingInitDto.categoryListOption(itemType.getValue(), itemType.getInfo()))
            .collect(Collectors.toList());

        // 仓库列表
        List<Location> cabinetList = locationService.getCabinetList();

        // 药房列表
        List<Location> pharmacyList = locationService.getPharmacyList();

        // 将位置列表转为树结构
        List<LocationDto> cabinetLocationTree = buildTree(cabinetList);
        List<LocationDto> pharmacyLocationTree = buildTree(pharmacyList);

        initDto.setCategoryListOptions(categoryListOptions).setCabinetListOptions(cabinetLocationTree)
            .setPharmacyListOptions(pharmacyLocationTree).setApplicantListOptions(applicantListOptions)
            .setSupplyStatusOptions(supplyStatusOptions);

        return R.ok(initDto);
    }

    /**
     * 获取单据号
     *
     * @return 初始化信息
     */
    @Override
    public R<?> productStocktakingBusNoInit() {

        ProductStocktakingInitDto initDto = new ProductStocktakingInitDto();
        // 单据号
        initDto.setBusNo(assignSeqUtil.getSeqByDay(AssignSeqEnum.STOCKTAKING_NUM.getPrefix(), 10));

        return R.ok(initDto);
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

    /**
     * 商品盘点列表
     *
     * @param stocktakingSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 盘点单据分页列表
     */
    @Override
    public R<?> getPage(ProductStocktakingSearchParam stocktakingSearchParam, Integer pageNo, Integer pageSize,
        String searchKey, HttpServletRequest request) {

        // 设置模糊查询的字段名
        HashSet<String> searchFields = new HashSet<>();
        searchFields.add(CommonConstants.FieldName.SupplyBusNo);
        // 构建查询条件
        QueryWrapper<ProductStocktakingSearchParam> queryWrapper =
            HisQueryUtils.buildQueryWrapper(stocktakingSearchParam, searchKey, searchFields, request);
        // 查询商品盘点分页列表
        Page<ReceiptPageDto> stocktakingReceiptPage =
            productStocktakingMapper.selectStocktakingReceiptPage(new Page<>(pageNo, pageSize), queryWrapper,
                SupplyType.PRODUCT_STOCKTAKING.getValue(), SupplyType.PRODUCT_BATCH_STOCKTAKING.getValue());

        stocktakingReceiptPage.getRecords().forEach(e -> {
            // 单据状态
            e.setStatusEnum_enumText(EnumUtils.getInfoByValue(SupplyStatus.class, e.getStatusEnum()));
            // 单据类型
            e.setTypeEnum_enumText(EnumUtils.getInfoByValue(SupplyType.class, e.getTypeEnum()));
        });
        return R.ok(stocktakingReceiptPage);
    }

    /**
     * 商品盘点详情
     *
     * @param busNo 单据号
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return 盘点单据详情
     */
    @Override
    public R<?> getDetail(String busNo, Integer pageNo, Integer pageSize) {

        Page<ReceiptDetailDto> receiptDetailList = productStocktakingMapper.selectDetail(new Page<>(pageNo, pageSize),
            busNo, ItemType.MEDICINE.getValue(), ItemType.DEVICE.getValue(),
            CommonConstants.TableName.MED_MEDICATION_DEFINITION, CommonConstants.TableName.ADM_DEVICE_DEFINITION);

        receiptDetailList.getRecords().forEach(e -> {
            List<UnitDto> unitList = new ArrayList<>();
            UnitDto unitDto = new UnitDto();
            // 单位列表
            unitDto.setUnitCode(e.getUnitCode()).setMinUnitCode(e.getMinUnitCode());
            unitList.add(unitDto);
            e.setUnitList(unitList);
        });

        return R.ok(receiptDetailList);
    }

    /**
     * 添加/编辑商品盘点(批量)
     *
     * @param productStocktakingDtoList 盘点信息
     * @return 操作结果
     */
    @Override
    public R<?> addOrEditProductStocktaking(List<ProductStocktakingDto> productStocktakingDtoList) {

        List<String> idList = new ArrayList<>();

        // 单据号取得
        List<String> busNoList =
            productStocktakingDtoList.stream().map(ProductStocktakingDto::getBusNo).collect(Collectors.toList());
        // 请求数据取得
        List<SupplyRequest> requestList = supplyRequestService.getSupplyByBusNo(busNoList.get(0));
        if (!requestList.isEmpty()) {
            // 请求id取得
            List<Long> requestIdList = requestList.stream().map(SupplyRequest::getId).collect(Collectors.toList());
            // 单据信息删除
            supplyRequestService.removeByIds(requestIdList);
        }

        List<SupplyRequest> supplyRequestList = new ArrayList<>();
        for (ProductStocktakingDto productStocktakingDto : productStocktakingDtoList) {
            // 获取当前时间
            Date now = DateUtils.getNowDate();
            // 当前登录账号
            LoginUser loginUser = SecurityUtils.getLoginUser();
            Practitioner user = practitionerService.getPractitionerByUserId(loginUser.getUserId());
            // 初始化盘点信息
            SupplyRequest supplyRequest = new SupplyRequest().setId(productStocktakingDto.getId())
                .setBusNo(productStocktakingDto.getBusNo()).setItemTable(productStocktakingDto.getItemTable())
                .setItemId(productStocktakingDto.getItemId()).setUnitCode(productStocktakingDto.getUnitCode())
                .setItemQuantity(productStocktakingDto.getItemQuantity()).setPrice(productStocktakingDto.getPrice())
                .setTotalPrice(productStocktakingDto.getTotalPrice())
                .setOccurrenceTime(productStocktakingDto.getOccurrenceTime())
                .setReason(productStocktakingDto.getReason()).setReasonCode(productStocktakingDto.getReasonCode())
                .setPurposeTypeEnum(productStocktakingDto.getPurposeTypeEnum())
                .setPurposeLocationId(productStocktakingDto.getPurposeLocationId())
                .setPurposeLocationStoreId(productStocktakingDto.getPurposeLocationStoreId())
                .setLotNumber(productStocktakingDto.getLotNumber()).setStartTime(productStocktakingDto.getStartTime())
                .setEndTime(productStocktakingDto.getEndTime()).setSupplierId(productStocktakingDto.getSupplierId())
                .setReason(productStocktakingDto.getReason()).setTraceNo(productStocktakingDto.getTraceNo());

            supplyRequest.setCreateBy(user.getId().toString());
            supplyRequest.setCreateTime(now);
            // 生成待发送的盘点单据
            supplyRequest
                // id
                .setId(null)
                // 单据分类：非库存供应
                .setCategoryEnum(SupplyCategory.NON_STOCK.getValue())
                // 单据类型：商品盘点
                .setTypeEnum(SupplyType.PRODUCT_STOCKTAKING.getValue())
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
        List<Long> requestIdList = supplyRequestIdList.stream().map(SupplyRequest::getId).collect(Collectors.toList());
        for (Long list : requestIdList) {
            idList.add(list.toString());
        }

        // 返回单据id
        return R.ok(idList, null);
    }

    /**
     * 生成批量盘点单
     *
     * @param stocktakingSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 盘点单据分页列表
     */
    @Override
    public R<?> getBatchDetail(StocktakingBatchSearchParam stocktakingSearchParam, Integer pageNo, Integer pageSize,
        String searchKey, HttpServletRequest request) {

        // 盘点仓库id
        Long sourceLocationId = stocktakingSearchParam.getSourceLocationId();
        stocktakingSearchParam.setSourceLocationId(null);

        // 构建查询条件
        QueryWrapper<StocktakingBatchSearchParam> queryWrapper =
            HisQueryUtils.buildQueryWrapper(stocktakingSearchParam, searchKey, null, request);
        // 查询可盘点商品
        Page<ReceiptDetailDto> stocktakingReceiptDetail = productStocktakingMapper.selectStocktakingReceiptDetail(
            new Page<>(pageNo, pageSize), queryWrapper, sourceLocationId,
            CommonConstants.TableName.MED_MEDICATION_DEFINITION, CommonConstants.TableName.ADM_DEVICE_DEFINITION,
            ItemType.MEDICINE.getValue(), ItemType.DEVICE.getValue(), PublicationStatus.ACTIVE.getValue());

        stocktakingReceiptDetail.getRecords().forEach(e -> {
            List<UnitDto> unitList = new ArrayList<>();
            UnitDto unitDto = new UnitDto();
            // 单位列表
            unitDto.setUnitCode(e.getUnitCode()).setMinUnitCode(e.getMinUnitCode());
            unitList.add(unitDto);
            e.setUnitList(unitList);

        });
        return R.ok(stocktakingReceiptDetail);
    }

    /**
     * 保存批量盘点单
     *
     * @param productStocktakingDtoList 盘点单据分页列表
     * @return 执行结果
     */
    @Override
    public R<?> addBatchDetail(List<ProductStocktakingDto> productStocktakingDtoList) {

        List<String> idList = new ArrayList<>();

        // 单据号取得
        List<String> busNoList =
            productStocktakingDtoList.stream().map(ProductStocktakingDto::getBusNo).collect(Collectors.toList());
        // 请求id取得
        List<SupplyRequest> requestList = supplyRequestService.getSupplyByBusNo(busNoList.get(0));
        if (!requestList.isEmpty()) {
            List<Long> requestIdList = requestList.stream().map(SupplyRequest::getId).collect(Collectors.toList());
            // 单据信息删除
            supplyRequestService.removeByIds(requestIdList);
        }

        // 生成批量盘点单据
        List<SupplyRequest> supplyRequestList = new ArrayList<>();
        for (ProductStocktakingDto productStocktakingDto : productStocktakingDtoList) {
            // 初始化盘点信息
            SupplyRequest supplyRequest = new SupplyRequest().setId(productStocktakingDto.getId())
                .setBusNo(productStocktakingDto.getBusNo()).setItemTable(productStocktakingDto.getItemTable())
                .setItemId(productStocktakingDto.getItemId()).setUnitCode(productStocktakingDto.getUnitCode())
                .setItemQuantity(productStocktakingDto.getItemQuantity()).setPrice(productStocktakingDto.getPrice())
                .setTotalPrice(productStocktakingDto.getTotalPrice())
                .setOccurrenceTime(productStocktakingDto.getOccurrenceTime())
                .setReason(productStocktakingDto.getReason()).setReasonCode(productStocktakingDto.getReasonCode())
                .setPurposeTypeEnum(productStocktakingDto.getPurposeTypeEnum())
                .setPurposeLocationId(productStocktakingDto.getPurposeLocationId())
                .setPurposeLocationStoreId(productStocktakingDto.getPurposeLocationStoreId())
                .setLotNumber(productStocktakingDto.getLotNumber()).setStartTime(productStocktakingDto.getStartTime())
                .setEndTime(productStocktakingDto.getEndTime()).setSupplierId(productStocktakingDto.getSupplierId());
            // 生成商品批量调拨单据
            supplyRequest
                // id
                .setId(null)
                // 单据分类：库存供应
                .setCategoryEnum(SupplyCategory.STOCK_SUPPLY.getValue())
                // 单据类型：商品批量盘点
                .setTypeEnum(SupplyType.PRODUCT_BATCH_STOCKTAKING.getValue())
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
        List<Long> requestIdList = supplyRequestIdList.stream().map(SupplyRequest::getId).collect(Collectors.toList());
        for (Long list : requestIdList) {
            idList.add(list.toString());
        }

        // 返回单据id
        return R.ok(idList, null);
    }

    /**
     * 删除盘点
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
}
