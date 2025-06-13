package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 【2403】住院信息变更
 *
 * @author yuanzs
 * @date 2025-05-08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class HospAdm2403AdmInfoParam {

    // 1. 就诊ID
    @JSONField(name = "mdtrt_id")
    private String mdtrtId;

    // 2. 人员编号
    @JSONField(name = "psn_no")
    private String psnNo;

    // 3. 联系人姓名
    @JSONField(name = "coner_name")
    private String conerName;

    // 4. 联系电话
    @JSONField(name = "tel")
    private String tel;

    // 5. 开始时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(name = "begntime")
    private Date begntime;

    // 6. 结束时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(name = "endtime")
    private Date endtime;

    // 7. 就诊凭证类型
    @JSONField(name = "mdtrt_cert_type")
    private String mdtrtCertType;

    // 8. 医疗类别
    @JSONField(name = "med_type")
    private String medType;

    // 9. 住院/门诊号
    @JSONField(name = "ipt_otp_no")
    private String iptOtpNo;

    // 10. 病历号
    @JSONField(name = "medrcdno")
    private String medrcdno;

    // 11. 主治医生编码
    @JSONField(name = "atddr_no")
    private String atddrNo;

    // 12. 主诊医师姓名
    @JSONField(name = "chfpdr_name")
    private String chfpdrName;

    // 13. 入院诊断描述
    @JSONField(name = "adm_diag_dscr")
    private String admDiagDscr;

    // 14. 入院科室编码
    @JSONField(name = "adm_dept_codg")
    private String admDeptCodg;

    // 15. 入院科室名称
    @JSONField(name = "adm_dept_name")
    private String admDeptName;

    // 16. 入院床位
    @JSONField(name = "adm_bed")
    private String admBed;

    // 17. 住院主诊断代码
    @JSONField(name = "dscg_maindiag_code")
    private String dscgMaindiagCode;

    // 18. 住院主诊断名称
    @JSONField(name = "dscg_maindiag_name")
    private String dscgMaindiagName;

    // 19. 主要病情描述
    @JSONField(name = "main_cond_dscr")
    private String mainCondDscr;

    // 20. 病种编码
    @JSONField(name = "dise_codg")
    private String diseCodg;

    // 21. 病种名称
    @JSONField(name = "dise_name")
    private String diseName;

    // 22. 手术操作代码
    @JSONField(name = "oprn_oprt_code")
    private String oprnOprtCode;

    // 23. 手术操作名称
    @JSONField(name = "oprn_oprt_name")
    private String oprnOprtName;

    // 24. 计划生育服务证号
    @JSONField(name = "fpsc_no")
    private String fpscNo;

    // 25. 生育类别
    @JSONField(name = "matn_type")
    private String matnType;

    // 26. 计划生育手术类别
    @JSONField(name = "birctrl_type")
    private String birctrlType;

    // 27. 晚育标志
    @JSONField(name = "latechb_flag")
    private String latechbFlag;

    // 28. 孕周数
    @JSONField(name = "esso_val")
    private Integer essoVal;

    // 29. 胎次
    @JSONField(name = "fetts")
    private Integer fetts;

    // 30. 胎儿数
    @JSONField(name = "fetus_cnt")
    private Integer fetusCnt;

    // 31. 早产标志
    @JSONField(name = "pret_flag")
    private String pretFlag;

    // 32. 计划生育手术或生育日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "birctrl_matn_date")
    private Date birctrlMatnDate;

    // 33. 病种编号
    @JSONField(name = "dise_type_code")
    private String diseTypeCode;

    // 34. 字段扩展
    @JSONField(name = "exp_content")
    private String expContent;

    // 35. 外伤标识（仅对异地根据情况填写）
    @JSONField(name = "trum_flag")
    private String trumFlag;

    // 36. 涉及第三方标志（仅对异地根据情况填写）
    @JSONField(name = "rel_ttp_flag")
    private String relTtpFlag;

    // 37. 就诊人群类型（仅对异地根据情况填写）
    @JSONField(name = "mdtrt_grp_type")
    private String mdtrtGrpType;

    // 38. 入院诊断信息集合
    private List<HospAdm2403DiseInfoParam> diseInfoParams;
}
