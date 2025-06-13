/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 险种类别
 *
 * @author SunJQ
 * @date 2025-04-15
 */
@Getter
@AllArgsConstructor
public enum YbInsuType {
    /**
     * 长期照护保险
     */
    YB_INSU_TYPE410("410", "长期照护保险"),
    /**
     * 职工基本医疗保险
     */
    YB_INSU_TYPE310("310", "职工基本医疗保险"),
    /**
     * 公务员医疗补助
     */
    YB_INSU_TYPE320("320", "公务员医疗补助"),
    /**
     * 离休人员医疗保障
     */
    YB_INSU_TYPE340("340", "离休人员医疗保障"),
    /**
     * 一至六级残废军人医疗补助
     */
    YB_INSU_TYPE350("350", "一至六级残废军人医疗补助"),
    /**
     * 大额医疗费用补助
     */
    YB_INSU_TYPE330("330", "大额医疗费用补助"),
    /**
     * 老红军医疗保障
     */
    YB_INSU_TYPE360("360", "老红军医疗保障"),
    /**
     * 企业补充医疗保险
     */
    YB_INSU_TYPE370("370", "企业补充医疗保险"),
    /**
     * 新型农村合作医疗
     */
    YB_INSU_TYPE380("380", "新型农村合作医疗"),
    /**
     * 城乡居民基本医疗保险
     */
    YB_INSU_TYPE390("390", "城乡居民基本医疗保险"),
    /**
     * 城镇居民基本医疗保险
     */
    YB_INSU_TYPE391("391", "城镇居民基本医疗保险"),
    /**
     * 城乡居民大病医疗保险
     */
    YB_INSU_TYPE392("392", "城乡居民大病医疗保险"),
    /**
     * 其他特殊人员医疗保障
     */
    YB_INSU_TYPE399("399", "其他特殊人员医疗保障");

    private String value;
    private String description;

    public static YbInsuType getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbInsuType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
