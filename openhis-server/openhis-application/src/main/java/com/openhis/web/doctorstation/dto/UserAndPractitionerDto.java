package com.openhis.web.doctorstation.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户 及 参与者 dto
 */
@Data
@Accessors(chain = true)
public class UserAndPractitionerDto {

    /**
     * 账号
     */
    private String userName;

    /**
     * 姓名
     */
    private String nickName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 电话
     */
    private String phonenumber;

    /**
     * 性别
     */
    private String sex;

    /**
     * 密码
     */
    private String password;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 出生日期
     */
    private Date birthDate;

    /**
     * 地址
     */
    private String address;

    /**
     * 医保码
     */
    private String ybNo;

    /**
     * 机构id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orgId;

    /**
     * 位置id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long locationId;

    /**
     * 角色id集合
     */
    private List<Long> roleIds;

    /**
     * 角色code集合
     */
    private List<String> roleCodes;

}
