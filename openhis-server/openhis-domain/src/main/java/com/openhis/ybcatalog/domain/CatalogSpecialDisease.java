package com.openhis.ybcatalog.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 特慢病目录Entity实体
 *
 * @author system
 * @date 2025-04-09
 */
@Data
@TableName("yb_catalog_special_disease")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CatalogSpecialDisease {
    /** 门慢门特病种目录代码 */
    @TableId(type = IdType.ASSIGN_ID)
    private String diseaseCatalogCode;

    /** 门慢门特病种大类名称 */
    private String diseaseCategoryName;

    /** 门慢门特病种细分类名称 */
    private String diseaseSubcategoryName;

    /** 医保区划 */
    private String medicalInsuranceArea;

    /** 备注 */
    private String remarks;

    /** 有效标志 */
    private String validFlag;

    /** 唯一记录号 */
    private String uniqueRecordId;

    /** 数据创建时间 */
    private String createTime;

    /** 数据更新时间 */
    private String updateTime;

    /** 版本号 */
    private String versionNumber;

    /** 病种内涵 */
    private String diseaseConnotation;

    /** 版本名称 */
    private String versionName;

    /** 诊疗指南页码 */
    private String treatmentGuidePage;

    /** 诊疗指南电子档案 */
    private String treatmentGuideFile;

    /** 门慢门特病种名称 */
    private String diseaseName;

    /** 门慢门特病种大类代码 */
    private String diseaseCategoryCode;
}