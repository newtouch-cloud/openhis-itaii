/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.basedatamanage.appservice.ILocationAppService;
import com.openhis.web.basedatamanage.dto.LocationAddOrEditDto;
import com.openhis.web.basedatamanage.dto.LocationPageParam;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 位置管理Controller业务层处理
 *
 * @author
 * @date 2025-02-21
 */
@RestController
@RequestMapping("/base-data-manage/location")
@Slf4j
@AllArgsConstructor
public class LocationController {

    @Resource
    private ILocationAppService locationAppService;

    /**
     * 位置初始化
     *
     * @return 初始化信息
     */
    @GetMapping(value = "/init")
    public R<?> init() {
        return locationAppService.locationInit();
    }

    /**
     * 位置分页列表-树型
     *
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return 位置分页列表
     */
    @GetMapping(value = "/location-page-tree")
    public R<?> getLocationPage(@RequestParam(required = false, value = "formKey", defaultValue = "") Integer formKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return locationAppService.getLocationTree(formKey, pageNo, pageSize);
    }

    /**
     * 位置分页列表
     *
     * @param locationPageParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询条件
     * @param request 请求
     * @return 位置分页列表
     */
    @GetMapping(value = "/location-page")
    public R<?> getLocationPage(LocationPageParam locationPageParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
        @RequestParam(name = "searchKey", required = false) String searchKey, HttpServletRequest request) {
        return locationAppService.getLocationPage(locationPageParam, searchKey, pageNo, pageSize, request);
    }

    /**
     * 获取位置信息详情
     *
     * @param locationId 位置Id
     * @return 位置信息
     */
    @GetMapping("/location")
    public R<?> getLocationById(@RequestParam Long locationId) {
        return locationAppService.getLocationById(locationId);
    }

    /**
     * 新增位置信息
     *
     * @param locationAddOrEditDto 库房位置信息
     * @return 操作结果
     */
    @PostMapping("/location")
    public R<?> addLocation(@Validated @RequestBody LocationAddOrEditDto locationAddOrEditDto) {
        return locationAppService.addLocation(locationAddOrEditDto);
    }

    /**
     * 编辑位置信息
     *
     * @param locationAddOrEditDto 库房位置信息
     * @return 操作结果
     */
    @PutMapping("/location")
    public R<?> editLocation(@Validated @RequestBody LocationAddOrEditDto locationAddOrEditDto) {
        return locationAppService.editLocation(locationAddOrEditDto);
    }

    /**
     * 删除位置信息
     *
     * @param busNo 位置信息编码
     * @return 操作结果
     */
    @DeleteMapping("/location")
    public R<?> deleteLocation(@RequestParam String busNo) {
        return locationAppService.deleteLocation(busNo);
    }

}
