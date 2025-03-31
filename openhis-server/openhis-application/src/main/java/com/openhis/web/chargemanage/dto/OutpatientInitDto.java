/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 门诊费用相关初始化
 *
 * @author zwh
 * @date 2025-03-30
 */
@Data
@Accessors(chain = true)
public class OutpatientInitDto {

    private List<OutpatientInitDto.chargeItemStatusOption> chargeItemStatusOptions;

    /**
     * 收费状态
     */
    @Data
    public static class chargeItemStatusOption {
        private Integer value;
        private String label;

        public chargeItemStatusOption(Integer value, String label) {
            this.value = value;
            this.label = label;
        }
    }
}
