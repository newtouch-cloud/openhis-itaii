/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventoryManage.dto;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.openhis.administration.domain.Patient;
import com.openhis.medication.domain.Medication;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 入库单据分页列表 dto
 *
 * @author zwh
 * @date 2025-02-18
 */
@Data
@Accessors(chain = true)
public class InventoryReceiptDto implements Serializable {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 类型 */
    private Integer typeEnum;

    /** 状态 */
    private Integer statusEnum;

    /** 意图 */
    private Integer intentEnum;

    /** 分类 */
    private Integer categoryEnum;

    /** 优先权 */
    private Integer priorityEnum;

    /** 汇总编号 */
    private Long summaryId;

    /** 交付 */
    private Long deliverId;

    /** 患者id */
    private Long patientId;

    /** 发放实体表名 */
    private String dispenseTable;

    /** 发放id */
    private Long dispenseId;

    /** 期望时间 */
    private Date occurrenceTime;

    /** 供应人 */
    private Long practitionerId;

    /** 供应商 */
    private Long supplierId;

    /** 理由 */
    private String reason;

    /** 源仓库类型 */
    private Integer sourceType;

    /** 源仓库 */
    private Long sourceLocationId;

    /** 源仓位 */
    private Long sourceLocationStoreId;

    /** 目的类型 */
    private Integer purposeType;

    /** 药品基本信息管理Entity实体 */
    private Medication medication;

    /** 患者管理Entity实体 */
    private Patient patient;

}
