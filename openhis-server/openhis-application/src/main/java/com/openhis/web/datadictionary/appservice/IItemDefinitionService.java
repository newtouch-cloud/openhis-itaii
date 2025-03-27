package com.openhis.web.datadictionary.appservice;

import com.openhis.administration.domain.ChargeItemDefinition;
import com.openhis.administration.domain.DeviceDefinition;
import com.openhis.medication.domain.MedicationDetail;
import com.openhis.web.datadictionary.dto.DeviceManageUpDto;
import com.openhis.web.datadictionary.dto.MedicationManageUpDto;

/**
 * 项目定价
 *
 * @author liuhr
 * @date 2025/3/25
 */
public interface IItemDefinitionService {

    /**
     * 添加药品的项目定价
     *
     * @param medicationManageUpDto 药品目录信息
     * @param medicationDetail 药品信息
     */
    boolean addItem(MedicationManageUpDto medicationManageUpDto, MedicationDetail medicationDetail);


//    /**
//     * 添加器材的项目定价
//     *
//     * @param deviceManageUpDto 器材目录信息
//     * @param deviceDefinition 器材信息
//     */
//    boolean addItem(DeviceManageUpDto deviceManageUpDto, DeviceDefinition deviceDefinition);

    /**
     * 修改项目定价表
     *
     * @param chargeItemDefinition 项目定价表信息
     */
    boolean updateItem(ChargeItemDefinition chargeItemDefinition);

}
