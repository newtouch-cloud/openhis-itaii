/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 位置信息新增编辑dto
 *
 * @author zwh
 * @date 2025-03-31
 */
@Data
@Accessors(chain = true)
public class LocationAddOrEditDto {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 编码 */
    private String busNo;

    /** 名称 */
    @NotNull
    private String name;

    /** 物理形式枚举 */
    @NotNull
    private String formEnum;

    /** 机构编码 */
    private Long organizationId;
}
