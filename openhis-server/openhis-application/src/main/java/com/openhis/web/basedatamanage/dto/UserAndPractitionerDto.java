package com.openhis.web.basedatamanage.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户 及 参与者 dto
 */
@Data
@Accessors(chain = true)
public class UserAndPractitionerDto {

    /**
     * 用户id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**
     * 参与者id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long practitionerId;

    /**
     * 账号
     */
    private String userName;

    /**
     * 姓名
     */
    private String nickName;

    /** 拼音码 */
    private String pyStr;

    /** 五笔码 */
    private String wbStr;

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
    @Dict(dictCode = "sys_user_sex")
    private String sex;
    private String sex_dictText;

    /**
     * 密码
     */
    private String password;

    /**
     * 状态（0正常 1停用）
     */
    @Dict(dictCode = "sys_normal_disable")
    private String status;
    private String status_dictText;

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
    @Dict(dictTable = "adm_organization", dictCode = "id", dictText = "name")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orgId;
    private String orgId_dictText;

    /**
     * 责任科室
     */
    private List<PractitionerOrgAndLocationDto> responsibilityOrgDtoList;

    /**
     * 参与者角色集合
     */
    private List<PractitionerRolesDto> practitionerRolesDtoList;

    /**
     * 医生出诊科室集合
     */
    private List<PractitionerOrgAndLocationDto> doctorVisitOrgDtoList;

    /**
     * 管理库房集合
     */
    private List<PractitionerOrgAndLocationDto> manageLocationDtoList;

}
