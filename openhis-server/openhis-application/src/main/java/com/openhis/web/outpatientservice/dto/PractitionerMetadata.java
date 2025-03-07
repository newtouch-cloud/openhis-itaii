package com.openhis.web.outpatientservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 参与者 元数据
 */
@Data
@Accessors(chain = true)
public class PractitionerMetadata {
    /**
     * ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 姓名 */
    private String name;

    /**
     * 性别编码
     */
    private Integer genderEnum;
    private String genderEnum_enumText;

    /** 拼音码 */
    private String pyStr;

    /** 五笔码 */
    private String wbStr;

}
