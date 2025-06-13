package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 【2402】出院办理
 *
 * @author yuanzs
 * @date 2025-05-08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class HospAdm2402DscgInfoParam {
    // 1. 就诊ID
    @JSONField(name = "mdtrt_id")
    private String mdtrtId;

    // 2. 人员编号
    @JSONField(name = "psn_no")
    private String psnNo;

    // 3. 险种类型
    @JSONField(name = "insutype")
    private String insutype;

    // 4. 结束时间（出院时间）
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(name = "endtime")
    private Date endtime;

    // 5. 病种编码
    @JSONField(name = "dise_codg")
    private String diseCodg;

    // 6. 病种名称
    @JSONField(name = "dise_name")
    private String diseName;

    // 7. 手术操作代码（日间手术病种时必填）
    @JSONField(name = "oprn_oprt_code")
    private String oprnOprtCode;

    // 8. 手术操作名称
    @JSONField(name = "oprn_oprt_name")
    private String oprnOprtName;

    // 9. 计划生育服务证号
    @JSONField(name = "fpsc_no")
    private String fpscNo;

    // 10. 生育类别
    @JSONField(name = "matn_type")
    private String matnType;

    // 11. 计划生育手术类别
    @JSONField(name = "birctrl_type")
    private String birctrlType;

    // 12. 晚育标志
    @JSONField(name = "latechb_flag")
    private String latechbFlag;

    // 13. 孕周数
    @JSONField(name = "esso_val")
    private BigDecimal essoVal;

    // 14. 胎次
    @JSONField(name = "fetts")
    private BigDecimal fetts;

    // 15. 胎儿数
    @JSONField(name = "fetus_cnt")
    private BigDecimal fetusCnt;

    // 16. 早产标志
    @JSONField(name = "pret_flag")
    private String pretFlag;

    // 17. 计划生育手术或生育日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "birctrl_matn_date")
    private Date birctrlMatnDate;

    // 18. 伴有并发症标志
    @JSONField(name = "cop_flag")
    private String copFlag;

    // 19. 出院科室编码
    @JSONField(name = "dscg_dept_codg")
    private String dscgDeptCodg;

    // 20. 出院科室名称
    @JSONField(name = "dscg_dept_name")
    private String dscgDeptName;

    // 21. 出院床位
    @JSONField(name = "dscg_bed")
    private String dscgBed;

    // 22. 离院方式
    @JSONField(name = "dscg_way")
    private String dscgWay;

    // 23. 死亡日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "die_date")
    private Date dieDate;

    // 24. 字段扩展
    @JSONField(name = "exp_content")
    private String expContent;

    // 25. 出院诊断信息
    private List<HospAdm2402DiseInfoParam> diseInfoParams;

}
