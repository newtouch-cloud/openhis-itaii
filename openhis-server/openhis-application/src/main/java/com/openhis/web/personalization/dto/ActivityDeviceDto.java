/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.personalization.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 耗材诊疗绑定 dto
 *
 * @author zwh
 * @date 2025-04-09
 */
@Data
@Accessors(chain = true)
public class ActivityDeviceDto {

    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 范围 */
    @Dict(dictCode = "use_range")
    private String rangeCode;
    private String rangeCode_dictText;

    /** 类型 */
    @Dict(dictCode = "bind_type")
    private String typeCode;
    private String typeCode_dictText;

    /** 诊疗/用法/号源的编码(id) */
    private String itemNo;

    /** 耗材/诊疗id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long devActId;

    /**
     * 耗材/诊疗表名
     */
    private String devActTable;

    /** 耗材名称 */
    private String deviceName;

    /** 耗材五笔码 */
    private String devicePyStr;

    /** 耗材拼音码 */
    private String deviceWbStr;

    /** 诊疗名称 */
    private String activityName;

    /** 诊疗拼音码 */
    private String activityPyStr;

    /** 诊疗五笔码 */
    private String activityWbStr;

    /** 耗材数量 */
    private Integer quantity;

    /** 状态 */
    private Integer statusEnum;
    private String statusEnum_enumText;
}
