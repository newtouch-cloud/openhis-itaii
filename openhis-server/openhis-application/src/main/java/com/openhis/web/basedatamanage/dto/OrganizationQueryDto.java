/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.dto;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.enums.OrganizationClass;
import com.openhis.common.enums.OrganizationType;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author
 * @date 2025-02-21
 */
@Data
@Accessors(chain = true)
public class OrganizationQueryDto {

    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 编码 */
    private String busNo;

    /** 名称 */
    private String name;

    /** 活动标识 */
    private Integer activeFlag;

    /** 机构类型枚举 */
    private OrganizationType typeEnum;

    /** 机构分类枚举 */
    private OrganizationClass classEnum;

    /** 拼音码 */
    private String pyStr;

    /** 五笔码 */
    private String wbStr;

    /** 医保码 */
    private String ybNo;

    /** 医保名称 */
    private String ybName;

    /** 显示顺序 */
    private Integer displayOrder;

    /** 子集合 */
    private List<OrganizationQueryDto> children = new ArrayList<>();
}
