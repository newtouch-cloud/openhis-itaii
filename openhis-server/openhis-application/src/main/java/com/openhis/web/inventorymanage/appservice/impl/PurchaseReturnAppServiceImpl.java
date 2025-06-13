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
import com.openhis.administration.service.IPractitionerService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.common.dto.UnitDto;
import com.openhis.web.inventorymanage.appservice.IPurchaseReturnAppService;
import com.openhis.web.inventorymanage.dto.*;
import com.openhis.web.inventorymanage.mapper.PurchaseReturnMapper;
import com.openhis.workflow.domain.SupplyRequest;
import com.openhis.workflow.service.ISupplyRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 采购退货 impl
 *
 * @author yuanzs
 * @date 2025-04-02
 */
@Service
public class PurchaseReturnAppServiceImpl implements IPurchaseReturnAppService {

    @Autowired
    private AssignSeqUtil assignSeqUtil;

    @Autowired
    private IPractitionerService practitionerService;

    @Autowired
    private PurchaseReturnMapper purchaseReturnMapper;

    @Autowired
    private ISupplyRequestService supplyRequestService;

    /**
     * 采购退货页面初始化
     *
     * @return 初始化信息
     */
    @Override
    public R<?> purchaseReturnInit() {

        PurchaseReturnInitDto initDto = new PurchaseReturnInitDto();

        // 查询经手人列表
        List<Practitioner> practitionerList = practitionerService.getList();
        // 经手人信息
        List<PurchaseInventoryInitDto.practitionerListOption> practitionerListOptions = practitionerList.stream()
            .map(practitioner -> new PurchaseInventoryInitDto.practitionerListOption(practitioner.getId(),
                practitioner.getName()))
            .collect(Collectors.toList());

        initDto.setPractitionerListOptions(practitionerListOptions);

        return R.ok(initDto);
    }

    /**
     * 单据号初始化
     *
     * @return 初始化信息
     */
    @Override
    public R<?> purchaseNoInit() {
        PurchaseInventoryInitDto initDto = new PurchaseInventoryInitDto();
        // 单据号
        initDto.setBusNo(assignSeqUtil.getSeqByDay(AssignSeqEnum.RETURN_BUS_NO.getPrefix(), 10));
        return R.ok(initDto);
    }

    /**
     * 根据单据号查询退货单据详情
     *
     * @param busNo 单据号
     * @return 退货单
     */
    @Override
    public R<?> getDetail(String busNo) {
        List<PurchaseReturnDetailDto> returnDetailList = purchaseReturnMapper.selectDetail(busNo,
            CommonConstants.TableName.MED_MEDICATION_DEFINITION, CommonConstants.TableName.ADM_DEVICE_DEFINITION,
            ItemType.MEDICINE.getValue(), ItemType.DEVICE.getValue(), SupplyType.PRODUCT_RETURN.getValue(), SupplyStatus.AGREE.getValue());
        if (returnDetailList.isEmpty()) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00006, null));
        }
        for (PurchaseReturnDetailDto dto : returnDetailList) {
            List<UnitDto> unitList = new ArrayList<>();
            UnitDto unitDto = new UnitDto();
            // 单位列表
            unitDto.setUnitCode(dto.getUnitCode()).setMinUnitCode(dto.getMinUnitCode());
            unitList.add(unitDto);
            dto.setUnitList(unitList);
        }
        return R.ok(returnDetailList);
    }

    /**
     * 根据单据号查询已生成的退货单
     *
     * @param busNo 单据号
     * @return 退货单
     */
    @Override
    public R<?> getGeneratedPage(String busNo) {
        // 查询条件：
        // 1.类型为采购退货
        // 2.状态为待审请，审核中，驳回，审核通过
        List<PurchaseReturnPageDto> generatedDetailList = purchaseReturnMapper.selectGeneratedDetail(busNo,
            SupplyType.PRODUCT_RETURN.getValue());
        if (generatedDetailList.isEmpty()) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00006, null));
        }
        for (PurchaseReturnPageDto dto : generatedDetailList) {
            // 单据状态
            dto.setStatusEnum_enumText(EnumUtils.getInfoByValue(SupplyStatus.class, dto.getStatusEnum()));
            // 单据类型
            dto.setTypeEnum_enumText(EnumUtils.getInfoByValue(SupplyType.class, dto.getTypeEnum()));
        }
        return R.ok(generatedDetailList);
    }

    /**
     * 生成退货单(批量)
     *
     * @param purchaseReturnDetailDtoList 退货单据号列表
     * @return 操作结果
     */
    @Override
    public R<?> returnGenerateBusNo(List<PurchaseReturnDetailDto> purchaseReturnDetailDtoList) {

        List<String> idList = new ArrayList<>();

        // 单据号取得
        List<String> busNoList =
                purchaseReturnDetailDtoList.stream().map(PurchaseReturnDetailDto::getBusNo).collect(Collectors.toList());
        // 请求数据取得
        List<SupplyRequest> requestList = supplyRequestService.getSupplyByBusNo(busNoList.get(0));
        if (!requestList.isEmpty()) {
            // 请求id取得
            List<Long> requestIdList = requestList.stream().map(SupplyRequest::getId).collect(Collectors.toList());
            // 单据信息删除
            supplyRequestService.removeByIds(requestIdList);
        }

        List<SupplyRequest> supplyRequestList = new ArrayList<>();
        for (PurchaseReturnDetailDto purchaseReturnDetailDto : purchaseReturnDetailDtoList) {
            // 初始化单据信息
            SupplyRequest supplyRequest = new SupplyRequest();
            BeanUtils.copyProperties(purchaseReturnDetailDto, supplyRequest);
            // 生成商品退货单据
            supplyRequest
                    // id
                    .setId(null)
                    // 单据类型：采购退货
                    .setTypeEnum(SupplyType.PRODUCT_RETURN.getValue())
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

        // 返回单据id
        return R.ok(idList, null);

    }

    /**
     * 删除退回退货单
     *
     * @param supplyRequestIds 供应请求id
     * @return 操作结果
     */
    @Override
    public R<?> returnDeleteBusNo(List<Long> supplyRequestIds) {
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
     * 驳回审批
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
