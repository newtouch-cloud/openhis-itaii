package com.openhis.workflow.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.common.enums.RequestStatus;
import com.openhis.workflow.domain.ServiceRequest;
import com.openhis.workflow.mapper.ServiceRequestMapper;
import com.openhis.workflow.service.IServiceRequestService;

/**
 * 服务申请管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class ServiceRequestServiceImpl extends ServiceImpl<ServiceRequestMapper, ServiceRequest>
    implements IServiceRequestService {

    /**
     * 查询服务申请管理中basedOnId相同的个数
     *
     * @param basedOnId 请求基于什么的ID
     * @return basedOnId相同的个数
     */
    @Override
    public Long countServiceRequestByBasedOnId(Long basedOnId) {
        LambdaQueryWrapper<ServiceRequest> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ServiceRequest::getBasedOnId, basedOnId);
        return baseMapper.selectCount(queryWrapper);
    }

    /**
     * 更新执行诊疗状态：停止
     *
     * @param serReqIdList 服务请求id列表
     */
    @Override
    public void updateStopRequestStatus(List<Long> serReqIdList) {
        baseMapper.update(new ServiceRequest().setStatusEnum(RequestStatus.STOPPED.getValue()),
            new LambdaUpdateWrapper<ServiceRequest>().in(ServiceRequest::getId, serReqIdList));
    }
}