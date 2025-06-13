package com.openhis.web.outpatientmanage.dto;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 门诊处置
 *
 * @author yuxj
 * @date 2025/4/10
 */
@Data
@Accessors(chain = true)
public class OutpatientDisposalInitDto {

    /** 科室列表 */
    private List<OutpatientDisposalInitDto.DepartmentOption> departmentOptions;

    /** 科室列表 */
    @Data
    public static class DepartmentOption {

        @JsonSerialize(using = ToStringSerializer.class)
        private Long value;
        private String label;

        public DepartmentOption(Long value, String label) {
            this.value = value;
            this.label = label;
        }
    }


}
