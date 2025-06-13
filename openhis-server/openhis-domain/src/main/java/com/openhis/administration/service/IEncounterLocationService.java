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

    /**
     * 自定义插入或更新方法,根据就诊id，病区,判断是更新还是插入
     * @param encounterLocation 实体对象
     * @return 是否成功
     */
    boolean saveOrUpdateEncounterLocation(EncounterLocation encounterLocation);

}