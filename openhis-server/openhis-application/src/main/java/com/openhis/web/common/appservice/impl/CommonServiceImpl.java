/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.common.appservice.impl;

import java.util.*;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.SecurityUtils;
import com.core.common.utils.StringUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.administration.domain.Location;
import com.openhis.administration.service.ILocationService;
import com.openhis.administration.service.IOrganizationService;
import com.openhis.administration.service.IPractitionerRoleService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.enums.ConditionCode;
import com.openhis.common.enums.ItemType;
import com.openhis.common.enums.OrganizationType;
import com.openhis.common.enums.PublicationStatus;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.common.appservice.ICommonService;
import com.openhis.web.common.dto.*;
import com.openhis.web.common.mapper.CommonAppMapper;
import com.openhis.workflow.domain.InventoryItem;
import com.openhis.workflow.service.IInventoryItemService;

/**
 * app常用接口
 *
 * @author zwh
 * @date 2025-04-01
 */
@Service
public class CommonServiceImpl implements ICommonService {

    @Resource
    private CommonAppMapper commonAppMapper;

    @Resource
    private ILocationService locationService;

    @Resource
    private IPractitionerRoleService practitionerRoleService;

    @Resource
    private IOrganizationService organizationService;

    @Resource
    private IInventoryItemService iInventoryItemService;

    /**
     * 获取药房列表
     *
     * @return 药房列表
     */
    @Override
    public List<LocationDto> getPharmacyList() {

        List<Location> pharmacyList = locationService.getPharmacyList();
        List<LocationDto> locationDtoList = new ArrayList<>();
        LocationDto locationDto;
        for (Location location : pharmacyList) {
            locationDto = new LocationDto();
            BeanUtils.copyProperties(location, locationDto);
            locationDtoList.add(locationDto);
        }
        return locationDtoList;
    }

    /**
     * 获取药房列表(库房用)
     *
     * @return 药房列表
     */
    @Override
    public List<LocationDto> getInventoryPharmacyList() {

        // 用户id
        Long practitionerId = SecurityUtils.getLoginUser().getPractitionerId();
        // 根据用户id获得管理库房信息
        List<Long> locationIds = practitionerRoleService.getLocationIdsByPractitionerId(practitionerId);

        List<Location> pharmacyList = locationService.getPharmacyList();
        List<LocationDto> locationDtoList = new ArrayList<>();
        LocationDto locationDto;
        for (Location location : pharmacyList) {
            for (Long locationId : locationIds) {
                if (location.getId().equals(locationId)) {
                    locationDto = new LocationDto();
                    BeanUtils.copyProperties(location, locationDto);
                    locationDtoList.add(locationDto);
                }
            }
        }
        return locationDtoList;
    }

    /**
     * 获取药库列表
     *
     * @return 药库列表
     */
    @Override
    public List<LocationDto> getCabinetList() {
        List<Location> pharmacyList = locationService.getCabinetList();
        List<LocationDto> locationDtoList = new ArrayList<>();
        LocationDto locationDto;
        for (Location location : pharmacyList) {
            locationDto = new LocationDto();
            BeanUtils.copyProperties(location, locationDto);
            locationDtoList.add(locationDto);
        }
        return locationDtoList;
    }

    /**
     * 获取药库列表(库房用)
     *
     * @return 药库列表
     */
    @Override
    public List<LocationDto> getInventoryCabinetList() {
        // 用户id
        Long practitionerId = SecurityUtils.getLoginUser().getPractitionerId();
        // 根据用户id获得管理库房信息
        List<Long> locationIds = practitionerRoleService.getLocationIdsByPractitionerId(practitionerId);

        List<Location> pharmacyList = locationService.getCabinetList();
        List<LocationDto> locationDtoList = new ArrayList<>();
        LocationDto locationDto;
        for (Location location : pharmacyList) {
            for (Long locationId : locationIds) {
                if (location.getId().equals(locationId)) {
                    locationDto = new LocationDto();
                    BeanUtils.copyProperties(location, locationDto);
                    locationDtoList.add(locationDto);
                }
            }
        }
        return locationDtoList;
    }

    /**
     * 药房药库列表
     *
     * @return 药房药库列表
     */
    @Override
    public List<LocationDto> getPharmacyCabinetList() {
        List<Location> pharmacyCabinetList = locationService.getPharmacyCabinetList();
        List<LocationDto> locationDtoList = new ArrayList<>();
        LocationDto locationDto;
        for (Location location : pharmacyCabinetList) {
            locationDto = new LocationDto();
            BeanUtils.copyProperties(location, locationDto);
            locationDtoList.add(locationDto);
        }
        return locationDtoList;
    }

    /**
     * 获取病区列表
     *
     * @return 病区列表
     */
    @Override
    public List<LocationDto> getWardList() {
        List<Location> getWardList = locationService.getWardList();
        List<LocationDto> locationDtoList = new ArrayList<>();
        LocationDto locationDto;
        for (Location location : getWardList) {
            locationDto = new LocationDto();
            BeanUtils.copyProperties(location, locationDto);
            locationDtoList.add(locationDto);
        }
        return locationDtoList;
    }

