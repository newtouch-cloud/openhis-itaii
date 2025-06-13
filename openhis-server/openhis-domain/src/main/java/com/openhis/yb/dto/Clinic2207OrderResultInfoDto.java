/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 结算基础信息
 *
 * @author SunJQ
 * @date 2025-05-08
 */
@Data
@Accessors(chain = true)
public class Clinic2207OrderResultInfoDto {
    // 主键字段
    @JSONField(name = "mdtrt_id")
    private String mdtrtId; // 就诊ID

    @JSONField(name = "psn_no")
    private String psnNo; // 人员编号

    @JSONField(name = "psn_name")
    private String psnName; // 人员姓名

    @JSONField(name = "psn_cert_type")
    private String psnCertType; // 人员证件类型

    @JSONField(name = "certno")
    private String certno; // 证件号码

    @JSONField(name = "gend")
    private String gend; // 性别

    @JSONField(name = "naty")
    private String naty; // 民族

    @JSONField(name = "brdy")
    private Date brdy; // 出生日期

    @JSONField(name = "age")
    private BigDecimal age; // 年龄

    @JSONField(name = "insutype")
    private String insutype; // 险种类型

    @JSONField(name = "psn_type")
    private String psnType; // 人员类别

    @JSONField(name = "cvlserv_flag")
    private String cvlservFlag; // 公务员标志

    @JSONField(name = "setl_time")
    private Date setlTime; // 结算时间

    @JSONField(name = "mdtrt_cert_type")
    private String mdtrtCertType; // 就诊凭证类型

    @JSONField(name = "med_type")
    private String medType; // 医疗类别

    @JSONField(name = "medins_setl_id")
    private String medinsSetlId; // 医药机构结算ID  存放发送方报文ID

    @JSONField(name = "setldetail")
    private List<Clinic2206FundPaymentResult> setldetail;// 结算详细信息
}
