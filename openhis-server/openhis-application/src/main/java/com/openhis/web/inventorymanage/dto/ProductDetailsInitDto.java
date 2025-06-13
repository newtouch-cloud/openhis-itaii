package com.openhis.web.inventorymanage.dto;

import com.openhis.web.datadictionary.dto.MedicationManageInitDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 库存商品明细初始dto
 *
 * @author yuanzs
 * @date 2025-05-06
 */
@Data
@Accessors(chain = true)
public class ProductDetailsInitDto {

    /** 医保等级 */
    private List<statusEnumOption> chrgitmLvOptions;

    /** 状态 */
    @Data
    public static class statusEnumOption {
        private Integer value;
        private String info;

        public statusEnumOption(Integer value, String info) {
            this.value = value;
            this.info = info;
        }
    }

}
