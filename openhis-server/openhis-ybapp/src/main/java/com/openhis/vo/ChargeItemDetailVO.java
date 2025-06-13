package com.openhis.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.openhis.common.annotation.Dict;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ChargeItemDetailVO {

    @Dict(dictCode = "chrgitm_lv")
    private String dirClass;//医保等级


    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 状态 */
    private Integer statusEnum;

    /**
     * 账单生成来源
     */
    private Integer generateSourceEnum;

    /** 层级 */
    private String busNo;

    /** 处方号 */
    private String prescriptionNo;

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
    private Integer quantityValue;

    /** 单位 */
    private String quantityUnit;

    /** 单价 */
    private BigDecimal unitPrice;

    /** 总价 */
    private BigDecimal totalPrice;

    /** 费用定价ID */
    private Long definitionId;

    /** 定价子表主键 */
    private Long defDetailId;

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

    /** 医疗服务所在表 */
    private String serviceTable;

    /** 医疗服务ID */
    private Long serviceId;

    /** 产品所在表 */
    private String productTable;

    /** 产品ID */
    private Long productId;

    /** 索赔结果 */
    private Integer claimStateEnum;

    /** 打印次数 */
    private Integer printCount;

    /** 关联账户ID */
    private Long accountId;

    /** 机构 */
    private Long orgId;

    /** 退费ID */
    private Long refundId;

    /** 诊断ID */
    private Long diagnosisId;//adm_encounter_diagnosis表主键

    /**
     * 子项json
     */
    private String childrenJson;

    /**
     * 诊断id
     */
    private Long conditionId;

    /**
     * 就诊诊断id
     */
    private Long encounterDiagnosisId;
}
