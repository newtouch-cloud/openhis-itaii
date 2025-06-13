package com.openhis.ybelep.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.core.common.core.domain.HisBaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 【电子处方上传预核验-输入-就诊信息（节点标识：mdtrtinfo） 】Entity实体
 *
 * @author system
 * @date 2025-04-17
 */
@Data
@TableName("yb_elep_veri_visit_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ElepVeriVisitInfo extends HisBaseEntity {
    /** 自增主键 */
    @JSONField(serialize=false)
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 定点医疗机构名称 */
    private String fixmedinsName;

    /** 定点医疗机构编号 */
    private String fixmedinsCode;

    /** 医保就诊ID(医保门诊挂号时返回) */
    private String mdtrtId;

    /** 医疗类别(参考med_type) */
    private String medType;

    /** 门诊/住院号 */
    private String iptOtpNo;

    /** 门诊住院标识(1-门诊、2-住院) */
    private String otpIptFlag;

    /** 医保人员编号 */
    private String psnNo;

    /** 患者姓名 */
    private String patnName;

    /** 人员证件类型(参考psn_cert_type) */
    private String psnCertType;

    /** 证件号码 */
    private String certno;

    /** 年龄 */
    private String patnAge;

    /** 患者身高(cm) */
    private String patnHgt;

    /** 患者体重(kg) */
    private String patnWt;

    /** 性别(参考gend) */
    private String gend;

    /** 计划生育手术类别 */
    private String birctrlType;

    /** 计划生育手术或生育日期(yyyy-MM-dd) */
    private String birctrlMatnDate;

    /** 生育类别 */
    private String matnType;

    /** 妊娠(孕周) */
    private String gesoVal;

    /** 新生儿标志(0-否、1-是) */
    private String nwbFlag;

    /** 新生儿日、月龄 */
    private String nwbAge;

    /** 哺乳期标志(0-否、1-是) */
    private String suckPrdFlag;

    /** 过敏史 */
    private String algsHis;

    /** 开方科室名称 */
    private String prscDeptName;

    /** 开方科室编号(与医药机构服务的科室管理保持一致) */
    private String prscDeptCode;

    /** 开方医保医师代码(国家医保医师代码) */
    private String drCode;

    /** 开方医师姓名 */
    private String prscDrName;

    /** 开方医师证件类型(参考psn_cert_type) */
    private String prscDrCertType;

    /** 开方医师证件号码 */
    private String prscDrCertno;

    /** 医生职称编码(参考drord_dr_proftt1) */
    private String drProfttlCodg;

    /** 医生职称名称 */
    private String drProfttlName;

    /** 医生科室编码(与医院科室编码字段保持一致) */
    private String drDeptCode;

    /** 医生科室名称 */
    private String drDeptName;

    /** 科别(参考科室代码dept) */
    private String caty;

    /** 就诊时间(yyyy-MM-dd HH:mm:ss) */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date mdtrtTime;

    /** 病种编码 */
    private String diseCodg;

    /** 病种名称 */
    private String diseName;

    /** 特殊病种标志(0-否、1-是) */
    private String spDiseFlag;

    /** 主诊断代码(医保疾病诊断代码) */
    private String maindiagCode;

    /** 主诊断名称 */
    private String maindiagName;

    /** 疾病病情描述 */
    private String diseCondDscr;

    /** 医保费用结算类型(参考hi_feeset1_type) */
    private String hiFeesetlType;

    /** 医保费用类别名称 */
    private String hiFeesetlName;

    /** 挂号费 */
    private String rgstFee;

    /** 医疗费总额 */
    private String medfeeSumamt;

    /** 是否初诊(0-否、1-是) */
    private String fstdiagFlag;

    /** 扩展数据(地方业务扩展信息) */
    private String extras;

    /** 院内内部处方号 */
    @JSONField(serialize=false)
    private String prescriptionNo;

    /** 医保处方编号(电子处方信息查询返回时插入记录) */
    @JSONField(serialize=false)
    private String hiRxno;

}