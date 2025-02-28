/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basicservice.controller;

import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.openhis.administration.domain.ChargeItemDefinition;
import com.openhis.administration.domain.HealthcareService;
import com.openhis.administration.service.IChargeItemDefinitionService;
import com.openhis.administration.service.IHealthcareServiceService;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.AccountStatus;
import com.openhis.common.enums.WhetherContainUnknown;
import com.openhis.web.basicservice.dto.HealthcareServiceAddOrUpdateParam;
import com.openhis.web.basicservice.dto.HealthcareServiceInitDto;
import com.openhis.web.basicservice.mapper.HealthcareServiceBizMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 服务管理基础数据初始化
     */
    @GetMapping(value = "/init")
    public R<?> init() {
        HealthcareServiceInitDto healthcareServiceInitDto = new HealthcareServiceInitDto();
        // 活动标记
        List<HealthcareServiceInitDto.activeFlagOption> activeFlagOptions = Stream.of(AccountStatus.values())
                .map(status -> new HealthcareServiceInitDto.activeFlagOption(status.getValue(), status.getInfo()))
                .collect(Collectors.toList());
        healthcareServiceInitDto.setActiveFlagOptions(activeFlagOptions);
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
    @Transactional(rollbackFor = Exception.class)
    public R<?> add(@Validated @RequestBody HealthcareServiceAddOrUpdateParam healthcareServiceAddOrUpdateParam) {
        // 服务管理-表单数据
        HealthcareService healthcareServiceFormData = healthcareServiceAddOrUpdateParam.getHealthcareServiceFormData();
        // 费用定价-表单数据
        ChargeItemDefinition chargeItemDefinitionFormData = healthcareServiceAddOrUpdateParam.getChargeItemDefinitionFormData();
        // 服务管理-新增
        HealthcareService healthcareService = iHealthcareServiceService.addHealthcareService(healthcareServiceFormData);
        // 同时保存费用定价
        boolean res = iChargeItemDefinitionService.addChargeItemDefinitionByHealthcareService(healthcareService, chargeItemDefinitionFormData);
        return res ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00001, new Object[]{"服务管理"})) :
                R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00010, null));
    }


}
