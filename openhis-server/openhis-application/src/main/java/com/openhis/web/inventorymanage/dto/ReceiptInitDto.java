/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 单据初始化 dto
 *
 * @author zwh
 * @date 2025-03-19
 */
@Data
@Accessors(chain = true)
public class ReceiptInitDto {

    /**
     * 单据类型
     */
    private List<ReceiptInitDto.supplyTypeOption> supplyTypeOptions;

    /**
     * 审批状态
     */
    private List<ReceiptInitDto.supplyStatusOption> supplyStatusOptions;

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

    /**
     * 审批状态
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
}
