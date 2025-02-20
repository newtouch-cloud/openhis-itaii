package com.openhis.medication.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.common.enums.DelFlag;
import com.openhis.medication.domain.Medication;
import com.openhis.medication.mapper.MedicationMapper;
import com.openhis.medication.service.IMedicationService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 药品基本信息管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Slf4j
@Service
@AllArgsConstructor
public class MedicationServiceImpl extends ServiceImpl<MedicationMapper, Medication> implements IMedicationService {

    /**
     * 查询药品信息列表
     *
     * @param medicationIdList 药品id列表
     * @return 药品信息列表
     */
    @Override
    public List<Medication> getList(List<Long> medicationIdList) {

        // 判断是否为空
        if (!medicationIdList.isEmpty()) {
            // 查询药品相关信息列表并返回
            return baseMapper.selectList(new LambdaQueryWrapper<Medication>()
                .eq(Medication::getDeleteFlag, DelFlag.NO.getValue()).in(Medication::getId, medicationIdList));
        }
        return null;
    }
}