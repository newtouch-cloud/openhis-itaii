/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.datadictionary.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 项目定价查询dto
 *
 * @author zxy
 * @date 2025-02-21
 */
@Data
@Accessors(chain = true)
public class ItemDefSearchParam {

    /** 定价类型 */
    private String definitionType;
    /** 定价项目 */
    private String chargeItem;
    /** 模糊搜索条件 */
    private String searchKey;
    /** 状态 */
    private Integer statusEnum;
}
