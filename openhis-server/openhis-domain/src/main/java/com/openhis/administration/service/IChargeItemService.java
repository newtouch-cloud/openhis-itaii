package com.openhis.administration.service;

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
     * 保存chargeItem相关信息
     * 
     * @return 保存结果
     */
    boolean saveChargeItem(ChargeItem chargeItem);

    /**
     * 更新收费项目
     * 
     * @param chargeItem 更新内容
     * @return 更新结果
     */
    boolean updateChargeItem(ChargeItem chargeItem);
}