package com.openhis.web.datadictionary.controller;

import java.util.ArrayList;
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
import com.core.common.core.domain.entity.SysDictData;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.bean.BeanUtils;
import com.core.system.service.ISysDictTypeService;
import com.openhis.administration.domain.Organization;
import com.openhis.administration.service.IOrganizationService;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.ActivityDefCategory;
import com.openhis.common.enums.OrganizationType;
import com.openhis.common.enums.PublicationStatus;
import com.openhis.common.enums.WhetherContainUnknown;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisPageUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.datadictionary.dto.DiagnosisTreatmentDto;
import com.openhis.web.datadictionary.dto.DiagnosisTreatmentInitDto;
import com.openhis.web.datadictionary.dto.DiagnosisTreatmentSelParam;
import com.openhis.web.datadictionary.dto.DiagnosisTreatmentUpDto;
import com.openhis.workflow.domain.ActivityDefinition;
import com.openhis.workflow.mapper.ActivityDefinitionMapper;
import com.openhis.workflow.service.IActivityDefinitionService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO:诊疗目录
 *
 * @author lpt
 * @date 2025-02-20
 */
@RestController
@RequestMapping("/datadictionary/diagnosistreatment")
@Slf4j
@AllArgsConstructor
public class DiagnosisTreatmentController {
    private final IActivityDefinitionService iActivityDefinitionService;
    private final ActivityDefinitionMapper activityDefinitionMapper;
    private final IOrganizationService iOrganizationService;
    private final ISysDictTypeService iSysDictTypeService;

    /**
     * 诊疗目录初期查询
     *
     * @return
     */
    @GetMapping("/init")
    public R<?> getDiseaseTreatmentInit() {
        DiagnosisTreatmentInitDto diagnosisTreatmentInitDto = new DiagnosisTreatmentInitDto();
        // 获取状态
        List<DiagnosisTreatmentInitDto.statusEnumOption> statusEnumOptions = Stream.of(PublicationStatus.values())
            .map(status -> new DiagnosisTreatmentInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        diagnosisTreatmentInitDto.setStatusFlagOptions(statusEnumOptions);
        // 获取执行科室
        LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Organization::getTypeEnum, OrganizationType.HOSPITAL_DEPARTMENT);
        List<Organization> organizations = iOrganizationService.list(queryWrapper);
        List<DiagnosisTreatmentInitDto.exeOrganization> exeOrganizations = organizations.stream()
            .map(exeOrg -> new DiagnosisTreatmentInitDto.exeOrganization(exeOrg.getId(), exeOrg.getName()))
            .collect(Collectors.toList());
        diagnosisTreatmentInitDto.setExeOrganizations(exeOrganizations);

        // 获取诊疗分类
        // 查询医疗服务项类型
        List<SysDictData> medical_service_items =
            iSysDictTypeService.selectDictDataByType(ActivityDefCategory.MEDICAL_SERVICE_ITEM.getCode());
        // 获取医疗服务项List
        List<DiagnosisTreatmentInitDto.diseaseTreatmentType> diseaseTreatmentCategoryList = medical_service_items
            .stream().map(status -> new DiagnosisTreatmentInitDto.diseaseTreatmentType(status.getDictValue(),
                status.getDictLabel()))
            .collect(Collectors.toList());
        List<DiagnosisTreatmentInitDto.diseaseTreatmentCategory> diseaseTreatmentCategories = new ArrayList<>();

        DiagnosisTreatmentInitDto.diseaseTreatmentCategory diseaseTreatmentCategory =
            new DiagnosisTreatmentInitDto.diseaseTreatmentCategory(ActivityDefCategory.MEDICAL_SERVICE_ITEM.getValue(),
                ActivityDefCategory.MEDICAL_SERVICE_ITEM.getInfo());
        diseaseTreatmentCategory.setChildren(diseaseTreatmentCategoryList);
        diseaseTreatmentCategories.add(diseaseTreatmentCategory);
        diagnosisTreatmentInitDto.setDiseaseTreatmentCategoryList(diseaseTreatmentCategories);

        // 查询手术与治疗类型
        List<SysDictData> medical_service_items2 =
            iSysDictTypeService.selectDictDataByType(ActivityDefCategory.TREATMENT_SURGERY.getCode());
        // 获取手术与治疗List
        List<DiagnosisTreatmentInitDto.diseaseTreatmentType> diseaseTreatmentCategoryList2 = medical_service_items2
            .stream().map(status -> new DiagnosisTreatmentInitDto.diseaseTreatmentType(status.getDictValue(),
                status.getDictLabel()))
            .collect(Collectors.toList());
        DiagnosisTreatmentInitDto.diseaseTreatmentCategory diseaseTreatmentCategory2 =
            new DiagnosisTreatmentInitDto.diseaseTreatmentCategory(ActivityDefCategory.TREATMENT_SURGERY.getValue(),
                ActivityDefCategory.TREATMENT_SURGERY.getInfo());
        diseaseTreatmentCategory2.setChildren(diseaseTreatmentCategoryList2);
        diseaseTreatmentCategories.add(diseaseTreatmentCategory2);

        diagnosisTreatmentInitDto.setDiseaseTreatmentCategoryList(diseaseTreatmentCategories);
        return R.ok(diagnosisTreatmentInitDto);
    }

