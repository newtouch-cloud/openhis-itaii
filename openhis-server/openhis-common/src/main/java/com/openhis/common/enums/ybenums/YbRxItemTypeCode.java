package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 处方项目分类代码
 *
 * @author liuhr
 * @date 2025-05-13
 */
@Getter
@AllArgsConstructor
public enum YbRxItemTypeCode {

    /**
     * 西药
     */
    WESTERN_MEDICINE("11", "西药"),
    /**
     * 中成药
     */
    CHINESE_PATENT_MEDICINE("12", "中成药"),
    /**
     * 中药饮片
     */
    CHINESE_MATERIA_MEDICA_SLICES("13", "中药饮片"),
    /**
     * 治疗
     */
    TREATMENT("21", "治疗"),
    /**
     * 检验
     */
    LAB_TEST("22", "检验"),
    /**
     * 检查
     */
    MEDICAL_IMAGING("23", "检查"),
    /**
     * 手术
     */
    SURGERY("24", "手术"),
    /**
     * 麻醉
     */
    ANESTHESIA("25", "麻醉"),
    /**
     * 护理
     */
    NURSING("26", "护理"),
    /**
     * 膳食
     */
    DIET("27", "膳食"),
    /**
     * 输血
     */
    BLOOD_TRANSFUSION("28", "输血"),
    /**
     * 输氧
     */
    OXYGEN_THERAPY("29", "输氧"),
    /**
     * 其他
     */
    OTHER("31", "其他"),
    /**
     * 转科
     */
    REFERRAL3("2", "转科"),
    /**
     * 术后
     */
    POST_OPERATIVE("33", "术后"),
    /**
     * 出院
     */
    DISCHARGE("34", "出院"),
    /**
     * 转院
     */
    TRANSFER("35", "转院"),
    /**
     * 死亡
     */
    DEATH("36", "死亡"),
    /**
     * 产后
     */
    POSTPARTUM("37", "产后");

    private String value;
    private String description;

    public static YbRxItemTypeCode getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbRxItemTypeCode val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}