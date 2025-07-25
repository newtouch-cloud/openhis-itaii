package com.openhis.administration.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.ChargeItem;

/**
 * 费用项管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IChargeItemService extends IService<ChargeItem> {

    /**
     * 创建已计费的采购账单
     * 
     * @param chargeItemList 采购账单
     */
    void createBilledPurchaseCharge(List<ChargeItem> chargeItemList);

    /**
     * 门诊挂号时保存 费用项
     * 
     * @param chargeItem 费用项
     */
    void saveChargeItemByRegister(ChargeItem chargeItem);

    /**
     * 更改就诊患者账户类型
     *
     * @param encounterId 就诊患者
     * @param accountId 账户id
     * @return 更新结果
     */
    boolean updateAccountType(Long encounterId, Long accountId);

    /**
     * 根据收费项目id列表获取收费信息
     *
     * @param chargeItemIdList 收费项目id列表
     * @return 收费信息
     */
    List<ChargeItem> getChargeItemInfo(List<Long> chargeItemIdList);

    /**
     * 更新收费状态：已退费
     *
     * @param chargeItemIdList 收费id列表
     */
    void updateRefundChargeStatus(List<Long> chargeItemIdList);
}