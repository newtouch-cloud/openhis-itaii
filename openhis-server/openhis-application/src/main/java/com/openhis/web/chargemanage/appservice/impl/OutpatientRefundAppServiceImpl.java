/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.appservice.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.exception.ServiceException;
import com.core.common.utils.AgeCalculatorUtil;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.MessageUtils;
import com.openhis.administration.domain.ChargeItem;
import com.openhis.administration.service.IChargeItemService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.financial.service.IPaymentReconciliationService;
import com.openhis.medication.domain.MedicationDispense;
import com.openhis.medication.domain.MedicationRequest;
import com.openhis.medication.service.IMedicationDispenseService;
import com.openhis.medication.service.IMedicationRequestService;
import com.openhis.web.chargemanage.appservice.IOutpatientRefundAppService;
import com.openhis.web.chargemanage.dto.*;
import com.openhis.web.chargemanage.mapper.OutpatientRefundAppMapper;
import com.openhis.workflow.domain.DeviceDispense;
import com.openhis.workflow.domain.DeviceRequest;
import com.openhis.workflow.domain.ServiceRequest;
import com.openhis.workflow.service.IDeviceDispenseService;
import com.openhis.workflow.service.IDeviceRequestService;
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
    @Autowired
    private IMedicationRequestService medicationRequestService;
    @Autowired
    private IDeviceRequestService deviceRequestService;
    @Autowired
    private AssignSeqUtil assignSeqUtil;

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

        // 患者账单信息列表
        List<EncounterPatientPaymentDto> encounterPatientPaymentList =
            outpatientRefundAppMapper.selectEncounterPatientPayment(encounterId, PaymentStatus.SUCCESS.getValue(),
                PaymentStatus.REFUND_PART.getValue(), PaymentStatus.REFUND_ALL.getValue());

        // 获取付款id集合
        List<Long> paymentIdList =
            encounterPatientPaymentList.stream().map(EncounterPatientPaymentDto::getId).collect(Collectors.toList());
        if (paymentIdList.isEmpty()) {
            return R.ok(null);
        }
        // 根据付款id获取对应收费项目的id列表(费用项id)
        List<Long> chargeItemIdList = paymentReconciliationService.getChargeItemIdListByPayment(paymentIdList);

        // 根据收费项目id列表查询费用项
        List<RefundItemDto> refundItemList = outpatientRefundAppMapper.selectRefundItem(chargeItemIdList,
            CommonConstants.TableName.MED_MEDICATION_REQUEST, CommonConstants.TableName.WOR_SERVICE_REQUEST,
            CommonConstants.TableName.WOR_DEVICE_REQUEST, CommonConstants.Common.THREE);
        refundItemList.forEach(e -> {
            // 退费状态
            e.setRefundStatus_enumText(EnumUtils.getInfoByValue(ChargeItemStatus.class, e.getRefundStatus()));
            // 发放状态
            e.setDispenseStatus_enumText(EnumUtils.getInfoByValue(DispenseStatus.class, e.getDispenseStatus()));
            // 执行状态
            e.setServiceStatus_enumText(EnumUtils.getInfoByValue(RequestStatus.class, e.getServiceStatus()));

        });

        return R.ok(refundItemList);
    }

    /**
     * 根据账单退费
     *
     * @param refundItemList 退费项目id列表
     * @return 操作结果
     */
    @Override
    public R<?> refundPayment(List<RefundItemParam> refundItemList) {

        // 未退费用项,生成新的请求发放费用项
        List<RefundItemParam> creatList = new ArrayList<>();
        for (RefundItemParam param : refundItemList) {
            if (!param.getRefundFlg()) {
                // 未退费费用项list
                creatList.add(param);
            }
        }

        if (!creatList.isEmpty()) {
            // 未退费费用项id集合
            List<Long> creatChargeItemIdList =
                creatList.stream().map(RefundItemParam::getChargeItemId).collect(Collectors.toList());
            // 根据费用项id查询费用项请求发放信息
            List<RefundItemDto> creatChargeItemList = outpatientRefundAppMapper.selectRefundItem(creatChargeItemIdList,
                CommonConstants.TableName.MED_MEDICATION_REQUEST, CommonConstants.TableName.WOR_SERVICE_REQUEST,
                CommonConstants.TableName.WOR_DEVICE_REQUEST, CommonConstants.Common.THREE);

            for (RefundItemDto creatdDto : creatChargeItemList) {

                // 未退费用项,生成新的请求,发放,费用项
                if (CommonConstants.TableName.MED_MEDICATION_REQUEST.equals(creatdDto.getServiceTable())) {
                    // 药品请求查询
                    MedicationRequest medicationRequest = medicationRequestService.getById(creatdDto.getRequestId());
                    // 生成新的药品请求
                    medicationRequest.setId(null); // 药品请求id
                    medicationRequest.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.MEDICATION_RES_NO.getPrefix(), 10)); // 药品请求编码
                    medicationRequest.setPrescriptionNo(
                        String.valueOf(new StringBuilder("C").append(creatdDto.getPrescriptionNo()))); // 处方号
                    medicationRequestService.save(medicationRequest);

                    // 药品发放查询
                    MedicationDispense medicationDispense =
                        medicationDispenseService.getById(creatdDto.getDispenseId());
                    // 生成新的药品发放
                    medicationDispense.setId(null); // 药品发放id
                    medicationDispense.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.MEDICATION_DIS_NO.getPrefix(), 10)); // 药品发放编码
                    medicationDispense.setMedReqId(medicationRequest.getId()); // 药品请求id
                    medicationDispenseService.save(medicationDispense);

                    // 费用项查询
                    ChargeItem chargeItem = chargeItemService.getById(creatdDto.getChargeItemId());
                    // 生成新的费用项
                    chargeItem.setRefundId(chargeItem.getId());// 退费id
                    chargeItem.setId(null); // 费用项id
                    chargeItem.setBusNo(AssignSeqEnum.CHARGE_ITEM_NO.getPrefix().concat(medicationRequest.getBusNo())); // 编码
                    chargeItem.setPrescriptionNo(
                        String.valueOf(new StringBuilder("C").append(creatdDto.getPrescriptionNo()))); // 处方号
                    chargeItem.setServiceId(medicationRequest.getId()); // 医疗服务ID
                    chargeItem.setStatusEnum(ChargeItemStatus.PLANNED.getValue());// 收费单状态：待收费
                    chargeItemService.save(chargeItem);

                } else if (CommonConstants.TableName.WOR_SERVICE_REQUEST.equals(creatdDto.getServiceTable())) {
                    // 服务请求查询
                    ServiceRequest serviceRequest = serviceRequestService.getById(creatdDto.getRequestId());
                    // 生成新的服务请求
                    serviceRequest.setId(null); // 服务请求id
                    serviceRequest.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.SERVICE_RES_NO.getPrefix(), 10)); // 服务请求编码
                    serviceRequestService.save(serviceRequest);

                    // 费用项查询
                    ChargeItem chargeItem = chargeItemService.getById(creatdDto.getChargeItemId());
                    // 生成新的费用项
                    chargeItem.setRefundId(chargeItem.getId());// 退费id
                    chargeItem.setId(null); // 费用项id
                    chargeItem.setBusNo(AssignSeqEnum.CHARGE_ITEM_NO.getPrefix().concat(serviceRequest.getBusNo())); // 编码
                    chargeItem.setServiceId(serviceRequest.getId()); // 医疗服务ID
                    chargeItem.setStatusEnum(ChargeItemStatus.PLANNED.getValue());// 收费单状态：待收费
                    chargeItemService.save(chargeItem);

                } else if (CommonConstants.TableName.WOR_DEVICE_REQUEST.equals(creatdDto.getServiceTable())) {
                    // 耗材请求查询
                    DeviceRequest deviceRequest = deviceRequestService.getById(creatdDto.getRequestId());
                    // 生成新的耗材请求
                    deviceRequest.setId(null); // 耗材请求id
                    deviceRequest.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.DEVICE_RES_NO.getPrefix(), 10)); // 耗材请求编码
                    deviceRequestService.save(deviceRequest);

                    // 耗材发放查询
                    DeviceDispense deviceDispense = deviceDispenseService.getById(creatdDto.getDispenseId());
                    // 生成新的耗材发放
                    deviceDispense.setId(null); // 耗材id
                    deviceDispense.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.DEVICE_DIS_NO.getPrefix(), 10)); // 器材发放id
                    deviceDispense.setDeviceReqId(deviceRequest.getId()); // 器材请求id
                    deviceDispenseService.save(deviceDispense);

                    // 费用项查询
                    ChargeItem chargeItem = chargeItemService.getById(creatdDto.getChargeItemId());
                    // 生成新的费用项
                    chargeItem.setRefundId(chargeItem.getId());// 退费id
                    chargeItem.setId(null); // 费用项id
                    chargeItem.setBusNo(AssignSeqEnum.CHARGE_ITEM_NO.getPrefix().concat(deviceRequest.getBusNo())); // 编码
                    chargeItem.setServiceId(deviceRequest.getId()); // 医疗服务ID
                    chargeItem.setStatusEnum(ChargeItemStatus.PLANNED.getValue());// 收费单状态：待收费
                    chargeItemService.save(chargeItem);

                }
            }
        }

        // 全部退费
        // 退费费用项id集合
        List<Long> chargeItemIdList =
            refundItemList.stream().map(RefundItemParam::getChargeItemId).collect(Collectors.toList());

        // 根据费用项id查询费用项请求发放信息
        List<RefundItemDto> chargeItemList = outpatientRefundAppMapper.selectRefundItem(chargeItemIdList,
            CommonConstants.TableName.MED_MEDICATION_REQUEST, CommonConstants.TableName.WOR_SERVICE_REQUEST,
            CommonConstants.TableName.WOR_DEVICE_REQUEST, CommonConstants.Common.THREE);

        List<Long> medDisIdList = new ArrayList<>();
        List<Long> devDisIdList = new ArrayList<>();
        List<Long> serReqIdList = new ArrayList<>();

        for (RefundItemDto dto : chargeItemList) {

            if (CommonConstants.TableName.MED_MEDICATION_REQUEST.equals(dto.getServiceTable())) {
                // 药品需要先退药
                if (DispenseStatus.COMPLETED.getValue().equals(dto.getDispenseStatus())
                    || DispenseStatus.PART_COMPLETED.getValue().equals(dto.getDispenseStatus())) {
                    // 药品请求查询
                    MedicationRequest medicationRequest = medicationRequestService.getById(dto.getRequestId());
                    if (medicationRequest.getRefundMedicineId() != null) {
                        throw new ServiceException("已申请退药，请勿重复申请");
                    }
                    // 生成药品请求(退药)
                    medicationRequest.setId(null); // 药品请求id
                    medicationRequest.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.MEDICATION_RES_NO.getPrefix(), 10)); // 药品请求编码
                    medicationRequest.setQuantity(dto.getQuantity() * (-1)); // 请求数量
                    medicationRequest.setUnitCode(dto.getUnitCode()); // 请求单位编码
                    medicationRequest.setStatusEnum(RequestStatus.IN_REFUND.getValue()); // 请求状态
                    medicationRequest.setRefundMedicineId(dto.getRequestId()); // 退药id
                    medicationRequest
                        .setPrescriptionNo(String.valueOf(new StringBuilder("T").append(dto.getPrescriptionNo())));
                    medicationRequestService.save(medicationRequest);

                } else {
                    if (DispenseStatus.STOPPED.getValue().equals(dto.getDispenseStatus())
                        && NotPerformedReason.REFUND.getValue().equals(dto.getNotPerformedReason())) {
                        throw new ServiceException("已申请退药，请勿重复申请");
                    }
                    medDisIdList.add(dto.getDispenseId());
                }

            } else if (CommonConstants.TableName.WOR_SERVICE_REQUEST.equals(dto.getServiceTable())) {
                // 诊疗项目需医技科室同意退费
                if (RequestStatus.COMPLETED.getValue().equals(dto.getServiceStatus())) {
                    // 服务请求查询
                    ServiceRequest serviceRequest = serviceRequestService.getById(dto.getRequestId());
                    if (serviceRequest.getRefundServiceId() != null) {
                        throw new ServiceException("已申请退药，请勿重复申请");
                    }
                    // 生成服务请求(取消服务)
                    serviceRequest.setId(null); // 服务请求id
                    serviceRequest.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.SERVICE_RES_NO.getPrefix(), 10)); // 服务请求编码
                    serviceRequest.setQuantity(dto.getQuantity() * (-1)); // 请求数量
                    serviceRequest.setUnitCode(dto.getUnitCode()); // 请求单位编码
                    serviceRequest.setStatusEnum(RequestStatus.ENDED.getValue()); // 请求状态
                    serviceRequest.setRefundServiceId(dto.getRequestId()); // 退药id
                    serviceRequestService.save(serviceRequest);

                } else {
                    if (RequestStatus.STOPPED.getValue().equals(dto.getServiceStatus())) {
                        throw new ServiceException("已申请退费，请勿重复申请");
                    }
                    serReqIdList.add(dto.getServiceId());
                }

            } else if (CommonConstants.TableName.WOR_DEVICE_REQUEST.equals(dto.getServiceTable())) {
                // 耗材需要先退药
                if (DispenseStatus.COMPLETED.getValue().equals(dto.getDispenseStatus())
                    || DispenseStatus.PART_COMPLETED.getValue().equals(dto.getDispenseStatus())) {
                    // 耗材请求查询
                    DeviceRequest deviceRequest = deviceRequestService.getById(dto.getRequestId());
                    if (deviceRequest.getRefundDeviceId() != null) {
                        throw new ServiceException("已申请退药，请勿重复申请");
                    }
                    // 生成耗材请求(退耗材)
                    deviceRequest.setId(null); // 耗材请求id
                    deviceRequest.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.DEVICE_RES_NO.getPrefix(), 10)); // 耗材请求编码
                    deviceRequest.setQuantity(dto.getQuantity() * (-1)); // 请求数量
                    deviceRequest.setUnitCode(dto.getUnitCode()); // 请求单位编码
                    deviceRequest.setStatusEnum(RequestStatus.IN_REFUND.getValue()); // 请求状态
                    deviceRequest.setRefundDeviceId(dto.getRequestId()); // 退药id
                    deviceRequestService.save(deviceRequest);

                } else {
                    if (DispenseStatus.STOPPED.getValue().equals(dto.getDispenseStatus())
                        && NotPerformedReason.REFUND.getValue().equals(dto.getNotPerformedReason())) {
                        throw new ServiceException("已申请退药，请勿重复申请");
                    }
                    devDisIdList.add(dto.getDispenseId());
                }
            }
        }

        // // 付款id集合
        // List<Long> paymentIdList =
        // refundItemList.stream().map(RefundItemParam::getPaymentId).collect(Collectors.toList());
        // // 付款id去重
        // paymentIdList = paymentIdList.stream().distinct().collect(Collectors.toList());
        // // 更新支付状态：退款中
        // paymentReconciliationService.updateRefundingStatus(paymentIdList);

        // 更新收费状态：退费中
        chargeItemService.updateRefundChargeStatus(chargeItemIdList);

        if (!medDisIdList.isEmpty()) {
            // 更新未发放药品状态：停止发放，停止原因：退费
            medicationDispenseService.updateStopDispenseStatus(medDisIdList, NotPerformedReason.REFUND.getValue());
        }
        if (!devDisIdList.isEmpty()) {
            // 更新未发放耗材状态：停止发放，停止原因：退费
            deviceDispenseService.updateStopDispenseStatus(devDisIdList, NotPerformedReason.REFUND.getValue());
        }
        if (!serReqIdList.isEmpty()) {
            // 更新未执行诊疗状态：停止
            serviceRequestService.updateStopRequestStatus(serReqIdList);
        }

        // 返回退费成功信息
        return R.ok(MessageUtils.createMessage(PromptMsgConstant.Common.M00004, new Object[] {"门诊退费"}));
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
            ChargeItemStatus.PART_REFUND.getValue(), AccountType.MEDICAL_ELECTRONIC_CERTIFICATE.getCode());

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

    /**
     * 根据就诊id查询患者的退费账单
     *
     * @param encounterId 就诊id
     * @return 退费账单列表
     */
    @Override
    public R<?> getEncounterPatientRefund(Long encounterId) {
        List<EncounterPatientRefundDto> refundDtoList = outpatientRefundAppMapper.selectEncounterPatientRefund(
            encounterId, ChargeItemStatus.REFUNDING.getValue(), ChargeItemStatus.REFUNDED.getValue(),
            ChargeItemStatus.PART_REFUND.getValue(), CommonConstants.TableName.WOR_SERVICE_REQUEST,
            CommonConstants.TableName.WOR_DEVICE_REQUEST, CommonConstants.TableName.MED_MEDICATION_REQUEST);
        refundDtoList.forEach(e -> {
            // 收费状态枚举
            e.setChargeStatus_enumText(EnumUtils.getInfoByValue(ChargeItemStatus.class, e.getChargeStatus()));
            // 支付状态枚举
            e.setPaymentStatus_enumText(EnumUtils.getInfoByValue(PaymentStatus.class, e.getPaymentStatus()));
            // 发药状态
            e.setDispenseStatus_enumText(EnumUtils.getInfoByValue(DispenseStatus.class, e.getDispenseStatus()));
            // 诊疗执行枚举
            e.setServiceStatus_enumText(EnumUtils.getInfoByValue(RequestStatus.class, e.getServiceStatus()));
        });
        return R.ok(refundDtoList);
    }

    /**
     * 根据就诊id查询患者因退费重新生成的账单
     *
     * @param encounterId 就诊id
     * @return 重新生成的账单列表
     */
    @Override
    public R<?> getRegenerateCharge(Long encounterId) {
        return R.ok(chargeItemService.getRegenerateCharge(encounterId));
    }

    /**
     * 校验是否可以退费（耗材/药品是否已退，诊疗是否取消执行）
     *
     * @param chargeItemIdList 收费项目id列表
     * @return 是否可退
     */
    @Override
    public R<?> verifyRefundable(List<Long> chargeItemIdList) {
        // 查询收费项目信息
        List<ChargeItem> chargeItemList = chargeItemService.listByIds(chargeItemIdList);

        // 分别获取各个请求id列表
        List<Long> medReqIdList = new ArrayList<>();
        List<Long> devReqIdList = new ArrayList<>();
        chargeItemList.forEach(item -> {
            switch (item.getServiceTable()) {
                case CommonConstants.TableName.MED_MEDICATION_REQUEST -> medReqIdList.add(item.getServiceId());
                case CommonConstants.TableName.WOR_DEVICE_REQUEST -> devReqIdList.add(item.getServiceId());
            }
        });

        if (!medReqIdList.isEmpty()) {
            List<MedicationRequest> medicationRequestList = medicationRequestService.list(
                new LambdaQueryWrapper<MedicationRequest>().in(MedicationRequest::getRefundMedicineId, medReqIdList));
            if (!medicationRequestList.isEmpty()) {
                if (medicationRequestList.stream().map(MedicationRequest::getStatusEnum)
                    .anyMatch(x -> x.equals(RequestStatus.IN_REFUND.getValue()))) {
                    throw new ServiceException("请先退药后再退费");
                }
            }
        }
        if (!devReqIdList.isEmpty()) {
            List<DeviceRequest> deviceRequestList = deviceRequestService
                .list(new LambdaQueryWrapper<DeviceRequest>().in(DeviceRequest::getRefundDeviceId, devReqIdList));
            if (!deviceRequestList.isEmpty()) {
                if (deviceRequestList.stream().map(DeviceRequest::getStatusEnum)
                    .anyMatch(x -> x.equals(RequestStatus.IN_REFUND.getValue()))) {
                    throw new ServiceException("请先退耗材后再退费");
                }
            }
        }
        return R.ok();
    }
}
