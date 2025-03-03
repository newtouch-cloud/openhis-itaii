package com.openhis.administration.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.core.common.core.domain.R;
import com.openhis.administration.domain.Supplier;
import com.openhis.clinical.domain.ConditionDefinition;
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
     * 新增供应商
     *
     * @param supplier 病种目录实体
     * @return
     */
    boolean addSupplier(Supplier supplier);
}