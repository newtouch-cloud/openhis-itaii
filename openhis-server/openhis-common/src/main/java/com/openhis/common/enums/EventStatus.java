package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventStatus {

    PREPARATION(1, "PREP", "Preparation stage, gathering resources and planning."),
    IN_PROGRESS(2, "IP", "Task is currently being worked on."),
    NOT_DONE(3, "ND", "Task has not been completed."),
    ON_HOLD(4, "OH", "Task is temporarily paused."),
    STOPPED(5, "ST", "Task has been stopped."),
    COMPLETED(6, "CMP", "Task has been completed."),
    ENTERED_IN_ERROR(7, "EIE", "Task status was entered in error."),
    UNKNOWN(8, "UNK", "Task status is unknown.");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
