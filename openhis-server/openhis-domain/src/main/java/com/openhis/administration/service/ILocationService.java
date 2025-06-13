package com.openhis.administration.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.Location;

/**
 * 位置管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface ILocationService extends IService<Location> {

    /**
     * 获取药房列表
     *
     * @return 药房列表
     */
    List<Location> getPharmacyList();

    /**
     * 获取药库列表
     *
     * @return 药库列表
     */
    List<Location> getCabinetList();

    /**
     * 获取药房药库列表
     *
     * @return 药房药库列表
     */
    List<Location> getPharmacyCabinetList();

    /**
     * 获取病区列表
     *
     * @return 病区列表
     */
    List<Location> getWardList();

    /**
     * 获取科室对应的病区列表
     *
     * @param id 科室id
     * @return 病区列表
     */
    List<Location> getWardList(Long id);


    /**
     * 获取病区下对应的病床列表
     *
     * @param id 病区id
     * @return 病床列表
     */
    List<Location> getBedList(Long id);

    /**
     * 依据id查询
     *
     * @param id
     * @return
     */
    Location getLocationById(Long id);

    /**
     * 新增位置信息
     * 
     * @param location 位置信息
     * @return 是否成功
     */
    boolean addLocation(Location location);

    /**
     * 编辑位置信息
     *
     * @param location 位置信息
     * @return 是否成功
     */
    boolean updateLocation(Location location);

    /**
     * 根据科室ID集合，查询对应信息
     *
     * @param ids 科室ID集合
     * @return 科室信息列表
     */
    List<Location> getLocationList(List<Long> ids);

    /**
     * 根据locationId，更新状态
     *
     * @param id 位置Id
     * @param status 操作状态
     * @return 是否成功
     */
    boolean updateStatusById(Long id, Integer status);

}