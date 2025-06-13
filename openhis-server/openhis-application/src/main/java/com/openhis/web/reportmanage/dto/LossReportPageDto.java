/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 报损明细报表 dto
 *
 * @author ym
 * @date 2025-05-21
 */
@Data
@Accessors(chain = true)
public class LossReportPageDto {

    /** 编码 */
    private String busNo;

    /** 项目名称 */
    private String name;

    /** 规格 */
    private String totalVolume;

    /** 厂家 */
    private String manufacturerText;

    /** 产品批号 */
    private String lotNumber;

    /** 仓库 */
    private String locationName;

    /** 计量单位 */
    @Dict(dictCode = "unit_code")
    private String unitCode;
    private String unitCode_dictText;

    /** 数量 */
    private BigDecimal itemQuantity;

    /** 审批时间(盘点时间) */
    private Date approvalTime;
}
