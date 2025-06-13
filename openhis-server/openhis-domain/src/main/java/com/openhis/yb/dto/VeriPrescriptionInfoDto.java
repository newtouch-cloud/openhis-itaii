/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 医保电子处方信息
 *
 * @author yuxj
 * @date 2025-05-06
 */
@Data
@Accessors(chain = true)
public class VeriPrescriptionInfoDto {

    /** 处方号 */
    private String prescriptionNo;
    /** 门诊号 */
    private String iptOtpNo;
    /** 姓名 */
    private String patientName;
    /** 证件号 */
    private String certno;
    /** 取药状态 */
    private String medStatus;
    /** 状态 */
    private Integer statusEnum;
    private String statusEnum_enumText;
    /** 开单科室 */
    private String prscDeptName;
    /** 挂号日期 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date mdtrtTime;
    /** 处方开立日期 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date prscTime;

    /** 租户ID */
    private Integer tenantId;
    /** 电子凭证线上身份核验流水号 */
    private String authNo;
    /** 电子凭证令牌 */
    private String ecToken;
    /** 医保处方编号 */
    private String hiRxno;

}
