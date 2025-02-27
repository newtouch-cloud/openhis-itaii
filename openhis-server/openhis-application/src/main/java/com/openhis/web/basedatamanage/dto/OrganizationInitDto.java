/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 科室初始化 dto
 *
 * @author
 * @date 2025-02-25
 */
@Data
@Accessors(chain = true)
public class OrganizationInitDto {

    private Integer value;
    private String code;

    public OrganizationInitDto(Integer value, String code) {
        this.code = code;
        this.value = value;
    }
}
