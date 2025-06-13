/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 位置信息
 *
 * @author zwh
 * @date 2025-03-31
 */
@Data
@Accessors(chain = true)
public class LocationInfoDto {

    /** ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 编码 */
    private String busNo;

    /** 名称 */
    private String name;

    /** 状态编码 */
    private Integer statusEnum;
    private String statusEnum_enumText;

    /** 操作状态 */
    private Integer operationalEnum;
    private String operationalEnum_enumText;

    /** 物理形式枚举 */
    private Integer formEnum;
    private String formEnum_enumText;

    /** 机构编码 */
    @Dict(dictCode = "id",dictText = "name",dictTable = "adm_organization")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long organizationId;
    private String organizationId_dictText;

}
