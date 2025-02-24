package com.openhis.administration.domain;

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

    /** 特殊病种标志 */
    private Integer spDiseFlag;

    /** 主诊断标记 */
    private Integer maindiseFlag;

    /** 最高诊断依据标记 */
    private Integer highDiseEvidFlag;


}