package com.openhis.web.doctorstation.dto;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 患者过敏不耐受初期数据Dto
 *
 * @author liuhr
 * @date 2025/4/10
 */
@Data
@Accessors(chain = true)
public class AllergyIntoInitDto {

    //临床状况列表
    private List<statusEnumOption> clinicalStatusOptions;
    //验证状态列表
    private List<statusEnumOption> verificationStatusOptions;
    //危险程度列表
    private List<statusEnumOption> criticalityOptions;
    //严重程度列表
    private List<statusEnumOption> severityOptions;

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
