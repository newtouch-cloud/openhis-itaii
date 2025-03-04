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
public class SupplierDto {

    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 编号 */
    private String busNo;

    /** 名称 */
    private String name;

    /** 类型 */
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
    private String activeFlag_enumText;

    /** 机构编号 */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orgId;
}
