package com.openhis.clinical.domain;

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
 * 疾病与诊断管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("cli_condition")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Condition extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 验证状态 */
    private Integer verificationStatusEnum;

    /** 临床特征 */
    private Integer clinicalStatusEnum;

    /** 分类 */
    private Integer categoryEnum;

    /** 严重程度 */
    private Integer severityEnum;

    /** 疾病或诊断编码 */
    private String busNo;

    /** 身体部位 */
    private String bodySiteJson;

    /** 身体结构 */
    private Long bodyStructureId;

    /** 病人Id */
    private Long patientId;

    /** 疾病与诊断描述 */
    private String description;

    /** 初次发病时间 */
    private Date onsetDatetime;

    /** 初次发病年龄 */
    private Integer onsetAge;

    /** 初次发病描述 */
    private String onsetText;

    /** 症状减轻时间 */
    private Date abatementDatetime;

    /** 症状减轻年龄 */
    private Integer abatementAge;

    /** 症状减轻时期 */
    private String abatementString;

    /** 记录时间 */
    private Date recordedDatetime;

    /** 记录人 */
    private Long recorderId;

    /** 阶段总结 */
    private String stageSummaryText;

    /** 证明 */
    private String evidence;

    /** 医保编码 */
    private String ybNo;


}