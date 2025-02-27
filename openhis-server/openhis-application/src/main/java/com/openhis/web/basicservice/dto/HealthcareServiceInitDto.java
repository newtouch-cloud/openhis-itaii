package com.openhis.web.basicservice.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 服务管理 基础数据
 */
@Data
@Accessors(chain = true)
public class HealthcareServiceInitDto {


    private List<activeFlagOption> activeFlagOptions;
    //private List<locationIdOption> locationIdOptions;
    private List<appointmentRequiredFlagOption> appointmentRequiredFlagOptions;


    /**
     * 活动标记
     */
    @Data
    public static class activeFlagOption {
        private Integer value;
        private String label;

        public activeFlagOption(Integer value, String label) {
            this.value = value;
            this.label = label;
        }
    }

/*    *//**
     * 位置信息
     *//*
    @Data
    public static class locationIdOption {
        private Long value;
        private String label;

        public locationIdOption(Long value, String label) {
            this.value = value;
            this.label = label;
        }
    }*/

    /**
     * 是否需要预约
     */
    @Data
    public static class appointmentRequiredFlagOption {
        private Integer value;
        private String label;

        public appointmentRequiredFlagOption(Integer value, String label) {
            this.value = value;
            this.label = label;
        }
    }

}
