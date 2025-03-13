package com.openhis.web.chargemanage.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 门诊挂号 init基础数据
 */
@Data
@Accessors(chain = true)
public class OutpatientRegistrationInitDto {

    private List<priorityLevelOption> priorityLevelOptionOptions;

    /**
     * 优先级
     */
    @Data
    public static class priorityLevelOption {
        private Integer value;
        private String label;

        public priorityLevelOption(Integer value, String label) {
            this.value = value;
            this.label = label;
        }
    }


}
