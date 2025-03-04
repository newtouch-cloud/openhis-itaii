/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basicservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.administration.domain.ChargeItemDefinition;
import com.openhis.administration.domain.HealthcareService;
import com.openhis.administration.service.IChargeItemDefinitionService;
import com.openhis.administration.service.IHealthcareServiceService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.AccountStatus;
import com.openhis.common.enums.WhetherContainUnknown;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.basicservice.dto.*;
import com.openhis.web.basicservice.mapper.HealthcareServiceBizMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 服务管理 controller
 */
@RestController
@RequestMapping("/basic-service/healthcare")
@Slf4j
@AllArgsConstructor
public class HealthcareServiceController {

    private final IHealthcareServiceService iHealthcareServiceService;
    private final IChargeItemDefinitionService iChargeItemDefinitionService;

    private final HealthcareServiceBizMapper healthcareServiceBizMapper;

    /**
     * 服务管理基础数据初始化
     */
    @GetMapping(value = "/init")
    public R<?> init() {
        HealthcareServiceInitDto healthcareServiceInitDto = new HealthcareServiceInitDto();
        // 活动标记
        List<HealthcareServiceInitDto.activeFlagOption> activeFlagOptions = Stream.of(AccountStatus.values())
                .map(status -> new HealthcareServiceInitDto.activeFlagOption(status.getValue(), status.getInfo()))
                .collect(Collectors.toList());
        healthcareServiceInitDto.setActiveFlagOptions(activeFlagOptions);
        // 是否需要预约
        List<HealthcareServiceInitDto.appointmentRequiredFlagOption> appointmentRequiredFlagOptions = Stream.of(WhetherContainUnknown.values())
                .map(wh -> new HealthcareServiceInitDto.appointmentRequiredFlagOption(wh.getValue(), wh.getInfo()))
                .collect(Collectors.toList());
        healthcareServiceInitDto.setAppointmentRequiredFlagOptions(appointmentRequiredFlagOptions);
        return R.ok(healthcareServiceInitDto);
    }

    /**
     * 服务管理 新增
     */
    @PostMapping(value = "/healthcare-service")
    public R<?> add(@Validated @RequestBody HealthcareServiceAddOrUpdateParam healthcareServiceAddOrUpdateParam) {
        // 服务管理-表单数据
        HealthcareServiceFormData healthcareServiceFormData = healthcareServiceAddOrUpdateParam.getHealthcareServiceFormData();
        // 费用定价-表单数据
        ChargeItemDefinitionFormData chargeItemDefinitionFormData = healthcareServiceAddOrUpdateParam.getChargeItemDefinitionFormData();
        // 服务管理-新增
        HealthcareService healthcareService = new HealthcareService();
        BeanUtils.copyProperties(healthcareServiceFormData, healthcareService);
        HealthcareService healthcareServiceAfterAdd = iHealthcareServiceService.addHealthcareService(healthcareService);
        // 同时保存费用定价
        ChargeItemDefinition chargeItemDefinition = new ChargeItemDefinition();
        BeanUtils.copyProperties(chargeItemDefinitionFormData, chargeItemDefinition);
        boolean res = iChargeItemDefinitionService.addChargeItemDefinitionByHealthcareService(healthcareServiceAfterAdd, chargeItemDefinition);
        return res ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00001, new Object[]{"服务管理"})) :
                R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00010, null));
    }

    /**
     * 服务管理 分页查询
     *
     * @param healthcareServiceDto 查询条件
     * @param searchKey            模糊查询关键字
     * @param pageNo               当前页码
     * @param pageSize             查询条数
     * @param request              请求数据
     * @return 列表信息
     */
    @GetMapping(value = "/healthcare-service-page")
    public R<?> getHealthcareServicePage(HealthcareServiceDto healthcareServiceDto,
                                         @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
                                         @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        // 构建查询条件
        QueryWrapper<HealthcareServiceDto> queryWrapper = HisQueryUtils.buildQueryWrapper(healthcareServiceDto, searchKey,
                new HashSet<>(Arrays.asList("name", "charge_name")), request);
        IPage<HealthcareServiceDto> healthcareServicePage = healthcareServiceBizMapper.getHealthcareServicePage(
                new Page<>(pageNo, pageSize), CommonConstants.TableName.ADM_HEALTHCARE_SERVICE, queryWrapper);
        healthcareServicePage.getRecords().forEach(e -> {
                    // 活动标记-枚举类回显赋值
                    e.setActiveFlag_enumText(EnumUtils.getInfoByValue(AccountStatus.class, e.getActiveFlag()));
                    // 预约要求-枚举类回显赋值
                    e.setAppointmentRequiredFlag_enumText(EnumUtils.getInfoByValue(WhetherContainUnknown.class, e.getAppointmentRequiredFlag()));
                }
        );
        return R.ok(healthcareServicePage, MessageUtils.createMessage(PromptMsgConstant.Common.M00009, null));
    }


    /**
     * 服务管理 编辑
     *
     * @param healthcareServiceAddOrUpdateParam 表单数据
     * @return 编辑结果
     */
    @PutMapping(value = "/healthcare-service")
    public R<?> edit(@Validated @RequestBody HealthcareServiceAddOrUpdateParam healthcareServiceAddOrUpdateParam) {
        // 服务管理-表单数据
        HealthcareServiceFormData healthcareServiceFormData = healthcareServiceAddOrUpdateParam.getHealthcareServiceFormData();
        HealthcareService healthcareService = new HealthcareService();
        BeanUtils.copyProperties(healthcareServiceFormData, healthcareService);
        boolean res = iHealthcareServiceService.updateHealthcareService(healthcareService);
        return res ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[]{"服务管理"})) :
                R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 服务管理 删除
     *
     * @param ids ID
     * @return 删除结果
     */
    @DeleteMapping(value = "/healthcare-service")
    public R<?> delete(@RequestParam String ids) {
        List<Long> idsList = new ArrayList<>();
        if (ids != null) {
            idsList = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
        }
        boolean res = iHealthcareServiceService.removeByIds(idsList);
        // 同时删除非同定价
        for (Long id : idsList) {
            LambdaQueryWrapper<ChargeItemDefinition> QueryWrapper = new LambdaQueryWrapper<>();
            QueryWrapper.eq(ChargeItemDefinition::getInstanceId, id).
                    eq(ChargeItemDefinition::getInstanceTable, CommonConstants.TableName.ADM_HEALTHCARE_SERVICE);
            iChargeItemDefinitionService.remove(QueryWrapper);
        }
        return res ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00005, new Object[]{"服务管理"})) :
                R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00006, null));
    }


}
