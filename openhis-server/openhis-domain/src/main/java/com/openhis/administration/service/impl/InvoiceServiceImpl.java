package com.openhis.administration.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.Invoice;
import com.openhis.administration.mapper.InvoiceMapper;
import com.openhis.administration.service.IInvoiceService;

/**
 * 发票管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class InvoiceServiceImpl extends ServiceImpl<InvoiceMapper, Invoice> implements IInvoiceService {

}