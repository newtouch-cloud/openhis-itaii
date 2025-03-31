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
 * 器材请求管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("wor_device_request")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class DeviceRequest extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 器材请求编码 */
    private String busNo;

    /** 处方号 */
    private String prescriptionNo;

    /** 相关诊疗项目 */
    private Long activityId;

    /** 组套id */
    private Long packageId;

    /** 器材请求状态 */
    private Integer statusEnum;

    /** 请求意图 */
    private String intentCode;

    /** 请求类型 */
    private Integer categoryEnum;

    /** 优先级 */
    private Integer priorityEnum;

    /** 是否停止执行 */
    private Integer performFlag;

    /** 器材类型 */
    private String deviceTypeCode;

    /** 请求数量 */
    private Integer quantity;

    /** 请求单位编码 */
    private String unitCode;

    /** 产品批号 */
    private String lotNumber;

    /** 耗材定义id */
    private Long deviceDefId;

    /** 器材规格 */
    private String deviceSpecifications;

    /** 请求发起人 */
    private Long requesterId;

    /** 请求发起的科室 */
    private Long orgId;

    /** 默认器材房 */
    private Long locationId;

    /** 发放耗材房 */
    private Long performLocation;

    /** 就诊id */
    private Long encounterId;

    /** 患者ID */
    private Long patientId;

    /** 用药频次 */
    private String rateCode;

    /** 预计使用时间 */
    private Date useTime;

    /** 预计使用开始时间 */
    private Date useStartTime;

    /** 预计使用结束时间 */
    private Date useEndTime;

    /** 预计使用周期时间 */
    private String useTiming;

    /** 请求开始时间 */
    private Date reqAuthoredTime;

    /** 执行人类型 */
    private Integer performerEnum;

    /** 执行人 */
    private Long performerId;

    /** 执行科室 */
    private Long performOrgId;

    /** 相关诊断 */
    private String conditionIdJson;

    /** 相关观测 */
    private String observationIdJson;

    /** 是否可以按需给出 */
    private Integer asNeedFlag;

    /** 按需使用原因 */
    private String asNeedReason;

    /** 合同Id */
    private String contractCode;

    /** 支持用药信息 */
    private String supportInfo;

}