package com.openhis.workflow.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.common.enums.DispenseStatus;
import com.openhis.workflow.domain.DeviceDispense;
import com.openhis.workflow.mapper.DeviceDispenseMapper;
import com.openhis.workflow.service.IDeviceDispenseService;

/**
 * 器材发放管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class DeviceDispenseServiceImpl extends ServiceImpl<DeviceDispenseMapper, DeviceDispense>
    implements IDeviceDispenseService {

    /**
     * 更新未发放耗材状态：停止发放
     *
     * @param devDisIdList 发放id列表
     * @param refund 停止原因：退费
     */
    @Override
    public void updateStopDispenseStatus(List<Long> devDisIdList, Integer refund) {
        baseMapper.update(
            new DeviceDispense().setStatusEnum(DispenseStatus.STOPPED.getValue()).setNotPerformedReasonEnum(refund),
            new LambdaUpdateWrapper<DeviceDispense>().in(DeviceDispense::getId, devDisIdList));
    }
}