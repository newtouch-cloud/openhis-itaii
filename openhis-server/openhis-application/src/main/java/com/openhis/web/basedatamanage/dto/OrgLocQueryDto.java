/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.dto;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

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
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 机构编码 */
    private Long organizationId;

    /** 位置编码 */
    private Long locationId;

    /** 默认药房 */
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
