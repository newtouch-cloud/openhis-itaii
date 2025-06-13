package com.openhis.workflow.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.common.enums.PublicationStatus;
import com.openhis.workflow.domain.ActivityDefinition;
import com.openhis.workflow.mapper.ActivityDefinitionMapper;
import com.openhis.workflow.service.IActivityDefinitionService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 诊疗定义管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Slf4j
@Service
@AllArgsConstructor
public class ActivityDefinitionServiceImpl extends ServiceImpl<ActivityDefinitionMapper, ActivityDefinition>
    implements IActivityDefinitionService {
    private final ActivityDefinitionMapper activityDefinitionMapper;

    /**
     * 增加外来诊疗项目
     * 
     * @param activityDefinition 诊疗项目实体
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addDiagnosisTreatment(ActivityDefinition activityDefinition) {
        // 根据诊疗编码判断诊疗项目是否存在
        List<ActivityDefinition> activityDefinitions =
            activityDefinitionMapper.selectList(new LambdaQueryWrapper<ActivityDefinition>()
                .eq(ActivityDefinition::getBusNo, activityDefinition.getBusNo()));
        if (activityDefinitions.size() > 0) {
            return false;
        }
        // 新增诊疗项目
        int insert = activityDefinitionMapper.insert(activityDefinition);
        if (insert != 1) {
            return false;
        }
        return true;
    }

    @Override
    public boolean addYbDiagnosisTreatment(ActivityDefinition activityDefinition) {
        return false;
    }

    /**
     * 查询指定诊疗的-诊疗定义id
     *
     * @param activityName 诊疗名称
     * @return 诊疗定义id
     */
    @Override
    public Long getAppointActivityDefinitionId(String activityName) {
        return baseMapper.getAppointActivityDefinitionId(activityName, PublicationStatus.ACTIVE.getValue());
    }

}