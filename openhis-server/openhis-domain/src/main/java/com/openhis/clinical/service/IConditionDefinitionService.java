package com.openhis.clinical.service;

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
     * 查询病种目录分页列表
     *
     * @param searchKey 查询条件
     * @param status 查询条件-状态
     * @param sourceEnum 查询条件-疾病种类
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return
     */
    Page<ConditionDefinition> getPage(String searchKey, Integer status, Integer sourceEnum, Integer pageNo,
        Integer pageSize);

    /**
     * 新增病种
     * 
     * @param conditionDefinition 病种目录实体
     * @return
     */
    boolean addDisease(ConditionDefinition conditionDefinition);

    /**
     * 新增医保病种
     * 
     * @param conditionDefinition 病种目录实体
     * @return
     */
    boolean addYbDisease(ConditionDefinition conditionDefinition);
}