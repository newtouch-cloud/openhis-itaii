/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @date 2025-02-21
 */
@Data
@Accessors(chain = true)
public class LocationQueryDto {

    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 编码 */
    private String no;

    /** 名称 */
    private String name;

    /** 子集合 */
    private List<LocationQueryDto> children = new ArrayList<>();

}
