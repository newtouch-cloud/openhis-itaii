package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConditionDefinitionSource implements HisEnumInterface {
    DISEASE_AND_DIAGNOSIS(1, "01", "疾病与诊断"),
    CHRONIC_DISEASE_DIAGNOSIS_CATALOG(2, "02", "特慢病诊断目录"),
    DIAGNOSIS_BY_DISEASE_CATALOG(3, "03", "按病种目录付费诊断"),
    DAY_SURGERY_TREATMENT_DISEASE(4, "04", "日间手术治疗病种"),
    TRADITIONAL_CHINESE_MEDICINE_DIAGNOSIS(5, "05", "中医诊断"),
    TRADITIONAL_CHINESE_MEDICINE_SYNDROME_CATALOG(6, "06", "中医证候目录"),
    TUMOR_MORPHOLOGY_CATALOG(7, "07", "肿瘤形态学目录");
    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
