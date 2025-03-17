package com.openhis.web.datadictionary.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.administration.domain.DeviceDefinition;
import com.openhis.administration.domain.Organization;
import com.openhis.administration.mapper.DeviceDefinitionMapper;
import com.openhis.administration.service.IDeviceDefinitionService;
import com.openhis.administration.service.IOrganizationService;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.DeviceCategory;
import com.openhis.common.enums.OrganizationType;
import com.openhis.common.enums.PublicationStatus;
import com.openhis.common.enums.WhetherContainUnknown;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisPageUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.datadictionary.dto.DeviceManageDto;
import com.openhis.web.datadictionary.dto.DeviceManageInitDto;
import com.openhis.web.datadictionary.dto.DeviceManageSelParam;
import com.openhis.web.datadictionary.dto.DeviceManageUpDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO:器材目录
 *
 * @author lpt
 * @date 2025-02-20
 */
@RestController
@RequestMapping("/data-dictionary/device")
@Slf4j
@AllArgsConstructor
public class DeviceManageController {
    private final IDeviceDefinitionService iDeviceDefinitionService;
    private final DeviceDefinitionMapper DeviceDefinitionMapper;
    private final IOrganizationService iOrganizationService;

    /**
     * TODO: 器材目录初期查询
     *
     * @return
     */
    @GetMapping("/init")
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
        List<Organization> organizations = iOrganizationService.list(queryWrapper);
        List<DeviceManageInitDto.exeOrganization> exeOrganizations = organizations.stream()
            .map(exeOrg -> new DeviceManageInitDto.exeOrganization(exeOrg.getId(), exeOrg.getName()))
            .collect(Collectors.toList());
        deviceManageInitDto.setExeOrganizations(exeOrganizations);
        // 获取分类
        List<DeviceManageInitDto.deviceCategory> deviceCategories = Stream.of(DeviceCategory.values())
            .map(category -> new DeviceManageInitDto.deviceCategory(category.getValue(), category.getInfo()))
            .collect(Collectors.toList());
        deviceManageInitDto.setDeviceCategories(deviceCategories);
        return R.ok(deviceManageInitDto);
    }

    /**
     * 查询器材目录分页列表
     *
     * @param DeviceManageSelParam 查询条件
     * @param searchKey 查询条件-模糊查询
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return
     */
    @GetMapping("/information-page")
    public R<?> getDevicePage(DeviceManageSelParam DeviceManageSelParam,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {

        // 构建查询条件
        QueryWrapper<DeviceDefinition> queryWrapper = HisQueryUtils.buildQueryWrapper(DeviceManageSelParam, searchKey,
            new HashSet<>(Arrays.asList("bus_no", "name", "py_str", "wb_str")), request);
        // 设置排序
        queryWrapper.orderByAsc("bus_no");

        // 分页查询
        Page<DeviceManageDto> deviceManagePage =
            HisPageUtils.selectPage(DeviceDefinitionMapper, queryWrapper, pageNo, pageSize, DeviceManageDto.class);

        deviceManagePage.getRecords().forEach(e -> {
            // 高值器材标志枚举类回显赋值
            e.setHvcmFlag_enumText(EnumUtils.getInfoByValue(WhetherContainUnknown.class, e.getHvcmFlag()));
            // 医保标记枚举类回显赋值
            e.setYbFlag_enumText(EnumUtils.getInfoByValue(WhetherContainUnknown.class, e.getYbFlag()));
            // 医保对码标记枚举类回显赋值
            e.setYbMatchFlag_enumText(EnumUtils.getInfoByValue(WhetherContainUnknown.class, e.getYbMatchFlag()));
            // 过敏标记枚举类回显赋值
            e.setAllergenFlag_enumText(EnumUtils.getInfoByValue(WhetherContainUnknown.class, e.getAllergenFlag()));
        });

        // 返回【器材目录列表DTO】分页
        return R.ok(deviceManagePage);
    }

    /**
     * 根据id查询器材详情
     *
     * @param id 器材ID
     * @return
     */
    @GetMapping("/information-one/{id}")
    public R<?> getDeviceOne(@PathVariable("id") Long id) {

        // 根据ID查询【器材目录】
        DeviceDefinition byId = iDeviceDefinitionService.getById(id);
        return R.ok(byId);
    }

    /**
     * 器材目录编辑
     *
     * @param DeviceManageDto 器材目录列表
     * @return
     */
    @PutMapping("/information")
    public R<?> editDevice(@RequestBody DeviceManageUpDto DeviceManageDto) {

        DeviceDefinition DeviceDefinition = new DeviceDefinition();
        BeanUtils.copyProperties(DeviceManageDto, DeviceDefinition);

        // 更新器材信息
        return iDeviceDefinitionService.updateById(DeviceDefinition)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"器材目录"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 器材目录停用
     *
     * @param ids 器材目录ID列表
     * @return
     */
    @PutMapping("/information-stop")
    public R<?> editDeviceStop(@RequestBody List<Long> ids) {
        List<DeviceDefinition> DeviceDefinitionList = new CopyOnWriteArrayList<>();

        // 取得更新值
        for (Long detail : ids) {
            DeviceDefinition DeviceDefinition = new DeviceDefinition();
            DeviceDefinition.setId(detail);
            DeviceDefinition.setStatusEnum(PublicationStatus.RETIRED);
            DeviceDefinitionList.add(DeviceDefinition);
        }
        // 更新器材信息
        return iDeviceDefinitionService.updateBatchById(DeviceDefinitionList)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"器材目录"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 器材目录启用
     *
     * @param ids 器材目录ID列表
     * @return
     */
    @PutMapping("/information-start")
    public R<?> editDeviceStart(@RequestBody List<Long> ids) {
        List<DeviceDefinition> DeviceDefinitionList = new CopyOnWriteArrayList<>();

        // 取得更新值
        for (Long detail : ids) {
            DeviceDefinition DeviceDefinition = new DeviceDefinition();
            DeviceDefinition.setId(detail);
            DeviceDefinition.setStatusEnum(PublicationStatus.ACTIVE);
            DeviceDefinitionList.add(DeviceDefinition);
        }
        // 更新器材信息
        return iDeviceDefinitionService.updateBatchById(DeviceDefinitionList)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"器材目录"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 新增外来器材目录
     *
     * @param DeviceManageUpDto 器材目录
     * @return
     */
    @PostMapping("/information")
    public R<?> addDevice(@Validated @RequestBody DeviceManageUpDto DeviceManageUpDto) {
        DeviceDefinition DeviceDefinition = new DeviceDefinition();
        BeanUtils.copyProperties(DeviceManageUpDto, DeviceDefinition);
        // 新增外来器材目录
        DeviceDefinition.setStatusEnum(PublicationStatus.DRAFT);
        return iDeviceDefinitionService.addDevice(DeviceDefinition)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"器材目录"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00008, null));
    }

    /**
     * 新增医保器材目录
     *
     * @param DeviceManageUpDto 器材目录
     * @return
     */
    @PostMapping("/information-yb")
    public R<?> addYbDevice(@RequestBody DeviceManageUpDto DeviceManageUpDto) {
        return null;
    }

    /**
     * 器材目录导出
     *
     * @param DeviceManageDto 器材目录
     * @return
     */
    @GetMapping("/information-export")
    public R<?> exportDevice(@RequestBody DeviceManageDto DeviceManageDto) {
        return null;
    }
}