    /**
     * 查询诊疗目录分页列表
     *
     * @param DiagnosisTreatmentSelParam 查询条件
     * @param searchKey 查询条件-模糊查询
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return
     */
    @GetMapping("/information-page")
    public R<?> getDiseaseTreatmentPage(DiagnosisTreatmentSelParam DiagnosisTreatmentSelParam,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {

        // 构建查询条件
        QueryWrapper<ActivityDefinition> queryWrapper = HisQueryUtils.buildQueryWrapper(DiagnosisTreatmentSelParam,
            searchKey, new HashSet<>(Arrays.asList("bus_no", "name", "py_str", "wb_str")), request);
        // 设置排序
        queryWrapper.orderByAsc("bus_no");
        // 分页查询
        Page<DiagnosisTreatmentDto> diseaseTreatmentPage = HisPageUtils.selectPage(activityDefinitionMapper,
            queryWrapper, pageNo, pageSize, DiagnosisTreatmentDto.class);

        diseaseTreatmentPage.getRecords().forEach(e -> {
            // 医保标记枚举类回显赋值
            e.setYbFlag_enumText(EnumUtils.getInfoByValue(WhetherContainUnknown.class, e.getYbFlag()));
            // 医保对码标记枚举类回显赋值
            e.setYbMatchFlag_enumText(EnumUtils.getInfoByValue(WhetherContainUnknown.class, e.getYbMatchFlag()));
        });

        // 返回【诊疗目录列表DTO】分页
        return R.ok(diseaseTreatmentPage);
    }

    /**
     * 根据id查询诊疗详情
     *
     * @param id 诊疗ID
     * @return
     */
    @GetMapping("/information-one/{id}")
    public R<?> getDiseaseTreatmentOne(@PathVariable("id") Long id) {
        DiagnosisTreatmentDto diagnosisTreatmentDto = new DiagnosisTreatmentDto();
        // 根据ID查询【诊疗目录】
        ActivityDefinition activityDefinition = iActivityDefinitionService.getById(id);
        BeanUtils.copyProperties(activityDefinition, diagnosisTreatmentDto);
        return R.ok(diagnosisTreatmentDto);
    }

    /**
     * 诊疗目录编辑
     *
     * @param diagnosisTreatmentUpDto 诊疗目录列表
     * @return
     */
    @PutMapping("/information")
    public R<?> editDiseaseTreatment(@RequestBody DiagnosisTreatmentUpDto diagnosisTreatmentUpDto) {

        ActivityDefinition ActivityDefinition = new ActivityDefinition();
        BeanUtils.copyProperties(diagnosisTreatmentUpDto, ActivityDefinition);

        // 更新诊疗信息
        return iActivityDefinitionService.updateById(ActivityDefinition)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"诊疗目录"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 诊疗目录停用
     *
     * @param ids 诊疗目录ID列表
     * @return
     */
    @PutMapping("/information-stop")
    public R<?> editDiseaseTreatmentStop(@RequestBody List<Long> ids) {
        List<ActivityDefinition> ActivityDefinitionList = new CopyOnWriteArrayList<>();

        // 取得更新值
        for (Long detail : ids) {
            ActivityDefinition ActivityDefinition = new ActivityDefinition();
            ActivityDefinition.setId(detail);
            ActivityDefinition.setStatusEnum(PublicationStatus.RETIRED);
            ActivityDefinitionList.add(ActivityDefinition);
        }
        // 更新诊疗信息
        return iActivityDefinitionService.updateBatchById(ActivityDefinitionList)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"诊疗目录"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 诊疗目录启用
     *
     * @param ids 诊疗目录ID列表
     * @return
     */
    @PutMapping("/information-start")
    public R<?> editDiseaseTreatmentStart(@RequestBody List<Long> ids) {
        List<ActivityDefinition> ActivityDefinitionList = new CopyOnWriteArrayList<>();

        // 取得更新值
        for (Long detail : ids) {
            ActivityDefinition ActivityDefinition = new ActivityDefinition();
            ActivityDefinition.setId(detail);
            ActivityDefinition.setStatusEnum(PublicationStatus.ACTIVE);
            ActivityDefinitionList.add(ActivityDefinition);
        }
        // 更新诊疗信息
        return iActivityDefinitionService.updateBatchById(ActivityDefinitionList)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"诊疗目录"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 新增外来诊疗目录
     *
     * @param diagnosisTreatmentUpDto 诊疗目录
     * @return
     */
    @PostMapping("/information")
    public R<?> addDiseaseTreatment(@Validated @RequestBody DiagnosisTreatmentUpDto diagnosisTreatmentUpDto) {
        ActivityDefinition ActivityDefinition = new ActivityDefinition();
        BeanUtils.copyProperties(diagnosisTreatmentUpDto, ActivityDefinition);
        // 新增外来诊疗目录
        ActivityDefinition.setStatusEnum(PublicationStatus.DRAFT);
        return iActivityDefinitionService.addDiagnosisTreatment(ActivityDefinition)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"诊疗目录"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00008, null));
    }

    /**
     * 新增医保诊疗目录
     *
     * @param diagnosisTreatmentUpDto 诊疗目录
     * @return
     */
    @PostMapping("/information-yb")
    public R<?> addYbDiseaseTreatment(@RequestBody DiagnosisTreatmentUpDto diagnosisTreatmentUpDto) {
        return null;
    }

    /**
     * 诊疗目录导出
     *
     * @param diagnosisTreatmentDto 诊疗目录
     * @return
     */
    @GetMapping("/information-export")
    public R<?> exportDiseaseTreatment(@RequestBody DiagnosisTreatmentDto diagnosisTreatmentDto) {
        return null;
    }
}
