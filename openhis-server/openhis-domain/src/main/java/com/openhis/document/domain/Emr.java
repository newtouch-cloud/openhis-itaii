package com.openhis.document.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.core.domain.HisBaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

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
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 患者ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long patientId;

    /** 就诊ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long encounterId;

    /** 病历信息 */
    private String contextJson;

    /** 病历状态 */
    private String emrEnum;

    /** 记录人 */
    private Long recordId;

}