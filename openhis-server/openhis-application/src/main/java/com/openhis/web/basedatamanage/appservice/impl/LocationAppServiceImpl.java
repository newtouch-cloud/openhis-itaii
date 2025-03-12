package com.openhis.web.basedatamanage.appservice.impl;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.openhis.common.enums.LocationBedStatus;
import com.openhis.common.enums.LocationMode;
import com.openhis.common.enums.LocationStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.administration.domain.Location;
import com.openhis.administration.service.ILocationService;
import com.openhis.common.enums.LocationForm;
import com.openhis.common.utils.EnumUtils;
import com.openhis.web.basedatamanage.appservice.ILocationAppService;
import com.openhis.web.basedatamanage.dto.LocationQueryDto;

@Service
public class LocationAppServiceImpl implements ILocationAppService {

    @Resource
    ILocationService locationService;

    @Override
    public Page<LocationQueryDto> getLocationTree(Integer formKey, Integer pageNo, Integer pageSize) {

        QueryWrapper<Location> queryWrapper = new QueryWrapper<>();
        if (formKey != null) {
            queryWrapper.eq("form_enum", formKey);
        }

        // 查询位置列表
        Page<Location> page = locationService.page(new Page<>(pageNo, pageSize), queryWrapper);
        List<Location> locationList = page.getRecords();
        // 将位置列表转为树结构
        List<LocationQueryDto> locationTree = buildTree(locationList);
        Page<LocationQueryDto> locationQueryDtoPage = new Page<>(pageNo, pageSize, page.getTotal());
        locationQueryDtoPage.setRecords(locationTree);

        locationQueryDtoPage.getRecords().forEach(e -> {
            // 物理形式枚举回显赋值
            e.setFormEnum_enumText(EnumUtils.getInfoByValue(LocationForm.class, e.getFormEnum()));
            // 状态编码回显赋值
            e.setStatusEnum_enumText(EnumUtils.getInfoByValue(LocationStatus.class, e.getStatusEnum()));
            // 操作状态回显赋值
            e.setOperationalEnum_enumText(EnumUtils.getInfoByValue(LocationBedStatus.class, e.getOperationalEnum()));
            // 模式编码回显赋值
            e.setModeEnum_enumText(EnumUtils.getInfoByValue(LocationMode.class, e.getModeEnum()));
        });

        return locationQueryDtoPage;
    }

    /**
     * 将位置列表转换为树结构
     *
     * @param records 位置列表
     * @return tree
     */
    private List<LocationQueryDto> buildTree(List<Location> records) {
        // 按b_no的层级排序，确保父节点先处理
        List<Location> sortedRecords = records.stream()
            .sorted(Comparator.comparingInt(r -> r.getBusNo().split("\\.").length)).collect(Collectors.toList());

        Map<String, LocationQueryDto> nodeMap = new HashMap<>();
        List<LocationQueryDto> tree = new ArrayList<>();

        for (Location record : sortedRecords) {
            String bNo = record.getBusNo();
            String[] parts = bNo.split("\\.");
            LocationQueryDto node = new LocationQueryDto();
            BeanUtils.copyProperties(record, node);
            // 将当前节点加入映射
            nodeMap.put(bNo, node);

            if (parts.length == 1) {
                // 根节点
                tree.add(node);
            } else {
                // 获取父节点的b_no（去掉最后一部分）
                String parentBNo = String.join(".", Arrays.copyOf(parts, parts.length - 1));
                LocationQueryDto parent = nodeMap.get(parentBNo);

                if (parent != null) {
                    parent.getChildren().add(node);
                } else {
                    // 处理父节点不存在的情况（例如数据缺失）
                    // 可根据需求调整为将节点加入根或抛出异常
                    tree.add(node);
                }
            }
        }
        return tree;
    }

}
