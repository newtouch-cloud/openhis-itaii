/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 【3101】明细审核事前分析服务（输入-就诊信息）
 * 【3102】明细审核事中分析服务（输入-就诊信息）
 *
 * @author SunJQ
 * @date 2025-04-15
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FsiEncounter3101Param {
    // 1. 就诊标识
    @JSONField(name = "mdtrt_id")
    private String mdtrtId;

    // 2. 医疗服务机构标识
    @JSONField(name = "medins_id")
    private String medinsId;

    // 3. 医疗机构名称
    @JSONField(name = "medins_name")
    private String medinsName;

    // 4. 医疗机构行政区划编码
    @JSONField(name = "medins_admdvs")
    private String medinsAdmdvs;

    // 5. 医疗服务机构类型
    @JSONField(name = "medins_type")
    private String medinsType;

    // 6. 医疗机构等级
    @JSONField(name = "medins_lv")
    private String medinsLv;

    // 7. 病区标识
    @JSONField(name = "wardarea_codg")
    private String wardareaCodg;

    // 8. 病房号
    @JSONField(name = "wardno")
    private String wardno;

    // 9. 病床号
    @JSONField(name = "bedno")
    private String bedno;

    // 10. 入院日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(name = "adm_date")
    private Date admDate;

    // 11. 出院日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(name = "dscg_date")
    private Date dscgDate;

    // 12. 主诊断编码
    @JSONField(name = "dscg_main_dise_codg")
    private String dscgMainDiseCodg;

    // 13. 主诊断名称
    @JSONField(name = "dscg_main_dise_name")
    private String dscgMainDiseName;

    // 14. 诊断信息DTO
    @JSONField(name = "fsi_diagnose_dtos")
    private List<FsiDiagnose3101Param> fsiDiagnoseDtos;

    // 15. 医师标识
    @JSONField(name = "dr_codg")
    private String drCodg;

    // 16. 入院科室标识
    @JSONField(name = "adm_dept_codg")
    private String admDeptCodg;

    // 17. 入院科室名称
    @JSONField(name = "adm_dept_name")
    private String admDeptName;

    // 18. 出院科室标识
    @JSONField(name = "dscg_dept_codg")
    private String dscgDeptCodg;

    // 19. 出院科室名称
    @JSONField(name = "dscg_dept_name")
    private String dscgDeptName;

    // 20. 就诊类型
    @JSONField(name = "med_mdtrt_type")
    private String medMdtrtType;

    // 21. 医疗类别
    @JSONField(name = "med_type")
    private String medType;

    // 22. 处方(医嘱)信息
    @JSONField(name = "fsi_order_dtos")
    private List<FsiOrder3101Param> fsiOrderDtos;

    // 23. 生育状态
    @JSONField(name = "matn_stas")
    private String matnStas;

    // 24. 总费用
    @JSONField(name = "medfee_sumamt")
    private BigDecimal medfeeSumamt;

    // 25. 自费金额
    @JSONField(name = "ownpay_amt")
    private BigDecimal ownpayAmt;

    // 26. 自付金额
    @JSONField(name = "selfpay_amt")
    private BigDecimal selfpayAmt;

    // 27. 个人账户支付金额
    @JSONField(name = "acct_payamt")
    private BigDecimal acctPayamt;

    // 28. 救助金支付金额
    @JSONField(name = "ma_amt")
    private BigDecimal maAmt;

    // 29. 统筹金支付金额
    @JSONField(name = "hifp_payamt")
    private BigDecimal hifpPayamt;

    // 30. 结算总次数
    @JSONField(name = "setl_totlnum")
    private Integer setlTotlnum;

    // 31. 险种
    @JSONField(name = "insutype")
    private String insutype;

    // 32. 报销标志
    @JSONField(name = "reim_flag")
    private String reimFlag;

    // 33. 异地结算标志
    @JSONField(name = "out_setl_flag")
    private String outSetlFlag;

    // 34. 手术操作集合
    @JSONField(name = "fsi_operation_dtos")
    private List<FsiOperation3101Param> fsiOperationDtos;
}
