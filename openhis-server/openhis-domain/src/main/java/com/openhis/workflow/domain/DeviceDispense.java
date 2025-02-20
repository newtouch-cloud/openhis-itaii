package com.openhis.workflow.domain;

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
 * 器材发放管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("wor_device_dispense")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class DeviceDispense extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 器材发放id */
    private String code;

    /** 器材请求id */
    private String deviceReqId;

    /** 器材发放状态 */
    private Integer statusEnum;

    /** 未发药原因 */
    private Integer notPerformedReasonEnum;

    /** 发药类型 */
    private Integer dispenseCategoryEnum;

    /** 器材编码 */
    private Long deviceDefId;

    /** 领药患者 */
    private Long patientId;

    /** 相关诊疗 */
    private Long encounterId;

    /** 发药人 */
    private Long performerId;

    /** 发放器材房 */
    private Long locationId;

    /** 支持用药信息 */
    private String supportInfo;

    /** 发药类型 */
    private String partTypeCode;

    /** 已发药数量 */
    private Integer dispenseQuantity;

    /** 发药频次 */
    private String dispenseFrequencyCode;

    /** 配药时间 */
    private Date prepareTime;

    /** 发药时间 */
    private Date dispenseTime;

    /** 限制发药时间 */
    private Date limitTime;

    /** 发药目的地 */
    private String desLocationId;

    /** 接收人 */
    private String recPractitionerId;

    /** 使用说明 */
    private String usageInstruction;

    /** 删除状态 */
    private Integer deleteFlag;

}