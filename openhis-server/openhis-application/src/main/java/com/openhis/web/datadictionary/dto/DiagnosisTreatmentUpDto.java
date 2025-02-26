package com.openhis.web.datadictionary.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 诊疗目录分页更新
 *
 * @author lpt
 * @date 2025-02-25
 */
@Data
@Accessors(chain = true)
public class DiagnosisTreatmentUpDto {
    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 目录类别 */
    private Integer categoryEnum;

    /** 编码 */
    @NotBlank(message = "项目编码不能为空")
    private String busNo;

    /** 项目名称 */
    @NotBlank(message = "项目名称不能为空")
    private String name;

    /** 项目名称拼音 */
    private String pyStr;

    /** 五笔拼音 */
    private String wbStr;

    /** 类型 */
    private Integer typeEnum;

    /** 使用单位 */
    private String permittedUnitCode;

    /** 医保标记 */
    private Integer ybFlag;

    /** 医保编码 */
    private String ybNo;

    /** 医保对码标记 */
    private Integer ybMatchFlag;

    /** 身体部位 */
    private String bodySiteCode;

    /** 所需标本 */
    private String specimenCode;

    /** 说明 */
    private String descriptionText;

    /** 规则id */
    private Integer ruleId;
}
