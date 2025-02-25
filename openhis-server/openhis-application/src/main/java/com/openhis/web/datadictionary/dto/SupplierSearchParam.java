/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.datadictionary.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 厂商/产地分页查询条件
 *
 * @author dh
 * @date 2025-02-18
 */
@Data
@Accessors(chain = true)
public class SupplierSearchParam implements Serializable {

    /**
     * 编号
     */
    @Schema(description = "编号")
    private String busNo;
    /**
     * 名称*
     */
    @Schema(description = "名称")
    private String name;

    /**
     * 名称*
     */
    @Schema(description = "供应商类型")
    private String typeEnum;

}
