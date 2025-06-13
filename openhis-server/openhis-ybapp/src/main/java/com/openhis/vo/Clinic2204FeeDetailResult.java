/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 【2204】接口返回参数
 *
 * @author SunJQ
 * @date 2025-04-19
 */
@Data
@Accessors(chain = true)
public class Clinic2204FeeDetailResult {
    // 主键字段（根据Y标识判断）
    @JSONField(name = "feedetl_sn")
    private String feedetlSn; // 费用明细流水号

    // 数值型字段使用BigDecimal保证精度
    @JSONField(name = "det_item_fee_sumamt")
    private BigDecimal detItemFeeSumamt; // 明细项目费用总额

    @JSONField(name = "cnt")
    private BigDecimal cnt; // 数量

    @JSONField(name = "pric")
    private BigDecimal pric; // 单价

    @JSONField(name = "pric_uplmt_amt")
    private BigDecimal pricUplmtAmt; // 定价上限金额

    @JSONField(name = "selfpay_prop")
    private BigDecimal selfpayProp; // 自付比例

    @JSONField(name = "fulamt_ownpay_amt")
    private BigDecimal fulamtOwnpayAmt; // 全自费金额

    @JSONField(name = "overlmt_amt")
    private BigDecimal overlmtAmt; // 超限价金额

    @JSONField(name = "preselfpay_amt")
    private BigDecimal preselfpayAmt; // 先行自付金额

    @JSONField(name = "inscp_scp_amt")
    private BigDecimal inscpScpAmt; // 符合政策范围金额

    // 字符型字段
    @JSONField(name = "chrgitm_lv")
    private String chrgitmLv; // 收费项目等级

    @JSONField(name = "med_chrgitm_type")
    private String medChrgitmType; // 医疗收费项目类别

    @JSONField(name = "bas_medn_flag")
    private String basMednFlag; // 基本药物标志

    @JSONField(name = "hi_nego_drug_flag")
    private String hiNegoDrugFlag; // 医保谈判药品标志

    @JSONField(name = "chld_medc_flag")
    private String chldMedcFlag; // 儿童用药标志

    @JSONField(name = "list_sp_item_flag")
    private String listSpItemFlag; // 目录特项标志

    @JSONField(name = "lmt_used_flag")
    private String lmtUsedFlag; // 限制使用标志

    @JSONField(name = "drt_reim_flag")
    private String drtReimFlag; // 直报标志

    @JSONField(name = "memo")
    private String memo; // 备注

    @JSONField(name = "exp_content")
    private String expContent; // 字段扩展
}
