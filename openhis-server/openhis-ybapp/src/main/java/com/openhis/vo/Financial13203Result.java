/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *【13203】医药机构费用结算日对账结果查询-输出参数
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Financial13203Result {

    // 退费标识
    @JSONField(name = "refd_setl_flag")
    private String refdSetlFlag;

    // 人员编号
    @JSONField(name = "psn_no")
    private String psnNo;

    // 就诊流水号
    @JSONField(name = "mdtrt_id")
    private String mdtrtId;

    // 结算ID
    @JSONField(name = "setl_id")
    private String setlId;

    // 人员名称
    @JSONField(name = "psn_name")
    private String psnName;

    // 对账结果
    @JSONField(name = "stmt_rslt")
    private String stmtRslt;

    // 总医疗费
    @JSONField(name = "medfee_sumamt")
    private String medfeeSumamt;

    // 清算机构编码
    @JSONField(name = "clr_optins")
    private String clrOptins;

    // 报文ID
    @JSONField(name = "msg_id")
    private String msgId;

}
