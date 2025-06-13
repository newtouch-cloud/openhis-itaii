package com.openhis.administration.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.core.common.utils.SecurityUtils;
import com.openhis.administration.domain.Supplier;
import com.openhis.common.enums.SupplyStatus;
import com.openhis.workflow.domain.SupplyRequest;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.Invoice;
import com.openhis.administration.mapper.InvoiceMapper;
import com.openhis.administration.service.IInvoiceService;

import java.util.Date;
import java.util.List;

/**
 * 发票管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class InvoiceServiceImpl extends ServiceImpl<InvoiceMapper, Invoice> implements IInvoiceService {
    /**
     * 新增发票
     *
     * @param invoice 发票实体
     * @return
     */
   public Long addInvoice(Invoice invoice){
       // 根据编码判断发票是否存在
       List<Invoice> invoices =
           baseMapper.selectList(new LambdaQueryWrapper<Invoice>().eq(Invoice::getBusNo, invoice.getBusNo()));
       if (invoices.size() > 0) {
           return null;
       }
       // 新增发票
       int insert = baseMapper.insert(invoice);
       if (insert != 1) {
           return null;
       }

       return invoice.getId();
   }

}