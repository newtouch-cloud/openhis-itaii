package com.openhis.administration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.EncounterParticipant;

/**
 * 就诊参与者管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IEncounterParticipantService extends IService<EncounterParticipant> {

    /**
     * 门诊挂号时保存就诊参与者
     * 
     * @param encounterParticipant 就诊参与者信息
     */
    void saveEncounterParticipantByRegister(EncounterParticipant encounterParticipant);

}