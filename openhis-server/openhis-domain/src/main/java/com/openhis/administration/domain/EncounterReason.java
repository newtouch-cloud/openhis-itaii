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
 * 就诊原因管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("adm_encounter_reason")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class EncounterReason extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 就诊id */
    private Long encounterId;

    /** 原因编码 */
    private Integer useCode;

    /** 具体原因类型 */
    private String valueTable;

    /** 原因ID */
    private Long valueId;

    /** 原因描述 */
    private String note;

    /** 删除状态 */
    private Integer deleteFlag;

}