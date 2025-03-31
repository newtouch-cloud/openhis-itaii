/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付类型
 *
 * @author SunJQ
 * @date 2025-03-29
 */
@Getter
@AllArgsConstructor
public enum Payment {

    /**
     * 现金支付
     */
    CASH(1, 1, "现金支付"),

    /**
     * 微信支付
     */
    VX_PAY(2, 1, "微信支付"),

    /**
     * 支付宝支付
     */
    ALI_PAY(3, 1, "支付宝支付"),

    /**
     * 银联支付
     */
    UNION_PAY(4, 1, "银联支付"),

    /**
     * 医保账户支付
     */
    YB_ZH_PAY(5, 1, "医保账户支付"),

    /**
     * 医保统筹支付合计
     */
    YB_TC_SUM_PAY(6, 1, "医保统筹支付合计"),

    /**
     * 医保统筹支付
     */
    YB_TC_PAY(7, 2, "医保统筹支付"),

    /**
     * 医保公务员补助
     */
    YB_GWY_PAY(8, 2, "医保公务员补助"),

    /**
     * 先行自付金额
     */
    XX_SELF_PAY(9, 2, "先行自付金额"),

    /**
     * 全自费金额
     */
    ALL_SELF_PAY(10, 2, "先行自付金额"),

    /**
     * 医疗工伤支付
     */
    YL_GS_PAY(11, 2, "医疗工伤支付"),

    /**
     * 老红军支付
     */
    YB_HJ_PAY(12, 2, "老红军支付"),

    /**
     * 离休人员医疗保障基金支付金额
     */
    YB_LTX_PAY(13, 2, "离休人员医疗保障基金支付金额"),

    /**
     * 居民统筹
     */
    YB_JM_TC_PAY(14, 2, "居民统筹"),
    /**
     * 居民大病
     */
    YB_JM_DB_PAY(15, 2, "居民大病"),
    /**
     * 补充医疗补助基金支付金额
     */
    YB_BC_PAY(16, 2, "补充医疗补助基金支付金额"),
    /**
     * 其他扶贫报销金额
     */
    YB_OTHER_FP_PAY(17, 2, "其他扶贫报销金额"),
    /**
     * 健康扶贫医疗基金
     */
    YB_JK_FP_PAY(18, 2, "健康扶贫医疗基金"),
    /**
     * 精准脱贫保险金额
     */
    YB_JZ_TP_PAY(19, 2, "精准脱贫保险金额"),
    /**
     * 提交医保总额
     */
    YB_SUM_FEE(20, 2, "提交医保总额"),
    /**
     * 二乙医疗专项医疗基金支出
     */
    YB_EY_PAY(21, 2, "二乙医疗专项医疗基金支出"),
    /**
     * 慢特病支付
     */
    YB_TM_PAY(22, 2, "补充医疗补助基金支付金额"),
    /**
     * 定点医疗机构垫支
     */
    YB_ORG_PAY(23, 2, "定点医疗机构垫支"),
    /**
     * 起付线公务员返还
     */
    YB_RETURN_GWY_PAY(24, 2, "起付线公务员返还"),
    /**
     * 大额理赔金额
     */
    YB_DELP_PAY(25, 2, "大额理赔金额"),
    /**
     * 民政救助金额
     */
    YB_MZJZ_PAY(26, 2, "民政救助金额"),
    /**
     * 生育基金支付
     */
    BIRTH_TC_PAY_AMOUNT(27, 1, "生育基金支付"),
    /**
     * 生育账户支付
     */
    BIRTH_ZH_PAY_AMOUNT(28, 1, "生育账户支付"),
    /**
     * 符合范围金额
     */
    YB_FHFW_AMOUNT(29, 2, "符合范围金额"),
    /**
     * 财政兜底基金支出
     */
    YB_CZDD_AMOUNT(30, 2, "财政兜底基金支出");
    @EnumValue
    private Integer value;
    private Integer level;
    private String info;

    public static Payment getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (Payment val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
