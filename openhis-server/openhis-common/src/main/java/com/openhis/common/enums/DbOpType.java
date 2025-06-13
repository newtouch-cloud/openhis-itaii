/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据库操作类型
 */
@Getter
@AllArgsConstructor
public enum DbOpType implements HisEnumInterface {

    /**
     * 新增
     */
    INSERT(1, "1", "新增"),

    /**
     * 修改
     */
    UPDATE(2, "2", "修改"),

    /**
     * 删除
     */
    DELETE(3, "3", "删除"),

    /**
     * 查询
     */
    SELECT(4, "4", "查询"),

    /**
     * 停用
     */
    STOP(5, "5", "停用"),

    /**
     * 启用
     */
    START(6, "6", "启用");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static DbOpType getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (DbOpType val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
