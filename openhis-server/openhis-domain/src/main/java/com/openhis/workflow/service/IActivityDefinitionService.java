package com.openhis.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.workflow.domain.ActivityDefinition;

/**
 * 诊疗定义管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IActivityDefinitionService extends IService<ActivityDefinition> {

    /**
     * 新增诊疗目录
     *
     * @param activityDefinition 诊疗目录实体
     * @return
     */
    boolean addDiagnosisTreatment(ActivityDefinition activityDefinition);

    /**
     * 新增医保诊疗目录
     *
     * @param activityDefinition 诊疗目录实体
     * @return
     */
    boolean addYbDiagnosisTreatment(ActivityDefinition activityDefinition);

    /**
     * 查询指定诊疗的-诊疗定义id
     *
     * @param activityName 诊疗名称
     * @return 皮试检查-诊疗定义id
     */
    Long getAppointActivityDefinitionId(String activityName);

}