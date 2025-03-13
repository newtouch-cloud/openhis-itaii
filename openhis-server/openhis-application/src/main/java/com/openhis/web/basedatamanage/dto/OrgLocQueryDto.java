/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.dto;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

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
    private Long organizationId;

    /** 位置编码 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long locationId;

    /** 默认药房 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long defLocationId;

    /** 药品类别 */
    private String medCategoryCode;

    /** 开始时间 */
    private Date startTime;

    /** 结束时间 */
    private Date endTime;

    /** 显示顺序 */
    private Integer displayOrder;

}
