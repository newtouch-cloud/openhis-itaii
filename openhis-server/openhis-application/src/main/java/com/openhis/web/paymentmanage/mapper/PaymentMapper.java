/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.paymentmanage.mapper;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.paymentmanage.dto.ChargeItemDto;
import com.openhis.web.paymentmanage.dto.DispenseQuantityDto;
import com.openhis.web.paymentmanage.dto.PaymentDetailDto;
import com.openhis.web.paymentmanage.dto.PaymentVO;

/**
 * 付款Dao
 *
 * @author SunJQ
 * @date 2025-03-29
 */
@Repository
public interface PaymentMapper {
    /**
     * 付款列表
     * 
     * @param page
     * @param queryWrapper
     * @return
     */
    IPage<PaymentVO> getPage(@org.apache.ibatis.annotations.Param("page") Page<Object> page,
        @org.apache.ibatis.annotations.Param(Constants.WRAPPER) QueryWrapper<PaymentVO> queryWrapper);

    /**
     * 付款详情
     * 
     * @param id
     * @return
     */
    List<PaymentDetailDto> getPaymentDetailList(@Param("id") Long id);

    /**
     * 查找某一类型的chargeItem
     * 
     * @param collect
     * @param s
     */
    List<ChargeItemDto> getChargeItemList(@Param("collect") String collect, @Param("s") String s);

    /**
     * 查找chargeItem集合
     * 
     * @param collect
     */
    List<ChargeItemDto> getChargeItems(@Param("collect") String collect);

    /**
     * 根据就诊ID查找收费项
     * 
     * @param encounterId
     * @return
     */
    List<ChargeItemDto> getChargeItemListByEncounterId(Long encounterId);

    /**
     * 查询发放数量
     * 
     * @param chargeItemIds 收费项ids
     * @return 发放数量
     */
    DispenseQuantityDto selectDispenseQuantity(@Param("chargeItemIds") List<Long> chargeItemIds);
}
