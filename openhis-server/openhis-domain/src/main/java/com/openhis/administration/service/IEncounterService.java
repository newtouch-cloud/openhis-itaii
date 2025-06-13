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

    /**
     * 退号
     * 
     * @param encounterId 就诊id
     */
    void returnRegister(Long encounterId);

    /**
     * 更新就诊管理
     *
     * @param encounter 就诊管理实体
     */
    boolean saveOrUpdateEncounter(Encounter encounter);


    /**
     * 通过 id 更新 priorityEnum 字段
     *
     * @param id             Encounter 的 id
     * @param priorityEnum   要更新的 priorityEnum 值
     * @return 更新是否成功
     */
    boolean updatePriorityEnumById(Long id, Integer priorityEnum);

}