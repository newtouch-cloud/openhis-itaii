/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 清单项目明细
 *
 * @author yuxj
 * @date 2025-04-22
 */
@Data
@Accessors(chain = true)
public class EleInvoiceListDetailDto {
    // 明细流水号
    private Long listDetailNo;
    // 医疗收费项目类别;
    private String ybType;
    // 药品编码
    private String code;
    // 药品名称
    private String name;
    // 医保项目编码
    private String ybCode;
    // 计量单位
    private String unit;
    // 单价
    private BigDecimal std;
    // 数量
    private Integer number;
    // 金额
    private BigDecimal amt;
    // 自费金额
    private BigDecimal selfAmt;
    // 医保药品分类
    private Integer medicalCareType;
    // 收费项目名称
    private String chargeName;
    // 收费项目代码
    private String chargeCode;
}
