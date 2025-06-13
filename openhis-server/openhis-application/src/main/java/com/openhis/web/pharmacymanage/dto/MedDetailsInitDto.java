package com.openhis.web.pharmacymanage.dto;

import com.openhis.administration.domain.Practitioner;
import com.openhis.web.datadictionary.dto.DiagnosisTreatmentInitDto;
import com.openhis.web.datadictionary.dto.SupplierInitDto;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.util.List;

import java.util.List;

/**
 * 发药明细初始化信息
 *
 * @author yuanzs
 * @date 2025-04-14
 */
@Data
@Accessors(chain = true)
public class MedDetailsInitDto {

    /** 发药人 */
    private List<Practitioner> practitionerList;

    /** 结算状态 */
     private List<statusEnumOption> statusSettlementOptions;

    /** 出院状态 */
    private List<statusEnumOption> statusDischargeOptions;

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
}
