package com.openhis.administration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.HealthcareService;

/**
 * 服务项目管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IHealthcareServiceService extends IService<HealthcareService> {

    HealthcareService addHealthcareService(HealthcareService healthcareService);

}