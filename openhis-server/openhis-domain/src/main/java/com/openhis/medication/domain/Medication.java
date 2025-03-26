package com.openhis.medication.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.core.domain.HisBaseEntity;
import com.openhis.common.enums.PublicationStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 药品基本信息管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("med_medication")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Medication extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 药品编码 */
    private Long medicationDefId;

    /** 药品状态 */
    private PublicationStatus statusEnum;

    /** 所属科室 */
    private Long orgId;

    /** 所在位置 */
    private Long locationId;

    /** 剂型 */
    private String doseFormCode;

    /** 规格 */
    private String totalVolume;

    /** 成分 */
    private String ingredientItem;

    /** 是否为活性 */
    private Integer activeFlag;

    /** 批次号 */
    private String lotNumber;

    /** 生效日期 */
    private Date effectiveDate;

    /** 到期日期 */
    private Date expirationDate;

    /** 用法 */
    private String methodCode;

    /** 用药频次 */
    private String rateCode;

    /** 单次剂量 */
    private BigDecimal dose;

    /** 剂量单位 */
    private String doseUnitCode;

    /** 单次最大剂量 */
    private BigDecimal maxUnit;

    /** 药品定义 */
    private String definition;

    /** 删除状态 */
    private String deleteFlag;

    /** 用量限定 */
    private BigDecimal usageLimit;

    /** DDD值 */
    private String dddCode;

    /** DDD单位 */
    private String dddUnitCode;

    /** 单次最小用药频次 */
    private String minRateCode;

    /** 单次最大用药频次 */
    private String maxRateCode;

}