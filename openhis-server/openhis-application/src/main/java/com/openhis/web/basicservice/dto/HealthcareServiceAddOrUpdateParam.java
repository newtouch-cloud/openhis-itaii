package com.openhis.web.basicservice.dto;

import lombok.Data;

/**
 * 服务项目管理 新增修改参数类
 *
 * @author system
 * @date 2025-02-20
 */
@Data
public class HealthcareServiceAddOrUpdateParam {
    /**
     * 服务管理
     */
    private HealthcareServiceFormData healthcareServiceFormData;

    /**
     * 费用定价
     */
    private ChargeItemDefinitionFormData chargeItemDefinitionFormData;

}