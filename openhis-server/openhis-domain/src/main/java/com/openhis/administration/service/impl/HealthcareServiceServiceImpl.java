package com.openhis.administration.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.HealthcareService;
import com.openhis.administration.mapper.HealthcareServiceMapper;
import com.openhis.administration.service.IHealthcareServiceService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务项目管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class HealthcareServiceServiceImpl extends ServiceImpl<HealthcareServiceMapper, HealthcareService> implements IHealthcareServiceService {


    /**
     * 服务管理 新增
     * @param healthcareService 表单信息
     * @return 入库后信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HealthcareService addHealthcareService(HealthcareService healthcareService){
        baseMapper.insert(healthcareService);
        return healthcareService;
    }

}