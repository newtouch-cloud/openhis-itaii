package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 【2203】门诊就诊信息上传
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Clinic2203MedicalParam {
    // 主键
    @JSONField(serialize = false)
    private String id;
    // 就诊ID内部
    @JSONField(serialize = false)
    private String regId;
    // 定点医药机构编号
    @JSONField(serialize = false)
    private String fixmedinsCode;
    // 定点医药机构名称
    @JSONField(serialize = false)
    private String fixmedinsName;
    // 就医地医保区划
    @JSONField(serialize = false)
    private String mdtrtareaAdmvs;
    // 参保地医保区划
    @JSONField(name = "insuplc_admdvs")
    private String insuplcAdmdvs;
    // 就诊ID
    @JSONField(name = "mdtrt_id")
    private String mdtrtId;
    // 人员编号
    @JSONField(name = "psn_no")
    private String psnNo;
    // 医疗类别
    @Dict(dictCode = "med_type")
    @JSONField(name = "med_type")
    private String medType;
    // 诊断时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(name = "begntime")
    private Date begntime;
    // 主要病情描述
    @JSONField(name = "main_cond_dscr")
    private String mainCondDscr;
    // 病种编码
    @JSONField(name = "dise_codg")
    private String diseCodg;
    // 病种名称
    @JSONField(name = "dise_name")
    private String diseName;
    // 计划生育手术类别
    @Dict(dictCode = "birctrl_type")
    @JSONField(name = "birctrl_type")
    private String birctrlType;
    // 计划生育手术或生育日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "birctrl_matn_date")
    private Date birctrlMatnDate;
    @JSONField(name = "diseinfo")
    private List<Clinic2203DiseInfoParam> diseinfoList;
}
