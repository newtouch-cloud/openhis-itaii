/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basicservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.openhis.administration.domain.ChargeItemDefinition;
import com.openhis.administration.domain.HealthcareService;
import com.openhis.administration.domain.Location;
import com.openhis.administration.mapper.LocationMapper;
import com.openhis.administration.service.IChargeItemDefinitionService;
import com.openhis.administration.service.IHealthcareServiceService;
import com.openhis.common.enums.AccountStatus;
import com.openhis.common.enums.WhetherContainUnknown;
import com.openhis.common.utils.HisPageUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.medication.domain.Medication;
import com.openhis.medication.domain.MedicationQueryDto;
import com.openhis.web.basicservice.dto.HealthcareServiceAddOrUpdateParam;
import com.openhis.web.basicservice.dto.HealthcareServiceInitDto;
import com.openhis.web.basicservice.mapper.HealthcareServiceBizMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 服务管理 controller
 */
@RestController
@RequestMapping("/basic-service/healthcare")
@Slf4j
@AllArgsConstructor
public class HealthcareServiceController {

    private final IHealthcareServiceService iHealthcareServiceService;
    private final IChargeItemDefinitionService iChargeItemDefinitionService;


    private final HealthcareServiceBizMapper healthcareServiceBizMapper;
    private final LocationMapper locationMapper;

    /**
     * 服务管理基础数据初始化
     */
    @GetMapping(value = "/init")
    public R<?> init(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                     @RequestParam(value = "searchKey", defaultValue = "") String searchKey) {
        HealthcareServiceInitDto healthcareServiceInitDto = new HealthcareServiceInitDto();
        // 活动标记
        List<HealthcareServiceInitDto.activeFlagOption> activeFlagOptions = Stream.of(AccountStatus.values())
                .map(status -> new HealthcareServiceInitDto.activeFlagOption(status.getValue(), status.getInfo()))
                .collect(Collectors.toList());
        healthcareServiceInitDto.setActiveFlagOptions(activeFlagOptions);
 /*       // 构建查询条件
        QueryWrapper<Location> queryWrapper = HisQueryUtils.buildQueryWrapper(null, searchKey,
                new HashSet<>(Arrays.asList("name")), null);
        Page<Location> LocationPage = HisPageUtils.selectPage(
                locationMapper, queryWrapper, pageNo, pageSize, Location.class);
        List<Location> LocationList = LocationPage.getRecords();
        // 位置信息
        List<HealthcareServiceInitDto.locationIdOption> locationIdOptions = LocationList.stream()
                .map(location -> new HealthcareServiceInitDto.locationIdOption(location.getId(), location.getName()))
                .collect(Collectors.toList());
        healthcareServiceInitDto.setLocationIdOptions(locationIdOptions);*/
        // 是否需要预约
        List<HealthcareServiceInitDto.appointmentRequiredFlagOption> appointmentRequiredFlagOptions = Stream.of(WhetherContainUnknown.values())
                .map(wh -> new HealthcareServiceInitDto.appointmentRequiredFlagOption(wh.getValue(), wh.getInfo()))
                .collect(Collectors.toList());
        healthcareServiceInitDto.setAppointmentRequiredFlagOptions(appointmentRequiredFlagOptions);
        return R.ok(healthcareServiceInitDto);
    }

    /**
     * 服务管理 新增
     */
    @PostMapping(value = "/healthcare-service")
    public R<?> add(@Validated @RequestBody HealthcareServiceAddOrUpdateParam healthcareServiceAddOrUpdateParam){
        // 服务管理-表单数据
        HealthcareService healthcareServiceFormData = healthcareServiceAddOrUpdateParam.getHealthcareServiceFormData();
        // 费用定价-表单数据
        ChargeItemDefinition chargeItemDefinitionFormData = healthcareServiceAddOrUpdateParam.getChargeItemDefinitionFormData();
        // 服务管理-新增
        HealthcareService healthcareService = iHealthcareServiceService.addHealthcareService(healthcareServiceFormData);

        return null;
    }


}
