package com.openhis.workflow.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.workflow.domain.DeviceDispense;

/**
 * 器材发放管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IDeviceDispenseService extends IService<DeviceDispense> {

    /**
     * 更新未发放耗材状态：停止发放
     *
     * @param devDisIdList 发放id列表
     * @param refund 停止原因：退费
     */
    void updateStopDispenseStatus(List<Long> devDisIdList, Integer refund);
}