/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.web.basedatamanage.dto.LocationDto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 报损单初始化 dto
 *
 * @author gyy
 * @date 2025-04-03
 */
@Data
@Accessors(chain = true)
public class LossReportFormInitDto {

    /**
     * 单据号
     */
    private String busNo;

    /**
     * 报损仓库
     */
    private List<LocationDto> WarehouseTypeList;

    /**
     * 药品类型
     */
    private List<LossReportFormInitDto.categoryListOption> categoryListOptions;

    /**
     * 审批状态
     */
    private List<LossReportFormInitDto.supplyStatusOption> supplyStatusOptions;

    /**
     * 制单人
     */
    private List<LossReportFormInitDto.applicantListOption> applicantListOptions;

    /**
     * 药品类型
     */
    @Data
    public static class categoryListOption {
        private Integer value;
        private String label;

        public categoryListOption(Integer value, String label) {
            this.value = value;
            this.label = label;
        }
    }

    /**
     * 审批状态
     */
    @Data
    public static class supplyStatusOption {
        private Integer value;
        private String label;

        public supplyStatusOption(Integer value, String label) {
            this.value = value;
            this.label = label;
        }
    }

    /**
     * 制单人
     */
    @Data
    public static class applicantListOption {
        private Long value;
        private String label;

        public applicantListOption(Long value, String label) {
            this.value = value;
            this.label = label;
        }
    }

}
