/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 住院患者疾病诊断类型
 *
 * @author SunJQ
 * @date 2025-04-15
 */
@Getter
@AllArgsConstructor
public enum YbIptDiseTypeCode {

    // 出院诊断
    DISCHARGE_DIAGNOSIS("1", "出院诊断"),

    // 入院诊断
    ADMISSION_DIAGNOSIS("31", "入院诊断"),

    // 并发症诊断
    COMPLICATION_DIAGNOSIS("10", "并发症诊断"),

    // 术前诊断
    PREOPERATIVE_DIAGNOSIS("4", "术前诊断"),

    // 院内感染诊断
    NOSOCOMIAL_INFECTION_DIAGNOSIS("11", "院内感染诊断"),

    // 术后诊断
    POSTOPERATIVE_DIAGNOSIS("5", "术后诊断"),

    // 主要诊断
    PRIMARY_DIAGNOSIS("12", "主要诊断"),

    // 次要诊断
    SECONDARY_DIAGNOSIS("13", "次要诊断"),

    // 中医出院诊断病名
    TCM_DISCHARGE_DIAGNOSIS_DISEASE_NAME("14", "中医出院诊断病名"),

    // 超声诊断
    ULTRASOUND_DIAGNOSIS("8", "超声诊断"),

    // 中医出院主病
    TCM_DISCHARGE_PRIMARY_DISEASE("141", "中医出院主病"),

    // 中医出院主证
    TCM_DISCHARGE_PRIMARY_SYNDROME("142", "中医出院主证"),

    // 损伤、中毒的外部原因
    EXTERNAL_CAUSE_OF_INJURY_OR_POISONING("98", "损伤、中毒的外部原因"),

    // 门诊诊断
    OUTPATIENT_DIAGNOSIS("2", "门诊诊断"),

    // 其他
    OTHER("99", "其他"),

    // 入院初步诊断
    ADMISSION_PRELIMINARY_DIAGNOSIS("3", "入院初步诊断"),

    // 尸检诊断
    AUTOPSY_DIAGNOSIS("6", "尸检诊断"),

    // 放射诊断
    RADIOLOGICAL_DIAGNOSIS("7", "放射诊断"),

    // 病理诊断
    PATHOLOGICAL_DIAGNOSIS("9", "病理诊断");

    private String value;
    private String description;

    public static YbIptDiseTypeCode getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbIptDiseTypeCode val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
