package com.openhis.workflow.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.workflow.domain.InventoryItem;
import com.openhis.workflow.mapper.InventoryItemMapper;
import com.openhis.workflow.service.IInventoryItemService;

/**
 * 库存项目管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class InventoryItemServiceImpl extends ServiceImpl<InventoryItemMapper, InventoryItem>
    implements IInventoryItemService {

    /**
     * 入库
     *
     * @param inventoryItemList 入库项目
     */
    @Override
    public void stockIn(List<InventoryItem> inventoryItemList) {
        for (InventoryItem inventoryItem : inventoryItemList) {
            // 此判断是为了避免插入时主键重复
            if (inventoryItem.getId() == null) {
                baseMapper.insert(inventoryItem);
            }
        }
    }

    /**
     * 更新库房数量
     *
     * @param id 主键
     * @param baseQuantity 常规单位库存数量
     * @param minQuantity 最小单位库存数量
     * @param now 当前时间
     * @return 更新件数
     */
    @Override
    public Boolean updateInventoryQuantity(Long id, BigDecimal baseQuantity, BigDecimal minQuantity, Date now) {
        int updateCount = baseMapper.update(null, new LambdaUpdateWrapper<InventoryItem>().eq(InventoryItem::getId, id)
            .set(InventoryItem::getUpdateTime, now).set(InventoryItem::getQuantity, minQuantity));
        return updateCount > 0;

    }

    /**
     * 查询
     *
     * @param itemId 项目编号
     * @param lotNumber 产品批号
     * @param locationId 仓库
     * @param tenantId 租户id
     * @return 单据详情
     */
    @Override
    public List<InventoryItem> selectInventoryByItemId(Long itemId, String lotNumber, Long locationId,
        Integer tenantId) {
        LambdaQueryWrapper<InventoryItem> queryWrapper =
            new LambdaQueryWrapper<InventoryItem>().eq(InventoryItem::getItemId, itemId)
                .eq(InventoryItem::getLotNumber, lotNumber).eq(InventoryItem::getTenantId, tenantId);
        if (locationId != null) {
            queryWrapper.eq(InventoryItem::getLocationId, locationId);
        }
        // 查询取库房信息
        return baseMapper.selectList(queryWrapper);
    }
}
