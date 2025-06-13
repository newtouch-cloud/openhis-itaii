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
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.DateUtils;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.SecurityUtils;
import com.core.common.utils.bean.BeanUtils;
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
import com.openhis.web.inventorymanage.appservice.ILossReportFormAppService;
import com.openhis.web.inventorymanage.dto.*;
import com.openhis.web.inventorymanage.mapper.LossReportFormMapper;
import com.openhis.workflow.domain.SupplyRequest;
import com.openhis.workflow.service.ISupplyRequestService;

/**
 * 报损单 impl
 *
 * @author gyy
 * @date 2025-04-07
 */
@Service
public class LossReportFormAppServiceImpl implements ILossReportFormAppService {

    @Autowired
    private LossReportFormMapper lossReportFormMapper;

    @Autowired
    private ISupplyRequestService supplyRequestService;

    @Autowired
    private ILocationService locationService;

    @Autowired
    private AssignSeqUtil assignSeqUtil;

    @Resource
    private IPractitionerService practitionerService;

    /**
     * 报损单单据列表
     *
     * @param lossReportSearchParam 供应申请查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 报损单单据分页列表
     */
    @Override
    public R<?> getPage(LossReportSearchParam lossReportSearchParam, Integer pageNo, Integer pageSize, String searchKey,
        HttpServletRequest request) {

        // 设置模糊查询的字段名
        HashSet<String> searchFields = new HashSet<>();
        searchFields.add(CommonConstants.FieldName.SupplyBusNo);

        // 构建查询条件
        QueryWrapper<LossReportSearchParam> queryWrapper =
            HisQueryUtils.buildQueryWrapper(lossReportSearchParam, searchKey, searchFields, request);
        // 查询报损单单据分页列表
        Page<LossReportFormPageDto> lossReceiptPage = lossReportFormMapper.selectLossReportFormPage(
            new Page<>(pageNo, pageSize), queryWrapper, SupplyType.LOSS_REPORT_FORM.getValue());

        lossReceiptPage.getRecords().forEach(e -> {
            // 单据状态
            e.setStatusEnum_enumText(EnumUtils.getInfoByValue(SupplyStatus.class, e.getStatusEnum()));
            // 单据类型
            e.setType_enumText(EnumUtils.getInfoByValue(SupplyType.class, e.getType()));
        });
        return R.ok(lossReceiptPage);
    }

    /**
     * 报损单据页面初始化
     *
     * @return 初始化信息
     */
    @Override
    public R<?> lossReportFormInit() {

        LossReportFormInitDto initDto = new LossReportFormInitDto();

        // 获取药房
        List<Location> pharmacyList = locationService.getPharmacyList();

        // 将位置列表转为树结构
        List<LocationDto> pharmacyLocationTree = buildTree(pharmacyList);

        // 药品类型
        List<LossReportFormInitDto.categoryListOption> categoryListOptions = Stream.of(ItemType.values())
            .map(itemType -> new LossReportFormInitDto.categoryListOption(itemType.getValue(), itemType.getInfo()))
            .collect(Collectors.toList());

        // 审批状态
        List<LossReportFormInitDto.supplyStatusOption> supplyStatusOptions = Stream.of(SupplyStatus.values())
            .map(supplyStatus -> new LossReportFormInitDto.supplyStatusOption(supplyStatus.getValue(),
                supplyStatus.getInfo()))
            .collect(Collectors.toList());

        // 查询制单人列表
        List<Practitioner> applicantList = practitionerService.getList();
        // 制单人信息
        List<LossReportFormInitDto.applicantListOption> applicantListOptions = applicantList.stream()
            .map(applicant -> new LossReportFormInitDto.applicantListOption(applicant.getId(), applicant.getName()))
            .collect(Collectors.toList());

        initDto.setCategoryListOptions(categoryListOptions).setWarehouseTypeList(pharmacyLocationTree)
            .setSupplyStatusOptions(supplyStatusOptions).setApplicantListOptions(applicantListOptions);

        return R.ok(initDto);
    }

    /**
     * 单据号初始化
     *
     * @return 初始化信息
     */
    @Override
    public R<?> lossReportNoInit() {
        LossReportFormInitDto initDto = new LossReportFormInitDto();
        // 单据号
        initDto.setBusNo(assignSeqUtil.getSeqByDay(AssignSeqEnum.LOSS_BUS_NO.getPrefix(), 10));
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
     * 报损单据详情
     *
     * @param busNo 单据号
     * @return 报损单据详情
     */
    @Override
    public R<?> getDetail(String busNo) {
        List<ReceiptDetailDto> receiptDetailList =
            lossReportFormMapper.selectDetail(busNo, ItemType.MEDICINE.getValue(), ItemType.DEVICE.getValue(),
                CommonConstants.TableName.MED_MEDICATION_DEFINITION, CommonConstants.TableName.ADM_DEVICE_DEFINITION);
        if (receiptDetailList.isEmpty()) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00006, null));
        }
        for (ReceiptDetailDto dto : receiptDetailList) {
            List<UnitDto> unitList = new ArrayList<>();
            UnitDto unitDto = new UnitDto();
            // 单位列表
            unitDto.setUnitCode(dto.getUnitCode()).setMinUnitCode(dto.getMinUnitCode());
            unitList.add(unitDto);
            dto.setUnitList(unitList);
        }
        return R.ok(receiptDetailList);
    }

    /**
     * 添加/编辑报损单据(批量)
     *
     * @param lossReportFormDtoList 报损单据
     * @return 操作结果
     */
    @Override
    public R<?> addOrEditLossReceipt(List<LossReportFormDto> lossReportFormDtoList) {

        List<String> idList = new ArrayList<>();

        // 单据号取得
        List<String> busNoList =
                lossReportFormDtoList.stream().map(LossReportFormDto::getBusNo).collect(Collectors.toList());
        // 请求数据取得
        List<SupplyRequest> requestList = supplyRequestService.getSupplyByBusNo(busNoList.get(0));
        if (!requestList.isEmpty()) {
            // 请求id取得
            List<Long> requestIdList = requestList.stream().map(SupplyRequest::getId).collect(Collectors.toList());
            // 单据信息删除
            supplyRequestService.removeByIds(requestIdList);
        }

        List<SupplyRequest> supplyRequestList = new ArrayList<>();
        for (LossReportFormDto lossReportFormDto : lossReportFormDtoList) {
            // 初始化单据信息
            SupplyRequest supplyRequest = new SupplyRequest();
            BeanUtils.copyProperties(lossReportFormDto, supplyRequest);
            supplyRequest.setPurposeLocationId(lossReportFormDto.getLossLocationId())
                    .setPurposeLocationStoreId(lossReportFormDto.getLossLocationStoreId())
                    .setPurposeTypeEnum(lossReportFormDto.getLossTypeEnum()).setReason(lossReportFormDto.getLossReason());
            // 生成待发送的报损单据
            supplyRequest
                    // id
                    .setId(null)
                    // 单据分类：库存供应
                    .setCategoryEnum(SupplyCategory.STOCK_SUPPLY.getValue())
                    // 单据类型：报损单
                    .setTypeEnum(SupplyType.LOSS_REPORT_FORM.getValue())
                    // 制单人
                    .setApplicantId(SecurityUtils.getLoginUser().getPractitionerId())
                    // 制单日期
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
}
