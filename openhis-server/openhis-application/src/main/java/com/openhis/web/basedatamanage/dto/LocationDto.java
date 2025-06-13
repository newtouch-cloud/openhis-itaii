/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author
 * @date 2025-02-21
 */
@Data
@Accessors(chain = true)
public class LocationDto {
    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 编码 */
    @NotBlank(message = "位置编码不能为空")
    private String busNo;

    /** 名称 */
    @NotBlank(message = "位置名称不能为空")
    private String name;

    /** 状态编码 */
    @NotNull(message = "状态编码不能为空")
    // private LocationStatus statusEnum;
    private Integer statusEnum;
    private String statusEnum_enumText;

    /** 操作状态 */
    @NotNull(message = "操作状态不能为空")
    // private LocationBedStatus operationalEnum;
    private Integer operationalEnum;
    private String operationalEnum_enumText;

    /** 模式编码 */
    @NotNull(message = "模式编码不能为空")
    // private LocationMode modeEnum;
    private Integer modeEnum;
    private String modeEnum_enumText;

    /** 模式编码 */
    private String typeCode;

    /** 功能编码 */
    @NotBlank(message = "功能编码不能为空")
    private String typeJson;

    /** 拼音码 */
    private String pyStr;

    /** 五笔码 */
    private String wbStr;

    /** 物理形式枚举 */
    @NotNull(message = "物理形式枚举不能为空")
    // private LocationForm formEnum;
    private Integer formEnum;
    private String formEnum_enumText;

    /** 机构编码 */
    @NotNull(message = "机构编码不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long organizationId;

    /** 显示顺序 */
    private Integer displayOrder;

    /** 子集合 */
    private List<LocationDto> children = new ArrayList<>();

}
