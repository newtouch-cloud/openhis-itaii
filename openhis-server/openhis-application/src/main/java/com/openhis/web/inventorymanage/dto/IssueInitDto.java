/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 出库初始化 dto
 *
 * @author CY
 * @date 2025-04-03
 */
@Data
@Accessors(chain = true)
public class IssueInitDto {

    /**
     * 单据号
     */
    private String busNo;

    /**
     * 供应商
     */
    private List<IssueInitDto.supplierListOption> supplierListOptions;

    /**
     * 经手人
     */
    private List<IssueInitDto.practitionerListOption> practitionerListOptions;

    /**
     * 部门列表
     */
    private List<IssueDepartmentDto> issueDepartmentDto;

    /**
     * 项目类型
     */
    private List<IssueInitDto.itemTypeOption> itemTypeOptions;

    /**
     * 审批状态
     */
    private List<IssueInitDto.supplyStatusOption> supplyStatusOptions;

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

    /**
     * 经手人
     */
    @Data
    public static class practitionerListOption {
        @JsonSerialize(using = ToStringSerializer.class)
        private Long value;
        private String label;

        public practitionerListOption(Long value, String label) {
            this.value = value;
            this.label = label;
        }
    }

    /**
     * 项目类型
     */
    @Data
    public static class itemTypeOption {
        private Integer value;
        private String label;

        public itemTypeOption(Integer value, String label) {
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
}
