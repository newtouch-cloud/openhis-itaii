package com.openhis.yb.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【2403】住院信息变更（输入-入院诊断信息）
 *
 * @author yuanzs
 * @date 2025-05-08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class HospAdm2403DiseInfoParam {

    // 1. 就诊ID
    @JSONField(name = "mdtrt_id")
    private String mdtrtId;

    // 2. 人员编号
    @JSONField(name = "psn_no")
    private String psnNo;

    // 3. 诊断类别
    @JSONField(name = "diag_type")
    private String diagType;

    // 4. 主诊断标志
    @JSONField(name = "maindiag_flag")
    private String maindiagFlag;

    // 5. 诊断排序号
    @JSONField(name = "diag_srt_no")
    private BigDecimal diagSrtNo;

    // 6. 诊断代码
    @JSONField(name = "diag_code")
    private String diagCode;

    // 7. 诊断名称
    @JSONField(name = "diag_name")
    private String diagName;

    // 8. 入院病情
    @JSONField(name = "adm_cond")
    private String admCond;

    // 9. 诊断科室
    @JSONField(name = "diag_dept")
    private String diagDept;

    // 10. 诊断医生编码
    @JSONField(name = "dise_dor_no")
    private String diseDorNo;

    // 11. 诊断医生姓名
    @JSONField(name = "dise_dor_name")
    private String diseDorName;

    // 12. 诊断时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(name = "diag_time")
    private Date diagTime;

}
