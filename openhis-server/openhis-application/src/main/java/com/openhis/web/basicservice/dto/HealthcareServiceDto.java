package com.openhis.web.basicservice.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 服务管理 Dto
 */
@Data
@Accessors(chain = true)
public class HealthcareServiceDto {
    /**
     * ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 活动标记
     */
    private Integer activeFlag;
    private String activeFlag_enumText;

    /**
     * 提供部门ID
     */
    @Dict(dictTable = "adm_organization", dictCode = "id", dictText = "name")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long offeredOrgId;
    private String offeredOrgId_dictText;

    /**
     * 服务分类
     */
    @Dict(dictCode = "category_code")
    private String categoryCode;
    private String categoryCode_dictText;

    /**
     * 服务类型
     */
    @Dict(dictCode = "service_type_code")
    private String typeCode;
    private String typeCode_dictText;

    /**
     * 服务专业
     */
    @Dict(dictCode = "specialty_code")
    private String specialtyCode;
    private String specialtyCode_dictText;

    /**
     * 地点
     */
    @Dict(dictTable = "adm_location", dictCode = "id", dictText = "name")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long locationId;
    private String locationId_dictText;

    /**
     * 服务名称
     */
    private String name;

    /**
     * 说明
     */
    private String comment;

    /**
     * 额外细节
     */
    private String extraDetails;

    /**
     * 联系方式
     */
    private String contact;

    /**
     * 预约要求
     */
    private Integer appointmentRequiredFlag;
    private String appointmentRequiredFlag_enumText;

    /**
     * 费用定价ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long definitionId;

    /**
     * 名称
     */
    private String chargeName;

    /**
     * 基础价格
     */
    private BigDecimal price;

    /**
     * 诊疗费
     */
    private BigDecimal activityPrice;

}
