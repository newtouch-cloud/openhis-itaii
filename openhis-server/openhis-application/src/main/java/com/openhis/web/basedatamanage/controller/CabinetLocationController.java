/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.controller;

import java.util.Arrays;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.administration.domain.Location;
import com.openhis.administration.mapper.LocationMapper;
import com.openhis.administration.service.ILocationService;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.LocationForm;
import com.openhis.common.utils.HisPageUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.basedatamanage.dto.LocationQueryDto;
import com.openhis.web.basedatamanage.dto.LocationQueryParam;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 位置管理Controller业务层处理
 *
 * @author
 * @date 2025-02-21
 */
@RestController
@RequestMapping("/basedatamanage/cabinet-location")
@Slf4j
@AllArgsConstructor
public class CabinetLocationController {

    private final ILocationService locationService;

    @Autowired
    private LocationMapper locationMapper;

    /**
     * 位置分页列表
     *
     * @param locationQueryParam 查询字段
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 位置分页列表
     */
    @GetMapping(value = "/cabinet-location")
    public R<?> getLocationPage(LocationQueryParam locationQueryParam,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {

        // 构建查询条件
        QueryWrapper<Location> queryWrapper = HisQueryUtils.buildQueryWrapper(locationQueryParam, searchKey,
            new HashSet<>(Arrays.asList("name", "py_str", "wb_str")), request);

        // 设置排序
        queryWrapper.orderByDesc("create_time");
        // 执行分页查询并转换为 locationQueryDtoPage
        Page<LocationQueryDto> locationQueryDtoPage =
            HisPageUtils.selectPage(locationMapper, queryWrapper, pageNo, pageSize, LocationQueryDto.class);

        return R.ok(locationQueryDtoPage,
            MessageUtils.createMessage(PromptMsgConstant.Common.M00009, new Object[] {"位置信息"}));
    }

    /**
     * 添加库房位置信息
     *
     * @param locationQueryDto 位置信息
     */
    @PostMapping("/cabinet-location")
    public R<?> addLocation(@Validated @RequestBody LocationQueryDto locationQueryDto) {

        LocationQueryDto locationQuery = new LocationQueryDto(LocationForm.CABINET);
        Location location = new Location();
        BeanUtils.copyProperties(locationQuery, location);

        boolean saveLocationSuccess = locationService.save(location);

        return saveLocationSuccess
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00001, new Object[] {"位置信息"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00003, new Object[] {"位置信息"}));
    }

    /**
     * 获取位置需要编辑的信息
     *
     * @param locationId 位置信息
     */
    @GetMapping("/cabinet-location-editById")
    public R<?> getLocationById(@Validated @RequestParam Long locationId) {

        Location location = locationService.getById(locationId);
        return R.ok(location, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"位置信息"}));
    }

    /**
     * 编辑位置信息
     *
     * @param location 位置信息
     */
    @PutMapping("/cabinet-location")
    public R<?> editLocation(@Validated @RequestBody Location location) {

        boolean updateLocationSuccess = locationService.updateById(location);

        return updateLocationSuccess
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"位置信息"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, new Object[] {"位置信息"}));
    }

    /**
     * 删除位置信息
     *
     * @param locationId 主表id
     */
    @DeleteMapping("/cabinet-location")
    public R<?> deleteLocation(@RequestParam Long locationId) {

        boolean deleteLocationSuccess = locationService.removeById(locationId);

        return deleteLocationSuccess
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00005, new Object[] {"位置信息"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00006, new Object[] {"位置信息"}));
    }

}
