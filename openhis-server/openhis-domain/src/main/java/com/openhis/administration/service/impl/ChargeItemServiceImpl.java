package com.openhis.administration.service.impl;

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
     * 保存chargeItem相关信息
     * 
     * @return 保存结果
     */
    @Override
    public boolean saveChargeItem(ChargeItem chargeItem) {
        // 假设此处有业务相关处理
        if (chargeItem.getCode() == null) {
            return false;
        }
        return chargeItemMapper.insert(chargeItem) > 0;
    }

    /**
     * 更新收费项目
     *
     * @param chargeItem 更新内容
     * @return 更新结果
     */
    @Override
    public boolean updateChargeItem(ChargeItem chargeItem) {
        // 更新样例 一切以实际为主
        if (chargeItem.getId() != null) {
            // 获取更新前收费项目，避免更新导致数据库崩溃
            if (chargeItemMapper.selectById(chargeItem.getId()) == null) {
                return false;
            } else {
                return chargeItemMapper.updateById(chargeItem) > 0;
            }
        } else {
            return false;
        }
    }
}