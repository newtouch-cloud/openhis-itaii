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
 * 【中医症候】Entity实体
 *
 * @author system
 * @date 2025-04-09
 */
@Data
@TableName("yb_catalog_zy_syndrome")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CatalogZySyndrome {

    /** $column.columnComment */
    @TableId(type = IdType.ASSIGN_ID)
    private String tcmSyndromeId;

    /** $column.columnComment */
    private String syndromeClassCode;

    /** $column.columnComment */
    private String syndromeClassName;

    /** $column.columnComment */
    private String syndromePropertyCode;

    /** $column.columnComment */
    private String syndromeProperty;

    /** $column.columnComment */
    private String syndromeTypeCode;

    /** $column.columnComment */
    private String syndromeTypeName;

    /** $column.columnComment */
    private String activeFlag;

    /** $column.columnComment */
    private String uniqueRecordId;

    /** $column.columnComment */
    private String craetTime;

    /** $column.columnComment */
    private String version;

    /** $column.columnComment */
    private String versionName;

}