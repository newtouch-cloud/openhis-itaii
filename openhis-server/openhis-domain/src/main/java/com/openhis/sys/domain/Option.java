package com.openhis.sys.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.core.domain.HisBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 系统选项配置Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("sys_option")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Option extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 选项key */
    private String optionKey;

    /** 选项value */
    private String optionValue;

    /** 说明 */
    private String optionDesc;

}
