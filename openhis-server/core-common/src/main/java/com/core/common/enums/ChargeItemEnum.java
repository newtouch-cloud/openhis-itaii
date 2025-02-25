/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.core.common.enums;

/**
 * 收费项目
 *
 * @author zxy
 * @date 2025-02-24
 */
public enum ChargeItemEnum {

    /**
     * 床位费
     */
    BED_FEE("01", "床位费"),
    /**
     * 诊察费
     */
    DIAGNOSTIC_FEE("02", "诊察费"),
    /**
     * 检查费
     */
    CHECK_FEE("03", "检查费"),
    /**
     * 化验费
     */
    DIAGNOSTIC_TEST_FEE("04", "化验费"),
    /**
     * 治疗费
     */
    MEDICAL_EXPENSE_FEE("05", "治疗费"),
    /**
     * 手术费
     */
    OPERATION_FEE("06", "手术费"),
    /**
     * 护理费
     */
    NURSING_FEE("07", "护理费"),
    /**
     * 卫生材料费
     */
    SANITARY_MATERIALS_FEE("08", "卫生材料费"),
    /**
     * 西药费
     */
    WEST_MEDICINE("09", "西药费"),
    /**
     * 中药饮片费
     */
    CHINESE_MEDICINE_SLICES_FEE("10", "中药饮片费"),
    /**
     * 中成药费
     */
    CHINESE_MEDICINE_FEE("11", "中成药费"),
    /**
     * 一般诊疗费
     */
    GENERAL_CONSULTATION_FEE("12", "一般诊疗费"),
    /**
     * 挂号费
     */
    REGISTRATION_FEE("13", "挂号费"),
    /**
     * 其他费
     */
    OTHER_FEE("14", "其他费");

    private final String code;
    private final String info;

    ChargeItemEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}