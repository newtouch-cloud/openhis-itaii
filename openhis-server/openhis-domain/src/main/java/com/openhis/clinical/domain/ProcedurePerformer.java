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
 * 手术与治疗执行人管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("cli_procedure_performer")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ProcedurePerformer extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 手术id */
    private Long procedureId;

    /** 参与者类型 */
    private String functionCode;

    /** 开始时间 */
    private Date startTiem;

    /** 结束时间 */
    private Date endTime;

    /** 参与者ID */
    private Long practitionerId;

    /** 飞刀机构 */
    private String orgFromId;


}