package com.openhis.administration.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.EncounterDiagnosis;
import com.openhis.administration.mapper.EncounterDiagnosisMapper;
import com.openhis.administration.service.IEncounterDiagnosisService;
import com.openhis.clinical.mapper.ConditionMapper;

/**
 * 就诊诊断管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class EncounterDiagnosisServiceImpl extends ServiceImpl<EncounterDiagnosisMapper, EncounterDiagnosis>
    implements IEncounterDiagnosisService {

    @Resource
    ConditionMapper conditionMapper;

    /**
     * 删除就诊信息
     *
     * @param encounterId 就诊id
     */
    @Override
    public void deleteEncounterDiagnosisInfos(Long encounterId) {
        // 不删除中医
        conditionMapper.deleteByEncounterId(encounterId);
        baseMapper.deleteByEncounterId(encounterId);
    }

    /**
     * 删除中医诊断
     *
     * @param encounterId 就诊id
     */
    @Override
    public void deleteTcmEncounterDiagnosisInfos(Long encounterId) {
        // 删除中医
        conditionMapper.deleteTcmByEncounterId(encounterId);
        baseMapper.deleteTcmByEncounterId(encounterId);
    }

    /**
     * 返回med_type类型
     * 
     * @param encounterId 就诊id
     * @return med_type集合
     */
    @Override
    public List<String> getMetTypeList(Long encounterId) {

        List<EncounterDiagnosis> encounterDiagnoses = baseMapper.selectList(
            new LambdaQueryWrapper<EncounterDiagnosis>().eq(EncounterDiagnosis::getEncounterId, encounterId));
        if (encounterDiagnoses == null) {
            return new ArrayList<String>();
        }
        return encounterDiagnoses.stream().distinct().map(EncounterDiagnosis::getMedTypeCode)
            .collect(Collectors.toList());
    }

    /**
     * 返回med_type类型
     * 
     * @param encounterId 就诊id
     * @return EncounterDiagnosis 集合
     */
    @Override
    public List<EncounterDiagnosis> getDiagnosisList(Long encounterId) {
        return baseMapper.selectList(
            new LambdaQueryWrapper<EncounterDiagnosis>().eq(EncounterDiagnosis::getEncounterId, encounterId));
    }

    /**
     *
     * @param diaIdList
     * @return
     */
    @Override
    public List<EncounterDiagnosis> getDiagnosisList(List<Long> diaIdList) {
        return baseMapper
            .selectList(new LambdaQueryWrapper<EncounterDiagnosis>().in(EncounterDiagnosis::getId, diaIdList));
    }
}