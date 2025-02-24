package com.openhis.web.datadictionary.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.core.common.utils.MessageUtils;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.openhis.clinical.domain.ConditionDefinition;
import com.openhis.clinical.service.IConditionDefinitionService;
import com.openhis.common.constant.PromptMsgConstant;

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
        return null;
    }

    /**
     * 查询病种目录分页列表
     * 
     * @param searchKey 查询条件
     * @param status 查询条件-状态
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return
     */
    @GetMapping("/information-page")
    public R<?> getDiseaseList(@RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "status", defaultValue = "-1") Integer status,
        @RequestParam(value = "sourceEnum", defaultValue = "-1") Integer sourceEnum,
        @RequestParam(value = "id", defaultValue = "-1") Long id,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {

        // 查询【病种目录】分页列表
        Page<ConditionDefinition> diseasePage =
            iConditionDefinitionService.getPage(searchKey, status, sourceEnum, id,pageNo, pageSize);
        // 返回【病种目录列表DTO】分页
        return R.ok(diseasePage);
    }

    // 病种目录编辑
    @PutMapping("/information")
    public R<?> editDisease(@RequestBody List<ConditionDefinition> conditionDefinitionList) {
        // 更新病种信息
        return iConditionDefinitionService.updateBatchById(conditionDefinitionList)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, null))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    // 新增外来病种目录
    @PostMapping("/information")
    public R<?> addDisease(@RequestBody ConditionDefinition conditionDefinition) {
        // 新增外来病种目录
        return iConditionDefinitionService.addDisease(conditionDefinition)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"病种目录"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00008, null));

    }

    // 新增医保病种目录
    @PostMapping("/information-yb")
    void AddYbDisease(@RequestBody ConditionDefinition conditionDefinition) {}
}
