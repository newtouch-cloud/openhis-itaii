/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.common.appservice;

import java.util.List;

import com.core.common.core.domain.R;
import com.openhis.web.common.dto.InventoryItemParam;
import com.openhis.web.common.dto.LocationDto;

/**
 * app常用接口
 *
 * @author zwh
 * @date 2025-04-01
 */
public interface ICommonService {

    /**
     * 药房列表
     *
     * @return 药房列表
     */
    List<LocationDto> getPharmacyList();

    /**
     * 药房列表(库房用)
     *
     * @return 药房列表
     */
    List<LocationDto> getInventoryPharmacyList();

    /**
     * 药库列表
     *
     * @return 药库列表
     */
    List<LocationDto> getCabinetList();

    /**
     * 药库列表(库房用)
     *
     * @return 药库列表
     */
    List<LocationDto> getInventoryCabinetList();

    /**
     * 药房药库列表
     *
     * @return 药房药库列表
     */
    List<LocationDto> getPharmacyCabinetList();

    /**
     * 获取病区列表
     *
     * @return 药库列表
     */
    List<LocationDto> getWardList();

    /**
     * 库存项目下拉列表（药库业务使用）
     *
     * @param inventoryItemParam 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 库存项目信息
     */
    R<?> getInventoryItemList(InventoryItemParam inventoryItemParam, String searchKey, Integer pageNo,
        Integer pageSize);

    /**
     * 根据项目相关信息查询项目库存相关信息
     *
     * @param inventoryItemParam 项目id
     * @return 项目库存相关信息
     */
    R<?> getInventoryItemInfo(InventoryItemParam inventoryItemParam);

    /**
     * 科室列表
     *
     * @return 科室列表
     */
    R<?> getDepartmentList();

    /**
     * 根据追溯码获取药品/耗材信息
     *
     * @param traceNoList 追溯码列表
     * @return 项目信息
     */
    R<?> getItemInfoByTraceNo(List<String> traceNoList);
}
