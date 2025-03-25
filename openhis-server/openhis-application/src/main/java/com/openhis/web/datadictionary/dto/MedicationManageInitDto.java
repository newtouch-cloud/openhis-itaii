package com.openhis.web.datadictionary.dto;

import java.util.ArrayList;
import java.util.List;

import com.openhis.web.inventorymanage.dto.PurchaseInventoryInitDto;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 药品目录初始dto
 *
 * @author lpt
 * @date 2025-02-25
 */
@Data
@Accessors(chain = true)
public class MedicationManageInitDto {
    private List<statusEnumOption> statusFlagOptions;
    private List<domainEnumOption> domainFlagOptions;
    //供应商
    private List<MedicationManageInitDto.supplierListOption> supplierListOptions;
    //药品类型
    private List<MedicationManageInitDto.dictCategoryCode> medicationCategoryCodeOptions;
    //单位编码
    private List<MedicationManageInitDto.dictCategoryCode> unitCodeOptions;

    /**
     * 状态
     */
    @Data
    public static class statusEnumOption {
        private Integer value;
        private String info;

        public statusEnumOption(Integer value, String info) {
            this.value = value;
            this.info = info;
        }
    }

    /**
     * 适用范围
     */
    @Data
    public static class domainEnumOption {
        private Integer value;
        private String info;

        public domainEnumOption(Integer value, String info) {
            this.value = value;
            this.info = info;
        }
    }

    /**
     * 供应商
     */
    @Data
    public static class supplierListOption {
        private Long value;
        private String label;

        public supplierListOption(Long value, String label) {
            this.value = value;
            this.label = label;
        }
    }

    /**
     * 药品类型
     */
    @Data
    public static class dictCategoryCode {
        private String value;
        private String info;

        public dictCategoryCode(String value, String info) {
            this.value = value;
            this.info = info;
        }
    }

}
