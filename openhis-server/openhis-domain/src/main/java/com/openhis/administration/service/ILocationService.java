package com.openhis.administration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.Location;

import java.util.List;

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
}