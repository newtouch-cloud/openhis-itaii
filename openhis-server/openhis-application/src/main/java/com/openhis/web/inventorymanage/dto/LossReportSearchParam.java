/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

/**
 * 报损单分页查询条件
 *
 * @author gyy
 * @date 2025-04-04
 */
@Data
@Accessors(chain = true)
public class LossReportSearchParam {

    /** 单据号 */
    @Length(max = 255)
    private String busNo;

    /** 状态 */
    private Integer statusEnum;

    /** 制单人 */
    private Long applicantId;

    /** 单据时间 */
    private Date createTime;
}
