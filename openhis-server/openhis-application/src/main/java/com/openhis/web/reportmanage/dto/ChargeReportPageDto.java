/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 费用明细报表 dto
 *
 * @author yuxj
 * @date 2025-05-20
 */
@Data
@Accessors(chain = true)
public class ChargeReportPageDto {

    /** 支付ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long paymentId;

    /** 费用项ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long chargeId;

    /**
     * 小计/合计
     */
    private BigDecimal subtotalAmount;
    /** 患者姓名 */
    private String name;

    /** 性别 */
    private Integer genderEnum;
    private String genderEnum_enumText;

    /** 出生日期 */
    private Date birthDate;

    /** 年龄 */
    private String age;

    /** 证件号 */
    private String idCard;

    /** 医保号 */
    private String ybCode;

    /** 就诊日期 */
    private String encounterDate;

    /** 患者院内编码/病历号/门诊号 */
    private String busNo;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long departmentId;
    private String departmentName;
    /**
     * 项目类型
     */
    private Integer clinicalType;
    /**
     * 项目名
     */
    private String clinicalName;
    /**
     * 项目编码
     */
    private String clinicalNo;

    /**
     * 医保码
     */
    private String ybNo;
    /**
     * 医保类别
     */
    @Dict(dictCode = "med_chrgitm_type")
    private String type;
    private String type_dictText;
    /**
     * 开单人
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long issuerId;
    private String issuerName;
    /**
     * 收款人
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long payeeId;
    private String payeeName;
    /**
     * 数量
     */
    private BigDecimal number;
    /**
     * 单位
     */
    @Dict(dictCode = "unit_code")
    private String quantityUnit;
    private String quantityUnit_dictText;
    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 金额
     */
    private BigDecimal totalPrice;

    /**
     * 收费时间
     */
    private String chargeTime;

    /**
     * 规格
     */
    private String totalVolume;

    /**
     * 医保等级
     */
    private Integer chrgitmLv;
    private String chrgitmLv_enumText;
    /**
     * 就诊id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long encounterId;
}
