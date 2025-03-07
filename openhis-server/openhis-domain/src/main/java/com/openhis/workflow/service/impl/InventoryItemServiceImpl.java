package com.openhis.workflow.service.impl;

import com.openhis.workflow.domain.SupplyRequest;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.workflow.domain.InventoryItem;
import com.openhis.workflow.mapper.InventoryItemMapper;
import com.openhis.workflow.service.IInventoryItemService;

import java.util.List;

/**
 * 库存项目管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class InventoryItemServiceImpl extends ServiceImpl<InventoryItemMapper, InventoryItem> implements IInventoryItemService {

    /**
     * 入库
     *
     * @param inventoryItemList 入库项目
     */
    @Override
    public void stockIn(List<InventoryItem> inventoryItemList) {

    }
}