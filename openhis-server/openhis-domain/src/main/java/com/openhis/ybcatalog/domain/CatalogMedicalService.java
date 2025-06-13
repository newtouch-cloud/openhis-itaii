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
 * 医疗服务项目Entity实体
 *
 * @author system
 * @date 2025-04-09
 */
@Data
@TableName("yb_catalog_medical_service")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CatalogMedicalService {

    /** 医疗目录编码 */
    @TableId(type = IdType.ASSIGN_ID)
    private String medicalCatalogCode;

    /** 计价单位 */
    private String billingUnit;

    /** $column.columnComment */
    private String billingUnitName;

    /** $column.columnComment */
    private String medicalItemDesc;

    /** $column.columnComment */
    private String exclusionContent;

    /** $column.columnComment */
    private String medicalItemConnotation;

    /** 有效标志(true:有效 false:无效) */
    private String validFlag;

    /** $column.columnComment */
    private String remarks;

    /** $column.columnComment */
    private String serviceCategory;

    /** $column.columnComment */
    private String medicalServiceName;

    /** $column.columnComment */
    private String projectDescription;

    /** $column.columnComment */
    private String startDate;

    /** $column.columnComment */
    private String endDate;

    /** 唯一记录号(UUID) */
    private String uniqueRecordId;

    /** $column.columnComment */
    private String versionNumber;

    /** $column.columnComment */
    private String versionName;

    /** $column.columnComment */
    private String issuanceFlag;

    /** $column.columnComment */
    private String transferDataId;

    /** $column.columnComment */
    private String effectiveTime;

    /** $column.columnComment */
    private String expiryTime;

}