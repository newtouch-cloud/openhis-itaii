/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 采购退货 dto
 *
 * @author yuanzs
 * @date 2025-04-02
 */
@Data
@Accessors(chain = true)
public class PurchaseReturnInitDto {

    /**
     * 经手人
     */
    private List<PurchaseInventoryInitDto.practitionerListOption> practitionerListOptions;

    /**
     * 经手人
     */
    @Data
    public static class practitionerListOption {
        private Long value;
        private String label;

        public practitionerListOption(Long value, String label) {
            this.value = value;
            this.label = label;
        }
    }
}
