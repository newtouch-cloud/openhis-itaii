package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * 【3101】明细审核事前分析服务（输出-违规信息）
 * 【3102】明细审核事中分析服务（输出-违规信息）
 *
 * @author gaoyy
 * @date 2025-05-08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FsiViolation3101Result {
    // 1. 违规标识
    @JSONField(name = "jr_id")
    private String jrId;

    // 2. 规则ID
    @JSONField(name = "rule_id")
    private String ruleId;

    // 3. 规则名称
    @JSONField(name = "rule_name")
    private String ruleName;

    // 4. 违规内容
    @JSONField(name = "vola_cont")
    private String volaCont;

    // 5. 参保人ID
    @JSONField(name = "patn_id")
    private String patnId;

    // 6. 就诊ID
    @JSONField(name = "mdtrt_id")
    private String mdtrtId;

    // 7. 违规明细
    @JSONField(name = "judge_result_detail_dtos")
    private List<FsiViolationDetail3101Result> judgeResultDetailDtos;

    // 8. 违规金额
    @JSONField(name = "vola_amt")
    private BigDecimal volaAmt;

    // 9. 违规金额计算状态
    @JSONField(name = "vola_amt_stas")
    private String volaAmtStas;

    // 10. 严重程度
    @JSONField(name = "sev_deg")
    private String sevDeg;

    // 11. 违规依据
    @JSONField(name = "vola_evid")
    private String volaEvid;

    // 12. 违规行为分类
    @JSONField(name = "vola_bhvr_type")
    private String volaBhvrType;

    // 13. 任务ID
    @JSONField(name = "task_id")
    private String taskId;
}
