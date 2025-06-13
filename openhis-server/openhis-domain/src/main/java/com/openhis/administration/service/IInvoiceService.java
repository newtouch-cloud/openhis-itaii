package com.openhis.administration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.Invoice;
import com.openhis.administration.domain.Supplier;

/**
 * 发票管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IInvoiceService extends IService<Invoice> {
    /**
     * 新增发票
     *
     * @param invoice 发票实体
     * @return
     */
    Long addInvoice(Invoice invoice);
}