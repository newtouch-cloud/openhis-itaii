package com.openhis.administration.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.Device;
import com.openhis.administration.mapper.DeviceMapper;
import com.openhis.administration.service.IDeviceService;

/**
 * 器材基本信息管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

}