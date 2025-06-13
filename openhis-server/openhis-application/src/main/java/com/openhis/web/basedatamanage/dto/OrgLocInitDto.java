/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.dto;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 科室位置关系初始化 dto
 *
 * @author
 * @date 2025-02-25
 */
@Data
@Accessors(chain = true)
public class OrgLocInitDto {

    /**
     * 收费状态
     */
    private List<OrgLocInitDto.locationFormOption> locationFormOptions;

    /** 科室列表 */
    private List<OrgLocInitDto.departmentOption> departmentOptions;

    /** 科室列表 */
    private List<OrgLocInitDto.locationOption> locationOptions;

    /** 位置列表 */
    @Data
    public static class locationOption {

        @JsonSerialize(using = ToStringSerializer.class)
        private Long value;
        private String label;

        public locationOption(Long value, String label) {
            this.value = value;
            this.label = label;
        }
    }

    /** 科室列表 */
    @Data
    public static class departmentOption {

        @JsonSerialize(using = ToStringSerializer.class)
        private Long value;
        private String label;

        public departmentOption(Long value, String label) {
            this.value = value;
            this.label = label;
        }
    }

    /**
     * 收费状态
     */
    @Data
    public static class locationFormOption {
        private Integer value;
        private String label;

        public locationFormOption(Integer value, String label) {
            this.value = value;
            this.label = label;
        }
    }
}
