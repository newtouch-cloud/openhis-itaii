/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.core.common.enums;

/**
 * 定价类型
 *
 * @author zxy
 * @date 2025-02-21
 */
public enum DefinitionTypeEnum {

    /**
     * 药品
     */
    MEDICATION("1", "药品"),
    /**
     * 耗材
     */
    DEVICE("2", "耗材"),
    /**
     * 手术
     */
    ACTIVITY("3", "手术");

    private final String code;
    private final String info;

    DefinitionTypeEnum(String code, String info) {
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