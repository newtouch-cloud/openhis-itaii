/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 7.13 人员证件类型(psn_cert_type)
 *
 * @author SunJQ
 * @date 2025-04-10
 */
@Getter
@AllArgsConstructor
public enum YbPsnCertType {

    /**
     * 中国护照
     */
    PSN_CERT_TYPE16("16", "中国护照"),
    /**
     * 港澳台居民居住证
     */
    PSN_CERT_TYPE17("17", "港澳台居民居住证"),
    /**
     * 居民身份证（户口簿）
     */
    PSN_CERT_TYPE01("01", "居民身份证（户口簿）"),
    /**
     * 中国人民解放军军官证
     */
    PSN_CERT_TYPE02("02", "中国人民解放军军官证"),
    /**
     * 中国人民武装警察警官证
     */
    PSN_CERT_TYPE03("03", "中国人民武装警察警官证"),
    /**
     * 香港特区护照/港澳居民来往内地通行证
     */
    PSN_CERT_TYPE04("04", "香港特区护照/港澳居民来往内地通行证"),
    /**
     * 澳门特区护照/港澳居民来往内地通行证
     */
    PSN_CERT_TYPE05("05", "澳门特区护照/港澳居民来往内地通行证"),
    /**
     * 台湾居民来往大陆通行证
     */
    PSN_CERT_TYPE06("06", "台湾居民来往大陆通行证"),
    /**
     * 外国人永久居留证
     */
    PSN_CERT_TYPE07("07", "外国人永久居留证"),
    /**
     * 外国人护照
     */
    PSN_CERT_TYPE08("08", "外国人护照"),
    /**
     * MEMBER_FIRST
     */
    PSN_CERT_TYPE09("09", "残疾人证"),
    /**
     * 军烈属证明
     */
    PSN_CERT_TYPE10("10", "军烈属证明"),
    /**
     * 外国人就业证
     */
    PSN_CERT_TYPE11("11", "外国人就业证"),
    /**
     * 外国专家证
     */
    PSN_CERT_TYPE12("12", "外国专家证"),
    /**
     * 外国人常驻记者证
     */
    PSN_CERT_TYPE13("13", "外国人常驻记者证"),
    /**
     * 台港澳人员就业证
     */
    PSN_CERT_TYPE14("14", "台港澳人员就业证"),
    /**
     * 回国（来华）定居专家证
     */
    PSN_CERT_TYPE15("15", "回国（来华）定居专家证"),
    /**
     * 社会保障卡
     */
    PSN_CERT_TYPE90("90", "社会保障卡"),
    /**
     * 其他身份证件
     */
    PSN_CERT_TYPE99("99", "其他身份证件");

    private String value;
    private String description;

    public static YbPsnCertType getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbPsnCertType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
