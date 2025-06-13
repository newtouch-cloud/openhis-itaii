/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.personalization.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.datadictionary.dto.DeviceManageSelParam;
import com.openhis.web.datadictionary.dto.DiagnosisTreatmentSelParam;
import com.openhis.web.personalization.appservice.IActivityDeviceAppService;
import com.openhis.web.personalization.dto.ActivityDeviceDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 耗材诊疗绑定 controller
 *
 * @author zwh
 * @date 2025-04-09
 */
@RestController
@RequestMapping("/personalization/activity-device")
@Slf4j
@AllArgsConstructor
public class ActivityDeviceController {

    @Resource
    private IActivityDeviceAppService activityDeviceAppService;

    /**
     * 诊疗耗材绑定页面初始化
     *
     * @return 初始化信息
     */
    @GetMapping(value = "/init")
    public R<?> activityDeviceInit() {
        return activityDeviceAppService.activityDeviceInit();
    }

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
    @GetMapping("/activity-page")
    public R<?> getActivityPage(DiagnosisTreatmentSelParam diagnosisTreatmentSelParam,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        return activityDeviceAppService.getActivityPage(diagnosisTreatmentSelParam, searchKey, pageNo, pageSize,
            request);
    }

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
    @GetMapping("/device-page")
    public R<?> getDevicePage(DeviceManageSelParam deviceManageSelParam,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "50") Integer pageSize, HttpServletRequest request) {
        return activityDeviceAppService.getDevicePage(deviceManageSelParam, searchKey, pageNo, pageSize, request);
    }

    /**
     * 查询诊疗耗材绑定项目分页列表
     *
     * @param activityDeviceDto 查询条件
     * @param searchKey 查询条件-模糊查询
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return 诊疗耗材绑定项目分页列表
     */
    @GetMapping("/activity-device")
    public R<?> getActivityDeviceBindPage(ActivityDeviceDto activityDeviceDto,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return activityDeviceAppService.getActivityDeviceBindPage(activityDeviceDto, searchKey, pageNo, pageSize);
    }

    /**
     * 新增/编辑诊疗耗材绑定记录
     *
     * @param activityDeviceDto 绑定信息
     * @return 操作结果
     */
    @PostMapping("/activity-device")
    public R<?> addOrEditActivityDeviceBind(@Validated @RequestBody ActivityDeviceDto activityDeviceDto) {
        return activityDeviceAppService.addOrEditActivityDeviceBind(activityDeviceDto);
    }

    /**
     * 删除绑定记录
     *
     * @param bindId 绑定id
     * @return 操作结果
     */
    @DeleteMapping("/activity-device")
    public R<?> deleteActivityDeviceBind(Long bindId) {
        return activityDeviceAppService.deleteActivityDeviceBind(bindId);
    }
}
