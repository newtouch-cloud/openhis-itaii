/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.outpatientmanage.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 就诊人员列表
 *
 * @author WangYang
 * @date 2025-03-18
 */
@Data
@Accessors(chain = true)
public class OutpatientDisposalEncounterInfoPageDto {

    /** 就诊ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long encounterId;

    /** 科室 */
    private String departmentName;

    /** 患者姓名 */
    private String patientName;

    /** 性别 */
    private Integer genderEnum;
    private String genderEnum_enumText;

    /** 就诊日期 */
    private String encounterDate;
}
