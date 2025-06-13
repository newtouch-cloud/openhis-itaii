package com.openhis.web.datadictionary.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;
import com.openhis.common.enums.ConditionDefinitionSource;
import com.openhis.common.enums.PublicationStatus;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 疾病目录分页Dto
 *
 * @author lpt
 * @date 2025-02-25
 */
@Data
@Accessors(chain = true)
public class DiseaseManageDto {
    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 所属分类 */
    private Integer sourceEnum;
    private String sourceEnum_enumText;

    /** 编码 */
    private String conditionCode;

    /** 诊断名称 */
    private String name;

    /** 诊断名称拼音 */
    private String pyStr;

    /** 诊断名称五笔拼音 */
    private String wbStr;

    /** 类型 */
    @Dict(dictCode = "condition_type_code")
    private String typeCode;
    private String typeCode_dictText;

    /** 描述 */
    private String description;

    /** 医保标记 */
    private Integer ybFlag;
    private String ybFlag_enumText;

    /** 医保编码 */
    private String ybNo;

    /** 医保对码标记 */
    private Integer ybMatchFlag;
    private String ybMatchFlag_enumText;

    /** 状态 */
    private Integer statusEnum;
    private String statusEnum_enumText;

}
