/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.paymentmanage.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 发放数量 dto
 *
 * @author zwh
 * @date 2025-06-04
 */
@Data
@Accessors(chain = true)
public class DispenseQuantityDto {

    /*
     * 本次结算应上传最小包装药品追溯码数量
     */
    private Integer minpacuntDrugTracCnt;

    /*
     * 本次结算应上传耗材追溯码数量
     */
    private Integer mcsTracCnt;
}
