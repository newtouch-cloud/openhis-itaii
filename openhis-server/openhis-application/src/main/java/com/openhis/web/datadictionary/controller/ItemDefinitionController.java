/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.datadictionary.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.enums.DefinitionTypeEnum;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.administration.domain.ChargeItemDefApp;
import com.openhis.administration.domain.ChargeItemDefinition;
import com.openhis.administration.service.IChargeItemDefAppService;
import com.openhis.administration.service.IChargeItemDefinitionService;
import com.openhis.administration.service.IDeviceDefinitionService;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.medication.service.IMedicationDefinitionService;
import com.openhis.web.datadictionary.dto.ChargeItemDefPageDto;
import com.openhis.web.datadictionary.dto.ItemDefSearchParam;
import com.openhis.web.datadictionary.dto.ItemDefinitionDto;
import com.openhis.web.datadictionary.mapper.ChargeItemDefSearchMapper;
import com.openhis.workflow.service.IActivityDefinitionService;

import lombok.extern.slf4j.Slf4j;

/**
 * 项目定价
 *
 * @author zxy
 * @date 2025-02-21
 */
@RestController
@RequestMapping("/dict-manager/definition")
@Slf4j
public class ItemDefinitionController {

    @Autowired(required = false)
    private IChargeItemDefinitionService chargeItemDefinitionService;
    @Autowired(required = false)
    private IChargeItemDefAppService chargeItemDefAppService;
    @Autowired(required = false)
    private ChargeItemDefSearchMapper chargeItemDefSearchMapper;
    @Autowired(required = false)
    private IMedicationDefinitionService medicationDefinitionService;
    @Autowired(required = false)
    private IDeviceDefinitionService deviceDefinitionService;
    @Autowired(required = false)
    private IActivityDefinitionService activityDefinitionService;

