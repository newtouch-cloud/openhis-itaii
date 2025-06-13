/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 挂号明细报表 dto
 *
 * @author yuxj
 * @date 2025-05-20
 */
@Data
@Accessors(chain = true)
public class RegisterReportPageDto {

    /** 患者院内编码/病历号 */
    private String busNo;

    /** 患者姓名 */
    private String name;

    /** 科室 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long departmentId;
    private String departmentName;

    /**
     * 项目名
     */
    private String clinicalName;

    /**
     * 医保码
     */
    private String ybNo;

    /**
     * 挂号医生
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long doctorId;
    private String doctorName;
    /**
     * 收款人
     */
    private String payeeName;
    /**
     * 数量
     */
    private BigDecimal number;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 单位
     */
    private String quantityUnit;
    private String quantityUnit_dictText;

    /**
     * 金额
     */
    private BigDecimal totalPrice;

    /**
     * 收费时间
     */
    private String chargeTime;

    /**
     * 就诊id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long encounterId;

}
