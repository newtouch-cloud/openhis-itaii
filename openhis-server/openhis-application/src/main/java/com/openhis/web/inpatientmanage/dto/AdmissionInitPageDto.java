package com.openhis.web.inpatientmanage.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 住院登记初始化页面信息
 *
 * @author liuhr
 * @since 2025/04/07
 */
@Data
@Accessors(chain = true)
public class AdmissionInitPageDto {

    //获取入院类型列表
    private List<statusEnumOption> admissionTypeList;
    //获取入院方式列表
    private List<statusEnumOption> admissionMethodList;
    //优先级编码列表
    private List<statusEnumOption> priorityEnumList;


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
