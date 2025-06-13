/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 【3101】明细审核事前分析服务（输入-处方（医嘱）信息）
 * 【3102】明细审核事中分析服务（输入-处方（医嘱）信息）
 *
 * @author SunJQ
 * @date 2025-04-15
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FsiOrder3101Param {

    // 1. 处方(医嘱)标识
    @JSONField(name = "rx_id")
    private String rxId;

    // 2. 处方号
    @JSONField(name = "rxno")
    private String rxno;//2025/05/05 李要求该字段对应耗材时使用buz_no,药品时使用处方号

    // 3. 组编号
    @JSONField(name = "grpno")
    private String grpno;

    // 4. 是否为长期医嘱 [1=是,0=否]
    @JSONField(name = "long_drord_flag")
    private String longDrordFlag;

    // 5. 目录类别（参考字典表）
    @JSONField(name = "hilist_type")
    private String hilistType;

    // 6. 收费类别（参考字典表）
    @JSONField(name = "chrg_type")
    private String chrgType;

    // 7. 医嘱行为（参考字典表）
    @JSONField(name = "drord_bhvr")
    private String drordBhvr;

    // 8. 医保目录代码（国家统一标准编码）
    @JSONField(name = "hilist_code")
    private String hilistCode;

    // 9. 医保目录名称（国家统一标准名称）
    @JSONField(name = "hilist_name")
    private String hilistName;

    // 10. 医保目录(药品)剂型（国家统一标准药品剂型）
    @JSONField(name = "hilist_dosform")
    private String hilistDosform;

    // 11. 医保目录等级
    @JSONField(name = "hilist_lv")
    private String hilistLv;

    // 12. 医保目录价格
    @JSONField(name = "hilist_pric")
    private BigDecimal hilistPric;

    // 13. 一级医院目录价格
    @JSONField(name = "lv1_hosp_item_pric")
    private BigDecimal lv1HospItemPric;

    // 14. 二级医院目录价格
    @JSONField(name = "lv2_hosp_item_pric")
    private BigDecimal lv2HospItemPric;

    // 15. 三级医院目录价格
    @JSONField(name = "lv3_hosp_item_pric")
    private BigDecimal lv3HospItemPric;

    // 16. 医保目录备注
    @JSONField(name = "hilist_memo")
    private String hilistMemo;

    // 17. 医院目录代码
    @JSONField(name = "hosplist_code")
    private String hosplistCode;

    // 18. 医院目录名称
    @JSONField(name = "hosplist_name")
    private String hosplistName;

    // 19. 医院目录(药品)剂型
    @JSONField(name = "hosplist_dosform")
    private String hosplistDosform;

    // 20. 数量
    @JSONField(name = "cnt")
    private BigDecimal cnt;

    // 21. 单价
    @JSONField(name = "pric")
    private BigDecimal pric;

    // 22. 总费用
    @JSONField(name = "sumamt")
    private BigDecimal sumamt;

    // 23. 自费金额
    @JSONField(name = "ownpay_amt")
    private BigDecimal ownpayAmt;

    // 24. 自付金额
    @JSONField(name = "selfpay_amt")
    private BigDecimal selfpayAmt;

    // 25. 规格
    @JSONField(name = "spec")
    private String spec;

    // 26. 数量单位
    @JSONField(name = "spec_unt")
    private String specUnt;

    // 27. 医嘱开始日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(name = "drord_begn_date")
    private Date drordBegnDate;

    // 28. 医嘱停止日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(name = "drord_stop_date")
    private Date drordStopDate;

    // 29. 下达医嘱的科室标识
    @JSONField(name = "drord_dept_codg")
    private String drordDeptCodg;

    // 30. 下达医嘱科室名称
    @JSONField(name = "drord_dept_name")
    private String drordDeptName;

    // 31. 开处方(医嘱)医生标识
    @JSONField(name = "drord_dr_codg")
    private String drordDrCodg;

    // 32. 开处方(医嘱)医生姓名
    @JSONField(name = "drord_dr_name")
    private String drordDrName;

    // 33. 开处方(医嘱)医职称
    @JSONField(name = "drord_dr_profttl")
    private String drordDrProfttl;

    // 34. 是否当前处方(医嘱)[1=是,0=否]
    @JSONField(name = "curr_drord_flag")
    private String currDrordFlag;
}
