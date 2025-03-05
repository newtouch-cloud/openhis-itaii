package com.openhis.web.outpatientservice.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 患者信息 元数据
 */
@Data
@Accessors(chain = true)
public class PatientMetadata {
    /**
     * ID
     */
    private Long id;

    /**
     * 患者姓名
     */
    private String name;

    /**
     * 性别编码
     */
    private Integer genderEnum;
    private String genderEnum_enumText;

}
