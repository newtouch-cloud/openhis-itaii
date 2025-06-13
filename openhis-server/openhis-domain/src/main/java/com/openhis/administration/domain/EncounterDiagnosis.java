package com.openhis.administration.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.core.domain.HisBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 就诊诊断管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("adm_encounter_diagnosis")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class EncounterDiagnosis extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 就诊id */
    private Long encounterId;

    /** 诊断_id */
    private Long conditionId;

    /** 住院患者疾病诊 */
    private Integer iptDiseCrsp;

    /** 住院患者疾病诊断类型代码 */
    private Integer iptDiseTypeCode;

    /** 入院疾病病情代码 */
    private Integer admDiseCondCode;

    /** 医疗类型 */
    private String medTypeCode;// 2025/05/23 该字段改为med_type 与医保同步

    /** 主诊断标记 */
    private Integer maindiseFlag;

    /** 最高诊断依据标记 */
    private Integer highDiseEvidFlag;

    /** 诊断排序（医保文档要求数值型字符长度为2） */
    private Integer diagSrtNo;

    /**
     * 中医证候组号
     */
    private String syndromeGroupNo;

    /**
     * 中医标识
     */
    private Integer tcmFlag;

}