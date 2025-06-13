package com.openhis.workflow.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.core.domain.HisBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 诊疗定义管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("wor_activity_definition")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ActivityDefinition extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 目录类别 */
    private String categoryCode;

    /** 编码 */
    private String busNo;

    /** 项目名称 */
    private String name;

    /** 项目名称拼音 */
    private String pyStr;

    /** 五笔拼音 */
    private String wbStr;

    /** 类型 */
    private Integer typeEnum;

    /** 使用单位 */
    private String permittedUnitCode;

    /** 所属科室 */
    private Long orgId;

    /** 所在位置 */
    private Long locationId;

    /** 医保标记 */
    private Integer ybFlag;

    /** 医保编码 */
    private String ybNo;

    /** 医保对码标记 */
    private Integer ybMatchFlag;

    /** 状态 */
    private Integer statusEnum;

    /** 身体部位 */
    private String bodySiteCode;

    /** 所需标本 */
    private String specimenCode;

    /** 说明 */
    private String descriptionText;

    /** 规则id */
    private Integer ruleId;

    /** 医保等级 */
    private Integer chrgitmLv;

    /** 子项json */
    private String childrenJson;

    /** 划价标记 */
    private Integer pricingFlag;
}