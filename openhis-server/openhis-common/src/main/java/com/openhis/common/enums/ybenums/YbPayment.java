/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.enums.ybenums;

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
public enum YbPayment {

    YB_FUND_PAY( 100000 ,1,null,"基金支付总额"),

    SELF_PAY(200000,1,null,"个人负担总金额"),

    OTHER_PAY(300000,1,null,"其他（如医院负担金额）"),

    /**
     * 基本医保统筹基金支出
     */
    YB_TC_FUND_AMOUNT(110000,2,YB_FUND_PAY,"基本医保统筹基金支出"),
    /**
     * 补充医疗保险基金支出
     */
    YB_BC_FUND_AMOUNT(120000,2,YB_FUND_PAY,"补充医疗保险基金支出"),
    /**
     * 医疗救助基金支出
     */
    YB_JZ_FUND_AMOUNT(130000,2,YB_FUND_PAY,"医疗救助基金支出"),
    /**
     * 其他支出
     */
    YB_OTHER_AMOUNT(140000,2,YB_FUND_PAY,"其他支出"),
    /**
     * 职工基本医疗保险
     */
    YB_TC_ZG_FUND_VALUE(110100,3,YB_TC_FUND_AMOUNT,"职工基本医疗保险"),
    /**
     * 居民基本医疗保险
     */
    YB_TC_JM_FUND_VALUE(110200,3,YB_TC_FUND_AMOUNT,"居民基本医疗保险"),
    /**
     * 全体参保人的居民大病保险
     */
    YB_BC_JM_DB_VALUE(120100,3,YB_BC_FUND_AMOUNT,"全体参保人的居民大病保险"),
    /**
     * 大额医疗费用补助
     */
    YB_BC_DE_BZ_VALUE(120200,3,YB_BC_FUND_AMOUNT,"大额医疗费用补助"),
    /**
     * 企业职工大额医疗费用补助
     */
    YB_BC_ZG_DE_BZ_VALUE(120300,3,YB_BC_FUND_AMOUNT,"企业职工大额医疗费用补助"),
    /**
     * 公务员医疗补助
     */
    YB_BC_GWY_BZ_VALUE(120400,3,YB_BC_FUND_AMOUNT,"公务员医疗补助"),
    /**
     * 伤残人员医疗保障基金支出
     */
    YB_OTHER_SC_BZ_FUND_VALUE(130100,3,YB_OTHER_AMOUNT,"伤残人员医疗保障基金支出"),
    /**
     *
     */
    SELF_YB_ZH_PAY(210000,2,SELF_PAY,"个人医保账户支付"),
    /**
     *
     */
    SELF_YB_ZH_GJ_VALUE(210100,3,SELF_YB_ZH_PAY,"账户共济支付金额"),
    /**
     *
     */
    SELF_CASH_PAY(220000,2,SELF_PAY,"个人现金支付金额"),
    /**
     *
     */
    SELF_VX_PAY(230000,2,SELF_PAY,"微信支付金额"),
    /**
     *
     */
    SELF_ALI_PAY(240000,2,SELF_PAY,"阿里支付金额"),
    /**
     *
     */
    SELF_CASH_VALUE(220400,3,SELF_CASH_PAY,"个人现金支付金额(现金)"),
    /**
     *
     */
    SELF_CASH_VX_VALUE(220100,3,SELF_CASH_PAY,"个人现金支付金额(微信)"),
    /**
     *
     */
    SELF_CASH_ALI_VALUE(220200,3,SELF_CASH_PAY,"个人现金支付金额(支付宝)"),
    /**
     *
     */
    SELF_CASH_UNION_VALUE(220300,3,SELF_CASH_PAY,"个人现金支付金额(银联)"),
    /**
     * 兜底基金支出
     */
    OTHER_PAY_DD_FUND_VALUE(300001,2,OTHER_PAY,"兜底基金支出"),
    /**
     * 意外伤害基金支出
     */
    OTHER_PAY_YW_SH_FUND_VALUE(300002,2,OTHER_PAY,"意外伤害基金支出"),
    /**
     * 离休人员医疗保障金支出
     */
    OTHER_PAY_LX_YL_FUND_VALUE(300003,2,OTHER_PAY,"离休人员医疗保障金支出"),
    /**
     * 离休人员优惠金支出
     */
    OTHER_PAY_LX_YH_FUND_VALUE(300004,2,OTHER_PAY,"离休人员优惠金支出"),
    /**
     * 财政基金支出
     */
    OTHER_PAY_CZ_FUND_VALUE(300005,2,OTHER_PAY,"财政基金支出"),
    /**
     * 财政预支支出
     */
    OTHER_PAY_CZ_YZ_FUND_VALUE(300006,2,OTHER_PAY,"财政预支支出"),
    /**
     * 职工大病基金支出
     */
    OTHER_PAY_ZG_DB_FUND_VALUE(300007,2,OTHER_PAY,"职工大病基金支出"),
    /**
     * 二乙基金支出
     */
    OTHER_PAY_EY_FUND_VALUE(300008,2,OTHER_PAY,"二乙基金支出"),
    /**
     * 倾斜救助支出
     */
    OTHER_PAY_QX_JZ_FUND_VALUE(300009,2,OTHER_PAY,"倾斜救助支出"),
    /**
     * 医疗救助再救助基金
     */
    OTHER_PAY_YL_JZ_FUND_VALUE(300010,2,OTHER_PAY,"医疗救助再救助基金"),
    /**
     * 医院负担金额
     */
    HOSP_PART_AMT(300011,2,OTHER_PAY,"医院负担金额"),


