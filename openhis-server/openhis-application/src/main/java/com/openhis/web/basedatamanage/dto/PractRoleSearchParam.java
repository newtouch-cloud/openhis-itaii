/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 岗位分页查询条件
 *
 * @author
 * @date 2025-02-21
 */
@Data
@Accessors(chain = true)
public class PractRoleSearchParam implements Serializable {

    /** 专业编码枚举 */
    private Integer specialtyEnum;

    /** 机构 */
    private Long orgId;

}
