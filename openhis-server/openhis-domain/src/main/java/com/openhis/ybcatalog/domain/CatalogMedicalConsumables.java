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
 * 医疗耗材信息Entity实体
 *
 * @author system
 * @date 2025-04-09
 */
@Data
@TableName("yb_catalog_medical_consumables")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CatalogMedicalConsumables {

    /** 医疗目录编码 */
    @TableId(type = IdType.ASSIGN_ID)
    private String medicalCatalogCode;

    /** $column.columnComment */
    private String consumableName;

    /** $column.columnComment */
    private String deviceUniqueId;

    /** $column.columnComment */
    private String insuranceGenericCode;

    /** $column.columnComment */
    private String insuranceGenericName;

    /** $column.columnComment */
    private String productModel;

    /** $column.columnComment */
    private String specCode;

    /** $column.columnComment */
    private String specification;

    /** $column.columnComment */
    private String consumableCategory;

    /** $column.columnComment */
    private String specModel;

    /** $column.columnComment */
    private String materialCode;

    /** $column.columnComment */
    private String materialType;

    /** $column.columnComment */
    private String packageSpec;

    /** $column.columnComment */
    private String packageQuantity;

    /** $column.columnComment */
    private String packageMaterial;

    /** $column.columnComment */
    private String packageUnit;

    /** $column.columnComment */
    private String conversionRatio;

    /** $column.columnComment */
    private String minUsageUnit;

    /** $column.columnComment */
    private String productionAreaType;

    /** $column.columnComment */
    private String productionAreaName;

    /** $column.columnComment */
    private String productStandard;

    /** $column.columnComment */
    private String expiryDate;

    /** $column.columnComment */
    private String structureComposition;

    /** $column.columnComment */
    private String applicableScope;

    /** $column.columnComment */
    private String usageMethod;

    /** $column.columnComment */
    private String imageCode;

    /** $column.columnComment */
    private String qualityStandard;

    /** $column.columnComment */
    private String instructions;

    /** $column.columnComment */
    private String proofMaterials;

    /** $column.columnComment */
    private String specialDeviceFlag;

    /** $column.columnComment */
    private String specialDeviceName;

    /** $column.columnComment */
    private String kitName;

    /** $column.columnComment */
    private String kitFlag;

    /** $column.columnComment */
    private String usageRestrictionFlag;

    /** $column.columnComment */
    private String insuranceRestriction;

    /** $column.columnComment */
    private String minSaleUnit;

    /** 高值耗材标志(true:是 false:否) */
    private String highValueFlag;

    /** $column.columnComment */
    private String medicalMaterialCode;

    /** $column.columnComment */
    private String implantFlag;

    /** $column.columnComment */
    private String sterilizationFlag;

    /** $column.columnComment */
    private String sterilizationName;

    /** $column.columnComment */
    private String implantInterventionFlag;

    /** $column.columnComment */
    private String implantInterventionName;

    /** $column.columnComment */
    private String disposableFlag;

    /** $column.columnComment */
    private String disposableFlagName;

    /** $column.columnComment */
    private String registrantName;

    /** $column.columnComment */
    private String startDate;

    /** $column.columnComment */
    private String endDate;

    /** $column.columnComment */
    private String deviceManagementCategory;

    /** $column.columnComment */
    private String deviceCategoryName;

    /** $column.columnComment */
    private String registrationNo;

    /** $column.columnComment */
    private String registeredProductName;

    /** $column.columnComment */
    private String structureDetails;

    /** $column.columnComment */
    private String otherContent;

    /** $column.columnComment */
    private String approvalDate;

    /** $column.columnComment */
    private String registrantAddress;

    /** $column.columnComment */
    private String certEffectiveStart;

    /** $column.columnComment */
    private String certEffectiveEnd;

    /** $column.columnComment */
    private String manufacturerCode;

    /** $column.columnComment */
    private String manufacturerName;

    /** $column.columnComment */
    private String productionAddress;

    /** $column.columnComment */
    private String agentCompany;

    /** $column.columnComment */
    private String agentAddress;

    /** $column.columnComment */
    private String productionCountry;

    /** $column.columnComment */
    private String serviceAgency;

    /** $column.columnComment */
    private String certArchivePath;

    /** $column.columnComment */
    private String productImages;

    /** 有效标志(true:有效 false:无效) */
    private String validFlag;

    /** $column.columnComment */
    private String uniqueRecordId;

    /** $column.columnComment */
    private String versionNumber;

    /** $column.columnComment */
    private String versionName;

}