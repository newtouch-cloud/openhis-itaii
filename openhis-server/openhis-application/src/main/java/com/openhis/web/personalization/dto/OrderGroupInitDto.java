/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.personalization.dto;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 组套初始化 dto
 *
 * @author yangmo
 * @date 2025-04-10
 */
@Data
@Accessors(chain = true)
public class OrderGroupInitDto {

    /**
     * 类型
     */
    private List<OrderGroupInitDto.typeOption> typeOptions;

    /**
     * 类型
     */
    @Data
    public static class typeOption {
        private Integer value;
        private String info;

        public typeOption(Integer value, String info) {
            this.value = value;
            this.info = info;
        }
    }

}
