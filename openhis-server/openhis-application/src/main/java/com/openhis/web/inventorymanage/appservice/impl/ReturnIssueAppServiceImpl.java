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
import com.core.common.utils.SecurityUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.administration.domain.Practitioner;
import com.openhis.administration.domain.Supplier;
import com.openhis.administration.service.IPractitionerService;
import com.openhis.administration.service.ISupplierService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.inventorymanage.appservice.IReturnIssueAppService;
import com.openhis.web.inventorymanage.dto.*;
import com.openhis.web.inventorymanage.mapper.ReturnIssueMapper;
import com.openhis.workflow.domain.SupplyRequest;
import com.openhis.workflow.service.ISupplyRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 退货出库 impl
 *
 * @author CY
 * @date 2025-04-03
 */
@Service
public class ReturnIssueAppServiceImpl implements IReturnIssueAppService {

    @Autowired
    private ReturnIssueMapper ReturnIssueMapper;

    @Autowired
    private ISupplyRequestService supplyRequestService;

    @Autowired
    private ISupplierService supplierService;

    @Autowired
    private IPractitionerService practitionerService;

    @Autowired
    private AssignSeqUtil assignSeqUtil;

    /**
     * 退货出库页面初始化
     *
     * @return 初始化信息
     */
    @Override
    public R<?> returnIssueInit() {

        IssueInitDto initDto = new IssueInitDto();
        // 查询供应商列表
        List<Supplier> supplierList = supplierService.getList();
        // 查询经手人列表
        List<Practitioner> practitionerList = practitionerService.getList();
        // 经手人信息
        List<IssueInitDto.practitionerListOption> practitionerListOptions = practitionerList.stream()
            .map(practitioner -> new IssueInitDto.practitionerListOption(practitioner.getId(), practitioner.getName()))
            .collect(Collectors.toList());
        // 部门列表
        List<IssueDepartmentDto> issueDepartmentDto =
            ReturnIssueMapper.selectReturnIssueDepartment(SupplyRequestType.DEPARTMENT.getValue());
        // 药品类型
        List<IssueInitDto.itemTypeOption> itemTypeOptions = Stream.of(ItemType.values())
            .map(itemType -> new IssueInitDto.itemTypeOption(itemType.getValue(), itemType.getInfo()))
            .collect(Collectors.toList());
        // 供应商信息
        List<IssueInitDto.supplierListOption> supplierListOptions = supplierList.stream()
            .map(supplier -> new IssueInitDto.supplierListOption(supplier.getId(), supplier.getName()))
            .collect(Collectors.toList());
        // 审批状态
        List<IssueInitDto.supplyStatusOption> supplyStatusOptions = Stream.of(SupplyStatus.values())
            .map(supplyStatus -> new IssueInitDto.supplyStatusOption(supplyStatus.getValue(), supplyStatus.getInfo()))
            .collect(Collectors.toList());

        initDto.setSupplierListOptions(supplierListOptions).setItemTypeOptions(itemTypeOptions)
            .setPractitionerListOptions(practitionerListOptions).setIssueDepartmentDto(issueDepartmentDto)
            .setSupplyStatusOptions(supplyStatusOptions);

        return R.ok(initDto);
    }

    /**
     * 单据号初始化
     *
     * @return 初始化信息
     */
    @Override
    public R<?> returnIssueNoInit() {
        IssueInitDto initDto = new IssueInitDto();
        // 单据号
        initDto.setBusNo(assignSeqUtil.getSeqByDay(AssignSeqEnum.RETURN_ISSUE_NUM.getPrefix(), 10));
        return R.ok(initDto);
    }

    /**
     * 退货出库单据列表
     *
     * @param issueSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 退货出库单据分页列表
     */
    @Override
    public R<?> getPage(IssueSearchParam issueSearchParam, Integer pageNo, Integer pageSize, String searchKey,
        HttpServletRequest request) {

        // 设置模糊查询的字段名
        HashSet<String> searchFields = new HashSet<>();
        searchFields.add(CommonConstants.FieldName.SupplyBusNo);

        // 构建查询条件
        QueryWrapper<IssueSearchParam> queryWrapper =
            HisQueryUtils.buildQueryWrapper(issueSearchParam, searchKey, searchFields, request);
        // 查询退货出库单据分页列表
        Page<IssuePageDto> returnReceiptPage = ReturnIssueMapper.selectReturnIssuePage(new Page<>(pageNo, pageSize),
            queryWrapper, SupplyType.RETURN_ISSUE.getValue());

        returnReceiptPage.getRecords().forEach(e -> {
            // 单据状态
            e.setStatusEnum_enumText(EnumUtils.getInfoByValue(SupplyStatus.class, e.getStatusEnum()));
        });
        return R.ok(returnReceiptPage);
    }

    /**
     * 退货出库单据详情
     *
     * @param busNo 单据号
     * @return 退货出库单据详情
     */
    @Override
    public R<?> getDetail(String busNo) {
        List<IssueDetailDto> issueDetailList = ReturnIssueMapper.returnIssueDetail(busNo,
            CommonConstants.TableName.MED_MEDICATION_DEFINITION, CommonConstants.TableName.ADM_DEVICE_DEFINITION,
            ItemType.MEDICINE.getValue(), ItemType.DEVICE.getValue());
        if (issueDetailList.isEmpty()) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00006, null));
        }
        return R.ok(issueDetailList);
    }

    /**
     * 添加/编辑退货出库单据(批量)
     *
     * @param returnIssueDtoList 退货出库单据
     * @return 操作结果
     */
    @Override
    public R<?> addOrEditIssueReceipt(List<IssueDto> returnIssueDtoList) {

        List<String> idList = new ArrayList<>();

        // 单据号取得
        List<String> busNoList =
                returnIssueDtoList.stream().map(IssueDto::getBusNo).collect(Collectors.toList());
        // 请求数据取得
        List<SupplyRequest> requestList = supplyRequestService.getSupplyByBusNo(busNoList.get(0));
        if (!requestList.isEmpty()) {
            // 请求id取得
            List<Long> requestIdList = requestList.stream().map(SupplyRequest::getId).collect(Collectors.toList());
            // 单据信息删除
            supplyRequestService.removeByIds(requestIdList);
        }

        List<SupplyRequest> supplyRequestList = new ArrayList<>();
        for (IssueDto issueDto : returnIssueDtoList) {
            // 初始化单据信息
            SupplyRequest supplyRequest = new SupplyRequest();
            BeanUtils.copyProperties(issueDto, supplyRequest);
            // 生成待发送的退货出库单据
            supplyRequest
                    // id
                    .setId(null)
                    // 单据分类：非库存供应
                    .setCategoryEnum(SupplyCategory.NON_STOCK.getValue())
                    // 单据类型：退货出库
                    .setTypeEnum(SupplyType.RETURN_ISSUE.getValue())
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
}
