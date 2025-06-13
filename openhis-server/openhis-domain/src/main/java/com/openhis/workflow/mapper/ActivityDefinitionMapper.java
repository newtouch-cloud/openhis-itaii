package com.openhis.workflow.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.openhis.workflow.domain.ActivityDefinition;

/**
 * 诊疗定义管理Mapper接口
 *
 * @author system
 * @date 2025-02-20
 */
@Repository
public interface ActivityDefinitionMapper extends BaseMapper<ActivityDefinition> {

    /**
     * 查询指定诊疗的-诊疗定义id
     *
     * @param activityName 诊疗名称
     * @return 诊疗定义id
     */
    Long getAppointActivityDefinitionId(@Param("skinTestInspection") String skinTestInspection,
        @Param("status") Integer status);

}