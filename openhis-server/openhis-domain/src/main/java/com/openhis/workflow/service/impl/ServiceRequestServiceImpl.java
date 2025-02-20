package com.openhis.workflow.service.impl;

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

}