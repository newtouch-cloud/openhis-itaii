package com.openhis.administration.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.core.common.core.domain.HisBaseEntity;
import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 机构位置关系管理Entity实体
 *
 * @author system
 * @date 2025-02-22
 */
@Data
@TableName("adm_organization_location")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OrganizationLocation extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 机构编码 */
    private Long organizationId;

    /** 位置编码 */
    private Long locationId;

    /** 默认发药药房 */
    private Long defLocationId;

    /** 默认执行科室 */
    private Long defOrganizationId;

    /** 发放类别 */
    @Dict(dictCode = "distribution_category_code")
    private String distributionCategoryCode;

    /** 开始时间 */
    private Date startTime;

    /** 结束时间 */
    private Date endTime;

    /** 显示顺序 */
    private Integer displayOrder;

}