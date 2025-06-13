/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.personalization.dto;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 诊疗耗材绑定初始化
 *
 * @author zwh
 * @date 2025-04-09
 */
@Data
@Accessors(chain = true)
public class ActivityDeviceInitDto {

    private List<ActivityDeviceInitDto.statusOption> statusOptions;

    /**
     * 状态
     */
    @Data
    public static class statusOption {
        private Integer value;
        private String info;

        public statusOption(Integer value, String info) {
            this.value = value;
            this.info = info;
        }
    }
}
