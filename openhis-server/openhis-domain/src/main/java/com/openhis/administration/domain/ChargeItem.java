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
 * 费用项管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("adm_charge_item")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ChargeItem extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 状态 */
    private Integer statusEnum;

    /** 层级 */
    private String busNo;

    /** 患者ID */
    private Long patientId;

    /** 类别 */
    private Integer contextEnum;

    /** 就诊ID */
    private Long encounterId;

    /** 发生时间 */
    private Date occurrenceTime;

    /** 执行人Id */
    private Long performerId;

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
    private Integer orgId;


}