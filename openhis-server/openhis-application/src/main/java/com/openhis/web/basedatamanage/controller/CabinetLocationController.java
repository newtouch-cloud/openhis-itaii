/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.openhis.administration.domain.Location;
import com.openhis.administration.mapper.LocationMapper;
import com.openhis.administration.service.ILocationService;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.LocationBedStatus;
import com.openhis.common.enums.LocationForm;
import com.openhis.common.enums.LocationMode;
import com.openhis.common.enums.LocationStatus;
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
@RequestMapping("/basedatamanage/cabinet-location")
@Slf4j
@AllArgsConstructor
public class CabinetLocationController {

    private final ILocationService locationService;
    private final ILocationAppService iLocationAppService;

    @Autowired
    private LocationMapper locationMapper;

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

        Page<LocationQueryDto> locationTree = iLocationAppService.getLocationTree(formKey, pageNo, pageSize);
        return R.ok(locationTree, MessageUtils.createMessage(PromptMsgConstant.Common.M00009, new Object[] {"机构信息"}));

    }

    /**
     * 添加库房位置信息
     *
     * @param locationQueryDto 库房位置信息
     */
    @PostMapping("/cabinet-location")
    public R<?> addLocation(@Validated @RequestBody LocationQueryDto locationQueryDto) {

        // 设置为库房
        // LocationQueryDto locationQuery = new LocationQueryDto(LocationForm.CABINET);
        // locationQueryDto.setFormEnum(LocationForm.CABINET);
        // BeanUtils.copyProperties(locationQueryDto, location);
        Location location = new Location(locationQueryDto.getId(), locationQueryDto.getBusNo(),
            locationQueryDto.getName(), LocationStatus.ACTIVE, LocationBedStatus.U, LocationMode.INSTANCE,
            locationQueryDto.getTypeCode(), locationQueryDto.getTypeJson(), locationQueryDto.getPyStr(),
            locationQueryDto.getWbStr(), LocationForm.CABINET.getValue(), locationQueryDto.getOrganizationId(),
            locationQueryDto.getDisplayOrder());

        boolean saveLocationSuccess = locationService.save(location);
        return saveLocationSuccess
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00001, new Object[] {"位置信息"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00003, new Object[] {"位置信息"}));
    }

    /**
     * 获取库房位置需要编辑的信息
     *
     * @param locationId 库房位置信息Id
     */
    @GetMapping("/cabinet-location-getById")
    public R<?> getLocationById(@Validated @RequestParam Long locationId) {

        Location location = locationService.getById(locationId);
        return R.ok(location, MessageUtils.createMessage(PromptMsgConstant.Common.M00009, new Object[] {"位置信息"}));
    }

    /**
     * 编辑库房位置信息
     *
     * @param locationQueryDto 库房位置信息
     */
    @PutMapping("/cabinet-location")
    public R<?> editLocation(@Validated @RequestBody LocationQueryDto locationQueryDto) {

        Location location = new Location(locationQueryDto.getId(), locationQueryDto.getBusNo(),
            locationQueryDto.getName(), LocationStatus.ACTIVE, LocationBedStatus.U, LocationMode.INSTANCE,
            locationQueryDto.getTypeCode(), locationQueryDto.getTypeJson(), locationQueryDto.getPyStr(),
            locationQueryDto.getWbStr(), LocationForm.CABINET.getValue(), locationQueryDto.getOrganizationId(),
            locationQueryDto.getDisplayOrder());

        boolean updateLocationSuccess = locationService.updateById(location);
        return updateLocationSuccess
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"位置信息"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, new Object[] {"位置信息"}));
    }

    /**
     * 删除库房位置信息
     *
     * @param locationId 库房位置信息Id
     */
    @DeleteMapping("/cabinet-location")
    public R<?> deleteLocation(@RequestParam Long locationId) {

        boolean deleteLocationSuccess = locationService.removeById(locationId);

        return deleteLocationSuccess
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00005, new Object[] {"位置信息"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00006, new Object[] {"位置信息"}));
    }

}
