package com.openhis.clinical.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.clinical.domain.ConditionDefinition;

/**
 * 诊断定义管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IConditionDefinitionService extends IService<ConditionDefinition> {
    /**
     * 病种分类查询
     *
     * @return
     */
    List<ConditionDefinition> getMedicationCategory();

    /**
     * 查询病种目录分页列表
     * 
     * @param searchKey 查询条件
     * @param status 查询条件-状态
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return
     */
    Page<ConditionDefinition> getPage(String searchKey, Integer status, Integer source_enum, Long id, Integer pageNo,
        Integer pageSize);

    // 新增病种目录
    boolean addDisease(ConditionDefinition conditionDefinition);

    // 新增医保病种目录
    boolean addYbDisease(ConditionDefinition conditionDefinition);
}