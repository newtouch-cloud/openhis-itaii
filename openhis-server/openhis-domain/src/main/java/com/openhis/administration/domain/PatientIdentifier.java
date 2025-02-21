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
 * 患者标识管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("adm_patient_identifier")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class PatientIdentifier extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 标识类型编码 */
    private String typeCode;

    /** 标识号 */
    private String identifierNo;

    /** 标识状态枚举 */
    private Integer stateEnum;

    /** 有效时间Start */
    private Date startTime;

    /** 有效时间end */
    private Date endTime;


}