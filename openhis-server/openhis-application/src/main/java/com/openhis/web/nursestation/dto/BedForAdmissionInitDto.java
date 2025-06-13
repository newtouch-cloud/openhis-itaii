package com.openhis.web.nursestation.dto;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 待入科初始化Dto
 *
 * @author liuhr
 * @date 2025/4/15
 */
@Data
@Accessors(chain = true)
public class BedForAdmissionInitDto {

    // 入院科室列表
    private List<exeOrganization> locationListOptions;

    //患者病情List
    private List<statusEnumOption> priorityListOptions;

    /**
     * 执行机构
     */
    @Data
    public static class exeOrganization {

        @JsonSerialize(using = ToStringSerializer.class)
        private Long value;
        private String label;

        public exeOrganization(Long value, String label) {
            this.value = value;
            this.label = label;
        }
    }

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
