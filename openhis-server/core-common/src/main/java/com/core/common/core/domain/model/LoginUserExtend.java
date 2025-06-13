package com.core.common.core.domain.model;

import lombok.Data;

/**
 * 登录用户扩展
 */
@Data
public class LoginUserExtend {

    /**
     * 机构/科室id
     */
    private Long orgId;
    /**
     * 科室名称
     */
    private String orgName;

    /**
     * 参与者id
     */
    private Long practitionerId;

}
