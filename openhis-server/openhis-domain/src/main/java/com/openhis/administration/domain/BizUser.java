package com.openhis.administration.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.core.domain.HisBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("sys_user")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class BizUser extends HisBaseEntity {

    /** 用户ID */
    // @TableId(type = IdType.ASSIGN_ID)
    private Long userId;

    /** 部门ID */
    private Long deptId;

    /** 用户账号 */
    private String userName;

    /** 用户昵称 */
    private String nickName;

    /** 用户类型 */
    private String userType;

    /** 用户邮箱 */
    private String email;

    /** 手机号码 */
    private String phonenumber;

    /** 用户性别 */
    private String sex;

    /** 用户头像 */
    private String avatar;

    /** 密码 */
    private String password;

    /** 帐号状态（0正常 1停用） */
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 最后登录IP */
    private String loginIp;

    /** 最后登录时间 */
    private Date loginDate;

    /** 备注 */
    private String remark;

}
