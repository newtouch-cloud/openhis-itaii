package com.openhis.workflow.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.workflow.domain.InventoryItem;

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

    /**
     * 更新库房数量
     *
     * @param id 主键
     * @param baseQuantity 常规单位库存数量
     * @param minQuantity 最小单位库存数量
     * @param now 当前时间
     * @return 更新件数
     */
    Boolean updateInventoryQuantity(Long id, BigDecimal baseQuantity, BigDecimal minQuantity, Date now);

    /**
     * 查询
     *
     * @param itemId 项目编号
     * @param lotNumber 产品批号
     * @param locationId 仓库
     * @param tenantId 租户id
     * @return 单据详情
     */
    List<InventoryItem> selectInventoryByItemId(Long itemId, String lotNumber, Long locationId,Integer tenantId);

}
