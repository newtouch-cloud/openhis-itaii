package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 门诊分类
 */
@Getter
@AllArgsConstructor
public enum OutpatientClass implements HisEnumInterface {

    GENERAL_OUTPATIENT_SERVICE(1, "GOS", "普通门诊"),
    EMERGENCY_TREATMENT(2, "ET", "急诊"),
    HEALTH_COUNSELING(3, "HC", "健康咨询"),
    SPECIALIZED_OUTPATIENT_DEPARTMENT(4, "SOD", "专科门诊"),
    VIP_CLINIC(5, "VC", "特需门诊"),
    SPECIALIZED_DISEASE_DEPARTMENT(6, "SOD", "专病门诊"),
    APPOINTMENT_FOR_REGISTRATION(7, "AFR", "预约挂号"),
    WESTERN_MEDICINE(11, "WM", "西医"),
    TRADITIONAL_CHINESE_MEDICAL_SCIENCE(12, "TCMS", "中医"),
    WESTERN_MEDICINE_EMERGENCY_DEPARTMENT(21, "WMED", "西医急诊"),
    TRADITIONAL_CHINESE_MEDICINE_EMERGENCY(22, "TCME", "中医急诊"),
    PHYSICAL_EXAMINATION(31, "PE", "体检"),
    PREVENTIVE_MEDICAL_EXAMINATION(32, "PME", "预防体检"),
    MATERNAL_AND_CHILD_HEALTH_CARE(33, "MACHC", "孕产保健"),
    OTHER(99, "OT", "其他");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

}
