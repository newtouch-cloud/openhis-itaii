package com.openhis.administration.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.ChargeItem;
import com.openhis.administration.mapper.ChargeItemMapper;
import com.openhis.administration.service.IChargeItemService;

import lombok.AllArgsConstructor;

/**
 * 费用项管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
@AllArgsConstructor
public class ChargeItemServiceImpl extends ServiceImpl<ChargeItemMapper, ChargeItem> implements IChargeItemService {

    private final ChargeItemMapper chargeItemMapper;

    /**
     * 创建已计费的采购账单
     *
     * @param chargeItemList 采购账单
     */
    @Override
    public void createBilledPurchaseCharge(List<ChargeItem> chargeItemList) {

    }
}