package com.openhis.administration.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.PatientIdentifier;
import com.openhis.administration.mapper.PatientIdentifierMapper;
import com.openhis.administration.service.IPatientIdentifierService;

/**
 * 患者标识管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class PatientIdentifierServiceImpl extends ServiceImpl<PatientIdentifierMapper, PatientIdentifier>
    implements IPatientIdentifierService {

    @Autowired
    PatientIdentifierMapper patientIdentifierMapper;

    /**
     * 根据患者Id查询病人标识
     *
     * @param patientId 患者Id
     */
    @Override
    public PatientIdentifier selectByPatientId(Long patientId) {
        // 构造查询条件
        LambdaQueryWrapper<PatientIdentifier> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PatientIdentifier::getPatientId, patientId);

        return baseMapper.selectOne(queryWrapper);

    }

    /**
     * 查询病人标识
     *
     * @param patientId 患者Id
     */
    public boolean updateTypeByPatientId(Long patientId, String typeCode) {

        // 创建LambdaUpdateWrapper实例
        LambdaUpdateWrapper<PatientIdentifier> updateWrapper = new LambdaUpdateWrapper<>();

        // 设置更新条件：根据patientId更新
        updateWrapper.eq(PatientIdentifier::getPatientId, patientId);

        // 设置要更新的字段和值
        updateWrapper.set(PatientIdentifier::getTypeCode, typeCode);

        // 执行更新操作
        return patientIdentifierMapper.update(null, updateWrapper) > 0;
    }

}