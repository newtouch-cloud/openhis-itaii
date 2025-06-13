/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.dto;

import java.io.Serializable;

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
public class OrgLocQueryParam implements Serializable {

    /**
     * 科室id
     */
    private Long organizationId;

    /**
     * 药房id
     */
    private Long defLocationId;

    /** 发放类别 */
    private String distributionCategoryCode;

}
