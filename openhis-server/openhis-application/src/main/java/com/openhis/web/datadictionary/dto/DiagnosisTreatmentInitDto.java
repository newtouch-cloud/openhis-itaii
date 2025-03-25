package com.openhis.web.datadictionary.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 诊疗目录初期查询
 *
 * @author lpt
 * @date 2025-02-25
 */
@Data
@Accessors(chain = true)
public class DiagnosisTreatmentInitDto {
    private List<statusEnumOption> statusFlagOptions;
    private List<diseaseTreatmentCategory> diseaseTreatmentCategoryList;
    private List<exeOrganization> exeOrganizations;
    private List<statusEnumOption> typeEnumOptions;

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
     * 诊疗分类
     */
    @Data
    public static class diseaseTreatmentCategory {
        private Integer value;
        private String info;
        private List<diseaseTreatmentType> children = new ArrayList<>();

        public diseaseTreatmentCategory(Integer value, String info) {
            this.value = value;
            this.info = info;
        }
    }

    /**
     * 诊疗类型
     */
    @Data
    public static class diseaseTreatmentType {
        private String value;
        private String info;
        private List<diseaseTreatmentType> children = new ArrayList<>();

        public diseaseTreatmentType(String value, String info) {
            this.value = value;
            this.info = info;
        }
    }

    /**
     * 执行机构
     */
    @Data
    public static class exeOrganization {
        private Long value;
        private String label;

        public exeOrganization(Long value, String label) {
            this.value = value;
            this.label = label;
        }
    }
}
