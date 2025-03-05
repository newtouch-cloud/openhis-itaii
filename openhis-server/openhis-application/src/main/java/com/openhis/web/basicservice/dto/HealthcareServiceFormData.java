package com.openhis.web.basicservice.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 服务项目管理表单数据
 *
 * @author system
 * @date 2025-02-20
 */
@Data
public class HealthcareServiceFormData {

    /**
     * ID
     */
    private Long id;

    /**
     * 活动标记
     */
    @NotBlank(message = "活动标记不能为空")
    private Integer activeFlag;

    /**
     * 提供部门ID
     */
    @NotBlank(message = "提供部门ID不能为空")
    private Long offeredOrgId;

    /**
     * 服务分类
     */
    @NotBlank(message = "服务分类不能为空")
    private String categoryCode;

    /**
     * 服务类型
     */
    @NotBlank(message = "服务类型不能为空")
    private String typeCode;

    /**
     * 服务专业
     */
    @NotBlank(message = "服务专业不能为空")
    private String specialtyCode;

    /**
     * 地点
     */
    @NotBlank(message = "地点不能为空")
    private Long locationId;

    /**
     * 服务名称
     */
    @NotBlank(message = "服务名称不能为空")
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
    @NotBlank(message = "联系方式不能为空")
    private String contact;

    /**
     * 预约要求
     */
    @NotBlank(message = "预约要求不能为空")
    private Integer appointmentRequiredFlag;


}