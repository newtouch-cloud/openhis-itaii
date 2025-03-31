/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.chargemanage.dto.EncounterPatientPageDto;
import com.openhis.web.chargemanage.dto.EncounterPatientPageParam;
import com.openhis.web.chargemanage.dto.EncounterPatientPaymentDto;
import com.openhis.web.chargemanage.dto.RefundItemDto;

/**
 * 门诊退费 appMapper
 *
 * @author zwh
 * @date 2025-03-15
 */
@Repository
public interface OutpatientRefundAppMapper {

    /**
     * 获取就诊患者账单列表
     *
     * @param encounterId 就诊id
     * @param success 支付状态：成功
     * @param refundAll 支付状态：全部退款
     * @param refundPart 支付状态：部分退款
     * @return 就诊患者账单列表
     */
    List<EncounterPatientPaymentDto> selectEncounterPatientPayment(@Param("encounterId") Long encounterId,
        @Param("success") Integer success, @Param("refundAll") Integer refundAll,
        @Param("refundPart") Integer refundPart);

    /**
     * 查询退费项目
     * 
     * @param chargeItemIdList 收费项列表
     * @param medMedicationRequest 药品请求表
     * @param worServiceRequest 服务请求表
     * @param worDeviceRequest 耗材请求表
     * @param three 用于字符截位
     * @return 退费项目列表
     */
    List<RefundItemDto> selectRefundItem(@Param("chargeItemIdList") List<Long> chargeItemIdList,
        @Param("medMedicationRequest") String medMedicationRequest,
        @Param("worServiceRequest") String worServiceRequest, @Param("worDeviceRequest") String worDeviceRequest,
        @Param("three") Integer three);

    /**
     * 查询已结算就诊患者分页列表
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @param billed 收费状态：已结算
     * @param refunding 收费状态：退费中
     * @param refunded 收费状态：已退费
     * @param partRefund 收费状态：部分退费
     * @param insurance 账户类型：医保
     * @return 已结算就诊患者分页列表
     */
    Page<EncounterPatientPageDto> selectBilledEncounterPatientPage(@Param("page") Page<EncounterPatientPageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<EncounterPatientPageParam> queryWrapper, @Param("billed") Integer billed,
        @Param("refunding") Integer refunding, @Param("refunded") Integer refunded,
        @Param("partRefund") Integer partRefund, @Param("insurance") Integer insurance);
}
