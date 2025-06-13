package com.openhis.common.enums.ybenums;

import com.github.pagehelper.util.StringUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * YbEncounterClass
 *
 * @author Wuser
 * @date 2025/4/22
 */
@Getter
@AllArgsConstructor
public enum YbEncounterClass {

    AMB("1", "门诊"),

    IMP("2", "住院"),

    REG("3", "挂号");

    private String value;
    private String description;

    public static YbEncounterClass getByValue(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        for (YbEncounterClass code : values()) {
            if (code.getValue().equals(value)) {
                return code;
            }
        }
        return null;
    }
}