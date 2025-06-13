package com.openhis.common.enums;

/**
 * 删除标识
 * 
 * @author system
 */
public enum DelFlag {
    /**
     * 未删除
     */
    NO(0, "0", "未删除"),
    /**
     * 已删除
     */
    YES(1, "1", "已删除");

    private final Integer value;
    private final String code;
    private final String info;

    DelFlag(Integer value, String code, String info) {
        this.value = value;
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public Integer getValue() {
        return value;
    }

    public static DelFlag getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (DelFlag val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
