package com.openhis.web.doctorstation.dto;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 诊断定义 业务分类
 */
@Data
@Accessors(chain = true)
public class ConditionDefinitionBusinessClass {

    /**
     * 病人历史诊断
     */
    List<ConditionDefinitionMetadata> patientHistoryList;

    /**
     * 医生常用诊断
     */
    List<ConditionDefinitionMetadata> doctorCommonUseList;

    /**
     * 用户个人诊断
     */
    List<ConditionDefinitionMetadata> userPersonalList;

    /**
     * 科室诊断
     */
    List<ConditionDefinitionMetadata> organizationList;
}
