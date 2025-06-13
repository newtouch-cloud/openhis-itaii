/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.dto;

import java.util.List;

import com.openhis.administration.domain.Practitioner;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 挂号明细报表下拉框 dto
 *
 * @author yuxj
 * @date 2025-05-20
 */
@Data
@Accessors(chain = true)
public class RegisterReportInitDto {

    /**
     * 科室
     */
    private List<RegisterReportInitDto.longCommonStatusOption>  departmentOptions;

    /**
     * 医生
     */
    private List<Practitioner>  doctorOptions;

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
