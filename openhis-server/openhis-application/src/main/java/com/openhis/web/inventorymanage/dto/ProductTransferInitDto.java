/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import com.openhis.web.basedatamanage.dto.LocationQueryDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 商品调拨初始化 dto
 *
 * @author my
 * @date 2025-03-20
 */
@Data
@Accessors(chain = true)
public class ProductTransferInitDto {

    /**
     * 单据号
     */
    private String busNo;

    /**
     * 源仓库
     */
    private List<LocationQueryDto> sourceTypeListOptions;

    /**
     * 目的仓库
     */
    private List<LocationQueryDto> purposeTypeListOptions;

    /**
     * 药品类型
     */
    private List<ProductTransferInitDto.categoryListOption> categoryListOptions;

    /**
     * 审批状态
     */
    private List<ProductTransferInitDto.supplyStatusOption> supplyStatusOptions;

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
}
