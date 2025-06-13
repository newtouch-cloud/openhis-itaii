/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *【3302】目录对照撤销
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MedicalDirectory3302Param {

    // 定点医药机构编号
    private String fixmedinsCode;

    // 定点医药机构目录编号
    private String fixmedinsHilistId;

    // 目录类别
    private String listType;

    // 医疗目录编码
    private String medListCodg;
}
