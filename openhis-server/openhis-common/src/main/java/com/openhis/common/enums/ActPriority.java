package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActPriority {

    ASAP(1, "A", "尽快，仅次于紧急状况后的最高优先级。"), CALLBACK_RESULTS(2, "CR", "结果一出，应立即通知申请者，即使是初步结果。（在HL7 2.3版的报告优先级中为'C'。）"),
    ELECTIVE(3, "EL", "对患者有益但不是生存所必需的。"), EMERGENCY(4, "EM", "一种未预见的情况组合或由此产生的需要立即采取行动的状态。"),
    PREOP(5, "P", "表示某项服务将在计划手术之前执行。在订购服务并使用术前优先级时，会检查执行服务所需的时间。下达订单时，可以生成一条消息，指示所需的服务时间，以避免与计划手术的时间冲突。"),
    AS_NEEDED(6, "PRN", "'按需'订单应附带构成需求的描述。此描述由作为先决条件的观察服务谓词表示。"), ROUTINE(7, "R", "常规服务，在正常工作时间进行。"),
    RUSH_REPORTING(8, "RR", "应尽快准备并发送报告。"), STAT(9, "S", "最高优先级（例如，紧急情况）。"),
    TIMING_CRITICAL(10, "T", "尽可能接近请求的时间至关重要（例如，对于通过抗菌水平）。"), USE_AS_DIRECTED(11, "UD", "药物应按处方者的指示使用。"),
    URGENT(12, "UR", "需要迅速采取行动。"), CALLBACK_FOR_SCHEDULING(13, "CS", "填写者应联系申请者（或目标）以安排服务。（在HL7 2.3版的TQ-优先级组件中为'C'。）"),
    CALLBACK_PLACER_FOR_SCHEDULING(14, "CSP", "填写者应联系申请者以安排服务。（在HL7 2.3版的TQ-优先级组件中为'C'。）"),
    CONTACT_RECIPIENT_FOR_SCHEDULING(15, "CSR", "填写者应联系服务接受者（目标）以安排服务。（在HL7 2.3版的TQ-优先级组件中为'C'。）");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static ActPriority getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (ActPriority val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
