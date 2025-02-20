package com.openhis.workflow.domain;

import java.math.BigDecimal;
import java.util.Date;

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
    private Integer categoryEnum;

    /** 编码 */
    private String code;

    /** 项目名称 */
    private String name;

    /** 项目名称拼音 */
    private String pyCode;

    /** 五笔拼音 */
    private String wbCode;

    /** 类型 */
    private Integer typeEnum;

    /** 使用单位 */
    private String permittedUnit;

    /** 医保标记 */
    private Integer ybFlag;

    /** 医保编码 */
    private String ybCode;

    /** 医保对码标记 */
    private Integer ybMatchFlag;

    /** 状态 */
    private String statusCode;

    /** 身体部位 */
    private String bodySiteCode;

    /** 所需标本 */
    private String specimenCode;

    /** 说明 */
    private String description;

    /** 规则id */
    private String ruleId;

    /** 删除状态 */
    private Integer deleteFlag;

}