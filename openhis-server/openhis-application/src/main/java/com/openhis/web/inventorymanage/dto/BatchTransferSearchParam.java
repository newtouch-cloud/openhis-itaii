/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 生成批量调拨单查询条件
 *
 * @author yangmo
 * @date 2025-04-21
 */
@Data
@Accessors(chain = true)
public class BatchTransferSearchParam {

    /** 源仓库 */
    private Long sourceLocationId;

    /** 目的仓库 */
    private Long purposeLocationId;

    /** 药品类型 */
    private Integer medicationType; // 1:药品 , 2: 耗材 , 3:诊疗
    private String medicationType_enumText;

}
