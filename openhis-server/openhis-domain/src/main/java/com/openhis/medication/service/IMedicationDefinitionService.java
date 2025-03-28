package com.openhis.medication.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.medication.domain.MedicationDefinition;
import com.openhis.medication.domain.MedicationDetail;

/**
 * 药品定义管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IMedicationDefinitionService extends IService<MedicationDefinition> {

    /**
     * 新增药品目录
     * 
     * @param medicationDetail
     * @return
     */
    boolean addMedication(MedicationDetail medicationDetail);

}