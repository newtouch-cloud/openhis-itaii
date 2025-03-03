package com.openhis.administration.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.HealthcareService;
import com.openhis.administration.mapper.HealthcareServiceMapper;
import com.openhis.administration.service.IHealthcareServiceService;
import org.springframework.stereotype.Service;
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
     *
     * @param healthcareService 表单信息
     * @return 入库后信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HealthcareService addHealthcareService(HealthcareService healthcareService) {
        baseMapper.insert(healthcareService);
        return healthcareService;
    }

    /**
     * 服务管理 编辑
     *
     * @param healthcareService 表单信息
     * @return 编辑结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateHealthcareService(HealthcareService healthcareService) {
        if (healthcareService.getId() == null) {
            return false;
        } else {
            return baseMapper.updateById(healthcareService) > 0;
        }
    }

}