    /**
     * 库存项目下拉列表（药库业务使用）
     *
     * @param inventoryItemParam 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 库存项目信息
     */
    @Override
    public R<?> getInventoryItemList(InventoryItemParam inventoryItemParam, String searchKey, Integer pageNo,
        Integer pageSize) {
        Integer purchaseFlag = inventoryItemParam.getPurchaseFlag();
        inventoryItemParam.setPurchaseFlag(null);

        // 构建查询条件
        QueryWrapper<InventoryItemParam> queryWrapper = HisQueryUtils.buildQueryWrapper(inventoryItemParam, searchKey,
            new HashSet<>(Arrays.asList(CommonConstants.FieldName.Name, CommonConstants.FieldName.PyStr,
                CommonConstants.FieldName.WbStr)),
            null);
        // 查询库存项目信息
        IPage<InventoryItemDto> inventoryItems = commonAppMapper.selectInventoryItemList(new Page<>(pageNo, pageSize),
            CommonConstants.TableName.MED_MEDICATION_DEFINITION, CommonConstants.TableName.ADM_DEVICE_DEFINITION,
            ItemType.MEDICINE.getValue(), ItemType.DEVICE.getValue(), purchaseFlag, ConditionCode.PURCHASE.getCode(),
            PublicationStatus.RETIRED.getValue(), queryWrapper);
        List<InventoryItemDto> inventoryItemDtoList = inventoryItems.getRecords();
        inventoryItemDtoList.forEach(e -> {
            // 项目类型
            e.setItemType_enumText(EnumUtils.getInfoByValue(ItemType.class, e.getItemType()));
        });
        for (InventoryItemDto inventoryItem : inventoryItemDtoList) {
            List<UnitDto> unitList = new ArrayList<>();
            UnitDto unitDto = new UnitDto();
            // 单位列表
            unitDto.setUnitCode(inventoryItem.getUnitCode()).setMinUnitCode(inventoryItem.getMinUnitCode());
            unitList.add(unitDto);
            inventoryItem.setUnitList(unitList);
        }
        return R.ok(inventoryItemDtoList);
    }

    /**
     * 根据项目相关信息查询项目库存相关信息
     *
     * @param inventoryItemParam 项目id
     * @return 项目库存相关信息
     */
    @Override
    public R<?> getInventoryItemInfo(InventoryItemParam inventoryItemParam) {
        // 查询项目库存相关信息
        List<LocationInventoryDto> locationInventoryDtoList = commonAppMapper.selectInventoryItemInfo(
            inventoryItemParam.getOrgLocationId(), CommonConstants.TableName.MED_MEDICATION_DEFINITION,
            CommonConstants.TableName.ADM_DEVICE_DEFINITION, inventoryItemParam.getObjLocationId(),
            inventoryItemParam.getLotNumber(), inventoryItemParam.getItemId(), ConditionCode.PURCHASE.getCode());

        // 医保编码和生产厂家校验
        for (LocationInventoryDto dto : locationInventoryDtoList) {
            if (StringUtils.isNotEmpty(dto.getYbNo()) && StringUtils.isEmpty(dto.getManufacturer())) {
                return R.fail("生产厂家不能为空，请到药品目录维护");
            }
        }

        return R.ok(locationInventoryDtoList);
    }

    /**
     * 科室列表
     *
     * @return 科室列表
     */
    @Override
    public R<?> getDepartmentList() {
        return R.ok(organizationService.getList(OrganizationType.DEPARTMENT.getValue(), null));
    }

    /**
     * 根据追溯码获取药品/耗材信息
     *
     * @param traceNoList 追溯码列表
     * @return 项目信息
     */
    @Override
    public R<?> getItemInfoByTraceNo(List<String> traceNoList) {
        Map<String, String> traceNoMap = new HashMap<>();
        for (String traceNo : traceNoList) {
            if (traceNo != null && !StringUtils.isEmpty(traceNo)) {
                InventoryItem inventoryItem = iInventoryItemService.getOne(
                    new LambdaQueryWrapper<InventoryItem>().like(InventoryItem::getTraceNo, traceNo).last("LIMIT 1"));
                if (inventoryItem != null) {
                    String itemId = inventoryItem.getItemId().toString();
                    // 如果map中已经存在该itemId，拼接traceNo，否则直接添加
                    if (traceNoMap.containsKey(itemId)) {
                        String existingTraceNos = traceNoMap.get(itemId);
                        traceNoMap.put(itemId, existingTraceNos + CommonConstants.Common.COMMA + traceNo);
                    } else {
                        traceNoMap.put(itemId, traceNo);
                    }
                } else {
                    return R.ok(null);
                }
            }
        }
        return R.ok(traceNoMap);
    }

}
