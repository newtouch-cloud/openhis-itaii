package com.openhis.clinical.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.clinical.domain.ConditionDefinition;
import com.openhis.clinical.mapper.ConditionDefinitionMapper;
import com.openhis.clinical.service.IConditionDefinitionService;

/**
 * 诊断定义管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class ConditionDefinitionServiceImpl extends ServiceImpl<ConditionDefinitionMapper, ConditionDefinition> implements IConditionDefinitionService {

}