    //医保结算返回值记录枚举
    FULAMT_OWNPAY_AMT(1,2,null,"全自费金额"),
    //PSN_PART_AMT(2,2,null,"个人负担总金额"),
    OVERLMT_SELFPAY(3,2,null,"超限价自费费用"),
    PRESELFPAY_AMT(4,2,null,"先行自付金额"),
    INSCP_SCP_AMT(5,2,null,"符合政策范围金额"),
    ACT_PAY_DEDC(6,2,null,"实际支付起付线"),
    POOL_PROP_SELFPAY(7,2,null,"基本医疗保险统筹基金支付比例"),
    BALC(8,2,null,"余额"),


    //基金类型，下述仅作记录
    BIRTH_FUND(510100,2,YB_FUND_PAY, "生育基金"),
    RETIREE_MEDICAL(340100,2,YB_FUND_PAY, "离休人员医疗保障基金"),
    URBAN_BASIC_MEDICAL(390100,2,YB_FUND_PAY, "城乡居民基本医疗保险基金"),
    URBAN_SERIOUS_ILLNESS(390200,2,YB_FUND_PAY, "城乡居民大病医疗保险基金"),
    MEDICAL_ASSISTANCE(610100,2,YB_FUND_PAY, "医疗救助基金"),
    GOVERNMENT_SUBSIDY(640100,2,YB_FUND_PAY, "政府兜底基金"),
    ACCIDENT_INSURANCE(390400,2,YB_FUND_PAY, "意外伤害基金"),
    CARE_INSURANCE(620100,2,YB_FUND_PAY, "照护保险基金"),
    FINANCIAL_FUND(360100, 2,YB_FUND_PAY,"财政基金"),
    HOSPITAL_ADVANCE(999900,2,YB_FUND_PAY, "医院垫付"),
    SUPPLEMENTARY_INSURANCE(390300,2,YB_FUND_PAY, "城乡居民大病补充保险基金"),
    HEALTHCARE_PREPAYMENT(360300, 2,YB_FUND_PAY,"保健预支基金");
//
//
//    /**
//     * 现金支付
//     */
//    CASH(1, 1, "现金支付"),
//
//    /**
//     * 微信支付
//     */
//    VX_PAY(2, 1, "微信支付"),
//
//    /**
//     * 支付宝支付
//     */
//    ALI_PAY(3, 1, "支付宝支付"),
//
//    /**
//     * 银联支付
//     */
//    UNION_PAY(4, 1, "银联支付"),
//
//    /**
//     * 医保账户支付
//     */
//    YB_ZH_PAY(5, 1, "医保账户支付"),
//
//    /**
//     * 医保统筹支付合计
//     */
//    YB_TC_SUM_PAY(6, 1, "医保统筹支付合计"),
//
//    /**
//     * 医保统筹支付
//     */
//    YB_TC_PAY(7, 2, "医保统筹支付"),
//
//    /**
//     * 医保公务员补助
//     */
//    YB_GWY_PAY(8, 2, "医保公务员补助"),
//
//    /**
//     * 先行自付金额
//     */
//    XX_SELF_PAY(9, 2, "先行自付金额"),
//
//    /**
//     * 全自费金额
//     */
//    ALL_SELF_PAY(10, 2, "先行自付金额"),
//
//    /**
//     * 医疗工伤支付
//     */
//    YL_GS_PAY(11, 2, "医疗工伤支付"),
//
//    /**
//     * 老红军支付
//     */
//    YB_HJ_PAY(12, 2, "老红军支付"),
//
//    /**
//     * 离休人员医疗保障基金支付金额
//     */
//    YB_LTX_PAY(13, 2, "离休人员医疗保障基金支付金额"),
//
//    /**
//     * 居民统筹
//     */
//    YB_JM_TC_PAY(14, 2, "居民统筹"),
//    /**
//     * 居民大病
//     */
//    YB_JM_DB_PAY(15, 2, "居民大病"),
//    /**
//     * 补充医疗补助基金支付金额
//     */
//    YB_BC_PAY(16, 2, "补充医疗补助基金支付金额"),
//    /**
//     * 其他扶贫报销金额
//     */
//    YB_OTHER_FP_PAY(17, 2, "其他扶贫报销金额"),
//    /**
//     * 健康扶贫医疗基金
//     */
//    YB_JK_FP_PAY(18, 2, "健康扶贫医疗基金"),
//    /**
//     * 精准脱贫保险金额
//     */
//    YB_JZ_TP_PAY(19, 2, "精准脱贫保险金额"),
//    /**
//     * 提交医保总额
//     */
//    YB_SUM_FEE(20, 2, "提交医保总额"),
//    /**
//     * 二乙医疗专项医疗基金支出
//     */
//    YB_EY_PAY(21, 2, "二乙医疗专项医疗基金支出"),
//    /**
//     * 慢特病支付
//     */
//    YB_TM_PAY(22, 2, "补充医疗补助基金支付金额"),
//    /**
//     * 定点医疗机构垫支
//     */
//    YB_ORG_PAY(23, 2, "定点医疗机构垫支"),
//    /**
//     * 起付线公务员返还
//     */
//    YB_RETURN_GWY_PAY(24, 2, "起付线公务员返还"),
//    /**
//     * 大额理赔金额
//     */
//    YB_DELP_PAY(25, 2, "大额理赔金额"),
//    /**
//     * 民政救助金额
//     */
//    YB_MZJZ_PAY(26, 2, "民政救助金额"),
//    /**
//     * 生育基金支付
//     */
//    BIRTH_TC_PAY_AMOUNT(27, 1, "生育基金支付"),
//    /**
//     * 生育账户支付
//     */
//    BIRTH_ZH_PAY_AMOUNT(28, 1, "生育账户支付"),
//    /**
//     * 符合范围金额
//     */
//    YB_FHFW_AMOUNT(29, 2, "符合范围金额"),
//    /**
//     * 财政兜底基金支出
//     */
//    YB_CZDD_AMOUNT(30, 2, "财政兜底基金支出");
    @EnumValue
    private Integer value;
    private Integer level;
    private YbPayment payment;
    private String info;

    public static YbPayment getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (YbPayment val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
