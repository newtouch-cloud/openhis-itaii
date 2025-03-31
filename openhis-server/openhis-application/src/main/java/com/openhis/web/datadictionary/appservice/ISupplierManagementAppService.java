/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.datadictionary.appservice;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.core.common.core.domain.R;
import com.openhis.web.datadictionary.dto.SupplierSearchParam;
import com.openhis.web.datadictionary.dto.SupplierUpDto;

/**
 * 厂商/产地 service
 *
 * @author dh
 * @date 2025-03-10
 */
public interface ISupplierManagementAppService {

    /**
     * 厂商/产地初始化
     *
     * @return
     */
    @GetMapping("/information-init")
    R<?> getSupplierInit();

    /**
     * 厂商/产地查询
     *
     * @param supplierSearchParam 查询条件
     * @param searchKey 查询条件-模糊查询
     * @param typeEnum 查询条件
     * @param pageNo 查询条件
     * @param pageSize 查询条件
     * @return 厂商/产地查询结果
     */
    R<?> getSupplierList(SupplierSearchParam supplierSearchParam, String searchKey,Integer typeEnum, Integer pageNo, Integer pageSize,
        HttpServletRequest request);

    /**
     * 添加供应商信息
     *
     * @param supplierUpDto 供应商信息
     */
    R<?> addSupplyRequest(SupplierUpDto supplierUpDto);

    /**
     * 编辑供应商信息
     *
     * @param supplierUpDto 供应商信息
     */
    R<?> editSupplyRequest(SupplierUpDto supplierUpDto);

    /**
     * 厂商/产地详细查询
     *
     * @param id 查询条件
     * @return 厂商/产地查询结果
     */
    R<?> getSupplierDetail(Long id);

    /**
     * 厂商/产地停用
     *
     * @param ids 厂商/产地ID列表
     * @return
     */
    R<?> editSupplierStop(@RequestBody List<Long> ids);

    /**
     * 厂商/产地启用
     *
     * @param ids 厂商/产地ID列表
     * @return
     */
    R<?> editSupplierStart(@RequestBody List<Long> ids);
}
