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
 * 供应发放管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("wor_supply_delivery")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SupplyDelivery extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 申请id */
    private Long requestId;

    /** 汇总编号 */
    private Long summaryId;

    /** 状态 */
    private Integer status;

    /** 患者id */
    private Long patientId;

    /** 类型 */
    private Integer type;

    /** 发放项目所属表 */
    private String itemTable;

    /** 发放项目id */
    private Long itemId;

    /** 计量单位 */
    private String unitIdCode;

    /** 数量 */
    private BigDecimal quantity;

    /** 状态说明 */
    private String condition;

    /** 产品批号 */
    private String lotNumber;

    /** 追溯码 */
    private String traceCode;

    /** 供应商 */
    private Long supplierId;

    /** 供应人 */
    private Long practitionerId;

    /** 发放时间 */
    private Date occurrenceTime;

    /** 发放开始时间 */
    private Date occurrenceStartTime;

    /** 发放结束时间 */
    private Date occurrenceEndTime;

    /** 发放周期时间 */
    private String occurrenceTiming;

    /** 源仓库类型 */
    private Integer sourceType;

    /** 源仓库 */
    private Long sourceLocationId;

    /** 源仓位 */
    private Long sourceLocationStoreId;

    /** 目的仓库类型 */
    private Integer purposeType;

    /** 目的仓库 */
    private Long purposeLocationId;

    /** 目的仓位 */
    private Long purposeLocationStoreId;

    /** 接收人 */
    private Long receiverId;

    /** 接收时间 */
    private Date receiveDateTime;


}