package com.openhis.workflow.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.Practitioner;
import com.openhis.common.enums.RequestStatus;
import com.openhis.workflow.domain.DeviceRequest;
import com.openhis.workflow.mapper.DeviceRequestMapper;
import com.openhis.workflow.service.IDeviceRequestService;

/**
 * 器材请求管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class DeviceRequestServiceImpl extends ServiceImpl<DeviceRequestMapper, DeviceRequest>
    implements IDeviceRequestService {

    /**
     * 器材请求状态：已完成
     *
     * @param id ID
     * @param loginUser 登录用户信息
     */
    @Override
    public boolean completedStatusEnum(Long id, Practitioner loginUser) {
        int updateCount = baseMapper.update(null,
            new LambdaUpdateWrapper<DeviceRequest>().eq(DeviceRequest::getId, id)
                .set(DeviceRequest::getStatusEnum, RequestStatus.COMPLETED.getValue())
                .set(DeviceRequest::getPerformerId, loginUser.getId())
                .set(DeviceRequest::getLocationId, loginUser.getOrgId()));
        return updateCount > 0;
    }

    /**
     * 器材请求状态：已发送
     *
     * @param id ID
     * @param loginUser 登录用户信息
     */
    @Override
    public boolean activeStatusEnum(Long id, Practitioner loginUser) {
        int updateCount = baseMapper.update(null,
            new LambdaUpdateWrapper<DeviceRequest>().eq(DeviceRequest::getId, id)
                .set(DeviceRequest::getStatusEnum, RequestStatus.ACTIVE.getValue())
                .set(DeviceRequest::getPerformerId, loginUser.getId())
                .set(DeviceRequest::getLocationId, loginUser.getOrgId()));
        return updateCount > 0;
    }

    /**
     * 更新请求状态：已完成
     *
     * @param devRequestIdList 耗材请求id列表
     */
    @Override
    public void updateCompletedStatusBatch(List<Long> devRequestIdList) {
        baseMapper.update(new DeviceRequest().setStatusEnum(RequestStatus.COMPLETED.getValue()),
            new LambdaUpdateWrapper<DeviceRequest>().in(DeviceRequest::getId, devRequestIdList));
    }

    /**
     * 更新请求状态：待发送
     *
     * @param devReqIdList 耗材请求id列表
     */
    @Override
    public void updateDraftStatusBatch(List<Long> devReqIdList) {
        baseMapper.update(null, new LambdaUpdateWrapper<DeviceRequest>().in(DeviceRequest::getId, devReqIdList)
            .set(DeviceRequest::getStatusEnum, RequestStatus.DRAFT.getValue()));
    }
}