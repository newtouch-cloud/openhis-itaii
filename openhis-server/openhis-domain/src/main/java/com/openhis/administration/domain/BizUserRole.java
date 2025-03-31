package com.openhis.administration.domain;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户角色Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("sys_user_role")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class BizUserRole {

    /** 用户ID */
    private Long userId;

    /** 角色ID */
    private Long roleId;

}
