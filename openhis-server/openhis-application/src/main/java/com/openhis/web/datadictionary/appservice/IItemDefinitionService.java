package com.openhis.web.datadictionary.appservice;

import com.openhis.administration.domain.ChargeItemDefinition;
import com.openhis.administration.domain.DeviceDefinition;
import com.openhis.medication.domain.MedicationDetail;
import com.openhis.web.datadictionary.dto.DeviceManageUpDto;
import com.openhis.web.datadictionary.dto.ItemUpFromDirectoryDto;
import com.openhis.web.datadictionary.dto.MedicationManageUpDto;

/**
 * 项目定价
 *
 * @author liuhr
 * @date 2025/3/25
 */
public interface IItemDefinitionService {

    /**
     * 添加药品/器材/诊疗的项目定价
     *
     * @param itemUpFromDirectoryDto 药品/器材/诊疗目录信息
     */
    boolean addItem(ItemUpFromDirectoryDto itemUpFromDirectoryDto);


//    /**
//     * 添加器材的项目定价
//     *
//     * @param deviceManageUpDto 器材目录信息
//     * @param deviceDefinition 器材信息
//     */
//    boolean addItem(ItemUpFromDirectoryDto itemUpFromDirectoryDto, DeviceDefinition deviceDefinition);
//
    /**
     * 修改项目定价表
     *
     * @param chargeItemDefinition 项目定价表信息
     */
    boolean updateItem(ChargeItemDefinition chargeItemDefinition);

}
