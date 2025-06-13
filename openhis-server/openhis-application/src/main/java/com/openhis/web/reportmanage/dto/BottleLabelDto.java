package com.openhis.web.reportmanage.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 护士输液的瓶签实体类
 *
 * @author Wuser
 * @date 2025/5/10
 */
@Data
@Accessors(chain = true)
public class BottleLabelDto {

    /**
     * 机构名
     */
    @Dict(dictTable = "adm_organization", dictCode = "id", dictText = "name")
    private Long orgId;
    private String orgId_dictText;

    /**
     * 药品请求编码
     */
    private String busNo;

    /**
     * 姓名
     */
    private String patientName;

    /**
     * 性别
     */
    private Integer genderEnum;
    private String genderEnum_enumText;

    /**
     * 生日
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthDate;

    /**
     * 年龄
     */
    private String age;

    /**
     * 药品名称
     */
    private String medName;

    /**
     * 药品规格
     */
    private String space;

    /**
     * 单次剂量
     */
    private BigDecimal dose;

    /**
     * 单次剂量单位
     */
    @Dict(dictCode = "unit_code")
    private String doseUnitCode;
    private String doseUnitCode_dictText;

    /**
     * 用药频次
     */
    @Dict(dictCode = "rate_code")
    private String rateCode;
    private String rateCode_dictText;

    /**
     * 用法
     */
    @Dict(dictCode = "method_code")
    private String methodCode;
    private String methodCode_dictText;

    /**
     * 执行时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date occurrenceStartTime;

    /**
     * 执行人
     */
    @Dict(dictTable = "adm_practitioner_role", dictCode = "practitioner_id", dictText = "name")
    private Long performerId;
    private String performerId_dictText;

}
