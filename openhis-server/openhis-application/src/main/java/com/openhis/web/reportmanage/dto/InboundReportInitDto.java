/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.dto;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 入库明细报表初始化 dto
 *
 * @author ym
 * @date 2025-05-23
 */
@Data
@Accessors(chain = true)
public class InboundReportInitDto {

    /**
     * 供应商
     */
    private List<InboundReportInitDto.supplierListOption> supplierListOptions;

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
