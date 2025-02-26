package com.openhis.administration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.DeviceDefinition;

/**
 * 器材定义管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IDeviceDefinitionService extends IService<DeviceDefinition> {

    /**
     * 新增器材目录
     *
     * @param deviceDefinition 耗材器材实体
     * @return
     */
    boolean addDevice(DeviceDefinition deviceDefinition);

    /**
     * 新增医保器材目录
     *
     * @param deviceDefinition 器材目录实体
     * @return
     */
    boolean addYbDevice(DeviceDefinition deviceDefinition);
}