package com.openhis.yb.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.annotation.Excel;
import com.core.common.core.domain.HisBaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 *【2203】门诊就诊信息上传
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
public class Clinic2203DiseInfoParam extends HisBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    //主键
    @TableId(type = IdType.ASSIGN_ID)
    @JSONField(serialize=false)
    private String id;
    //就诊ID内部
    @Excel(name = "就诊ID内部", width = 15)
    @JSONField(serialize=false)
    private String encounterId;
    //定点医药机构编号
    @JSONField(serialize=false)
    private String fixmedinsCode;

    //定点医药机构名称
    @JSONField(serialize=false)
    private String fixmedinsName;
    // 就医地医保区划 
    @JSONField(serialize=false)
    private String mdtrtareaAdmvs;
    // 参保地医保区划 
    @JSONField(serialize=false)
    private String insuplcAdmdvs;
    // 就诊ID 
    @Excel(name = "就诊ID", width = 15)
    @JSONField(serialize=false)
    private String mdtrtId;
    // 就诊信息ID 
    @JSONField(serialize=false)
    private String medicalId;
    // 诊断类别 
    @JSONField(name="diag_type")
    private String diagType;
    // 诊断排序号 
    @JSONField(name="diag_srt_no")
    private String diagSrtNo;
    // 诊断代码 
    @JSONField(name="diag_code")
    private String diagCode;
    // 诊断名称 
    @JSONField(name="diag_name")
    private String diagName;
    // 诊断科室 
    @JSONField(name="diag_dept")
    private String diagDept;
    // 诊断医生编码 
    @JSONField(name="dise_dor_no")
    private String diseDorNo;
    // 诊断医生姓名 
    @JSONField(name="dise_dor_name")
    private String diseDorName;
    // 诊断时间 
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(name="diag_time")
    private Date diagTime;
    // 有效标志 
    @JSONField(name="vali_flag")
    private String valiFlag;
//    // 创建人 
//    @JSONField(serialize=false)
//    private String createBy;
//    // 创建日期 
//    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @JSONField(serialize=false)
//    private Date createTime;
//    // 更新人 
//    @JSONField(serialize=false)
//    private String updateBy;
//    // 更新日期 
//    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @JSONField(serialize=false)
//    private Date updateTime;
    // 所属部门 
//    @JSONField(serialize=false)
//    private String sysOrgCode;
//    // 租户编码 
//    @JSONField(serialize=false)
//    private String tenantId;
//    @JSONField(serialize=false)
//    private String district;
}
