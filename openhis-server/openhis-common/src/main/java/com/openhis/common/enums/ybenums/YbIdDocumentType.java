package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 证件类型枚举
 *
 * @author YourName
 * @date 2025-04-22
 */
@Getter
@AllArgsConstructor
public enum YbIdDocumentType {

    /**
     * 居民身份证（户口簿）
     */
    RESIDENT_ID_CARD("01", "居民身份证（户口簿）"),

    /**
     * 中国人民解放军军官证
     */
    PLA_OFFICER_ID_CARD("02", "中国人民解放军军官证"),

    /**
     * 中国人民武装警察警官证
     */
    ARMED_POLICE_OFFICER_ID_CARD("03", "中国人民武装警察警官证"),

    /**
     * 香港特区护照/港澳居民来往内地通行证
     */
    HK_PASSPORT_OR_PERMIT("04", "香港特区护照/港澳居民来往内地通行证"),

    /**
     * 澳门特区护照/港澳居民来往内地通行证
     */
    MACAO_PASSPORT_OR_PERMIT("05", "澳门特区护照/港澳居民来往内地通行证"),

    /**
     * 台湾居民来往大陆通行证
     */
    TAIWAN_PERMIT("06", "台湾居民来往大陆通行证"),

    /**
     * 外国人永久居留证
     */
    FOREIGNER_PERMANENT_RESIDENCE_CARD("07", "外国人永久居留证"),

    /**
     * 外国人护照
     */
    FOREIGNER_PASSPORT("08", "外国人护照"),

    /**
     * 残疾人证
     */
    DISABILITY_CARD("09", "残疾人证"),

    /**
     * 军烈属证明
     */
    MARTYRS_FAMILY_CERTIFICATE("10", "军烈属证明"),

    /**
     * 外国人就业证
     */
    FOREIGNER_EMPLOYMENT_CERTIFICATE("11", "外国人就业证"),

    /**
     * 外国专家证
     */
    FOREIGN_EXPERT_CERTIFICATE("12", "外国专家证"),

    /**
     * 外国人常驻记者证
     */
    FOREIGN_JOURNALIST_CERTIFICATE("13", "外国人常驻记者证"),

    /**
     * 台港澳人员就业证
     */
    TAIWAN_HONGKONG_MACAO_EMPLOYMENT_CERTIFICATE("14", "台港澳人员就业证"),

    /**
     * 回国（来华）定居专家证
     */
    EXPATRIATE_SETTLEMENT_CERTIFICATE("15", "回国（来华）定居专家证"),

    /**
     * 中国护照
     */
    CHINESE_PASSPORT("16", "中国护照"),

    /**
     * 港澳台居民居住证
     */
    HK_MACAO_TAIWAN_RESIDENCE_PERMIT("17", "港澳台居民居住证"),

    /**
     * 社会保障卡
     */
    SOCIAL_SECURITY_CARD("90", "社会保障卡"),

    /**
     * 其他身份证件
     */
    OTHER_ID_DOCUMENT("99", "其他身份证件"),

    /**
     * 医学出生证明
     */
    MEDICAL_BIRTH_CERTIFICATE("990201", "医学出生证明"),

    /**
     * 扶贫人口编码
     */
    POVERTY_POPULATION_CODE("990102", "扶贫人口编码");

    private String value;
    private String description;

    public static YbIdDocumentType getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbIdDocumentType type : values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }
}
