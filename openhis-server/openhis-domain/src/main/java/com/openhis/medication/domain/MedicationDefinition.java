package com.openhis.medication.domain;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.core.domain.HisBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 药品定义管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("med_medication_definition")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MedicationDefinition extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 药品编号 */
    private String busNo;

    /** 药品名称 */
    private String name;

    /** 适用范围 */
    private Integer domainEnum;

    /** 药品版本 */
    private String version;

    /** 英文药名 */
    private String nameEn;

    /** 药品名称拼音码 */
    private String pyStr;

    /** 药品五笔码 */
    private String wbStr;

    /** 药品分类 */
    private String categoryCode;

    /** 商品名称 */
    private String merchandiseName;

    /** 商品名称拼音码 */
    private String merchandisePyStr;

    /** 商品五笔码 */
    private String merchandiseWbStr;

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
    private String ybNo;

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

    /** 最小库存警戒数量(常规单位) */
    private BigDecimal itemMinQuantity;

    /** 最大库存警戒数量(常规单位) */
    private BigDecimal itemMaxQuantity;

    /** 默认门诊单位 */
    private String defEncounterUnitCode;

    /** 默认住院单位 */
    private String defInhospitalUnitCode;

}