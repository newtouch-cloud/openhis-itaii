/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.InventoryManage.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 更新dto
 *
 * @author zxy
 * @date 2025-02-20
 */
@Data
@Accessors(chain = true)
public class SupplySaveRequestDto {
    /** ID */
    private Long id;

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
    private Long code;

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
    private Integer sourceType;

    /** 源仓库 */
    private Long sourceLocationId;

    /** 源仓位 */
    private Long sourceLocationStoreId;

    /** 目的类型 */
    private Integer purposeType;

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

    /** 删除状态 */
    private Integer deleteFlag;

    // =======chargeItem实体============

    /** ID */
    private Long chargeItemId;

    /** 类别 */
    private Integer context;

    /** 就诊ID */
    private Long encounterId;

    /** 执行人Id */
    private Long performer;

    /** 执行科室 */
    private Long performingOrgId;

    /** 开立科室 */
    private Long requestingOrgId;

    /** 成本科室 */
    private Long costOrgId;

    /** 数量 */
    private Long quantityValue;

    /** 单位 */
    private Long quantityUnit;

    /** 单价 */
    private BigDecimal unitPrice;

    /** 总价 */
    private BigDecimal totalPrice;

    /** 费用定价ID */
    private Long definitionId;

    /** applicability表主键 */
    private Long applicabilityId;

    /** 原价 */
    private BigDecimal baseAmount;

    /** 折后价格 */
    private BigDecimal discountAmount;

    /** 附加价格 */
    private BigDecimal surchargeAmount;

    /** 改价原因 */
    private String overrideReasonCode;

    /** 改价原因文本 */
    private String overrideReasonText;

    /** 开立人ID */
    private Long entererId;

    /** 开立时间 */
    private Date enteredDate;

    /** 医疗服务类型 */
    private String serviceTable;

    /** 医疗服务ID */
    private Long serviceId;

    /** 索赔结果 */
    private Integer claimStateEnum;

    /** 关联账户ID */
    private Long accountId;

    /** 机构 */
    private String orgCode;
}
