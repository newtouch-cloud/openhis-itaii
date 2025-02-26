package com.openhis.administration.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.HealthcareService;
import com.openhis.administration.mapper.HealthcareServiceMapper;
import com.openhis.administration.service.IHealthcareServiceService;

/**
 * 服务项目管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class HealthcareServiceServiceImpl extends ServiceImpl<HealthcareServiceMapper, HealthcareService> implements IHealthcareServiceService {

    @Override
    public HealthcareService addHealthcareService(HealthcareService healthcareService){
        baseMapper.insert(healthcareService);
        return healthcareService;
    }

}