package com.openhis.web.reportmanage.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 出库明细账分页列表 dto
 *
 * @author yuanzs
 * @date 2025-04-21
 */
@Data
@Accessors(chain = true)
public class OutboundReportPageDto {

    /** 单据号 */
    private String supplyBusno;

    /** 药品名称 */
    private String name;

    /** 物品编码（编码） */
    private String busNo;

    /** 批次号 */
    private String lotNumber;

    /** 源仓库id(存放仓库) */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sourceLocationId;

    /** 源仓库名称(存放仓库) */
    private String sourceLocationName;

    /** 源货位id(存放仓库) */
    private Long sourceLocationStoreId;

    /** 源货位名称(存放仓库) */
    private String sourceLocationStoreName;

    /** 目的仓库id(领用科室) */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long purposeLocationId;

    /** 目的仓库名称(领用科室) */
    private String purposeLocationName;

    /** 计量单位 */
    @Dict(dictCode = "unit_code")
    private String unitCode;
    private String unitCode_dictText;

    /** 出库数量 */
    private BigDecimal itemQuantity;

    /** 采购单价 */
    private BigDecimal purchasePrice;

    /** 领用单价 */
    private BigDecimal price;

    /** 金额 */
    private BigDecimal totalPrice;

    /** 供应商 */
    @Dict(dictCode = "id", dictText = "name", dictTable = "adm_supplier")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long supplierId;
    private String supplierId_dictText;

    /** 审核人 */
    @Dict(dictCode = "id", dictText = "name", dictTable = "adm_practitioner")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long approverId;
    private String approverId_dictText;

    /** 审批日期 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date approvalTime;

    /** 备注 */
    private String remake;

}
