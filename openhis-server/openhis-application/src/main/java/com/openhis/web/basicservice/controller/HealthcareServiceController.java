/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basicservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.openhis.administration.domain.Location;
import com.openhis.administration.mapper.LocationMapper;
import com.openhis.administration.service.IHealthcareServiceService;
import com.openhis.common.enums.AccountStatus;
import com.openhis.common.utils.HisPageUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.medication.domain.Medication;
import com.openhis.medication.domain.MedicationQueryDto;
import com.openhis.web.basicservice.dto.HealthcareServiceInitDto;
import com.openhis.web.basicservice.mapper.HealthcareServiceBizMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        // 构建查询条件
        QueryWrapper<Location> queryWrapper = HisQueryUtils.buildQueryWrapper(null, searchKey,
                new HashSet<>(Arrays.asList("name")), null);
        Page<Location> LocationPage = HisPageUtils.selectPage(
                locationMapper, queryWrapper, pageNo, pageSize, Location.class);
        List<Location> LocationList = LocationPage.getRecords();
        // 位置信息
        List<HealthcareServiceInitDto.locationIdOption> locationIdOptions = LocationList.stream()
                .map(location -> new HealthcareServiceInitDto.locationIdOption(location.getId(), location.getName()))
                .collect(Collectors.toList());
        healthcareServiceInitDto.setLocationIdOptions(locationIdOptions);
        return R.ok(healthcareServiceInitDto);
    }


}
