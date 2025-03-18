/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.datadictionary.appservice;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.administration.domain.Supplier;
import com.openhis.administration.mapper.SupplierMapper;
import com.openhis.administration.service.ISupplierService;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.AccountStatus;
import com.openhis.common.enums.SupplierType;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisPageUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.datadictionary.dto.SupplierDto;
import com.openhis.web.datadictionary.dto.SupplierInitDto;
import com.openhis.web.datadictionary.dto.SupplierSearchParam;
import com.openhis.web.datadictionary.dto.SupplierUpDto;
import com.openhis.web.inventorymanage.dto.InventoryReceiptDto;
import com.openhis.web.inventorymanage.dto.InventorySearchParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
     * @param pageNo 查询条件
     * @param pageSize 查询条件
     * @return 厂商/产地查询结果
     */
    R<?> getSupplierList(SupplierSearchParam supplierSearchParam, String searchKey, Integer pageNo, Integer pageSize,
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
