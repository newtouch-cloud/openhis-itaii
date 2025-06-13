/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Set;

/**
 * 【3101】明细审核事前分析服务（输入-规则分析信息）
 * 【3102】明细审核事中分析服务（输入-规则分析信息）
 *
 * @author SunJQ
 * @date 2025-04-15
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FsiConsultation3101Param {

    // 1. 系统编码
    @JSONField(name = "syscode")
    private String syscode;

    // 2. 参保人信息
    @JSONField(name = "patient_dtos")
    private List<FsiPatient3101Param> patientDtos;

    // 3. 规则标识集合（非必填）
    @JSONField(name = "rule_ids")
    private Set<String> ruleIds;

    // 4. 任务ID
    @JSONField(name = "task_id")
    private String taskId;

    // 5. 触发场景
    @JSONField(name = "trig_scen")
    private String trigScen;
}
