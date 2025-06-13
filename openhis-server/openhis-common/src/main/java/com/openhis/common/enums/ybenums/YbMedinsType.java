/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 7.12 定点医疗服务机构类型
 *
 * @author SunJQ
 * @date 2025-04-15
 */
@Getter
@AllArgsConstructor
public enum YbMedinsType {

    /**
     * 定点医疗机构
     */
    FIXMEDINS_TYPE1("1", "定点医疗机构"),
    /**
     * 定点零售药店
     */
    FIXMEDINS_TYPE2("2", "定点零售药店"),
    /**
     * 工伤定点康复机构
     */
    FIXMEDINS_TYPE3("3", "工伤定点康复机构"),
    /**
     * 辅助器具配置机构
     */
    FIXMEDINS_TYPE4("4", "辅助器具配置机构"),
    /**
     * 计划生育服务机构
     */
    FIXMEDINS_TYPE5("5", "计划生育服务机构");

    private String value;
    private String description;

    public static YbMedinsType getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbMedinsType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
