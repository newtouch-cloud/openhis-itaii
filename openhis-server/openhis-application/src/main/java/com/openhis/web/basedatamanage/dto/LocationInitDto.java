/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.dto;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 位置信息初始化dto
 *
 * @author zwh
 * @date 2025-03-31
 */
@Data
@Accessors(chain = true)
public class LocationInitDto {

    private List<LocationInitDto.locationStatusOption> locationStatusOptions;

    /**
     * 位置状态
     */
    @Data
    public static class locationStatusOption {
        private Integer value;
        private String info;

        public locationStatusOption(Integer value, String info) {
            this.value = value;
            this.info = info;
        }
    }
}
