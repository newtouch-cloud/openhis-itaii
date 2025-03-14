/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import java.util.List;

import com.openhis.administration.domain.Location;
import com.openhis.administration.domain.Supplier;
import com.openhis.medication.domain.MedicationDetail;

import com.openhis.web.basedatamanage.dto.LocationQueryDto;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 采购入库初始化 dto
 *
 * @author zwh
 * @date 2025-02-20
 */
@Data
@Accessors(chain = true)
public class InventoryReceiptInitDto {

    // 返回前台的应是dto 懒得定义请勿学习

    /** 药房信息 */
    private List<Location> location;

    /** 供应商信息 */
    private List<Supplier> supplier;

    /** 药品详细信息 */
    private List<MedicationDetail> medicationDetail;
}
