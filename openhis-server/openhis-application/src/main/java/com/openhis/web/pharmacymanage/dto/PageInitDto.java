/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.pharmacymanage.dto;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 * @author wangyang
 * @date 2025-03-14
 */
@Data
@Accessors(chain = true)
public class PageInitDto {

    /** 科室列表 */
    private List<DepartmentOption> departmentOptions;

    /** 未发药原因 */
    private List<NotPerformedReasonOption> notPerformedReasonOptions;

    @Data
    public static class DepartmentOption {

        @JsonSerialize(using = ToStringSerializer.class)
        private Long value;
        private String label;

        public DepartmentOption(Long value, String label) {
            this.value = value;
            this.label = label;
        }
    }

    @Data
    public static class NotPerformedReasonOption {

        private Integer value;
        private String label;

        public NotPerformedReasonOption(Integer value, String label) {
            this.value = value;
            this.label = label;
        }
    }
}
