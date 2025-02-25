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
 * 供应申请管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("wor_supply_request")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SupplyRequest extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 单据号 */
    private String busNo;

    /** 类型 */
    private Integer typeEnum;

    /** 状态 */
    private Integer statusEnum;

    /** 意图 */
    private Integer intentEnum;

    /** 分类 */
    private Integer categoryEnum;

    /** 优先权 */
    private Integer priorityEnum;

    /** 汇总编号 */
    private Long summaryId;

    /** 交付 */
    private Long deliverId;

    /** 患者id */
    private Long patientId;

    /** 发放实体表名 */
    private String dispenseTable;

    /** 发放id */
    private Long dispenseId;

    /** 项目 */
    private String itemTable;

    /** 数量 */
    private Integer itemQuantity;

    /** 物品编码 */
    private Long itemId;

    /** 物品计量单位 */
    private String unitIdCode;

    /** 物品数量 */
    private Integer unitQuantity;

    /** 请求细节 */
    private String detailJson;

    /** 期望时间 */
    private Date occurrenceTime;

    /** 供应人 */
    private Long practitionerId;

    /** 供应商 */
    private Long supplierId;

    /** 理由 */
    private String reason;

    /** 源仓库类型 */
    private Integer sourceTypeEnum;

    /** 源仓库 */
    private Long sourceLocationId;

    /** 源仓位 */
    private Long sourceLocationStoreId;

    /** 目的类型 */
    private Integer purposeTypeEnum;

    /** 目的仓库 */
    private Long purposeLocationId;

    /** 目的仓位 */
    private Long purposeLocationStoreId;

    /** 审批人 */
    private Long approverId;

    /** 审批时间 */
    private Date approvalTime;

    /** 申请人 */
    private Long applicantId;

    /** 申请时间 */
    private Date applyTime;

}