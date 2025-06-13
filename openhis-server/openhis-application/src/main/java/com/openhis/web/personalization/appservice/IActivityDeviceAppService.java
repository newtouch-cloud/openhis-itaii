/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.personalization.appservice;

import javax.servlet.http.HttpServletRequest;

import com.core.common.core.domain.R;
import com.openhis.web.datadictionary.dto.DeviceManageSelParam;
import com.openhis.web.datadictionary.dto.DiagnosisTreatmentSelParam;
import com.openhis.web.personalization.dto.ActivityDeviceDto;

/**
 * 诊疗耗材绑定 service
 *
 * @author zwh
 * @date 2025-04-09
 */
public interface IActivityDeviceAppService {

    /**
     * 查询诊疗项目分页列表
     *
     * @param diagnosisTreatmentSelParam 查询条件
     * @param searchKey 查询条件-模糊查询
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request http请求
     * @return 诊疗项目分页列表
     */
    R<?> getActivityPage(DiagnosisTreatmentSelParam diagnosisTreatmentSelParam, String searchKey, Integer pageNo,
        Integer pageSize, HttpServletRequest request);

    /**
     * 查询耗材分页列表
     *
     * @param deviceManageSelParam 查询条件
     * @param searchKey 查询条件-模糊查询
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request http请求
     * @return 耗材分页列表
     */
    R<?> getDevicePage(DeviceManageSelParam deviceManageSelParam, String searchKey, Integer pageNo, Integer pageSize,
        HttpServletRequest request);

    /**
     * 新增/编辑诊疗耗材绑定记录
     *
     * @param activityDeviceDto 绑定信息
     * @return 操作结果
     */
    R<?> addOrEditActivityDeviceBind(ActivityDeviceDto activityDeviceDto);

    /**
     * 查询诊疗耗材绑定项目分页列表
     *
     * @param activityDeviceDto 查询条件
     * @param searchKey 查询条件-模糊查询
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return 诊疗耗材绑定项目分页列表
     */
    R<?> getActivityDeviceBindPage(ActivityDeviceDto activityDeviceDto, String searchKey, Integer pageNo,
        Integer pageSize);

    /**
     * 删除绑定记录
     *
     * @param bindId 绑定id
     * @return 操作结果
     */
    R<?> deleteActivityDeviceBind(Long bindId);

    /**
     * 诊疗耗材绑定页面初始化
     *
     * @return 初始化信息
     */
    R<?> activityDeviceInit();
}