    /**
     * 项目定价列表
     *
     * @param itemDefSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 项目定价列表
     */
    @GetMapping(value = "/item-definition-page")
    public R<?> getDefinitionPage(ItemDefSearchParam itemDefSearchParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        // region
        /// todo: 代码未测试
        // 获取定价查询条件
        // ChargeItemDefinition chargeItemDefinition = new ChargeItemDefinition();
        // BeanUtils.copyProperties(itemDefSearchParam, chargeItemDefinition);

        // ====================================================================================

        // 查询【费用定价管理】分页列表
        // Page<ChargeItemDefinition> chargeItemDefinitionPage =
        // chargeItemDefinitionMapper.getPage(itemDefSearchParam, pageNo, pageSize);
        // if (chargeItemDefinitionPage.getRecords() != null) {
        // List<ChargeItemDefApp> chargeItemDefAppList = chargeItemDefAppService.listByIds(chargeItemDefinitionPage
        // .getRecords().stream().filter(e -> e.getInstanceTable().equals("adm_charge_item_def_app"))
        // .map(ChargeItemDefinition::getInstanceId).collect(Collectors.toList()));
        // // 通过 DefinitionType 区分药品定价/器具定价/手术定价
        // if (DefinitionTypeEnum.MEDICATION.getCode().equals(itemDefSearchParam.getDefinitionType())) {
        // // 获取药品定价列表
        // List<MedicationDefinition> medicationList =
        // medicationDefinitionService.listByIds(chargeItemDefinitionPage.getRecords().stream()
        // .filter(e -> e.getInstanceTable().equals("med_medication_definition"))
        // .map(ChargeItemDefinition::getInstanceId).collect(Collectors.toList()));
        //
        // return R.ok(ItemDefinitionAssembler.assembleMedDefinitionDto(chargeItemDefinitionPage,
        // chargeItemDefAppList, medicationList, itemDefSearchParam));
        // } else if (DefinitionTypeEnum.DEVICE.getCode().equals(itemDefSearchParam.getDefinitionType())) {
        // // 获取器具定价列表
        // List<DeviceDefinition> deviceDefinitionList = deviceDefinitionService.listByIds(chargeItemDefinitionPage
        // .getRecords().stream().filter(e -> e.getInstanceTable().equals("adm_device_definition"))
        // .map(ChargeItemDefinition::getInstanceId).collect(Collectors.toList()));
        //
        // return R.ok(ItemDefinitionAssembler.assembleDevDefinitionDto(chargeItemDefinitionPage,
        // chargeItemDefAppList, deviceDefinitionList, itemDefSearchParam));
        // } else if (DefinitionTypeEnum.ACTIVITY.getCode().equals(itemDefSearchParam.getDefinitionType())) {
        // List<ActivityDefinition> activityDefinitionList =
        // activityDefinitionService.listByIds(chargeItemDefinitionPage.getRecords().stream()
        // .filter(e -> e.getInstanceTable().equals("wor_activity_definition"))
        // .map(ChargeItemDefinition::getInstanceId).collect(Collectors.toList()));
        //
        // return R.ok(ItemDefinitionAssembler.assembleProDefinitionDto(chargeItemDefinitionPage,
        // chargeItemDefAppList, activityDefinitionList, itemDefSearchParam));
        // } else {
        // return R.ok(new Page<ChargeItemDefPageDto>());
        // }
        // } else {
        // return R.ok(new Page<ChargeItemDefPageDto>());
        // }
        // endregion

        IPage<ChargeItemDefPageDto> chargeItemDefinitionPage = new Page<>();
        List<ChargeItemDefPageDto> chargeItemDefinitionList;

        // TODO: 待测试
        // 跳过的数量
        int skipCount = 0;
        if (pageNo > 0) {
            skipCount = (pageNo - 1) * pageSize;
        }
        // 通过 DefinitionType 区分药品定价/器具定价/手术定价
        if (DefinitionTypeEnum.MEDICATION.getCode().equals(itemDefSearchParam.getDefinitionType())) {
            chargeItemDefinitionList =
                chargeItemDefSearchMapper.getMedList(itemDefSearchParam, pageNo, pageSize, skipCount);
            chargeItemDefinitionPage.setSize(pageSize);
            chargeItemDefinitionPage.setCurrent(pageNo);
            if (chargeItemDefinitionList.size() > 0) {
                chargeItemDefinitionPage.setTotal(chargeItemDefinitionList.get(0).getTotalCount());
                chargeItemDefinitionPage.setRecords(chargeItemDefinitionList);
            } else {
                chargeItemDefinitionPage.setTotal(0);
                chargeItemDefinitionPage.setRecords(new ArrayList<>());
            }
            return R.ok(chargeItemDefinitionPage);
        } else if (DefinitionTypeEnum.DEVICE.getCode().equals(itemDefSearchParam.getDefinitionType())) {
            chargeItemDefinitionList =
                chargeItemDefSearchMapper.getDevList(itemDefSearchParam, pageNo, pageSize, skipCount);
            chargeItemDefinitionPage.setSize(pageSize);
            chargeItemDefinitionPage.setCurrent(pageNo);
            if (chargeItemDefinitionList.size() > 0) {
                chargeItemDefinitionPage.setTotal(chargeItemDefinitionList.get(0).getTotalCount());
                chargeItemDefinitionPage.setRecords(chargeItemDefinitionList);
            } else {
                chargeItemDefinitionPage.setTotal(0);
                chargeItemDefinitionPage.setRecords(new ArrayList<>());
            }
            return R.ok(chargeItemDefinitionPage);
        } else if (DefinitionTypeEnum.ACTIVITY.getCode().equals(itemDefSearchParam.getDefinitionType())) {
            chargeItemDefinitionList =
                chargeItemDefSearchMapper.getActList(itemDefSearchParam, pageNo, pageSize, skipCount);
            chargeItemDefinitionPage.setSize(pageSize);
            chargeItemDefinitionPage.setCurrent(pageNo);
            if (chargeItemDefinitionList.size() > 0) {
                chargeItemDefinitionPage.setTotal(chargeItemDefinitionList.get(0).getTotalCount());
                chargeItemDefinitionPage.setRecords(chargeItemDefinitionList);
            } else {
                chargeItemDefinitionPage.setTotal(0);
                chargeItemDefinitionPage.setRecords(new ArrayList<>());
            }
            return R.ok(chargeItemDefinitionPage);
        } else {
            return R.ok();
        }
    }

    /**
     * 修改项目定价
     *
     * @param itemDefinitionDto 修改内容
     * @return 修改结果
     */
    @PutMapping(value = "/item-definition")
    public R<?> edit(@Validated @RequestBody ItemDefinitionDto itemDefinitionDto) {
        // 更新adm_charge_item_definition信息
        ChargeItemDefinition chargeItemDefinition = new ChargeItemDefinition();
        BeanUtils.copyProperties(itemDefinitionDto, chargeItemDefinition);
        if (!chargeItemDefinitionService.updateById(chargeItemDefinition)) {
            return R.fail();
        }

        // 更新收费项目adm_charge_item_def_app
        ChargeItemDefApp chargeItemDefApp = new ChargeItemDefApp();
        BeanUtils.copyProperties(itemDefinitionDto, chargeItemDefApp);
        chargeItemDefApp.setDefinitionId(itemDefinitionDto.getId());
        chargeItemDefApp.setId(itemDefinitionDto.getItemId());
        return chargeItemDefAppService.updateChargeItemDefApp(chargeItemDefApp)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"费用定价"}))
            : R.fail(PromptMsgConstant.Common.M00007, null);
    }
}
