package com.openhis.administration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.EncounterLocation;

/**
 * 就诊位置管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IEncounterLocationService extends IService<EncounterLocation> {

    /**
     * 门诊挂号时保存就诊位置信息
     * 
     * @param encounterLocation 就诊位置信息
     */
    void saveEncounterLocationByRegister(EncounterLocation encounterLocation);

}