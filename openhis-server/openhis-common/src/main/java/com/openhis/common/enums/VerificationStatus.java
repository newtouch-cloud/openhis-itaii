package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 过敏与不耐受中验证状态
 *
 * @author liuhr
 * @date 2025/3/6
 */
@Getter
@AllArgsConstructor
public enum VerificationStatus implements HisEnumInterface  {
    UNCONFIRMED(1, "unconfirmed", "未确认"),
    CONFIRMED(2, "confirmed", "已确认"),
    REFUTED(3, "refuted", "已反驳"),
    ENTERED_IN_ERROR(4, "entered-in-error", "输入错误");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
