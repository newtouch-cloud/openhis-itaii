package com.openhis.web.datadictionary.appservice;

import com.openhis.medication.domain.MedicationDetail;
import com.openhis.web.datadictionary.dto.MedicationManageUpDto;

/**
 * 项目定价
 *
 * @author liuhr
 * @date 2025/3/25
 */
public interface IItemDefinitionService {

    /**
     * 添加项目定价
     *
     * @param medicationManageUpDto 药品目录信息
     * @param medicationDetail 药品信息
     */
    boolean addItem(MedicationManageUpDto medicationManageUpDto, MedicationDetail medicationDetail);

}
