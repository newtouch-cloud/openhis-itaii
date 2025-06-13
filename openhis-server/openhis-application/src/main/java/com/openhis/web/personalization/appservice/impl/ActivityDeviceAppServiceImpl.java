/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.personalization.appservice.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.PublicationStatus;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.template.domain.ActivityDevice;
import com.openhis.template.service.IActivityDeviceService;
import com.openhis.web.datadictionary.appservice.IDeviceManageAppService;
import com.openhis.web.datadictionary.appservice.IDiagTreatMAppService;
import com.openhis.web.datadictionary.dto.DeviceManageSelParam;
import com.openhis.web.datadictionary.dto.DiagnosisTreatmentSelParam;
import com.openhis.web.personalization.appservice.IActivityDeviceAppService;
import com.openhis.web.personalization.dto.ActivityDeviceDto;
import com.openhis.web.personalization.dto.ActivityDeviceInitDto;
import com.openhis.web.personalization.mapper.ActivityDeviceAppMapper;

/**
 * 诊疗耗材绑定 实现类
 *
 * @author zwh
 * @date 2025-04-09
 */
@Service
public class ActivityDeviceAppServiceImpl implements IActivityDeviceAppService {

    @Resource
    private IDiagTreatMAppService diagnosisTreatmentManageAppService;
    @Resource
    private IDeviceManageAppService deviceManageAppService;
    @Resource
    private IActivityDeviceService activityDeviceService;
    @Resource
    private ActivityDeviceAppMapper activityDeviceAppMapper;

    /**
     * 诊疗耗材绑定页面初始化
     *
     * @return 初始化信息
     */
    @Override
    public R<?> activityDeviceInit() {
        ActivityDeviceInitDto initDto = new ActivityDeviceInitDto();
        // 状态
        List<ActivityDeviceInitDto.statusOption> statusOptions = new ArrayList<>();
        statusOptions.add(new ActivityDeviceInitDto.statusOption(PublicationStatus.ACTIVE.getValue(),
            PublicationStatus.ACTIVE.getInfo()));
        statusOptions.add(new ActivityDeviceInitDto.statusOption(PublicationStatus.RETIRED.getValue(),
            PublicationStatus.RETIRED.getInfo()));
        initDto.setStatusOptions(statusOptions);
        return R.ok(initDto);
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
    @Override
    public R<?> getActivityPage(DiagnosisTreatmentSelParam diagnosisTreatmentSelParam, String searchKey, Integer pageNo,
        Integer pageSize, HttpServletRequest request) {
        return diagnosisTreatmentManageAppService.getDiseaseTreatmentPage(diagnosisTreatmentSelParam, searchKey, pageNo,
            pageSize, request);
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
    @Override
    public R<?> getDevicePage(DeviceManageSelParam deviceManageSelParam, String searchKey, Integer pageNo,
        Integer pageSize, HttpServletRequest request) {
        // 只查询单次消耗类耗材
        deviceManageSelParam.setCategoryCode(CommonConstants.BusinessName.SINGLE_CONSUMPTION);
        return deviceManageAppService.getDevicePage(deviceManageSelParam, searchKey, pageNo, pageSize, request);
    }

    /**
     * 新增/编辑诊疗耗材绑定记录
     *
     * @param activityDeviceDto 绑定信息
     * @return 操作结果
     */
    @Override
    public R<?> addOrEditActivityDeviceBind(ActivityDeviceDto activityDeviceDto) {
        ActivityDevice activityDevice = new ActivityDevice();
        if (activityDeviceDto.getId() != null) {
            activityDeviceService.update(new LambdaUpdateWrapper<ActivityDevice>()
                .set(ActivityDevice::getQuantity, activityDeviceDto.getQuantity())
                .set(ActivityDevice::getRangeCode, activityDeviceDto.getRangeCode())
                .set(ActivityDevice::getStatusEnum, activityDeviceDto.getStatusEnum())
                .set(ActivityDevice::getDevActTable, activityDeviceDto.getDevActTable())
                .eq(ActivityDevice::getId, activityDeviceDto.getId()));
            return R.ok();
        } else {
            activityDevice.setDevActId(activityDeviceDto.getDevActId())
                .setDevActTable(activityDeviceDto.getDevActTable()).setItemNo(activityDeviceDto.getItemNo())
                .setQuantity(activityDeviceDto.getQuantity()).setRangeCode(activityDeviceDto.getRangeCode())
                .setTypeCode(activityDeviceDto.getTypeCode());
            activityDeviceService.save(activityDevice);
        }
        return R.ok(activityDevice.getId().toString());
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
    @Override
    public R<?> getActivityDeviceBindPage(ActivityDeviceDto activityDeviceDto, String searchKey, Integer pageNo,
        Integer pageSize) {

        // 构建查询条件
        QueryWrapper<ActivityDeviceDto> queryWrapper = HisQueryUtils.buildQueryWrapper(activityDeviceDto, searchKey,
            new HashSet<>(Arrays.asList(CommonConstants.FieldName.ActivityName, CommonConstants.FieldName.ActivityWbStr,
                CommonConstants.FieldName.ActivityPyStr, CommonConstants.FieldName.DevicePyStr,
                CommonConstants.FieldName.DeviceWbStr, CommonConstants.FieldName.DeviceName)),
            null);

        // 查询诊疗耗材绑定项目分页列表
        Page<ActivityDeviceDto> activityDevicePage =
            activityDeviceAppMapper.selectActivityDevicePage(new Page<>(pageNo, pageSize), queryWrapper);
        activityDevicePage.getRecords().forEach(e -> {
            // 状态
            e.setStatusEnum_enumText(EnumUtils.getInfoByValue(PublicationStatus.class, e.getStatusEnum()));
        });
        return R.ok(activityDevicePage);
    }

    /**
     * 删除绑定记录
     *
     * @param bindId 绑定id
     * @return 操作结果
     */
    @Override
    public R<?> deleteActivityDeviceBind(Long bindId) {
        activityDeviceService.removeById(bindId);
        return R.ok(MessageUtils.createMessage(PromptMsgConstant.Common.M00005, null));
    }
}
