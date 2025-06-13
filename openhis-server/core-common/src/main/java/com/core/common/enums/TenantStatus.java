package com.core.common.enums;

/**
 * 租户状态
 * 
 * @author system
 */
public enum TenantStatus {
    ENABLE("0", "启用"), DISABLE("1", "停用");

    private final String code;
    private final String info;

    TenantStatus(String code, String info) {
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
