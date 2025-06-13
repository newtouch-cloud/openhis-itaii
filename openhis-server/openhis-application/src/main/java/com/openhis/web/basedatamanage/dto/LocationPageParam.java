/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 位置信息分页查询条件
 *
 * @author zwh
 * @date 2025-03-31
 */
@Data
@Accessors(chain = true)
public class LocationPageParam {

    /** 编码 */
    private String busNo;

    /** 状态编码 */
    private Integer statusEnum;

    /** 位置类型 */
    private Integer formEnum;

    /**
     * 位置分页默认初始类型（前端传）
     */
    @NotEmpty
    private List<Integer> locationFormList;
}
