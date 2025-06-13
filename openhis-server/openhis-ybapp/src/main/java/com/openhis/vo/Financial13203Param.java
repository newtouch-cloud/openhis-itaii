/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *【13203】医药机构费用结算日对账结果查询
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Financial13203Param {

    // 对账日期，格式：YYYY-MM-DD
    @JSONField(name = "stmt_date")
    private String stmtDate;

    // 明细对账结果，1：不平，0：平
    @JSONField(name = "stmt_rslt")
    private String stmtRslt;

    // 查询页面数据量，最大100
    @JSONField(name = "page_size")
    private String pageSize;

    // 页数，默认1
    @JSONField(name = "page_num")
    private String pageNum;

    // 清算机构编码
    @JSONField(name = "clr_optins")
    private String clrOptins;

    // 清算类别
    @JSONField(name = "clr_type")
    private String clrType;

    // 险种
    @JSONField(name = "insutype")
    private String insutype;

    // 就医类别，2：本地就医，3：省内异地就医，4：跨省就医
    @JSONField(name = "clr_pay_loc")
    private String clrPayLoc;
}
