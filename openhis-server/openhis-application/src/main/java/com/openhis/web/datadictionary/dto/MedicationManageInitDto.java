package com.openhis.web.datadictionary.dto;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 药品目录初始dto
 *
 * @author lpt
 * @date 2025-02-25
 */
@Data
@Accessors(chain = true)
public class MedicationManageInitDto {
    private List<statusEnumOption> statusFlagOptions;

    /**
     * 状态
     */
    @Data
    public static class statusEnumOption {
        private Integer value;
        private String info;

        public statusEnumOption(Integer value, String info) {
            this.value = value;
            this.info = info;
        }
    }
}
