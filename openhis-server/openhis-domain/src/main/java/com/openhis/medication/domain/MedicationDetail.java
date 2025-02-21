/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.medication.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.core.common.core.domain.HisBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 药品信息详情
 *
 * @author zwh
 * @date 2025-02-20
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MedicationDetail extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 药品编码 */
    private Long medicationProductId;

    /** 药品状态 */
    private Integer statusEnum;

    /** 所属科室 */
    private Long orgId;

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

    /** 药品编号 */
    private Long code;

    /** 药品名称 */
    private String name;

    /** 适用范围 */
    private Integer domainEnum;

    /** 药品版本 */
    private String version;

    /** 英文药名 */
    private String nameEn;

    /** 药品名称拼音码 */
    private String pyCode;

    /** 药品五笔码 */
    private String wbCode;

    /** 药品分类 */
    private Integer categoryCode;

    /** 商品名称 */
    private String merchandiseName;

    /** 商品名称拼音码 */
    private String merchandisePyCode;

    /** 商品五笔码 */
    private String merchandiseWbCode;

    /** 药品单位 */
    private String unitCode;

    /** 最小单位 */
    private String minUnitCode;

    /** 所含耗材 */
    private String comprisedText;

    /** 成分 */
    private String ingredient;

    /** 拆零比 */
    private BigDecimal partPercent;

    /** 剂量形式 */
    private Integer doseFrom;

    /** 批准文号 */
    private String approvalNumber;

    /** 医保是否对码 */
    private Integer ybMatchFlag;

    /** 医保编码 */
    private String ybCode;

    /** 药理作用分类 */
    private String pharmacologyCategoryCode;

    /** 是否皮试 */
    private Integer skinTestFlag;

    /** 是否为注射药物 */
    private Integer injectFlag;

    /** 生产厂家 */
    private Long manufacturerId;

    /** 供应商 */
    private Long supplyId;

    /** 是否限制使用 */
    private Integer restrictedFlag;

    /** 限制使用范围 */
    private String restrictedScope;

    /** 儿童用药标志 */
    private Integer childrenFlag;

    /** 产品特性 */
    private Integer characteristic;

}
