/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.datadictionary.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 供应商保存dto
 *
 * @author dh
 * @date 2025-02-24
 */
@Data
@Accessors(chain = true)
public class SupplierUpDto {

    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 编号 */
    private String busNo;

    /** 名称 */
    @NotBlank(message = "名称不能为空")
    private String name;

    /** 类型 */
    @NotNull(message = "类型不能为空")
    private Integer typeEnum;

    /** 地址 */
    private String address;

    /** 拼音码 */
    private String pyStr;

    /** 五笔码 */
    private String wbStr;

    /** 联系人电话 */
    private String phone;

    /** 联系人邮箱 */
    private String email;

    /** 活动标识 */
    private Integer activeFlag;

    /** 机构编号 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orgId;
}
