package com.openhis.yb.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.yb.domain.InventoryChangeRecord;
import com.openhis.yb.mapper.InventoryChangeRecordMapper;
import com.openhis.yb.service.IInventoryChangeRecordService;

/**
 * 库存信息变更记录Service业务层处理
 *
 * @author system
 * @date 2025-04-30
 */
@Service
public class InventoryChangeRecordServiceImpl extends ServiceImpl<InventoryChangeRecordMapper, InventoryChangeRecord> implements IInventoryChangeRecordService {

}