package com.openhis.workflow.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.core.common.core.domain.R;
import com.openhis.workflow.domain.SupplyDelivery;
import com.openhis.workflow.domain.SupplyRequest;

/**
 * 供应发放管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface ISupplyDeliveryService extends IService<SupplyDelivery> {

    /**
     * 根据单据，发放物品
     *
     * @param supplyRequestList 单据信息
     * @param supplyRequestList 单据信息
     */
    List<SupplyDelivery> createCompletedSupplyDelivery(List<SupplyRequest> supplyRequestList, Date now);
}