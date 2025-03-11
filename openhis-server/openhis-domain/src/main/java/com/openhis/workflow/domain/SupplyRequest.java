package com.openhis.workflow.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.core.domain.HisBaseEntity;
import com.openhis.common.enums.ActPriority;
import com.openhis.common.enums.SupplyCategory;
import com.openhis.common.enums.SupplyStatus;
import com.openhis.common.enums.SupplyType;

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

    /** 项目 */
    private String itemTable;

    /** 数量 */
    private BigDecimal itemQuantity;

    /** 物品编码 */
    private Long itemId;

    /** 物品计量单位 */
    private String unitCode;

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

    /** 产品批号 */
    private String lotNumber;

    /** 追溯码 */
    private String traceNo;

    /** 发票号 */
    private String invoiceNo;

    /** 开始时间 */
    private Date startTime;

    /** 结束时间 */
    private Date endTime;

    /** 单价 */
    private BigDecimal price;

    /** 总价 */
    private BigDecimal totalPrice;

    /** 售价 */
    private BigDecimal sellPrice;

    /** 拆零售价 */
    private BigDecimal minSellPrice;

    public SupplyRequest() {
        // 默认单据类型：商品调拨
        this.typeEnum = SupplyType.PRODUCT_ALLOCATION.getValue();
        // 默认单据状态：待审核
        this.statusEnum = SupplyStatus.PENDING_APPROVAL.getValue();
        // 默认单据分类：库存供应
        this.categoryEnum = SupplyCategory.STOCK_SUPPLY.getValue();
        // 默认优先级：常规
        this.priorityEnum = ActPriority.ROUTINE.getValue();
    }
}