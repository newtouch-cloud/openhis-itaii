/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.dto;

import java.sql.Time;
import java.util.Date;
import java.util.Timer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author
 * @date 2025-02-21
 */
@Data
@Accessors(chain = true)
public class OrgLocQueryDto {

    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 机构编码 */
    @JsonSerialize(using = ToStringSerializer.class)
    @Dict(dictTable = "adm_organization", dictCode = "id", dictText = "name")
    private Long organizationId;
    private String organizationId_dictText;

    /** 默认药房 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long defLocationId;

    /** 发放类别 */
    private String distributionCategoryCode;

    /**
     * 诊疗定义id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long activityDefinitionId;

    /**
     * 诊疗类型
     */
    @Dict(dictCode = "activity_category_code")
    private String activityCategoryCode;
    private String activityCategoryCode_dictText;

    /** 开始时间 */
    //@JsonFormat(pattern = "HH:mm:ss")
    private Time startTime;

    /** 结束时间 */
    //@JsonFormat(pattern = "HH:mm:ss")
    private Time endTime;

    /** 显示顺序 */
    private Integer displayOrder;

}
