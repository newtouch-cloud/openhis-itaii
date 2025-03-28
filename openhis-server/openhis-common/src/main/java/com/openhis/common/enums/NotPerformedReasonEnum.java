package com.openhis.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 药品请求状态
 *
 * @author wangyang
 * @date 2025-03-14
 */
@Getter
@AllArgsConstructor
public enum NotPerformedReasonEnum implements HisEnumInterface {

    /**
     * 订单已停止
     */
    ORDER_STOPPED(1,"OS", "订单已停止"),

    /**
     * 过时订单
     */
    OUTDATED_ORDER(2, "OO", "过时订单"),

    /**
     * 不完整的数据
     */
    INCOMPLETE_DATA(3, "ID", "不完整的数据"),

    /**
     * 产品不可用
     */
    PRODUCT_NOT_AVAILABLE(4, "PNA", "产品不可用"),

    /**
     * 道德/宗教
     */
    MORAL_RELIGIOUS(5, "MR", "道德/宗教"),

    /**
     * 无法提供护理
     */
    UNABLE_PROVIDE(6, "UP", "无法提供护理"),

    /**
     * 先尝试其他治疗方法
     */
    OTHER_TREATMENT(7, "OT", "先尝试其他治疗方法"),

    /**
     * 处方/请求需要澄清
     */
    PRESCRIPTION_REQUEST_CLARIFICATION(8, "PRC", "处方/请求需要澄清"),

    /**
     * 药物水平过高
     */
    EXCESSIVE_DRUG_LEVELS(9, "EDL", "药物水平过高"),

    /**
     * 入院
     */
    HOSPITALIZED(10, "HO", "入院"),

    /**
     * 实验室干扰问题
     */
    LABORATORY_INTERFERENCE_ISSUE(11, "LII", "实验室干扰问题"),

    /**
     * 患者不可用
     */
    PATIENT_UNAVAILABLE(12, "PU", "患者不可用"),

    /**
     * 患者怀孕或哺乳
     */
    PATIENT_PREGNANT_BREASTFEEDING(13, "PPB", "患者怀孕或哺乳"),

    /**
     * 过敏
     */
    ALLERGY(14, "AL", "过敏"),

    /**
     * 药物与另一种药物相互作用
     */
    ANOTHER_DRUG(15, "AD", "药物与另一种药物相互作用"),

    /**
     * 重复治疗
     */
    REPEAT_TREATMENT(16, "RT", "重复治疗"),

    /**
     * 疑似不耐受
     */
    SUSPECTED_INTOLERANCE(17, "SI", "疑似不耐受"),

    /**
     * 计划手术的患者
     */
    PATIENTS_PLANNING_SURGERY(18, "PPS", "计划手术的患者"),

    /**
     * 冲洗
     */
    WASH(19, "WA", "冲洗"),

    /**
     * 药物缺货
     */
    DRUG_SHORTAGE(20, "DS", "药物缺货"),

    /**
     * 药物不可用
     */
    MEDICATION_NOT_AVAILABLE(21, "MNA", "药物不可用");

    private Integer value;
    private String code;
    private String info;

    public static NotPerformedReasonEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (NotPerformedReasonEnum val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
