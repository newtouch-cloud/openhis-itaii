package com.openhis.administration.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.EncounterParticipant;
import com.openhis.administration.mapper.EncounterParticipantMapper;
import com.openhis.administration.service.IEncounterParticipantService;

/**
 * 就诊参与者管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class EncounterParticipantServiceImpl extends ServiceImpl<EncounterParticipantMapper, EncounterParticipant>
    implements IEncounterParticipantService {

    /**
     * 门诊挂号时保存就诊参与者
     *
     * @param encounterParticipant 就诊参与者信息
     */
    @Override
    public void saveEncounterParticipantByRegister(EncounterParticipant encounterParticipant) {
        baseMapper.insert(encounterParticipant);
    }

}