package com.openhis.web.datadictionary.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.enums.ConditionDefinitionSource;
import com.openhis.common.enums.PublicationStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 疾病目录更新Dto
 *
 * @author lpt
 * @date 2025-02-25
 */

@Data
@Accessors(chain = true)
public class DiseaseManageUpDto {
    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 所属分类 */
    private Integer sourceEnum;

    /** 编码 */
    private String conditionCode;

    /** 诊断名称 */
    @NotBlank(message = "疾病名称不能为空")
    private String name;

    /** 诊断名称拼音 */
    private String pyStr;

    /** 诊断名称五笔拼音 */
    private String wbStr;

    /** 类型 */
    private String typeCode;

    /** 描述 */
    private String description;

    /** 医保标记 */
    private Integer ybFlag;

    /** 医保编码 */
    private String ybNo;

    /** 医保对码标记 */
    private Integer ybMatchFlag;
}
