package com.openhis.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

    @Autowired
    private ServiceRequestMapper serviceRequestMapper;

    /**
     * 查询服务申请管理中basedOnId相同的个数
     *
     * @param basedOnId 请求基于什么的ID
     * @return basedOnId相同的个数
     */
    @Override
    public Long countServiceRequestByBasedOnId(Long basedOnId) {
        QueryWrapper<ServiceRequest> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("based_on_id", basedOnId);
        return serviceRequestMapper.selectCount(queryWrapper);
    }
}