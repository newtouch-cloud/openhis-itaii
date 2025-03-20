/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.common.enums.LocationForm;
import com.openhis.common.enums.PriorityLevel;
import com.openhis.web.basedatamanage.appservice.ILocationAppService;
import com.openhis.web.chargemanage.appservice.IOutpatientRegistrationAppService;
import com.openhis.web.chargemanage.dto.OutpatientRegistrationAddParam;
import com.openhis.web.chargemanage.dto.OutpatientRegistrationInitDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 门诊挂号 controller
 */
@RestController
@RequestMapping("/charge-manage/register")
@Slf4j
@AllArgsConstructor
public class OutpatientRegistrationController {

    private final IOutpatientRegistrationAppService iOutpatientRegistrationAppService;
    private final ILocationAppService iLocationAppService;

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
     * 查询就诊位置
     *
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return 位置分页列表
     */
    @GetMapping(value = "/location-tree")
    public R<?> getLocationTree(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return iLocationAppService.getLocationTree(LocationForm.ROOM.getValue(), pageNo, pageSize);

    }

    /**
     * 根据位置id筛选医生
     */
    @GetMapping(value = "/practitioner-metadata")
    public R<?> getPractitionerMetadata(@RequestParam(value = "locationId") Long locationId,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return R.ok(iOutpatientRegistrationAppService.getPractitionerMetadataByLocationId(locationId, searchKey, pageNo,
            pageSize));
    }

    /**
     * 根据机构id筛选服务项目
     */
    @GetMapping(value = "/healthcare-metadata")
    public R<?> getHealthcareMetadata(@RequestParam(value = "organizationId") Long organizationId,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return R.ok(iOutpatientRegistrationAppService.getHealthcareMetadataByOrganizationId(organizationId, searchKey,
            pageNo, pageSize));
    }

    /**
     * 保存挂号
     *
     * @param outpatientRegistrationAddParam 就诊表单信息
     * @return 结果
     */
    @PostMapping(value = "/save")
    public R<?> saveRegister(@Valid @RequestBody OutpatientRegistrationAddParam outpatientRegistrationAddParam) {
        return iOutpatientRegistrationAppService.saveRegister(outpatientRegistrationAddParam);
    }

    /**
     * 查询当日就诊数据
     * 
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 当日就诊数据
     */
    @GetMapping(value = "/current-day-encounter")
    public R<?> getCurrentDayEncounter(@RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return R.ok(iOutpatientRegistrationAppService.getCurrentDayEncounter(searchKey, pageNo, pageSize));
    }

}
