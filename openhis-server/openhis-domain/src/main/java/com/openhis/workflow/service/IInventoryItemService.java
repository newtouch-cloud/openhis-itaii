package com.openhis.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.workflow.domain.InventoryItem;
import com.openhis.workflow.domain.SupplyRequest;

import java.util.List;

/**
 * 库存项目管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IInventoryItemService extends IService<InventoryItem> {

    /**
     * 入库
     *
     * @param InventoryItemList 入库项目
     */
    void stockIn(List<InventoryItem> InventoryItemList);
}