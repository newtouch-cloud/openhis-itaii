package com.openhis.administration.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.DeviceDefinition;
import com.openhis.administration.mapper.DeviceDefinitionMapper;
import com.openhis.administration.service.IDeviceDefinitionService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 器材定义管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Slf4j
@Service
@AllArgsConstructor
public class DeviceDefinitionServiceImpl extends ServiceImpl<DeviceDefinitionMapper, DeviceDefinition>
    implements IDeviceDefinitionService {

    private final DeviceDefinitionMapper deviceDefinitionMapper;

    @Override
    public boolean addDevice(DeviceDefinition deviceDefinition) {
        // 根据器材编码判断器材是否存在
        List<DeviceDefinition> activityDefinitions = deviceDefinitionMapper.selectList(
            new LambdaQueryWrapper<DeviceDefinition>().eq(DeviceDefinition::getBusNo, deviceDefinition.getBusNo()));
        if (activityDefinitions.size() > 0) {
            return false;
        }
        // 新增器材项目
        int insert = deviceDefinitionMapper.insert(deviceDefinition);
        if (insert != 1) {
            return false;
        }
        return true;
    }

    @Override
    public boolean addYbDevice(DeviceDefinition deviceDefinition) {
        return false;
    }
}