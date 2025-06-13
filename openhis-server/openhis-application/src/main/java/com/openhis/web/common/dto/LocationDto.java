package com.openhis.web.common.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 位置 dto
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@Accessors(chain = true)
public class LocationDto {

    /**
     * ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 编码
     */
    private String busNo;

    /**
     * 名称
     */
    private String name;

    /**
     * 状态编码
     */
    private Integer statusEnum;

    /**
     * 操作状态
     */
    private Integer operationalEnum;

    /**
     * 模式编码
     */
    private Integer modeEnum;

    /**
     * 功能编码
     */
    private String typeJson;

    /**
     * 拼音码
     */
    private String pyStr;

    /**
     * 五笔码
     */
    private String wbStr;

    /**
     * 物理形式枚举
     */
    private Integer formEnum;

    /**
     * 机构编码
     */
    private Long organizationId;

    /**
     * 显示顺序
     */
    private Integer displayOrder;

}
