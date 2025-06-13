/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.datadictionary.appservice;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.core.common.core.domain.R;
import com.openhis.web.datadictionary.dto.DeviceManageSelParam;
import com.openhis.web.datadictionary.dto.DeviceManageUpDto;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 器材目录 service
 *
 * @author dh
 * @date 2025-03-10
 */
public interface IDeviceManageAppService {

    /**
     * 器材目录初始化
     *
     * @return
     */
    R<?> getDeviceManageInit();

    /**
     * 器材目录查询
     *
     * @param deviceManageSelParam 查询条件
     * @param searchKey 查询条件-模糊查询
     * @param pageNo 查询条件
     * @param pageSize 查询条件
     * @return 器材目录查询结果
     */
    R<?> getDevicePage(DeviceManageSelParam deviceManageSelParam, String searchKey, Integer pageNo, Integer pageSize,
        HttpServletRequest request);

    /**
     * 添加器材目录信息
     *
     * @param deviceManageUpDto 器材目录信息
     */
    R<?> addDevice(DeviceManageUpDto deviceManageUpDto);

    /**
     * 编辑供应商信息
     *
     * @param deviceManageDto 器材目录信息
     */
    R<?> editDevice(DeviceManageUpDto deviceManageDto);

    /**
     * 器材目录详细查询
     *
     * @param id 查询条件
     * @return 器材目录查询结果
     */
    R<?> getDeviceOne(@RequestParam Long id);

    /**
     * 器材目录停用
     *
     * @param ids 器材目录ID列表
     * @return
     */
    R<?> editDeviceStop(@RequestBody List<Long> ids);

    /**
     * 器材目录启用
     *
     * @param ids 器材目录ID列表
     * @return
     */
    R<?> editDeviceStart(@RequestBody List<Long> ids);
}
