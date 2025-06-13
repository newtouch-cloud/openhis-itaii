package com.openhis.workflow.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.core.common.core.domain.R;
import com.core.common.core.domain.model.LoginUser;
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
     * @param now 当前时间
     */
    List<SupplyDelivery> createCompletedSupplyDelivery(List<SupplyRequest> supplyRequestList, Date now);

    /**
     * 校验(已经审批通过的单号(发放状态是已完成),不能再重复审批通过)
     *
     * @param supplyReqIdList 供应申请id列表
     */
    boolean supplyDeliveryValidation(List<Long> supplyReqIdList);
}
