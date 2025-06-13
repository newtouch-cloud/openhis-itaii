/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.template.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.template.domain.ActivityDevice;
import com.openhis.template.mapper.ActivityDeviceMapper;
import com.openhis.template.service.IActivityDeviceService;

/**
 * TODO:概括描述当前类的主要用途和注意事项
 *
 * @author zwh
 * @date 2025-04-09
 */
@Service
public class ActivityDeviceServiceImpl extends ServiceImpl<ActivityDeviceMapper, ActivityDevice>
    implements IActivityDeviceService {}
