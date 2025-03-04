/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.mapper;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.openhis.workflow.domain.SupplyRequest;

/**
 * 库存管理查询用 mapper
 *
 * @author zwh
 * @date 2025-02-25
 */
@Repository
public interface InventoryManageMapper extends BaseMapper<SupplyRequest> {}
