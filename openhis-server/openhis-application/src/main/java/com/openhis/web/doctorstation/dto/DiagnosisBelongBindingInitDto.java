package com.openhis.web.doctorstation.dto;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 诊断归属绑定 init基础数据
 */
@Data
@Accessors(chain = true)
public class DiagnosisBelongBindingInitDto {

    private List<DiagnosisBelongBindingOption> diagnosisBelongBindingOptions;

    /**
     * 诊断绑定类型 - 用于维护诊断归属绑定关系
     */
    @Data
    public static class DiagnosisBelongBindingOption {
        private Integer value;
        private String label;

        public DiagnosisBelongBindingOption(Integer value, String label) {
            this.value = value;
            this.label = label;
        }
    }

}
