package com.openhis.web.datadictionary.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 器材目录初始dto
 *
 * @author lpt
 * @date 2025-02-25
 */
@Data
@Accessors(chain = true)
public class DeviceManageInitDto {
    private List<statusEnumOption> statusFlagOptions;
    private List<dictCategoryCode> deviceCategories;
    private List<exeOrganization> exeOrganizations;
    // 供应商
    private List<supplierListOption> supplierListOptions;
    // 是/否 状态
    private List<statusEnumOption> statusYBWeatherOptions;

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
     * 器材分类
     */
    @Data
    public static class deviceCategory {
        private Integer value;
        private String info;
        List<deviceCategory> children = new ArrayList<>();

        public deviceCategory(Integer value, String info) {
            this.value = value;
            this.info = info;
        }
    }

    /**
     * 器材分类
     */
    @Data
    public static class dictCategoryCode {
        private String value;
        private String info;
        private List<dictCategoryCode> children = new ArrayList<>();

        public dictCategoryCode(String value, String info) {
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

}
