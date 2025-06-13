package com.openhis.clinical.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.openhis.administration.domain.Encounter;
import com.openhis.common.enums.AssignSeqEnum;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.clinical.domain.AllergyIntolerance;
import com.openhis.clinical.mapper.AllergyIntoleranceMapper;
import com.openhis.clinical.service.IAllergyIntoleranceService;

/**
 * 过敏与不耐受Service业务层处理
 *
 * @author system
 * @date 2025-03-07
 */
@Service
public class AllergyIntoleranceServiceImpl extends ServiceImpl<AllergyIntoleranceMapper, AllergyIntolerance>
    implements IAllergyIntoleranceService {

    /**
     * 更新或者保存过敏与不耐受
     *
     * @param allergyIntolerance 过敏与不耐受实体
     */
    @Override
    public boolean saveOrUpdateAllergyIntolerance(AllergyIntolerance allergyIntolerance) {

        // 创建 LambdaQueryWrapper
        LambdaQueryWrapper<AllergyIntolerance> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AllergyIntolerance::getId, allergyIntolerance.getId())
            .eq(AllergyIntolerance::getPatientId, allergyIntolerance.getPatientId());

        // 查询是否存在记录
        AllergyIntolerance existingEncounter = baseMapper.selectOne(queryWrapper);
        if (existingEncounter != null) {
            // 如果记录存在，更新记录
            allergyIntolerance.setId(existingEncounter.getId()); // 设置主键
            return baseMapper.updateById(allergyIntolerance) > 0;
        } else {
            // 如果记录不存在，插入新记录
            return baseMapper.insert(allergyIntolerance) > 0;
        }
    }

}