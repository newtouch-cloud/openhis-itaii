/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.datadictionary.appservice.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.core.domain.entity.SysDictData;
import com.core.common.utils.*;
import com.core.common.utils.bean.BeanUtils;
import com.core.system.service.ISysDictTypeService;
import com.openhis.administration.domain.ChargeItemDefinition;
import com.openhis.administration.domain.DeviceDefinition;
import com.openhis.administration.domain.Organization;
import com.openhis.administration.mapper.DeviceDefinitionMapper;
import com.openhis.administration.service.IDeviceDefinitionService;
import com.openhis.administration.service.IOrganizationService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.AssignSeqEnum;
import com.openhis.common.enums.OrganizationType;
import com.openhis.common.enums.PublicationStatus;
import com.openhis.common.enums.Whether;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.datadictionary.appservice.IDeviceManageAppService;
import com.openhis.web.datadictionary.appservice.IItemDefinitionService;
import com.openhis.web.datadictionary.dto.*;
import com.openhis.web.datadictionary.mapper.DeviceManageMapper;

/**
 * 器材目录 impl
 *
 * @author
 * @date 2025-03-17
 */
@Service
public class DeviceManageAppServiceImpl implements IDeviceManageAppService {

    @Autowired
    private DeviceDefinitionMapper deviceDefinitionMapper;

    @Autowired
    private IDeviceDefinitionService deviceDefinitionService;

    @Autowired
    private IOrganizationService organizationService;

    @Resource
    DeviceManageMapper deviceManageMapper;

    @Autowired
    private ISysDictTypeService sysDictTypeService;

    @Autowired
    private IItemDefinitionService itemDefinitionServic;

    @Autowired(required = false)
    AssignSeqUtil assignSeqUtil;

    /**
     * 器材目录初始化
     *
     * @return
     */
    @Override
    public R<?> getDeviceManageInit() {

        DeviceManageInitDto deviceManageInitDto = new DeviceManageInitDto();

        // 获取状态
        List<DeviceManageInitDto.statusEnumOption> statusEnumOptions = Stream.of(PublicationStatus.values())
            .map(status -> new DeviceManageInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        deviceManageInitDto.setStatusFlagOptions(statusEnumOptions);

        // 获取执行科室
        LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Organization::getTypeEnum, OrganizationType.HOSPITAL_DEPARTMENT);
        List<Organization> organizations = organizationService.list(queryWrapper);
        List<DeviceManageInitDto.exeOrganization> exeOrganizations = organizations.stream()
            .map(exeOrg -> new DeviceManageInitDto.exeOrganization(exeOrg.getId(), exeOrg.getName()))
            .collect(Collectors.toList());
        deviceManageInitDto.setExeOrganizations(exeOrganizations);
        // // 从枚举中获取器材分类
        // List<DeviceManageInitDto.deviceCategory> deviceCategories = Stream.of(DeviceCategory.values())
        // .map(category -> new DeviceManageInitDto.deviceCategory(category.getValue(), category.getInfo()))
        // .collect(Collectors.toList());
        // deviceManageInitDto.setDeviceCategories(deviceCategories);

        // 获取器材
        List<SysDictData> devicelList =
            sysDictTypeService.selectDictDataByType(CommonConstants.DictName.DEVICE_CATEGORY_CODE);
        // 从字典中获取器材分类
        List<DeviceManageInitDto.dictCategoryCode> deviceCategories = devicelList.stream()
            .map(category -> new DeviceManageInitDto.dictCategoryCode(category.getDictValue(), category.getDictLabel()))
            .collect(Collectors.toList());
        deviceManageInitDto.setDeviceCategories(deviceCategories);

        // 获取医保是否对码
        List<DeviceManageInitDto.statusEnumOption> statusYBWeatherOption = Stream.of(Whether.values())
            .map(status -> new DeviceManageInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        deviceManageInitDto.setStatusYBWeatherOptions(statusYBWeatherOption);

        return R.ok(deviceManageInitDto);
    }

    /**
     * 器材目录查询
     *
     * @param
     * @param searchKey 查询条件-模糊查询
     * @param pageNo 查询条件
     * @param pageSize 查询条件
     * @return 器材目录查询结果
     */
    @Override
    public R<?> getDevicePage(DeviceManageSelParam deviceManageSelParam,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {

        // 构建查询条件
        QueryWrapper<DeviceManageDto> queryWrapper = HisQueryUtils.buildQueryWrapper(deviceManageSelParam, searchKey,
            new HashSet<>(Arrays.asList("bus_no", "name", "py_str", "wb_str")), request);

        // 分页查询
        IPage<DeviceManageDto> deviceManagePage =
            deviceManageMapper.getDevicePage(new Page<>(pageNo, pageSize), queryWrapper);

        deviceManagePage.getRecords().forEach(e -> {
            // 高值器材标志枚举类回显赋值
            e.setHvcmFlag_enumText(EnumUtils.getInfoByValue(Whether.class, e.getHvcmFlag()));
            // 医保标记枚举类回显赋值
            e.setYbFlag_enumText(EnumUtils.getInfoByValue(Whether.class, e.getYbFlag()));
            // 医保对码标记枚举类回显赋值
            e.setYbMatchFlag_enumText(EnumUtils.getInfoByValue(Whether.class, e.getYbMatchFlag()));
            // 过敏标记枚举类回显赋值
            e.setAllergenFlag_enumText(EnumUtils.getInfoByValue(Whether.class, e.getAllergenFlag()));
            // 器材分类
            // e.setCategoryEnum_enumText(EnumUtils.getInfoByValue(DeviceCategory.class, e.getCategoryEnum()));

            // 器材状态
            e.setStatusEnum_enumText(EnumUtils.getInfoByValue(PublicationStatus.class, e.getStatusEnum()));
        });

        // 返回【器材目录列表DTO】分页
        return R.ok(deviceManagePage);
    }

    /**
     * 编辑器材信息
     *
     * @param deviceManageDto 器材信息
     */
    @Override
    public R<?> editDevice(DeviceManageUpDto deviceManageDto) {

        DeviceDefinition deviceDefinition = new DeviceDefinition();
        BeanUtils.copyProperties(deviceManageDto, deviceDefinition);
        // 拼音码
        deviceDefinition.setPyStr(ChineseConvertUtils.toPinyinFirstLetter(deviceDefinition.getName()));
        // 五笔码
        deviceDefinition.setWbStr(ChineseConvertUtils.toWBFirstLetter(deviceDefinition.getName()));

        // 更新器材信息
        if (deviceDefinitionService.updateById(deviceDefinition)) {
            ChargeItemDefinition chargeItemDefinition = new ChargeItemDefinition();
            chargeItemDefinition.setYbType(deviceManageDto.getItemTypeCode()).setTypeCode(deviceManageDto.getTypeCode())
                .setInstanceTable(CommonConstants.TableName.ADM_DEVICE_DEFINITION)
                .setInstanceId(deviceDefinition.getId());

            // 更新价格表
            return itemDefinitionServic.updateItem(chargeItemDefinition)
                ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"器材目录"}))
                : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }
        return R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 器材目录详细查询
     *
     * @param id 查询条件
     * @return 器材目录查询结果
     */
    @Override
    public R<?> getDeviceOne(@RequestParam Long id) {

        // 获取租户ID
        Integer tenantId = SecurityUtils.getLoginUser().getTenantId();
        // 根据ID查询【器材目录】
        DeviceManageDto deviceManageDto = deviceManageMapper.getOne(id, tenantId);

        return R.ok(deviceManageDto);
    }

