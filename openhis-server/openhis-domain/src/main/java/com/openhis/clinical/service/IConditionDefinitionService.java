package com.openhis.clinical.service;

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