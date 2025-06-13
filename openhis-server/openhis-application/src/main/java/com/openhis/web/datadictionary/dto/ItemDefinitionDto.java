/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.datadictionary.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 收费项目 dto
 *
 * @author zxy
 * @date 2025-02-21
 */
@Data
@Accessors(chain = true)
public class ItemDefinitionDto {

    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 名称 */
    private String chargeName;

    /** 状态 */
    private Integer statusEnum;
    private String statusEnum_enumText;

    /** 所属科室 */
    @Dict(dictTable = "adm_organization", dictCode = "id", dictText = "name")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orgId;
    private String orgId_dictText;

    /** 财务类别 */
    @Dict(dictCode = "fin_type_code")
    private String typeCode;
    private String typeCode_dictText;

    /** 医保类别 */
    @Dict(dictCode = "yb_type")
    private String ybType;
    private String ybType_dictText;

    /** 代码 */
    private String instanceTable;

    /** 基础价格 */
    private BigDecimal price;

    /**
     * 费用明细个数
     */
    private Integer detailCount;

}
