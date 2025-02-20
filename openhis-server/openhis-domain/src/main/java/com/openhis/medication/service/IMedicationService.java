package com.openhis.medication.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.medication.domain.Medication;

/**
 * 药品基本信息管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IMedicationService extends IService<Medication> {

    /**
     * 查询药品信息列表
     *
     * @param medicationIdList 药品id列表
     * @return 药品信息列表
     */
    List<Medication> getList(List<Long> medicationIdList);
}