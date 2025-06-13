package com.openhis.web.doctorstation.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 电子处方 dto
 */
@Data
@Accessors(chain = true)
public class ElepPrescriptionInfoDto {
    /** 租户ID */
    private Integer tenantId;
    /** 处方号 */
    private String prescriptionNo;
    /** 门诊号 */
    private String iptOtpNo;
    /** 科室病区 */
    private String departmentWard;
    /** 有效天数 */
    private Integer validityDays;
    /** 状态 */
    private Integer statusEnum;
    private String statusEnum_enumText;
    /** 开方医生名 */
    private String practitionerName;
    /** 开单科室 */
    private String prscDeptName;
    /** 处方开立日期 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date prscTime;
    /** 取药状态 */
    private String medStatus;
    /** 延长原因 */
    private String extensionReason;
    /** 撤销原因 */
    private String quashReason;
    /** 诊断名 */
    private String conditionName;
    /** 处方类别 */
    private Integer rxTypeCode;
    private String rxTypeCode_enumText;
}
