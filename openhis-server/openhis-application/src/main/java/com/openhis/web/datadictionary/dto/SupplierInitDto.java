package com.openhis.web.datadictionary.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 厂商/产地初始dto
 *
 * @author dh
 * @date 2025-02-28
 */
@Data
@Accessors(chain = true)
public class SupplierInitDto {
    private List<supplierTypeOption> supplierTypeOptions;

    /**
     * 状态
     */
    @Data
    public static class supplierTypeOption {
        private Integer value;
        private String info;

        public supplierTypeOption(Integer value, String info) {
            this.value = value;
            this.info = info;
        }
    }
}
