/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.appservice.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.AgeCalculatorUtil;
import com.core.common.utils.MessageUtils;
import com.openhis.administration.service.IChargeItemService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.financial.service.IPaymentReconciliationService;
import com.openhis.medication.service.IMedicationDispenseService;
import com.openhis.web.chargemanage.appservice.IOutpatientRefundAppService;
import com.openhis.web.chargemanage.dto.*;
import com.openhis.web.chargemanage.mapper.OutpatientRefundAppMapper;
import com.openhis.workflow.service.IDeviceDispenseService;
import com.openhis.workflow.service.IServiceRequestService;

/**
 * 门诊退费 impl
 *
 * @author zwh
 * @date 2025-03-15
 */
@Service
public class OutpatientRefundAppServiceImpl implements IOutpatientRefundAppService {

    @Autowired
    private OutpatientRefundAppMapper outpatientRefundAppMapper;
    @Autowired
    private IPaymentReconciliationService paymentReconciliationService;
    @Autowired
    private IChargeItemService chargeItemService;
    @Autowired
    private IMedicationDispenseService medicationDispenseService;
    @Autowired
    private IDeviceDispenseService deviceDispenseService;
    @Autowired
    private IServiceRequestService serviceRequestService;

    /**
     * 门诊退费页面初始化
     *
     * @return 初始化信息
     */
    @Override
    public R<?> outpatientRefundInit() {
        OutpatientInitDto initDto = new OutpatientInitDto();
        List<OutpatientInitDto.chargeItemStatusOption> chargeItemStatusOptions = new ArrayList<>();
        chargeItemStatusOptions.add(new OutpatientInitDto.chargeItemStatusOption(ChargeItemStatus.BILLED.getValue(),
            ChargeItemStatus.BILLED.getInfo()));
        chargeItemStatusOptions.add(new OutpatientInitDto.chargeItemStatusOption(ChargeItemStatus.REFUNDING.getValue(),
            ChargeItemStatus.REFUNDING.getInfo()));
        chargeItemStatusOptions.add(new OutpatientInitDto.chargeItemStatusOption(ChargeItemStatus.REFUNDED.getValue(),
            ChargeItemStatus.REFUNDED.getInfo()));
        chargeItemStatusOptions.add(new OutpatientInitDto.chargeItemStatusOption(
            ChargeItemStatus.PART_REFUND.getValue(), ChargeItemStatus.PART_REFUND.getInfo()));
        initDto.setChargeItemStatusOptions(chargeItemStatusOptions);
        return R.ok(initDto);
    }

    /**
     * 根据就诊id查询患者的账单
     *
     * @param encounterId 就诊id
     * @return 患者账单列表
     */
    @Override
    public R<?> getEncounterPatientPayment(Long encounterId) {
        List<EncounterPatientPaymentDto> patientPaymentList =
            outpatientRefundAppMapper.selectEncounterPatientPayment(encounterId, PaymentStatus.SUCCESS.getValue(),
                PaymentStatus.REFUND_ALL.getValue(), PaymentStatus.REFUND_PART.getValue());
        return R.ok(patientPaymentList);
    }

