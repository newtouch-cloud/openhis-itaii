/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import java.util.List;

import com.openhis.web.basedatamanage.dto.LocationDto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商品入库初始化 dto
 *
 * @author yxj
 * @date 2025-04-3
 */
@Data
@Accessors(chain = true)
public class ProductStocktakingInitDto {

    /**
     * 单据号
     */
    private String busNo;

    /**
     * 审批状态
     */
    private List<ProductStocktakingInitDto.supplyStatusOption> supplyStatusOptions;

    /**
     * 制单人
     */
    private List<ProductStocktakingInitDto.applicantListOption> applicantListOptions;

    /**
     * 药品类型
     */
    private List<ProductStocktakingInitDto.categoryListOption> categoryListOptions;

    /**
     * 仓库
     */
    private List<LocationDto> cabinetListOptions;

    /**
     * 药房
     */
    private List<LocationDto> pharmacyListOptions;

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

    /**
     * 盈亏原因
     */
    @Data
    public static class profitReasonOption {
        private Integer value;
        private String label;

        public profitReasonOption(Integer value, String label) {
            this.value = value;
            this.label = label;
        }
    }

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

}
