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
 * 就诊参与者管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("adm_encounter_participant")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class EncounterParticipant extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 就诊id */
    private Long encounterId;

    /** 参与者类型 */
    private String typeCode;

    /** 开始时间 */
    private Date startTiem;

    /** 结束时间 */
    private Date endTime;

    /** 参与者ID */
    private Long practitionerId;


}