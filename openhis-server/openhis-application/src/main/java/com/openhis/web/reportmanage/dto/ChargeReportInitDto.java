/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.dto;

import com.openhis.administration.domain.Practitioner;
import com.openhis.web.doctorstation.dto.ElepPrescriptionInitDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * 费用明细报表下拉框 dto
 *
 * @author yuxj
 * @date 2025-05-20
 */
@Data
@Accessors(chain = true)
public class ChargeReportInitDto {
    /**
     * 统计类型
     */
    private List<ChargeReportInitDto.commonStatusOption> statisticsTypeOptions;
    /**
     * 科室
     */
    private List<ChargeReportInitDto.longCommonStatusOption>  departmentOptions;

    /**
     * 开单人
     */
    private List<Practitioner>  issuerOptions;
    /**
     * 收款人
     */
    private List<Practitioner>  payeeOptions;

    /**
     * 项目类型
     */
    private List<ChargeReportInitDto.commonStatusOption> clinicalTypeOptions;


    @Data
    public static class commonStatusOption {
        private Integer value;
        private String label;

        public commonStatusOption(Integer value, String label) {
            this.value = value;
            this.label = label;
        }
    }
    @Data
    public static class longCommonStatusOption {
        private Long value;
        private String label;

        public longCommonStatusOption(Long value, String label) {
            this.value = value;
            this.label = label;
        }
    }

}
