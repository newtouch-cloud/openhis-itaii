/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.datadictionary.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

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
    @NotBlank(message = "编号不能为空")
    private String busNo;

    /** 名称 */
    @NotBlank(message = "名称不能为空")
    private String name;

    /** 类型 */
    @NotNull(message = "类型不能为空")
    private Integer typeEnum;

    /** 地址 */
    @NotBlank(message = "地址不能为空")
    private String address;

    /** 拼音码 */
    @NotBlank(message = "拼音码不能为空")
    private String pyStr;

    /** 五笔码 */
    @NotBlank(message = "五笔码不能为空")
    private String wbStr;

    /** 联系人电话 */
    @NotBlank(message = "联系人电话不能为空")
    private String phone;

    /** 联系人邮箱 */
    @NotBlank(message = "联系人邮箱不能为空")
    private String email;

    /** 活动标识 */
    private Integer activeFlag;

    /** 机构编号 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orgId;
}
