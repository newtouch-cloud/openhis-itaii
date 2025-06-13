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
 * 西药中成药目录Entity实体
 *
 * @author system
 * @date 2025-04-09
 */
@Data
@TableName("yb_catalog_drug_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CatalogDrugInfo {

    /** 医疗目录编码 */
    @TableId(type = IdType.ASSIGN_ID)
    private String medicalCatalogCode;

    /** 药品商品名 */
    private String drugTradeName;

    /** 通用名编号 */
    private String genericNameId;

    /** 药品通用名 */
    private String drugGenericName;

    /** 化学名称 */
    private String chemicalName;

    /** 别名  */
    private String alias;

    /** 英文名称 */
    private String englishName;

    /** 注册名称 */
    private String registeredName;

    /** 药监本位码 */
    private String drugSupervisionCode;

    /** 药品剂型 */
    private String drugForm;

    /** 药品剂型名称 */
    private String drugFormName;

    /** 药品类别 */
    private String drugCategory;

    /** 药品类别名称 */
    private String drugCategoryName;

    /** 药品规格 */
    private String drugSpecification;

    /** 药品规格代码 */
    private String drugSpecCode;

    /** 注册剂型 */
    private String registeredForm;

    /** 注册规格 */
    private String registeredSpec;

    /** 注册规格代码 */
    private String registeredSpecCode;

    /** 每次用量 */
    private String dosage;

    /** 使用频次 */
    private String frequency;

    /** 酸根盐基 */
    private String acidBase;

    /** 国家药品编号 */
    private String nationalDrugCode;

    /** 用法 */
    private String usage;

    /** 中成药标志 */
    private String tcmFlag;

    /** 生产地类别 */
    private String productionAreaType;

    /** 生产地类别名称 */
    private String productionAreaName;

    /** 计价单位类型 */
    private String pricingUnitType;

    /** 非处方药标志 */
    private String otcFlag;

    /** 非处方药标志名称 */
    private String otcFlagName;

    /** 包装材质 */
    private String packagingMaterial;

    /** 包装材质名称 */
    private String packagingMaterialName;

    /** 包装规格 */
    private String packagingSpec;

    /** 包装数量 */
    private String packagingQuantity;

    /** 功能主治 */
    private String functionIndication;

    /** 给药途径 */
    private String administrationRoute;

    /** 说明书 */
    private String instructions;

    /** 开始日期 */
    private String startDate;

    /** 结束日期 */
    private String endDate;

    /** 最小使用单位 */
    private String minUseUnit;

    /** 最小销售单位 */
    private String minSaleUnit;

    /** 最小计量单位 */
    private String minMeasurementUnit;

    /** 最小包装数量 */
    private String minPackageQuantity;

    /** 最小包装单位 */
    private String minPackageUnit;

    /** 最小制剂单位 */
    private String minPreparationUnit;

    /** 最小包装单位名称 */
    private String minPackageUnitName;

    /** 最小制剂单位名称 */
    private String minPreparationUnitName;

    /** 转换比 */
    private String conversionRatio;

    /** 药品有效期 */
    private String shelfLife;

    /** 最小计价单位 */
    private String minPricingUnit;

    /** 五笔助记码 */
    private String wubiCode;

    /** 拼音助记码 */
    private String pinyinCode;

    /** 分包装厂家 */
    private String repackager;

    /** 生产企业编号 */
    private String manufacturerCode;

    /** 生产企业名称 */
    private String manufacturerName;

    /** 特殊限价药品标志 */
    private String specialPriceLimitFlag;

    /** 特殊药品标志 */
    private String specialDrugFlag;

    /** 限制使用范围 */
    private String useRestriction;

    /** 限制使用标志 */
    private String useRestrictionFlag;

    /** 药品注册证号 */
    private String registrationCertNo;

    /** 药品注册证号开始日期 */
    private String regCertStartDate;

    /** 药品注册证号结束日期 */
    private String regCertEndDate;

    /** 批准文号 */
    private String approvalNo;

    /** 批准文号开始日期 */
    private String approvalStartDate;

    /** 批准文号结束日期 */
    private String approvalEndDate;

    /** 市场状态 */
    private String marketStatus;

    /** 市场状态名称 */
    private String marketStatusName;

    /** 药品注册批件电子档案 */
    private String regDocumentArchive;

    /** 药品补充申请批件电子档案 */
    private String suppApplicationArchive;

    /** 国家医保药品目录备注 */
    private String nationalInsuranceNotes;

    /** 基本药物标志名称 */
    private String essentialDrugFlagName;

    /** 基本药物标志 */
    private String essentialDrugFlag;

    /** 增值税调整药品标志 */
    private String vatAdjustmentFlag;

    /** 增值税调整药品名称 */
    private String vatAdjustmentName;

    /** 上市药品目录集药品 */
    private String listedDrugFlag;

    /** 医保谈判药品标志 */
    private String negotiationDrugFlag;

    /** 医保谈判药品名称 */
    private String negotiationDrugName;

    /** 卫健委药品编码 */
    private String nhcDrugCode;

    /** 备注 */
    private String remarks;

    /** 有效标志 */
    private String validFlag;

    /** 唯一记录号  */
    private String uniqueRecordId;

    /** 数据创建时间 */
    private String createdAt;

    /** 数据更新时间 */
    private String updatedAt;

    /** 版本号 */
    private String version;

    /** 版本名称 */
    private String versionName;

    /** 儿童用药 */
    private String pediatricUse;

    /** 公司名称 */
    private String companyName;

    /** 仿制药一致性评价药品 */
    private String genericEvaluationFlag;

    /** 经销企业 */
    private String distributionCompany;

    /** 经销企业联系人 */
    private String distributionContact;

    /** 经销企业授权书电子档案 */
    private String distributionAuthArchive;

    /** 国家医保药品目录剂型 */
    private String insuranceForm;

    /** 国家医保药品目录甲乙类标识 */
    private String insuranceClass;

    /** 上市许可证持有人 */
    private String marketingAuthorizationHolder;

    /** 下发标志 */
    private String releaseFlag;

    /** 传输数据ID */
    private String transmissionDataId;

    /** 生效时间 */
    private String validFrom;

    /** 失效时间 */
    private String validTo;

}