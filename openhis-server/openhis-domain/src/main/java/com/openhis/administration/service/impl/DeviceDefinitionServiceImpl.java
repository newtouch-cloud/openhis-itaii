package com.openhis.administration.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.DeviceDefinition;
import com.openhis.administration.mapper.DeviceDefinitionMapper;
import com.openhis.administration.service.IDeviceDefinitionService;

/**
 * 器材定义管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class DeviceDefinitionServiceImpl extends ServiceImpl<DeviceDefinitionMapper, DeviceDefinition> implements IDeviceDefinitionService {

}