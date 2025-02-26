package com.openhis.web.datadictionary.dto;

import com.openhis.common.enums.PublicationStatus;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 诊疗目录分页检索条件
 *
 * @author lpt
 * @date 2025-02-25
 */
@Data
@Accessors(chain = true)
public class DiagnosisTreatmentSelParam {
    /** 目录类别 */
    private Integer categoryEnum;

    /** 类型 */
    private Integer typeEnum;

    /** 医保对码标记 */
    private Integer ybMatchFlag;

    /** 规则id */
    private Integer ruleId;

    /** 状态 */
    private Integer statusEnum;
}
