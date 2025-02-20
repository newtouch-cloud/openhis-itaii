package com.openhis.administration.service.impl;

import com.core.common.utils.DateUtils;
import com.core.common.utils.StringUtils;
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
     * @return
     */
    @Override
    public boolean saveChargeItem(ChargeItem chargeItem) {
        // 假设此处有业务相关处理
        if (chargeItem.getCode() == null){
            return false;
        }
        return chargeItemMapper.insert(chargeItem) > 0;
    }
}