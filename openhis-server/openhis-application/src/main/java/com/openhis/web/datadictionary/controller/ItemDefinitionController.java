/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.datadictionary.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.common.enums.ChargeItemContext;
import com.openhis.common.enums.PublicationStatus;
import com.openhis.web.datadictionary.appservice.ItemDefinitionAppService;
import com.openhis.web.datadictionary.dto.ItemDefinitionDto;
import com.openhis.web.datadictionary.dto.ItemDefinitionInitDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 项目定价
 */
@RestController
@RequestMapping("/dict-dictionary/definition")
@Slf4j
@AllArgsConstructor
public class ItemDefinitionController {

    private final ItemDefinitionAppService itemDefinitionAppService;

    /**
     * 项目定价基础数据初始化
     * 
     * @return 项目定价基础数据
     */
    @GetMapping(value = "/init")
    public R<?> init() {
        ItemDefinitionInitDto itemDefinitionInitDto = new ItemDefinitionInitDto();
        // 状态
        List<ItemDefinitionInitDto.PublicationStatusOption> publicationStatusOptions =
            Stream.of(PublicationStatus.values())
                .map(status -> new ItemDefinitionInitDto.PublicationStatusOption(status.getValue(), status.getInfo()))
                .collect(Collectors.toList());
        itemDefinitionInitDto.setPublicationStatusOptions(publicationStatusOptions);
        // 收费项目类型
        List<ItemDefinitionInitDto.ChargeItemContextOption> chargeItemContextOptions =
            Stream.of(ChargeItemContext.values())
                .map(status -> new ItemDefinitionInitDto.ChargeItemContextOption(status.getValue(), status.getInfo()))
                .collect(Collectors.toList());
        itemDefinitionInitDto.setChargeItemContextOptions(chargeItemContextOptions);
        return R.ok(itemDefinitionInitDto);
    }

    /**
     * 项目定价 分页
     * 
     * @param itemDefinitionDto dto
     * @param searchKey 模糊查询关键字
     * @param chargeItemContext 收费项目类型
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 项目定价
     */
    @GetMapping(value = "/charge-item-info")
    public R<?> getChargeItemInfo(ItemDefinitionDto itemDefinitionDto,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "chargeItemContext") Integer chargeItemContext,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return R.ok(itemDefinitionAppService.getChargeItemInfo(itemDefinitionDto, chargeItemContext, searchKey, pageNo,
            pageSize));
    }

    /**
     * 项目定价详细
     *
     * @param id id
     * @return 项目定价详细
     */
    @GetMapping(value = "/charge-item-info-detail")
    public R<?> getChargeItemInfoDetail(@RequestParam Long id) {
        return R.ok(itemDefinitionAppService.getChargeItemInfoDetail(id));
    }

    /**
     * 改价
     * 
     * @param id id
     * @param price 价格
     * @return 结果
     */
    @PutMapping(value = "/update-charge-item")
    public R<?> updateChargeItemInfo(@RequestParam("id") Long id, @RequestParam("price") BigDecimal price) {
        return itemDefinitionAppService.updateChargeItemInfo(id, price);
    }

}
