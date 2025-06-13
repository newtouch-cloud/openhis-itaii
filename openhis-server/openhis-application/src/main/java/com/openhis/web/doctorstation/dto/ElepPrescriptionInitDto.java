package com.openhis.web.doctorstation.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.openhis.web.basedatamanage.dto.LocationDto;
import com.openhis.web.inventorymanage.dto.ProductStocktakingInitDto;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 电子处方下拉框
 */
@Data
@Accessors(chain = true)
public class ElepPrescriptionInitDto {
    /**
     * 处方类别
     */
    private List<ElepPrescriptionInitDto.commonStatusOption> rxTypeCodeListOptions;


    @Data
    public static class commonStatusOption {
        private String value;
        private String label;

        public commonStatusOption(String value, String label) {
            this.value = value;
            this.label = label;
        }
    }


}
