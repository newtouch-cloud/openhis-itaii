/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.outpatientservice.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.core.common.core.domain.R;
import com.openhis.common.enums.PriorityLevel;
import com.openhis.web.basedatamanage.appservice.IOrganizationAppService;
import com.openhis.web.outpatientservice.appservice.IOutpatientRegistrationAppService;
import com.openhis.web.outpatientservice.dto.OutpatientRegistrationInitDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 门诊挂号 controller
 */
@RestController
@RequestMapping("/outpatient-service/register")
@Slf4j
@AllArgsConstructor
public class OutpatientRegistrationController {

    private final IOutpatientRegistrationAppService iOutpatientRegistrationAppService;
    private final IOrganizationAppService iOrganizationAppService;

    /**
     * 基础数据初始化
     */
    @GetMapping(value = "/init")
    public R<?> init() {
        OutpatientRegistrationInitDto outpatientRegistrationInitDto = new OutpatientRegistrationInitDto();
        // 优先级
        List<OutpatientRegistrationInitDto.priorityLevelOption> priorityLevelOptionOptions =
            Stream.of(PriorityLevel.values())
                .map(e -> new OutpatientRegistrationInitDto.priorityLevelOption(e.getValue(), e.getInfo()))
                .collect(Collectors.toList());
        outpatientRegistrationInitDto.setPriorityLevelOptionOptions(priorityLevelOptionOptions);
        return R.ok(outpatientRegistrationInitDto);
    }

    /**
     * 查询患者信息
     *
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 患者信息
     */
    @GetMapping(value = "/patient-metadata")
    public R<?> getPatientMetadata(@RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return R.ok(iOutpatientRegistrationAppService.getPatientMetadataBySearchKey(searchKey, pageNo, pageSize));
    }

    /**
     * 查询费用性质
     * 
     * @return 费用性质
     */
    @GetMapping(value = "/contract-list")
    public R<?> getContractList() {
        return R.ok(iOutpatientRegistrationAppService.getContractMetadata());
    }

    /**
     * 查询诊断信息
     */
    @GetMapping(value = "/condition-definition-metadata")
    public R<?> getConditionDefinitionMetadata(@RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return R
            .ok(iOutpatientRegistrationAppService.getConditionDefinitionMetadataSearchKey(searchKey, pageNo, pageSize));
    }

    /**
     * 查询就诊位置
     */

    /**
     * 根据位置id筛选医生
     */

    /**
     * 根据机构id筛选服务项目
     */

}
