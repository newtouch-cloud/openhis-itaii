package com.openhis.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.core.common.core.domain.model.LoginUser;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.workflow.domain.InventoryItem;
import com.openhis.workflow.mapper.InventoryItemMapper;
import com.openhis.workflow.service.IInventoryItemService;

import java.math.BigDecimal;
import java.util.Date;
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
     * @param loginUser 登录用户信息
     * @param now 当前时间
     * @return 更新件数
     */
    @Override
    public Boolean updateInventoryQuantity(Long id, BigDecimal baseQuantity,BigDecimal minQuantity, LoginUser loginUser, Date now) {

        int updateCount = baseMapper.update(null,
                new LambdaUpdateWrapper<InventoryItem>().eq(InventoryItem::getId, id)
                        .set(InventoryItem::getUpdateTime, now)
                        .set(InventoryItem::getUpdateBy, loginUser.getUserId())
                        .set(InventoryItem::getBaseQuantity, baseQuantity)
                        .set(InventoryItem::getMinQuantity, minQuantity));

        return updateCount > 0;

    }

    /**
     * 查询库房信息
     *
     * @param lotNumber 产品批号
     * @param locationId 仓库
     * @param locationStoreId 库位
     */
    @Override
    public InventoryItem selectInventoryByLotNumber(String lotNumber, Long locationId, Long locationStoreId) {

        // 查询取库房信息
        InventoryItem inventoryItem =
                baseMapper.selectOne(new LambdaQueryWrapper<InventoryItem>()
                        .eq(InventoryItem::getLotNumber, lotNumber)
                        .eq(InventoryItem::getLocationId, locationId)
                        .eq(InventoryItem::getLocationStoreId, locationStoreId));
        if (inventoryItem == null) {
            return null;
        }

        return inventoryItem;

    }

}