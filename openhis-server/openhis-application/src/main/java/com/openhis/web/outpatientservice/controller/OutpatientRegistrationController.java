/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.outpatientservice.controller;

import com.core.common.core.domain.R;
import com.openhis.common.enums.AccountStatus;
import com.openhis.common.enums.PriorityLevel;
import com.openhis.web.basicservice.dto.HealthcareServiceInitDto;
import com.openhis.web.outpatientservice.dto.OutpatientRegistrationInitDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 门诊挂号 controller
 */
@RestController
@RequestMapping("/outpatient-service/register")
@Slf4j
@AllArgsConstructor
public class OutpatientRegistrationController {

    /**
     * 门诊挂号基础数据初始化
     */
    @GetMapping(value = "/init")
    public R<?> init() {
        OutpatientRegistrationInitDto outpatientRegistrationInitDto = new OutpatientRegistrationInitDto();
        // 优先级
        List<OutpatientRegistrationInitDto.priorityLevelOption> priorityLevelOptionOptions = Stream.of(PriorityLevel.values())
                .map(e -> new OutpatientRegistrationInitDto.priorityLevelOption(e.getValue(), e.getInfo()))
                .collect(Collectors.toList());
        outpatientRegistrationInitDto.setPriorityLevelOptionOptions(priorityLevelOptionOptions);
        return R.ok(outpatientRegistrationInitDto);
    }

}
