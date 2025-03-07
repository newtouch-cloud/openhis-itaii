package com.openhis.administration.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.ChargeItem;
import com.openhis.workflow.domain.SupplyRequest;

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
}