/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.paymentmanage.mapper;

import java.util.List;

import com.openhis.web.paymentmanage.dto.EleInvoiceChargeDetailDto;
import com.openhis.web.paymentmanage.dto.EleInvoiceListDetailDto;

import com.openhis.web.paymentmanage.dto.EleInvoicePatientInfoDto;
import com.openhis.web.paymentmanage.dto.EleInvoicePaymentInfoDto;
import org.apache.ibatis.annotations.Param;

/**
 * 电子发票
 *
 * @author yuxj
 * @date 2025-04-22
 */
public interface EleInvoiceMapper {

    /**
     * 获取付款信息
     *
     * @param paymentId 付款ID
     * @param encounterId 就诊ID
     */
    EleInvoicePaymentInfoDto getPaymentInfo(@Param("paymentId") Long paymentId,@Param("encounterId") Long encounterId,@Param("selfYbZhPay") Integer selfYbZhPay, @Param("selfCashPay") Integer selfCashPay,
        @Param("selfCashVxValue") Integer selfCashVxValue, @Param("selfCashAliValue") Integer selfCashAliValue,
        @Param("selfCashUnionValue") Integer selfCashUnionValue, @Param("ybFundPay") Integer ybFundPay, @Param("otherPay") Integer otherPay, @Param("selfYbZhGjValue") Integer selfYbZhGjValue);

    /**
     * 获取患者信息
     *
     * @param encounterId 就诊ID
     * @param encClassEnum 类别编码
     * @param orgClassEnum 类别编码
     */
    EleInvoicePatientInfoDto getPatientInfo(@Param("encounterId") Long encounterId,
        @Param("encClassEnum") Integer encClassEnum, @Param("orgClassEnum") Integer orgClassEnum);

    /**
     * 获取收费项目明细
     *
     * @param encounterId 就诊ID
     * @param ybTypeList 医疗收费项目类别
     * @param chrgitmType 字典类型
     * @param contrastType 对照类型
     */
    List<EleInvoiceChargeDetailDto> getChargeDetail(@Param("encounterId") Long encounterId,
        @Param("ybTypeList") List<String> ybTypeList, @Param("chrgitmType") String chrgitmType,
        @Param("contrastType") Integer contrastType,@Param("chargeItemIds") List<Long> chargeItemIds);

    /**
     * 获取清单项目明细
     *
     * @param encounterId 就诊ID
     * @param ybTypeList 医疗收费项目类别
     * @param chrgitmType 字典类型
     * @param contrastType 对照类型
     */
    List<EleInvoiceListDetailDto> getListDetail(@Param("encounterId") Long encounterId,
        @Param("ybTypeList") List<String> ybTypeList, @Param("chrgitmType") String chrgitmType,
        @Param("contrastType") Integer contrastType,@Param("chargeItemIds") List<Long> chargeItemIds);

    /**
     * 获取挂号清单项目明细
     *
     * @param encounterId 就诊ID
     * @param ybTypeList 医疗收费项目类别
     * @param chrgitmType 字典类型
     * @param contrastType 对照类型
     */
    List<EleInvoiceListDetailDto> getRegListDetail(@Param("encounterId") Long encounterId,
                                                @Param("ybTypeList") List<String> ybTypeList, @Param("chrgitmType") String chrgitmType,
                                                @Param("contrastType") Integer contrastType,@Param("chargeItemIds") List<Long> chargeItemIds);

}
