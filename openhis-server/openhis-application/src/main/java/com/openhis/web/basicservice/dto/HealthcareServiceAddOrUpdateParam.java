package com.openhis.web.basicservice.dto;

import com.openhis.administration.domain.ChargeItemDefinition;
import com.openhis.administration.domain.HealthcareService;
import lombok.Data;

/**
 * 服务项目管理 新增修改参数类
 *
 * @author system
 * @date 2025-02-20
 */
@Data
public class HealthcareServiceAddOrUpdateParam{
    /**
     *  服务管理
     */
    private HealthcareService healthcareServiceFormData;

    /**
     *  费用定价
     */
    private ChargeItemDefinition chargeItemDefinitionFormData;

}