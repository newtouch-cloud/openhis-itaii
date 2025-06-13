package com.core.system.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 租户信息表
 * 
 * @author system
 */
@Data
@TableName("sys_tenant")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SysTenant implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 租户ID（使用数据库序列生成）
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 租户名称
     */
    private String tenantName;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除状态（0未删除 1已删除）
     */
    private String deleteFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

}
