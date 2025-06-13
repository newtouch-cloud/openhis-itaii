/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.pharmacymanage.dto;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import com.openhis.web.inventorymanage.dto.PurchaseInventoryInitDto;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 * @author yangmo
 * @date 2025-04-04
 */
@Data
@Accessors(chain = true)
public class ReturnMedicineInitDto {

    /** 科室列表 */
    private List<ReturnMedicineInitDto.DepartmentOption> departmentOptions;

    /** 退药状态 */
    private List<ReturnMedicineInitDto.RefundStatusOption> refundStatusOptions;

    /**
     * 科室列表
     */
    @Data
    public static class DepartmentOption {

        @JsonSerialize(using = ToStringSerializer.class)
        private Long value;
        private String label;

        public DepartmentOption(Long value, String label) {
            this.value = value;
            this.label = label;
        }
    }

    /**
     * 退药状态
     */
    @Data
    public static class RefundStatusOption {

        private Integer value;
        private String label;

        public RefundStatusOption(Integer value, String label) {
            this.value = value;
            this.label = label;
        }
    }

}
