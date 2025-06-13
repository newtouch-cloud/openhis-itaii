/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

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
public class PurchaseInventoryInitDto {

    /**
     * 单据号
     */
    private String busNo;

    /**
     * 供应商
     */
    private List<PurchaseInventoryInitDto.supplierListOption> supplierListOptions;

    /**
     * 经手人
     */
    private List<PurchaseInventoryInitDto.practitionerListOption> practitionerListOptions;

    /**
     * 审批状态
     */
    private List<PurchaseInventoryInitDto.supplyStatusOption> supplyStatusOptions;

    /**
     * 单据类型
     */
    private List<PurchaseInventoryInitDto.supplyTypeOption> supplyTypeOptions;

    /**
     * 供应商
     */
    @Data
    public static class supplierListOption {
        @JsonSerialize(using = ToStringSerializer.class)
        private Long value;
        private String label;

        public supplierListOption(Long value, String label) {
            this.value = value;
            this.label = label;
        }
    }

    /**
     * 经手人
     */
    @Data
    public static class practitionerListOption {
        @JsonSerialize(using = ToStringSerializer.class)
        private Long value;
        private String label;

        public practitionerListOption(Long value, String label) {
            this.value = value;
            this.label = label;
        }
    }

    /**
     * 入库项目类型
     */
    @Data
    public static class itemTypeOption {
        private Integer value;
        private String label;

        public itemTypeOption(Integer value, String label) {
            this.value = value;
            this.label = label;
        }
    }

    /**
     * 单据状态
     */
    @Data
    public static class supplyStatusOption {
        private Integer value;
        private String label;

        public supplyStatusOption(Integer value, String label) {
            this.value = value;
            this.label = label;
        }
    }

    /**
     * 单据类型
     */
    @Data
    public static class supplyTypeOption {
        private Integer value;
        private String label;

        public supplyTypeOption(Integer value, String label) {
            this.value = value;
            this.label = label;
        }
    }
}
