package com.openhis.web.datadictionary.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.enums.ActivityDefCategory;
import com.openhis.common.enums.PublicationStatus;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 诊疗目录分页检索
 *
 * @author lpt
 * @date 2025-02-25
 */
@Data
@Accessors(chain = true)
public class DiagnosisTreatmentDto {
    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 目录类别 */
    private ActivityDefCategory categoryEnum;

    /** 编码 */
    private String busNo;

    /** 项目名称 */
    private String name;

    /** 项目名称拼音 */
    private String pyStr;

    /** 五笔拼音 */
    private String wbStr;

    /** 类型 */
    private String typeCode;

    /** 使用单位 */
    private String permittedUnitCode;

    /** 医保标记 */
    private Integer ybFlag;
    private String ybFlag_enumText;

    /** 医保编码 */
    private String ybNo;

    /** 医保对码标记 */
    private Integer ybMatchFlag;
    private String ybMatchFlag_enumText;

    /** 状态 */
    private PublicationStatus statusEnum;

    /** 身体部位 */
    private String bodySiteCode;

    /** 所需标本 */
    private String specimenCode;

    /** 说明 */
    private String descriptionText;

    /** 规则id */
    private Integer ruleId;
}
