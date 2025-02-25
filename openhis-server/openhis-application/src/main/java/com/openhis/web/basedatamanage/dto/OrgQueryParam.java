/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.dto;

import java.io.Serializable;

import com.openhis.common.enums.OrganizationClass;
import com.openhis.common.enums.OrganizationType;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 科室分页查询条件
 *
 * @author
 * @date 2025-02-21
 */
@Data
@Accessors(chain = true)
public class OrgQueryParam implements Serializable {

    /** 活动标识 */
    private Integer activeFlag;

    /** 机构类型枚举 */
    private OrganizationType typeEnum;

    /** 机构分类枚举 */
    private OrganizationClass classEnum;

}
