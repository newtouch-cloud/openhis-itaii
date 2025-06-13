package com.openhis.workflow.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.core.domain.HisBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 服务申请管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("wor_service_request")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ServiceRequest extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 服务编码 */
    private String busNo;

    /** 处方号 */
    private String prescriptionNo;

    /** 请求基于什么 */
    private String basedOnTable;

    /** 请求基于什么的ID */
    private Long basedOnId;

    /** 组合id */
    private Long requisitionId;

    /** 状态 */
    private Integer statusEnum;

    /** 意图 */
    private Integer intentEnum;

    /** 请求类型 */
    private Integer categoryEnum;

    /** 分组编号 */
    private Long groupId;

    /** 优先权 */
    private Integer priorityEnum;

    /** 是否执行 */
    private Integer performFlag;

    /** 诊疗定义id */
    private Long activityId;

    /** 数量 */
    private Integer quantity;

    /** 单位 */
    private String unitCode;

    /** 请求执行的目标患者 */
    private Long patientId;

    /** 就诊ID */
    private Long encounterId;

    /** 请求签发时间 */
    private Date authoredTime;

    /** 请求者 */
    private Long requesterId;

    /** 执行者类型 */
    private String performerTypeCode;

    /** 执行人 */
    private Long performerId;

    /** 核对人 */
    private Long performerCheckId;

    /** 执行位置 */
    private Long locationId;

    /**
     * 执行科室
     */
    private Long orgId;

    /** 理由 */
    private String reasonText;

    /** 合同Id */
    private Long contractId;

    /** 预计执行时间 */
    private Date occurrenceStartTime;

    /** 预计结束 */
    private Date occurrenceEndTime;

    /** 历史请求 */
    private Long relevantHistoryId;

    /** 取消服务id */
    private Long refundServiceId;

    /**
     * 请求内容json
     */
    private String contentJson;

    /**
     * 类别医保编码
     */
    private Integer ybClassEnum;

    /**
     * 打印次数
     */
    private Integer printCount;

    /**
     * 诊断id
     */
    private Long conditionId;

    /**
     * 就诊诊断id
     */
    private Long encounterDiagnosisId;

}
