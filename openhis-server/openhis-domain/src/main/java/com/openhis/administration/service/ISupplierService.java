package com.openhis.administration.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.core.common.core.domain.R;
import com.openhis.administration.domain.Supplier;
import com.openhis.workflow.domain.SupplyRequest;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * 供应商管理Service接口
 *
 * @author dh
 * @date 2025-02-20
 */
public interface ISupplierService extends IService<Supplier> {
    /**
     * 查询厂商/供应商分页列表
     *
     * @param supplier 查询条件
     * @param pageNo 查询条件
     * @param pageSize 查询条件
     * @return 供应申请列表
     */
    Page<Supplier> getPage(Supplier supplier, Integer pageNo, Integer pageSize);



}