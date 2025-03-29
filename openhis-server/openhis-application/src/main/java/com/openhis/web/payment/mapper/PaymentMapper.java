/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.payment.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.payment.dto.PaymentDetailDto;
import com.openhis.web.payment.dto.PaymentDto;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 付款Dao
 *
 * @author SunJQ
 * @date 2025-03-29
 */
public interface PaymentMapper {
    /**
     * 付款列表
     * @param page
     * @param queryWrapper
     * @return
     */
    IPage<PaymentDto> getPage(Page<Object> page, QueryWrapper<PaymentDto> queryWrapper);

    /**
     * 付款详情
     * @param id
     * @return
     */
    List<PaymentDetailDto> getPaymentDetailList(@Param("id") Long id);
}
