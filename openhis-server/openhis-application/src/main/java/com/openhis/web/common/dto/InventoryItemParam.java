/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.common.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 库存项目筛选条件
 *
 * @author zwh
 * @date 2025-04-19
 */
@Data
@Accessors(chain = true)
public class InventoryItemParam {

    /** 项目类型 */
    private Integer itemType; // 1:药品 , 2: 耗材 , 3:诊疗

    /** 项目id */
    private Long itemId;

    /** 项目名称 */
    private String name;

    /** 拼音码 */
    private String pyStr;

    /** 五笔码 */
    private String wbStr;

    /** 源仓库 */
    private Long orgLocationId;

    /** 目的仓库 */
    private Long objLocationId;

    /** 采购标志 */
    private Integer purchaseFlag;

    /**
     * 产品批号
     */
    private String lotNumber;
}
