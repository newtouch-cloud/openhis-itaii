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
 * 西医疾病诊断信息Entity实体
 *
 * @author system
 * @date 2025-04-09
 */
@Data
@TableName("yb_catalog_western_disease")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CatalogWesternDisease {

    /** $column.columnComment */
    @TableId(type = IdType.ASSIGN_ID)
    private String diseaseId;

    /** $column.columnComment */
    private String chapter;

    /** $column.columnComment */
    private String chapterCodeRange;

    /** $column.columnComment */
    private String chapterName;

    /** $column.columnComment */
    private String sectionCodeRange;

    /** $column.columnComment */
    private String sectionName;

    /** $column.columnComment */
    private String categoryCode;

    /** $column.columnComment */
    private String categoryName;

    /** $column.columnComment */
    private String subcategoryCode;

    /** $column.columnComment */
    private String subcategoryName;

    /** 诊断代码 */
    private String diagnosisCode;

    /** $column.columnComment */
    private String diagnosisName;

    /** 使用标记(true:启用 false:停用) */
    private String usageFlag;

    /** $column.columnComment */
    private String gbDiagnosisCode;

    /** $column.columnComment */
    private String gbDiagnosisName;

    /** $column.columnComment */
    private String clinicalCode;

    /** $column.columnComment */
    private String clinicalName;

    /** $column.columnComment */
    private String remarks;

    /** 有效标志(true:有效 false:无效) */
    private String validFlag;

    /** $column.columnComment */
    private String uniqueRecordId;

    /** $column.columnComment */
    private String versionNumber;

    /** $column.columnComment */
    private String versionName;

}