    /**
     * 器材目录停用
     *
     * @param ids 器材目录ID列表
     * @return
     */
    @Override
    public R<?> editDeviceStop(List<Long> ids) {

        List<DeviceDefinition> DeviceDefinitionList = new CopyOnWriteArrayList<>();

        // 取得更新值
        for (Long detail : ids) {
            DeviceDefinition deviceDefinition = new DeviceDefinition();
            deviceDefinition.setId(detail);
            deviceDefinition.setStatusEnum(PublicationStatus.RETIRED.getValue());
            DeviceDefinitionList.add(deviceDefinition);
        }

        // 更新器材信息
        return deviceDefinitionService.updateBatchById(DeviceDefinitionList)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"器材目录"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 器材目录启用
     *
     * @param ids 器材目录ID列表
     * @return
     */
    @Override
    public R<?> editDeviceStart(List<Long> ids) {

        List<DeviceDefinition> DeviceDefinitionList = new CopyOnWriteArrayList<>();

        // 取得更新值
        for (Long detail : ids) {
            DeviceDefinition DeviceDefinition = new DeviceDefinition();
            DeviceDefinition.setId(detail);
            DeviceDefinition.setStatusEnum(PublicationStatus.ACTIVE.getValue());
            DeviceDefinitionList.add(DeviceDefinition);
        }

        // 更新器材信息
        return deviceDefinitionService.updateBatchById(DeviceDefinitionList)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"器材目录"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 添加供应商信息
     *
     * @param deviceManageUpDto 供应商信息
     */
    @Override
    public R<?> addDevice(@Validated @RequestBody DeviceManageUpDto deviceManageUpDto) {

        DeviceDefinition deviceDefinition = new DeviceDefinition();
        BeanUtils.copyProperties(deviceManageUpDto, deviceDefinition);

        // 使用10位数基础采番
        String code = assignSeqUtil.getSeq(AssignSeqEnum.DEVICE_NUM.getPrefix(), 10);
        deviceDefinition.setBusNo(code);
        // 拼音码
        deviceDefinition.setPyStr(ChineseConvertUtils.toPinyinFirstLetter(deviceDefinition.getName()));
        // 五笔码
        deviceDefinition.setWbStr(ChineseConvertUtils.toWBFirstLetter(deviceDefinition.getName()));

        // 新增外来器材目录
        deviceDefinition.setStatusEnum(PublicationStatus.DRAFT.getValue());

        if (deviceDefinitionService.addDevice(deviceDefinition)) {
            ItemUpFromDirectoryDto itemUpFromDirectoryDto = new ItemUpFromDirectoryDto();
            BeanUtils.copyProperties(deviceManageUpDto, itemUpFromDirectoryDto);
            itemUpFromDirectoryDto.setTypeCode(deviceManageUpDto.getItemTypeCode())
                .setInstanceTable(CommonConstants.TableName.ADM_DEVICE_DEFINITION)
                .setEffectiveStart(DateUtils.getNowDate()).setStatusEnum(PublicationStatus.ACTIVE.getValue())
                .setConditionFlag(Whether.YES.getValue()).setChargeName(deviceManageUpDto.getName())
                .setInstanceId(deviceDefinition.getId())
                .setPrice(deviceManageUpDto.getRetailPrice());

            return itemDefinitionServic.addItem(itemUpFromDirectoryDto)
                ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"器材目录"}))
                : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00008, null));
        }

        return R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00008, null));
    }
}
