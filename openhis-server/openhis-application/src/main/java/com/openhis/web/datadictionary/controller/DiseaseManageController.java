package com.openhis.web.datadictionary.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.clinical.domain.ConditionDefinition;
import com.openhis.clinical.mapper.ConditionDefinitionMapper;
import com.openhis.clinical.service.IConditionDefinitionService;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.ConditionDefinitionSource;
import com.openhis.common.enums.PublicationStatus;
import com.openhis.common.utils.HisPageUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.datadictionary.dto.DiseaseManageDto;
import com.openhis.web.datadictionary.dto.DiseaseManageSelParam;
import com.openhis.web.datadictionary.dto.DiseaseManageUpDto;
import com.openhis.web.datadictionary.dto.DiseaseSourceDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO:病种目录
 *
 * @author lpt
 * @date 2025-02-20
 */
@RestController
@RequestMapping("/datadictionary/disease")
@Slf4j
@AllArgsConstructor
public class DiseaseManageController {
    private final IConditionDefinitionService iConditionDefinitionService;
    private final ConditionDefinitionMapper conditionDefinitionMapper;

    /**
     * 病种目录分类查询
     *
     * @return
     */
    @GetMapping("/information-category")
    public R<?> getDiseaseCategory() {
        // 获取疾病目录种类
        List<ConditionDefinitionSource> statusList = Arrays.asList(ConditionDefinitionSource.values());
        List<DiseaseSourceDto> diseaseSourceDtos = new ArrayList<>();
        // 取得更新值
        for (ConditionDefinitionSource detail : statusList) {
            DiseaseSourceDto diseaseSourceDto = new DiseaseSourceDto();
            diseaseSourceDto.setCode(detail.getCode());
            diseaseSourceDto.setValue(detail.getValue());
            diseaseSourceDto.setInfo(detail.getInfo());
            diseaseSourceDtos.add(diseaseSourceDto);
        }
        return R.ok(diseaseSourceDtos);
    }

    /**
     * 查询病种目录分页列表
     *
     * @param diseaseManageSelParam 查询条件
     * @param searchKey 查询条件-模糊查询
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return
     */
    @GetMapping("/information-page")
    public R<?> getDiseaseList(DiseaseManageSelParam diseaseManageSelParam,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {

        // 构建查询条件
        QueryWrapper<ConditionDefinition> queryWrapper = HisQueryUtils.buildQueryWrapper(diseaseManageSelParam,
            searchKey, new HashSet<>(Arrays.asList("condition_code", "name", "py_str", "wb_str")), request);
        // 设置排序
        queryWrapper.orderByAsc("condition_code");
        // 分页查询
        Page<DiseaseManageDto> diseasePage =
            HisPageUtils.selectPage(conditionDefinitionMapper, queryWrapper, pageNo, pageSize, DiseaseManageDto.class);
        // 返回【病种目录列表DTO】分页
        return R.ok(diseasePage);
    }

    /**
     * 根据id查询疾病详情
     *
     * @param id 疾病ID
     * @return
     */
    @GetMapping("/information-one/{id}")
    public R<?> getDiseaseOne(@PathVariable("id") Long id) {
        DiseaseManageDto diseaseManageDto = new DiseaseManageDto();
        // 根据ID查询【病种目录】
        ConditionDefinition conditionDefinition = iConditionDefinitionService.getById(id);
        BeanUtils.copyProperties(conditionDefinition, diseaseManageDto);
        return R.ok(diseaseManageDto);
    }

    /**
     * 病种目录编辑
     * 
     * @param diseaseManageDto 病种目录列表
     * @return
     */
    @PutMapping("/information")
    public R<?> editDisease(@RequestBody DiseaseManageUpDto diseaseManageDto) {

        ConditionDefinition conditionDefinition = new ConditionDefinition();
        BeanUtils.copyProperties(diseaseManageDto, conditionDefinition);

        // 更新病种信息
        return iConditionDefinitionService.updateById(conditionDefinition)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"疾病目录"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 病种目录停用
     *
     * @param ids 病种目录ID列表
     * @return
     */
    @PutMapping("/information-stop")
    public R<?> editDiseaseStop(@RequestBody List<Long> ids) {
        List<ConditionDefinition> conditionDefinitionList = new CopyOnWriteArrayList<>();

        // 取得更新值
        for (Long detail : ids) {
            ConditionDefinition conditionDefinition = new ConditionDefinition();
            conditionDefinition.setId(detail);
            conditionDefinition.setStatusEnum(PublicationStatus.RETIRED);
            conditionDefinitionList.add(conditionDefinition);
        }
        // 更新病种信息
        return iConditionDefinitionService.updateBatchById(conditionDefinitionList)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"疾病目录"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 病种目录启用
     *
     * @param ids 病种目录ID列表
     * @return
     */
    @PutMapping("/information-start")
    public R<?> editDiseaseStart(@RequestBody List<Long> ids) {
        List<ConditionDefinition> conditionDefinitionList = new CopyOnWriteArrayList<>();

        // 取得更新值
        for (Long detail : ids) {
            ConditionDefinition conditionDefinition = new ConditionDefinition();
            conditionDefinition.setId(detail);
            conditionDefinition.setStatusEnum(PublicationStatus.ACTIVE);
            conditionDefinitionList.add(conditionDefinition);
        }
        // 更新病种信息
        return iConditionDefinitionService.updateBatchById(conditionDefinitionList)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"疾病目录"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 新增外来病种目录
     * 
     * @param diseaseManageUpDto 病种目录
     * @return
     */
    @PostMapping("/information")
    public R<?> addDisease(@Validated @RequestBody DiseaseManageUpDto diseaseManageUpDto) {
        ConditionDefinition conditionDefinition = new ConditionDefinition();
        BeanUtils.copyProperties(diseaseManageUpDto, conditionDefinition);
        // 新增外来病种目录
        conditionDefinition.setStatusEnum(PublicationStatus.DRAFT);
        return iConditionDefinitionService.addDisease(conditionDefinition)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"疾病目录"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00008, null));
    }

    /**
     * 新增医保病种目录
     * 
     * @param diseaseManageUpDto 病种目录
     * @return
     */
    @PostMapping("/information-yb")
    public R<?> addYbDisease(@RequestBody DiseaseManageUpDto diseaseManageUpDto) {
        return null;
    }

    /**
     * 病种目录导出
     *
     * @param diseaseManageDto 病种目录
     * @return
     */
    @GetMapping("/information-export")
    public R<?> exportDisease(@RequestBody DiseaseManageDto diseaseManageDto) {
        return null;
    }
}
