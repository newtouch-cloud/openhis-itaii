package com.openhis.workflow.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.workflow.domain.ServiceRequestDetail;
import com.openhis.workflow.mapper.ServiceRequestDetailMapper;
import com.openhis.workflow.service.IServiceRequestDetailService;

/**
 * 服务申请详情管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class ServiceRequestDetailServiceImpl extends ServiceImpl<ServiceRequestDetailMapper, ServiceRequestDetail> implements IServiceRequestDetailService {

}