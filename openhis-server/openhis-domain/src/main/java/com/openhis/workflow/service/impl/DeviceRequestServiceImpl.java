package com.openhis.workflow.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class DeviceRequestServiceImpl extends ServiceImpl<DeviceRequestMapper, DeviceRequest> implements IDeviceRequestService {

}