package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 【2501A】转院备案（输入）
 *
 * @author gaoyy
 * @date 2025-05-08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class HospTrf2501ARefmedinParam {
    // 1. 人员编号
    @JSONField(name = "psn_no")
    private String psnNo;

    // 2. 险种类型
    @JSONField(name = "insutype")
    private String insutype;

    // 3. 联系电话
    @JSONField(name = "tel")
    private String tel;

    // 4. 联系地址
    @JSONField(name = "addr")
    private String addr;

    // 5. 参保机构医保区划
    @JSONField(name = "insu_optins")
    private String insuOptins;

    // 6. 诊断代码
    @JSONField(name = "diag_code")
    private String diagCode;

    // 7. 诊断名称
    @JSONField(name = "diag_name")
    private String diagName;

    // 8. 疾病病情描述
    @JSONField(name = "dise_cond_dscr")
    private String diseCondDscr;

    // 9. 转往定点医药机构编号
    @JSONField(name = "reflin_medins_no")
    private String reflinMedinsNo;

    // 10. 转往医院名称
    @JSONField(name = "reflin_medins_name")
    private String reflinMedinsName;

    // 11. 就医地行政区划
    @JSONField(name = "mdtrtarea_admdvs")
    private String mdtrtareaAdmdvs;

    // 12. 医院同意转院标志
    @JSONField(name = "hosp_agre_refl_flag")
    private String hospAgreReflFlag;

    // 13. 转院类型
    @JSONField(name = "refl_type")
    private String reflType;

    // 14. 转院日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "refl_date")
    private Date reflDate;

    // 15. 转院原因
    @JSONField(name = "refl_rea")
    private String reflRea;

    // 16. 转院意见
    @JSONField(name = "refl_opnn")
    private String reflOpnn;

    // 17. 开始日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "begndate")
    private Date begndate;

    // 18. 结束日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "enddate")
    private Date enddate;

    // 19. 转院前就诊id
    @JSONField(name = "refl_old_mdtrt_id")
    private String reflOldMdtrtId;

    // 20. 待遇申报明细流水号（输出）
    @JSONField(name = "trt_dcla_detl_sn")
    private String trtDclaDetlSn;
}
