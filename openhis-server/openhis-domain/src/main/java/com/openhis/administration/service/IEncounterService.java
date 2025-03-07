package com.openhis.administration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.Encounter;

/**
 * 就诊管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IEncounterService extends IService<Encounter> {
    /**
     * 保存就诊信息
     * 
     * @param encounter 就诊信息
     * @return 保存后的信息
     */
    Long saveEncounterByRegister(Encounter encounter);

}