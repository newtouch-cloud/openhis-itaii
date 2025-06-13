package com.openhis.ybelep.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.core.common.core.domain.HisBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【电子处方信息查询-输出-就诊信息（节点标识：rxOtpinfo） 】Entity实体
 *
 * @author system
 * @date 2025-04-17
 */
@Data
@TableName("yb_elep_quer_visit_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ElepQuerVisitInfo extends HisBaseEntity {

    /** 自增主键 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 医疗类别(参考医疗类别med_type) */
    private String medType;

    /** 门诊/住院号 */
    private String iptOtpNo;

    /** 门诊住院标志(1-门诊，2-住院) */
    private String otpIptFlag;

    /** 患者姓名 */
    private String patnName;

    /** 年龄 */
    private BigDecimal patnAge;

    /** 患者身高(cm) */
    private BigDecimal patnHgt;

    /** 患者体重(kg) */
    private BigDecimal patnWt;

    /** 性别 */
    private String gend;

    /** 妊娠(孕周) */
    private Integer gesoVal;

    /** 新生儿标志(0-否、1-是) */
    private String nwbFlag;

    /** 新生儿日、月龄 */
    private String nwbAge;

    /** 哺乳期标志(0-否、1-是) */
    private Integer suckPrdFlag;

    /** 过敏史 */
    private String algsHis;

    /** 险种类型 */
    private String insutype;

    /** 开方科室名称 */
    private String prscDeptName;

    /** 开方医师姓名 */
    private String prscDrName;

    /** 药师姓名 */
    private String pharName;

    /** 医疗机构药师审方时间(yyyy-MM-dd HH:mm:ss) */
    private String pharChkTime;

    /** 就诊时间(yyyy-MM-dd HH:mm:ss) */
    private String mdtrtTime;

    /** 病种编码(按病种结算病种目录代码/门诊慢特病种目录代码) */
    private String diseCodg;

    /** 病种名称 */
    private String diseName;

    /** 是否特殊病种 */
    private String spDiseFlag;

    /** 主诊断代码 */
    private String maindiagCode;

    /** 主诊断名称 */
    private String maindiagName;

    /** 疾病病情描述 */
    private String diseCondDscr;

    /** 是否初诊(0-否、1-是) */
    private String fstdiagFlag;

}