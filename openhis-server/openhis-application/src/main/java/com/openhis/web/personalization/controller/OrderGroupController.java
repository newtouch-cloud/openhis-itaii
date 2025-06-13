/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.personalization.controller;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.personalization.appservice.IOrderGroupAppService;
import com.openhis.web.personalization.dto.OrderGroupDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 组套 controller
 *
 * @author yangmo
 * @date 2025-04-10
 */
@RestController
@RequestMapping("/personalization/order-group")
@Slf4j
@AllArgsConstructor
public class OrderGroupController {

    @Resource
    private IOrderGroupAppService orderGroupAppService;

    /**
     * 组套页面初始化
     *
     * @return 初始化信息
     */
    @GetMapping(value = "/init")
    public R<?> init() {
        return orderGroupAppService.init();
    }

    /**
     * 查询组套信息
     *
     * @return 组套信息
     */
    @GetMapping("/order-group")
    public R<?> getOrderGroup(OrderGroupDto orderGroupDto, String searchKey) {
        return orderGroupAppService.getOrderGroup(orderGroupDto, searchKey);
    }

    /**
     * 新增组套信息
     *
     * @param orderGroupDto 组套信息
     * @return 操作结果
     */
    @PostMapping("/order-group")
    public R<?> addOrderGroup(@Validated @RequestBody OrderGroupDto orderGroupDto) {
        return orderGroupAppService.addOrderGroup(orderGroupDto);
    }

    /**
     * 编辑组套信息
     *
     * @param orderGroupDto 组套信息
     * @return 操作结果
     */
    @PutMapping("/order-group")
    public R<?> editOrderGroup(@Validated @RequestBody OrderGroupDto orderGroupDto) {
        return orderGroupAppService.editOrderGroup(orderGroupDto);
    }

    /**
     * 删除组套信息
     *
     * @param id 组套id
     * @return 操作结果
     */
    @DeleteMapping("/order-group")
    public R<?> deleteOrderGroup(@RequestParam Long id) {
        return orderGroupAppService.deleteOrderGroup(id);
    }
}
