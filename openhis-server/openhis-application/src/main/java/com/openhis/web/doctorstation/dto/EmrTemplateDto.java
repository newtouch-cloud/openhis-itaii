/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.doctorstation.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 病历模板Dto
 *
 * @author ZhangYC
 * @date 2025-02-22
 */

@Data
@Accessors(chain = true)
public class EmrTemplateDto implements Serializable {

    /** 模板名称 */
    @NotBlank
    private String templateName;

    /** 模板类型 */
    private String templateTypeEnum;

    /** 使用范围 */
    @NotBlank
    private String useScopeCode;

    /** 个人/科室ID */
    private Long userId;

    /** 病历内容 */
    private String contextJson;

}
