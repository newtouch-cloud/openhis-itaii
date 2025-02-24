package com.openhis.clinical.domain;

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
 * 诊断定义管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("cli_condition_definition")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ConditionDefinition extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 所属分类 */
    private Integer sourceEnum;

    /** 编码 */
    private String condition_code;

    /** 诊断名称 */
    private String name;

    /** 诊断名称拼音 */
    private String pyStr;

    /** 诊断名称五笔拼音 */
    private String wbStr;

    /** 类型 */
    private String typeCode;

    /** 描述 */
    private String description;

    /** 医保标记 */
    private Integer ybFlag;

    /** 医保编码 */
    private String ybNo;

    /** 医保对码标记 */
    private Integer ybMatchFlag;

    /** 状态 */
    private Integer statusEnum;


}