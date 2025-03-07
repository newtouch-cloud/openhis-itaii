package com.openhis.administration.service.impl;

import javax.annotation.Resource;

import com.core.common.enums.AssignSeqEnum;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.core.common.utils.AssignSeqUtil;
import com.openhis.administration.domain.Encounter;
import com.openhis.administration.mapper.EncounterMapper;
import com.openhis.administration.service.IEncounterService;
import com.openhis.common.enums.EncounterType;

/**
 * 就诊管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class EncounterServiceImpl extends ServiceImpl<EncounterMapper, Encounter> implements IEncounterService {

    @Resource
    AssignSeqUtil assignSeqUtil;

    /**
     * 保存就诊信息
     *
     * @param encounter 就诊信息
     * @return 保存后的信息
     */
    @Override
    public Long saveEncounterByRegister(Encounter encounter) {
        // 生成就诊编码
        encounter.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.ENCOUNTER_NUM.getPrefix(), 8));
        // 生成就诊序号 (患者ID + 科室ID 作为当日就诊号的唯一标识)
        String preFix = encounter.getPatientId() + String.valueOf(encounter.getOrganizationId());
        encounter.setDisplayOrder(assignSeqUtil.getSeqNoByDay(preFix));
        // 患者ID
        Long patientId = encounter.getPatientId();
        // 初复诊
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<Encounter>().eq(Encounter::getPatientId, patientId));
        if (count > 0L) {
            encounter.setFirstEnum(EncounterType.FOLLOW_UP.getValue());
        }
        baseMapper.insert(encounter);
        return encounter.getId();
    }

}