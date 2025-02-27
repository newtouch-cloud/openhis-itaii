package com.openhis.web.datadictionary.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 疾病目录初始dto
 *
 * @author lpt
 * @date 2025-02-25
 */
@Data
@Accessors(chain = true)
public class DiseaseManageInitDto {
    private List<statusEnumOption> statusFlagOptions;
    private List<diseaseCategory> diseaseCategoryList;

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

    /**
     * 疾病分类
     */
    @Data
    public static class diseaseCategory {
        private Integer value;
        private String info;
        List<diseaseCategory> children = new ArrayList<>();

        public diseaseCategory(Integer value, String info) {
            this.value = value;
            this.info = info;
        }
    }
}
