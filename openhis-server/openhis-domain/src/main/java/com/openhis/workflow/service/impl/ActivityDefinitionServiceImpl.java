package com.openhis.workflow.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.workflow.domain.ActivityDefinition;
import com.openhis.workflow.mapper.ActivityDefinitionMapper;
import com.openhis.workflow.service.IActivityDefinitionService;

/**
 * 诊疗定义管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class ActivityDefinitionServiceImpl extends ServiceImpl<ActivityDefinitionMapper, ActivityDefinition> implements IActivityDefinitionService {

}