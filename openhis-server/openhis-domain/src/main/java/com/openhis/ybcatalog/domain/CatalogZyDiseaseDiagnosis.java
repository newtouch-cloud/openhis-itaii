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
 * 中医疾病诊断Entity实体
 *
 * @author system
 * @date 2025-04-09
 */
@Data
@TableName("yb_catalog_zy_disease_diagnosis")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CatalogZyDiseaseDiagnosis {

    /** $column.columnComment */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /** $column.columnComment */
    private String categoryCode;

    /** $column.columnComment */
    private String categoryName;

    /** $column.columnComment */
    private String specialtySystemCategoryCode;

    /** $column.columnComment */
    private String specialtySystemCategoryName;

    /** $column.columnComment */
    private String diseaseCategoryCode;

    /** $column.columnComment */
    private String diseaseCategoryName;

    /** $column.columnComment */
    private String remarks;

    /** $column.columnComment */
    private String validFlag;

    /** $column.columnComment */
    private String uniqueRecordId;

    /** $column.columnComment */
    private String versionNumber;

    /** $column.columnComment */
    private String versionName;

}