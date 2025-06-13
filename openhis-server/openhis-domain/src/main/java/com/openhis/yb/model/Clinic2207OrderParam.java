/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 【2207】
 *
 * @author SunJQ
 * @date 2025-05-06
 */
@Data
@Accessors(chain = true)
public class Clinic2207OrderParam {

    @JsonProperty("psn_no")
    private String psnNo;// 人员编号

    // 参保地医保区划
    @JsonProperty("insuplc_admdvs")
    private String insuplcAdmdvs;

    @JsonProperty("mdtrt_cert_type")
    private String mdtrtCertType;// 就诊凭证类型

    @JsonProperty("mdtrt_cert_no")
    private String mdtrtCertNo;// 就诊凭证编号

    @JsonProperty("med_type")
    private String medType;// 医疗类别

    @JsonProperty("medfee_sumamt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#0.00")
    private Double medfeeSumamt;// 医疗费总额

    @JsonProperty("psn_setlway")
    private String psnSetlway;// 个人结算方式

    @JsonProperty("mdtrt_id")
    private String mdtrtId;// 就诊ID

    @JsonProperty("chrg_bchno")
    private String chrgBchno;// 收费批次号

    @JsonProperty("insutype")
    private String insutype;// 险种类型

    @JsonProperty("acct_used_flag")
    private String acctUsedFlag;// 个人账户使用标志

    @JsonProperty("invono")
    private String invono;// 发票号

    @JsonProperty("fulamt_ownpay_amt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#0.00")
    private Double fulamtOwnpayAmt;// 全自费金额

    @JsonProperty("overlmt_selfpay")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#0.00")
    private Double overlmtSelfpay;// 超限价金额

    @JsonProperty("preselfpay_amt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#0.00")
    private Double preselfpayAmt;// 先行自付金额

    @JsonProperty("inscp_scp_amt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#0.00")
    private Double inscpScpAmt;// 符合政策范围金额

    @JsonProperty("exp_content")
    private String expContent;// 字段扩展

    @JsonProperty("pub_hosp_rfom_flag")
    private String pubHospRfomFlag;// 公立医院改革标志

    @JsonProperty("minpacunt_drug_trac_cnt")
    private Integer minpacuntDrugTracCnt;// 本次结算应上传最小包装药品追溯码数量

    @JsonProperty("mcs_trac_cnt")
    private Integer mcsTracCnt;// 本次结算应上传耗材追溯码数量

}
