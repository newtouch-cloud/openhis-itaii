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
 * 【电子处方信息查询-输出-诊断信息（节点表示： rxDiseList) 】Entity实体
 *
 * @author system
 * @date 2025-04-17
 */
@Data
@TableName("yb_elep_quer_diagnosis_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ElepQuerDiagnosisInfo extends HisBaseEntity {

    /** 自增主键 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 诊断类别 */
    private String diagType;

    /** 主诊断标志 */
    private String maindiagFlag;

    /** 诊断排序号 */
    private String diagSrtNo;

    /** 诊断代码 */
    private String diagCode;

    /** 诊断名称 */
    private String diagName;

    /** 诊断科室 */
    private String diagDept;

    /** 诊断科室代码 */
    private String diagDeptCode;

    /** 诊断医生编码 */
    private String diagDrNo;

    /** 诊断医生姓名 */
    private String diagDrName;

    /** 诊断时间(yyyy-MM-dd HH:mm:ss) */
    private String diagTime;

    /** 中医病名代码 */
    private String tcmDiseCode;

    /** 中医病名名称 */
    private String tcmDiseName;

    /** 中医症候代码 */
    private String tcmsympCode;

    /** 中医症候 */
    private String tcmsymp;

}