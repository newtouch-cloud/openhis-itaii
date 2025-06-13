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
 * 手术标准目录Entity实体
 *
 * @author system
 * @date 2025-04-09
 */
@Data
@TableName("yb_catalog_surgery_standard_directory")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CatalogSurgeryStandardDirectory {

    /** $column.columnComment */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /** $column.columnComment */
    private String chapter;

    /** $column.columnComment */
    private String chapterCodeRange;

    /** $column.columnComment */
    private String chapterName;

    /** $column.columnComment */
    private String categoryCode;

    /** $column.columnComment */
    private String categoryName;

    /** $column.columnComment */
    private String subcategoryCode;

    /** $column.columnComment */
    private String subcategoryName;

    /** $column.columnComment */
    private String itemCode;

    /** $column.columnComment */
    private String itemName;

    /** $column.columnComment */
    private String operationCode;

    /** $column.columnComment */
    private String operationName;

    /** $column.columnComment */
    private String usageFlag;

    /** $column.columnComment */
    private String groupStandardOperationCode;

    /** $column.columnComment */
    private String groupStandardOperationName;

    /** $column.columnComment */
    private String clinicalVersionOperationCode;

    /** $column.columnComment */
    private String clinicalVersionOperationName;

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