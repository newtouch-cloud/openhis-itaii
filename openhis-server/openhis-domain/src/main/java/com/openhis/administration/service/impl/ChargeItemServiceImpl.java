package com.openhis.administration.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.ChargeItem;
import com.openhis.administration.mapper.ChargeItemMapper;
import com.openhis.administration.service.IChargeItemService;
import com.openhis.common.enums.ChargeItemStatus;

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

    /**
     * 更改就诊患者账户类型
     *
     * @param encounterId 就诊患者
     * @param accountId 账户id
     * @return 更新结果
     */
    @Override
    public boolean updateAccountType(Long encounterId, Long accountId) {
        int update = baseMapper.update(new ChargeItem().setAccountId(accountId),
            new LambdaUpdateWrapper<ChargeItem>().eq(ChargeItem::getEncounterId, encounterId));
        return update > 0;
    }

    /**
     * 根据收费项目id列表获取收费信息
     *
     * @param chargeItemIdList 收费项目id列表
     * @return 收费信息
     */
    @Override
    public List<ChargeItem> getChargeItemInfo(List<Long> chargeItemIdList) {
        return baseMapper.selectBatchIds(chargeItemIdList);
    }

    /**
     * 更新收费状态：已退费
     *
     * @param chargeItemIdList 收费id列表
     */
    @Override
    public void updateRefundChargeStatus(List<Long> chargeItemIdList) {
        baseMapper.update(new ChargeItem().setStatusEnum(ChargeItemStatus.REFUNDED.getValue()),
            new LambdaUpdateWrapper<ChargeItem>().in(ChargeItem::getId, chargeItemIdList));
    }
}