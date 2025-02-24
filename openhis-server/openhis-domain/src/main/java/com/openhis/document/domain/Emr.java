package com.openhis.document.domain;

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
 * 病历信息Entity实体
 *
 * @author system
 * @date 2025-02-21
 */
@Data
@TableName("doc_emr")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Emr extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 患者ID */
    private Long patientId;

    /** 就诊ID */
    private Long encounterId;

    /** 病历信息 */
    private String contextJson;

    /** 病历状态 */
    private String emrEnum;

    /** 记录人 */
    private Long recordId;


}