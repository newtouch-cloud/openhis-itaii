package com.openhis.administration.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.core.common.utils.SecurityUtils;
import com.openhis.administration.domain.Location;
import com.openhis.administration.mapper.LocationMapper;
import com.openhis.administration.service.ILocationService;
import com.openhis.common.enums.LocationBedStatus;
import com.openhis.common.enums.LocationForm;
import com.openhis.common.enums.PublicationStatus;

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

    /**
     * 获取药房药库列表
     *
     * @return 药房药库列表
     */
    @Override
    public List<Location> getPharmacyCabinetList() {
        return baseMapper.selectList(new LambdaQueryWrapper<Location>().in(Location::getFormEnum,
            LocationForm.CABINET.getValue(), LocationForm.PHARMACY.getValue()));
    }

    /**
     * 获取病区列表
     *
     * @return 病区列表
     */
    @Override
    public List<Location> getWardList() {
        return baseMapper
            .selectList(new LambdaQueryWrapper<Location>().eq(Location::getFormEnum, LocationForm.WARD.getValue()));
    }

    /**
     * 依据id查询
     *
     * @param id
     * @return
     */
    public Location getLocationById(Long id) {

        return baseMapper.selectById(id);

    }

    /**
     * 获取科室对应的病区列表
     *
     * @param id 科室id
     * @return 病区列表
     */
    @Override
    public List<Location> getWardList(Long id) {

        LambdaQueryWrapper<Location> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Location::getId, Location::getName) // 只查询 id 和 name 字段
            .eq(Location::getOrganizationId, id) // 科室id
            .eq(Location::getFormEnum, LocationForm.WARD.getValue()); // form 值为 4:病区

        // 查询满足条件的 Location 对象列表
        List<Location> locations = baseMapper.selectList(queryWrapper);

        return locations;
    }

    /**
     * 获取病区下对应的病床列表
     *
     * @param id 病区id
     * @return 病床列表
     */
    @Override
    public List<Location> getBedList(Long id) {

        Location location = baseMapper.selectById(id);
        if (location == null || location.getBusNo() == null) {
            return null;
        }
        LambdaQueryWrapper<Location> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Location::getId, Location::getName) // 只查询 id 和 name 字段
            .likeRight(Location::getBusNo, location.getBusNo()) // 查询病区busNo开头的
            .eq(Location::getFormEnum, LocationForm.BED.getValue()) // form 值为 8:病床
            .eq(Location::getOperationalEnum, LocationBedStatus.U.getValue());// 空闲状态

        // 查询满足条件的 Location 对象列表
        List<Location> locations = baseMapper.selectList(queryWrapper);

        return locations;
    }

    /**
     * 新增位置信息
     *
     * @param location 位置信息
     * @return 是否成功
     */
    @Override
    public boolean addLocation(Location location) {
        return baseMapper.insert(location) > 0;
    }

    /**
     * 编辑位置信息
     *
     * @param location 位置信息
     * @return 是否成功
     */
    @Override
    public boolean updateLocation(Location location) {
        return baseMapper.update(location,
            new LambdaUpdateWrapper<Location>().eq(Location::getId, location.getId())) > 0;
    }

    /**
     * 根据科室ID集合，查询对应信息
     *
     * @param ids 科室ID集合
     * @return 科室信息列表
     */
    @Override
    public List<Location> getLocationList(List<Long> ids) {
        // 如果 ids 是空的，获取当前登录用户的 orgId
        if (ids == null || ids.isEmpty()) {
            Long orgId = SecurityUtils.getLoginUser().getOrgId();
            if (orgId != null) {
                ids = Collections.singletonList(orgId); // 将 orgId 转换为单元素列表
            }
        }

        // 创建查询条件
        LambdaQueryWrapper<Location> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Location::getId, Location::getName) // 指定只查询 id 和 name 字段
            .in(Location::getId, ids)// 条件：id 在传入的 ids 集合中
            .eq(Location::getStatusEnum, PublicationStatus.ACTIVE.getValue()); // 状态有效的

        // 查询满足条件的 Supplier 对象列表
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 根据locationId，更新状态
     *
     * @param id 位置Id
     * @param status 操作状态
     * @return 是否成功
     */
    @Override
    public boolean updateStatusById(Long id, Integer status) {

        Location location = new Location();
        location.setId(id);
        location.setOperationalEnum(status);

        return baseMapper.updateById(location) > 0;
    }

}