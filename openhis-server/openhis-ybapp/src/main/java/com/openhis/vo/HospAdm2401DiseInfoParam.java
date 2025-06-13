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
 * 【2401】入院办理（输入-入院诊断信息）
 *
 * @author yuanzs
 * @date 2025-05-07
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class HospAdm2401DiseInfoParam {
    // 1. 人员编号
    @JSONField(name = "psn_no")
    private String psnNo;

    // 2. 诊断类别
    @JSONField(name = "diag_type")
    private String diagType;

    // 3. 主诊断标志
    @JSONField(name = "maindiag_flag")
    private String maindiagFlag;

    // 4. 诊断排序号
    @JSONField(name = "diag_srt_no")
    private BigDecimal diagSrtNo;

    // 5. 诊断代码
    @JSONField(name = "diag_code")
    private String diagCode;

    // 6. 诊断名称
    @JSONField(name = "diag_name")
    private String diagName;

    // 7. 入院病情
    @JSONField(name = "adm_cond")
    private String admCond;

    // 8. 诊断科室
    @JSONField(name = "diag_dept")
    private String diagDept;

    // 9. 诊断医生编码
    @JSONField(name = "dise_dor_no")
    private String diseDorNo;

    // 10. 诊断医生姓名
    @JSONField(name = "dise_dor_name")
    private String diseDorName;

    // 11. 诊断时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(name = "diag_time")
    private Date diagTime;
}
