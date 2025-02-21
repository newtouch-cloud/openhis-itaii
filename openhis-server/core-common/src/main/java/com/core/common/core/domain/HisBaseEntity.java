package com.core.common.core.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * Entity基类
 * 
 * @author system
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class HisBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 创建者 */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /** 更新者 */
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    /** 更新时间 */
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    /** 租户ID */
    private Integer tenantId;

    /** 删除标识 */
    private Integer deleteFlag;

}
