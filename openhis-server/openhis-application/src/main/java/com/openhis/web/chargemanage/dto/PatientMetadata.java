package com.openhis.web.chargemanage.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 患者信息 元数据
 */
@Data
@Accessors(chain = true)
public class PatientMetadata {
    /**
     * ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
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

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 电话
     */
    private String phone;

    /**
     * 生日
     */

    private Date birthDate;

    /**
     * 年龄
     */
    private String age;

    /**
     * 初复诊
     */
    private String firstEnum_enumText;

}