    /**
     * 根据账单退费
     *
     * @param paymentIdList 付费id列表
     * @return 操作结果
     */
    @Override
    public R<?> refundPayment(List<Long> paymentIdList) {
        // todo：医生同意退费（审批流）
        // 根据支付id获取对应收费项目的id列表
        List<Long> chargeItemIdList = paymentReconciliationService.getChargeItemIdListByPayment(paymentIdList);

        // 根据收费项目id列表查询退费项
        List<RefundItemDto> refundItemList = outpatientRefundAppMapper.selectRefundItem(chargeItemIdList,
            CommonConstants.TableName.MED_MEDICATION_REQUEST, CommonConstants.TableName.WOR_SERVICE_REQUEST,
            CommonConstants.TableName.WOR_DEVICE_REQUEST, CommonConstants.Common.THREE);

        List<Long> medDisIdList = new ArrayList<>();
        List<Long> devDisIdList = new ArrayList<>();
        List<Long> serReqIdList = new ArrayList<>();

        for (RefundItemDto refundItem : refundItemList) {
            if (CommonConstants.TableName.MED_MEDICATION_REQUEST.equals(refundItem.getServiceTable())) {

                // 药品需要先退药
                if (DispenseStatus.COMPLETED.getValue().equals(refundItem.getRefundStatus())
                    || DispenseStatus.PART_COMPLETED.getValue().equals(refundItem.getRefundStatus())) {
                    return R
                        .fail(MessageUtils.createMessage(PromptMsgConstant.ChargeRefund.M00001, new Object[] {"药品"}));
                } else {
                    // todo:审批流药师同意退费
                    medDisIdList.add(refundItem.getDispenseId());
                }
            } else if (CommonConstants.TableName.WOR_SERVICE_REQUEST.equals(refundItem.getServiceTable())) {
                // 诊疗项目需医技科室同意退费
                if (RequestStatus.COMPLETED.getValue().equals(refundItem.getRefundStatus())) {
                    return R.fail(MessageUtils.createMessage(PromptMsgConstant.ChargeRefund.M00002, null));
                } else {
                    // todo:审批流医技师同意退费
                    serReqIdList.add(refundItem.getServiceId());
                }
            } else if (CommonConstants.TableName.WOR_DEVICE_REQUEST.equals(refundItem.getServiceTable())) {
                // 耗材需要先退药
                if (DispenseStatus.COMPLETED.getValue().equals(refundItem.getRefundStatus())
                    || DispenseStatus.PART_COMPLETED.getValue().equals(refundItem.getRefundStatus())) {
                    return R
                        .fail(MessageUtils.createMessage(PromptMsgConstant.ChargeRefund.M00001, new Object[] {"耗材"}));
                } else {
                    devDisIdList.add(refundItem.getDispenseId());
                }
            }
        }
        // 更新收费状态：已退费
        chargeItemService.updateRefundChargeStatus(chargeItemIdList);
        // 更新未发放药品状态：停止发放，停止原因：退费
        medicationDispenseService.updateStopDispenseStatus(medDisIdList, NotPerformedReason.REFUND.getValue());
        // 更新未发放耗材状态：停止发放，停止原因：退费
        deviceDispenseService.updateStopDispenseStatus(devDisIdList, NotPerformedReason.REFUND.getValue());
        // 更新执行诊疗状态：停止
        serviceRequestService.updateStopRequestStatus(serReqIdList);
        // 返回退费成功信息
        return R.ok(MessageUtils.createMessage(PromptMsgConstant.Common.M00004, new Object[] {"处方退费"}));
    }

    /**
     * 查询结算过的就诊患者分页列表
     *
     * @param encounterPatientPageParam 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @param request 请求
     * @return 就诊患者分页列表
     */
    @Override
    public R<?> getBilledEncounterPatientPage(EncounterPatientPageParam encounterPatientPageParam, String searchKey,
        Integer pageNo, Integer pageSize, HttpServletRequest request) {
        // 构建查询条件
        QueryWrapper<EncounterPatientPageParam> queryWrapper = HisQueryUtils.buildQueryWrapper(
            encounterPatientPageParam, searchKey,
            new HashSet<>(Arrays.asList(CommonConstants.FieldName.PatientWbStr, CommonConstants.FieldName.PatientPyStr,
                CommonConstants.FieldName.PatientName, CommonConstants.FieldName.PatientBusNo,
                CommonConstants.FieldName.EncounterBusNo, CommonConstants.FieldName.idCard)),
            request);
        // 就诊患者分页列表
        Page<EncounterPatientPageDto> encounterPatientPage = outpatientRefundAppMapper.selectBilledEncounterPatientPage(
            new Page<>(pageNo, pageSize), queryWrapper, ChargeItemStatus.BILLED.getValue(),
            ChargeItemStatus.REFUNDING.getValue(), ChargeItemStatus.REFUNDED.getValue(),
            ChargeItemStatus.PART_REFUND.getValue(), AccountType.MEDICAL_INSURANCE.getValue());

        encounterPatientPage.getRecords().forEach(e -> {
            // 性别枚举
            e.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, e.getGenderEnum()));
            // 收费状态枚举
            e.setStatusEnum_enumText(EnumUtils.getInfoByValue(ChargeItemStatus.class, e.getStatusEnum()));
            // 计算年龄
            e.setAge(AgeCalculatorUtil.getAge(e.getBirthDate()));
            // 合同类型枚举
            e.setCategoryEnum_enumText(EnumUtils.getInfoByValue(ContractCategory.class, e.getCategoryEnum()));
        });
        return R.ok(encounterPatientPage);
    }
}
