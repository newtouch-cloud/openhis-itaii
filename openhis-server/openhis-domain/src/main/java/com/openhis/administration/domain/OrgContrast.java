package com.openhis.administration.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.core.common.core.domain.HisBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【对照表】Entity实体
 *
 * @author system
 * @date 2025-04-25
 */
@Data
@TableName("org_contrast")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OrgContrast extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 字典编码 */
    private Long dictCode;

    /** 项目代码 */
    private String code;

    /** 类型 */
    private Integer typeEnum;

}