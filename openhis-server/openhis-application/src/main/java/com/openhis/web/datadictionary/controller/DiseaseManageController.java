package com.openhis.web.datadictionary.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.openhis.clinical.domain.ConditionDefinition;
import com.openhis.clinical.service.IConditionDefinitionService;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.ConditionDefinitionSource;

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

    /**
     * 病种目录分类查询
     *
     * @return
     */
    @GetMapping("/information-category")
    public R<?> getDiseaseCategory() {
        // 获取疾病目录种类
        List<ConditionDefinitionSource> statusList = Arrays.asList(ConditionDefinitionSource.values());
        return R.ok(statusList);
    }

    /**
     * 查询病种目录分页列表
     * 
     * @param searchKey 查询条件
     * @param status 查询条件-状态
     * @param status 查询条件-疾病种类
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return
     */
    @GetMapping("/information-page")
    public R<?> getDiseaseList(@RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "status", defaultValue = "-1") Integer status,
        @RequestParam(value = "sourceEnum", defaultValue = "-1") Integer sourceEnum,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {

        // 查询【病种目录】分页列表
        Page<ConditionDefinition> diseasePage =
            iConditionDefinitionService.getPage(searchKey, status, sourceEnum, pageNo, pageSize);
        // 返回【病种目录列表DTO】分页
        return R.ok(diseasePage);
    }

    /**
     * 根据id查询疾病详情
     *
     * @param id 疾病ID
     * @return
     */
    @GetMapping("/information-one")
    public R<?> getDiseaseOne(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {

        // 根据ID查询【病种目录】
        ConditionDefinition byId = iConditionDefinitionService.getById(id);
        return R.ok(byId);
    }

    /**
     * 病种目录编辑
     * 
     * @param conditionDefinitionList 病种目录实体列表
     * @return
     */
    @PutMapping("/information")
    public R<?> editDisease(@RequestBody List<ConditionDefinition> conditionDefinitionList) {
        // 更新病种信息
        return iConditionDefinitionService.updateBatchById(conditionDefinitionList)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, null))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 新增外来病种目录
     * 
     * @param conditionDefinition 病种目录实体
     * @return
     */
    @PostMapping("/information")
    public R<?> addDisease(@RequestBody ConditionDefinition conditionDefinition) {
        // 新增外来病种目录
        return iConditionDefinitionService.addDisease(conditionDefinition)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"疾病目录"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00008, null));

    }

    /**
     * 新增医保病种目录
     * 
     * @param conditionDefinition 病种目录实体
     * @return
     */
    @PostMapping("/information-yb")
    void AddYbDisease(@RequestBody ConditionDefinition conditionDefinition) {}
}
