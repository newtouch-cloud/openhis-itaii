package com.openhis.ybcatalog.domain;

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
 * 中药材信息Entity实体
 *
 * @author system
 * @date 2025-04-09
 */
@Data
@TableName("yb_catalog_medical_herb_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CatalogMedicalHerbInfo {

    /** 医疗目录编码 */
    @TableId(type = IdType.ASSIGN_ID)
    private String medicalCatalogCode;

    /** $column.columnComment */
    private String singleDrugName;

    /** 单复方标志(true:复方 false:单方) */
    private String singleCompoundFlag;

    /** $column.columnComment */
    private String qualityGrade;

    /** 中草药年份 */
    private String herbalYear;

    /** $column.columnComment */
    private String medicinalPart;

    /** $column.columnComment */
    private String safeDosage;

    /** $column.columnComment */
    private String conventionalUsage;

    /** $column.columnComment */
    private String propertiesTaste;

    /** $column.columnComment */
    private String meridianAttribution;

    /** $column.columnComment */
    private String species;

    /** $column.columnComment */
    private String startDate;

    /** $column.columnComment */
    private String endDate;

    /** 有效标志(true:有效 false:无效) */
    private String validFlag;

    /** $column.columnComment */
    private String uniqueRecordId;

    /** $column.columnComment */
    private String versionNumber;

    /** $column.columnComment */
    private String versionName;

    /** $column.columnComment */
    private String herbName;

    /** $column.columnComment */
    private String indications;

    /** $column.columnComment */
    private String processingMethod;

    /** $column.columnComment */
    private String efficacyClassification;

    /** $column.columnComment */
    private String herbSource;

    /** $column.columnComment */
    private String nationalInsurancePolicy;

    /** $column.columnComment */
    private String provincialInsurancePolicy;

    /** $column.columnComment */
    private String standardName;

    /** $column.columnComment */
    private String standardPage;

    /** $column.columnComment */
    private String electronicRecord;

    /** $column.columnComment */
    private String issuanceFlag;

    /** $column.columnComment */
    private String transferDataId;

    /** $column.columnComment */
    private String effectiveTime;

    /** $column.columnComment */
    private String expiryTime;

}