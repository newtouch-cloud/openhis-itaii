package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 药物使用途径代码
 *
 * @author liuhr
 * @date 2025-04-21
 */
@Getter
@AllArgsConstructor
public enum YbDrugMedWay {

    ORAL_DRUG("1", "口服"),

    RECTAL_DRUG("2", "直肠给药"),

    SUBLINGUAL_DRUG("3", "舌下给药"),

    INJECTION_DRUG("4", "注射给药"),

    INHALATION_DRUG("5", "吸入给药"),

    TOPICAL_DRUG("6", "局部用药"),

    SPINAL_DRUG("601", "椎管内用药"),

    INTRA_ARTICULAR_DRUG("602", "关节腔内用药"),

    PLEURAL_DRUG("603", "胸膜腔用药"),

    INTRA_PERITONEAL_DRUG("604", "腹腔用药"),

    VAGINAL_DRUG("605", "阴道用药"),

    ENDOTRACHEAL_DRUG("606", "气管内用药"),

    EYE_DROPS("607", "滴眼"),

    NASAL_DROPS("608", "滴鼻"),

    THROAT_SPRAY("609", "喷喉"),

    BUCCAL("610", "含化"),

    WOUND_APPLICATION("611", "敷伤口"),

    TOPICAL_SKIN("612", "擦皮肤"),

    OTHER_TOPICAL_ROUTE("699", "其他局部给药途径"),

    OTHER_ROUTE("9", "其他给药途径");

    private String value;
    private String description;

    public static YbDrugMedWay getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbDrugMedWay code : values()) {
            if (code.getValue().equals(value)) {
                return code;
            }
        }
        return null;
    }
}