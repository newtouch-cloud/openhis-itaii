package com.openhis.administration.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.Location;
import com.openhis.administration.mapper.LocationMapper;
import com.openhis.administration.service.ILocationService;
import com.openhis.common.enums.LocationForm;

/**
 * 位置管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class LocationServiceImpl extends ServiceImpl<LocationMapper, Location> implements ILocationService {

    /**
     * 获取药房列表
     *
     * @return 药房列表
     */
    @Override
    public List<Location> getPharmacyList() {
        return baseMapper
            .selectList(new LambdaQueryWrapper<Location>().eq(Location::getFormEnum, LocationForm.PHARMACY.getValue()));
    }

    /**
     * 获取药库列表
     *
     * @return 药库列表
     */
    @Override
    public List<Location> getCabinetList() {
        return baseMapper
            .selectList(new LambdaQueryWrapper<Location>().eq(Location::getFormEnum, LocationForm.CABINET.getValue()));
    }
}