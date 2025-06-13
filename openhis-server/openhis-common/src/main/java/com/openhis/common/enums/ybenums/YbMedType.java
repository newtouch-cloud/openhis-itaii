/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 医保医疗类别
 *
 * @author SunJQ
 * @date 2025-04-14
 */
@Getter
@AllArgsConstructor
public enum YbMedType {

    // 异地住院
    CROSS_PROVINCIAL_HOSPITALIZATION("25", "异地住院"),
    // 单病种住院
    SINGLE_DISEASE_HOSPITALIZATION("26", "单病种住院"),
    // 自主就医
    SELF_MEDICATION("27", "自主就医"),
    // 意外伤害门诊
    ACCIDENTAL_INJURY_OUTPATIENT("81", "意外伤害门诊"),
    // 特药
    SPECIAL_MEDICATION("15", "特药"),
    // 日间手术
    DAY_SURGERY("28", "日间手术"),
    // 照护保险
    CARE_INSURANCE("61", "照护保险"),
    // 普通门诊
    GENERAL_OUTPATIENT("11", "普通门诊"),
    // 门诊挂号
    OUTPATIENT_REGISTRATION("12", "门诊挂号"),
    // 急诊
    EMERGENCY("13", "急诊"),
    // 门诊慢特病
    CHRONIC_SPECIAL_DISEASE_OUTPATIENT("14", "门诊慢特病"),
    // 门诊特检特治（限吉林市）
    SPECIAL_EXAMINATION_TREATMENT_OUTPATIENT_JILIN("990404", "门诊特检特治（限吉林市）"),
    // 普通住院
    GENERAL_HOSPITALIZATION("21", "普通住院"),
    // 外伤住院
    TRAUMA_HOSPITALIZATION("22", "外伤住院"),
    // 转外诊治住院
    EXTERNAL_TREATMENT_HOSPITALIZATION("23", "转外诊治住院"),
    // 急诊转住院
    EMERGENCY_TO_HOSPITALIZATION("24", "急诊转住院"),
    // 定点药店购药
    DESIGNATED_PHARMACY_PURCHASE("41", "定点药店购药"),
    // 生育门诊
    MATERNITY_OUTPATIENT("51", "生育门诊"),
    // 生育住院
    MATERNITY_HOSPITALIZATION("52", "生育住院"),
    // 生育新生儿费用
    NEWBORN_MATERNITY_EXPENSES("5212", "生育新生儿费用"),
    // 中医特色门诊
    TRADITIONAL_CHINESE_MEDICINE_OUTPATIENT("16", "中医特色门诊"),
    // 起付线治疗
    DEDUCTIBLE_TREATMENT("29", "起付线治疗"),
    // 体检
    PHYSICAL_EXAMINATION("9107", "体检"),
    // 低自付住院
    LOW_COPAY_HOSPITALIZATION("3101", "低自付住院"),
    // 低自付门诊
    LOW_COPAY_OUTPATIENT("3102", "低自付门诊"),
    // 门诊慢病
    CHRONIC_DISEASE_OUTPATIENT("140104", "门诊慢病"),
    // 门诊特病
    SPECIAL_DISEASE_OUTPATIENT("140201", "门诊特病"),
    // 舒缓疗护住院
    PALLIATIVE_CARE_HOSPITALIZATION("2114", "舒缓疗护住院");

    private String value;
    private String description;

    public static YbMedType getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbMedType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
