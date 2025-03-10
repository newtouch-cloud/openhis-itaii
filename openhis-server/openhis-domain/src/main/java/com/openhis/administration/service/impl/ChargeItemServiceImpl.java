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

    /**
     * 创建已计费的采购账单
     *
     * @param chargeItemList 采购账单
     */
    @Override
    public void createBilledPurchaseCharge(List<ChargeItem> chargeItemList) {
        for (ChargeItem chargeItem : chargeItemList) {
            // 此判断是为了避免插入时主键重复
            if (chargeItem.getId() == null) {
                baseMapper.insert(chargeItem);
            }
        }
    }

    /**
     * 门诊挂号时保存 费用项
     *
     * @param chargeItem 费用项
     */
    @Override
    public void saveChargeItemByRegister(ChargeItem chargeItem) {
        baseMapper.insert(chargeItem);
    }
}