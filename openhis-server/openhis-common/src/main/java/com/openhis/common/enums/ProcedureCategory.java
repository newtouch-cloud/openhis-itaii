package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProcedureCategory {
    PSYCHIATRY(1, "24642003", "精神疗法A procedure or service related to psychiatry."),
    COUNSELLING(2, "409063005", "咨询A form of advice or guidance, typically provided by a professional."),
    EDUCATION(3, "409073007",
        "宣教The process of imparting or acquiring general knowledge, developing the powers of reasoning and judgment, and generally of preparing oneself or others intellectually for mature life."),
    SURGICAL(4, "387713003", "手术A procedure involving a surgical operation."),
    LABORATORY(5, "15220000",
        "化验A test performed in a laboratory setting to obtain information about the health of a patient."),
    IMAGING(6, "363679005",
        "影像A procedure that involves the use of imaging technology to visualize internal structures of the body."),
    MEASUREMENT(7, "122869004", "测量The action or process of measuring something."),
    MANIPULATION(8, "46947000",
        "治疗A therapeutic procedure involving the manipulation of the spine or other parts of the body to alleviate pain or discomfort."),
    SOCIAL_SERVICE(9, "410606002", "社会相关A procedure related to providing social services.");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static ProcedureCategory getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (ProcedureCategory val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
