package com.core.common.enums;

/**
 * 删除标志
 * 
 * @author system
 */
public enum DeleteFlag {
    NOT_DELETED("0", "未删除"), DELETED("1", "已删除");

    private final String code;
    private final String info;

    DeleteFlag(String code, String info) {
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
