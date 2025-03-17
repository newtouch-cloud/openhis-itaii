/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.basedatamanage.appservice.ILocationAppService;
import com.openhis.web.basedatamanage.dto.LocationQueryDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 位置管理Controller业务层处理
 *
 * @author
 * @date 2025-02-21
 */
@RestController
@RequestMapping("/base-data-manage/cabinet-location")
@Slf4j
@AllArgsConstructor
public class CabinetLocationController {

    private final ILocationAppService iLocationAppService;

    /**
     * 位置分页列表
     *
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return 位置分页列表
     */
    @GetMapping(value = "/cabinet-location")
    public R<?> getLocationPage(@RequestParam(required = false, value = "formKey", defaultValue = "") Integer formKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return iLocationAppService.getLocationTree(formKey, pageNo, pageSize);
    }

    /**
     * 获取库房位置需要编辑的信息
     *
     * @param locationId 库房位置信息Id
     */
    @GetMapping("/cabinet-location-getById")
    public R<?> getLocationById(@Validated @RequestParam Long locationId) {
        return iLocationAppService.getLocationById(locationId);
    }

    /**
     * 编辑库房位置信息
     *
     * @param locationQueryDto 库房位置信息
     */
    @PutMapping("/cabinet-location")
    public R<?> addOrEditLocation(@Validated @RequestBody LocationQueryDto locationQueryDto) {
        return iLocationAppService.addOrEditInventoryReceipt(locationQueryDto);
    }

    /**
     * 删除库房位置信息
     *
     * @param locationId 库房位置信息Id
     */
    @DeleteMapping("/cabinet-location")
    public R<?> deleteLocation(@RequestParam Long locationId) {
        return iLocationAppService.deleteLocation(locationId);
    }

}
