/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums.ybenums;

import com.openhis.common.enums.ChargeItemEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 医疗收费项目类别【MED_CHRGITM_TYPE】 【chrg_type】
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Getter
@AllArgsConstructor
public enum YbMedChrgItmType {
    /**
     * 床位费
     */
    BED_FEE(1, "01","床位费"),
    /**
     * 诊察费
     */
    DIAGNOSTIC_FEE(2, "02","诊察费"),
    /**
     * 检查费
     */
    CHECK_FEE(3, "03","检查费"),
    /**
     * 化验费
     */
    DIAGNOSTIC_TEST_FEE(4, "04","化验费"),
    /**
     * 治疗费
     */
    MEDICAL_EXPENSE_FEE(5, "05","治疗费"),
    /**
     * 手术费
     */
    OPERATION_FEE(6, "06","手术费"),
    /**
     * 护理费
     */
    NURSING_FEE(7, "07","护理费"),
    /**
     * 卫生材料费
     */
    SANITARY_MATERIALS_FEE(8, "08","卫生材料费"),
    /**
     * 西药费
     */
    WEST_MEDICINE(9, "09","西药费"),
    /**
     * 中药饮片费
     */
    CHINESE_MEDICINE_SLICES_FEE(10, "10","中药饮片费"),
    /**
     * 中成药费
     */
    CHINESE_MEDICINE_FEE(11, "11","中成药费"),
    /**
     * 一般诊疗费
     */
    GENERAL_CONSULTATION_FEE(12, "12","一般诊疗费"),
    /**
     * 挂号费
     */
    REGISTRATION_FEE(13, "13","挂号费"),
    /**
     * 其他费
     */
    OTHER_FEE(14, "14","其他费");

    private final Integer code;
    private final String value;
    private final String info;

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public static YbMedChrgItmType getByCode(Integer value) {
        if (value == null) {
            return null;
        }
        for (YbMedChrgItmType val : values()) {
            if (val.getCode().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
