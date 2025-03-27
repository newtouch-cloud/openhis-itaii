package com.openhis.medication.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.common.enums.DelFlag;
import com.openhis.medication.domain.Medication;
import com.openhis.medication.domain.MedicationDetail;
import com.openhis.medication.mapper.MedicationMapper;
import com.openhis.medication.service.IMedicationService;

import lombok.extern.slf4j.Slf4j;

/**
 * 药品基本信息管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Slf4j
@Service
public class MedicationServiceImpl extends ServiceImpl<MedicationMapper, Medication> implements IMedicationService {

    @Autowired
    private MedicationMapper medicationMapper;

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

    /**
     * 查询药品详细信息列表
     *
     * @return 药品详细信息列表
     */
    @Override
    public List<MedicationDetail> getDetailList() {
        return medicationMapper.selectDetailList();
    }

    /**
     * 新增药品目录
     * 
     * @param medicationDetail
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addMedication(MedicationDetail medicationDetail) {
        Medication medication = new Medication();
        BeanUtils.copyProperties(medicationDetail, medication);
        // 根据药品编码判断药品是否存在
        List<Medication> medications = medicationMapper.selectList(
            new LambdaQueryWrapper<Medication>().eq(Medication::getMedicationDefId, medication.getMedicationDefId()));
        if (medications.size() > 0) {
            return false;
        }
        // 新增药品目录
        int insert = medicationMapper.insert(medication);
        if (insert != 1) {
            return false;
        }
        return true;
    }

}