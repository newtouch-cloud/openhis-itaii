/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.outpatientmanage.dto;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 门诊输液初始化dto
 *
 * @author zwh
 * @date 2025-05-19
 */
@Data
@Accessors(chain = true)
public class OutpatientInfusionInitDto {

    /** 执行状态 */
    private List<OutpatientInfusionInitDto.ServiceStatus> serviceStatusOptions;

    /** 执行状态 */
    @Data
    public static class ServiceStatus {
        private Integer value;
        private String label;

        public ServiceStatus(Integer value, String label) {
            this.value = value;
            this.label = label;
        }
    }
}
