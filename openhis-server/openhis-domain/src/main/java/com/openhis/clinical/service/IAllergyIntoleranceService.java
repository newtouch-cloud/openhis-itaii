package com.openhis.clinical.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.Encounter;
import com.openhis.clinical.domain.AllergyIntolerance;

/**
 * 过敏与不耐受Service接口
 *
 * @author system
 * @date 2025-03-07
 */
public interface IAllergyIntoleranceService extends IService<AllergyIntolerance> {

    /**
     * 更新或者保存过敏与不耐受
     *
     * @param allergyIntolerance 过敏与不耐受实体
     */
    boolean saveOrUpdateAllergyIntolerance(AllergyIntolerance allergyIntolerance);

}