package com.openhis.web.datadictionary.appservice.impl;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.core.common.utils.SecurityUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.administration.domain.DeviceDefinition;
import com.openhis.common.enums.ConditionCode;
import com.openhis.web.datadictionary.dto.DeviceManageUpDto;
import com.openhis.web.datadictionary.dto.ItemUpFromDirectoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.common.utils.DateUtils;
import com.core.common.utils.StringUtils;
import com.openhis.administration.domain.ChargeItemDefDetail;
import com.openhis.administration.domain.ChargeItemDefinition;
import com.openhis.administration.service.IChargeItemDefDetailService;
import com.openhis.administration.service.IChargeItemDefinitionService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.enums.PublicationStatus;
import com.openhis.common.enums.Whether;
import com.openhis.medication.domain.MedicationDetail;
import com.openhis.web.datadictionary.appservice.IItemDefinitionService;
import com.openhis.web.datadictionary.dto.MedicationManageUpDto;

/**
 * 项目定价 实现
 *
 * @author liuhr
 * @date 2025/3/25
 */
@Service
public class ItemDefinitionServiceImpl implements IItemDefinitionService {

    @Autowired
    IChargeItemDefinitionService chargeItemDefinitionService;

    @Autowired
    IChargeItemDefDetailService chargeItemDefDetailService;


    /**
     * 添加药品/器材/诊疗的项目定价
     *
     * @param itemUpFromDirectoryDto 药品/器材/诊疗目录信息
     */
    @Override
    public boolean addItem(ItemUpFromDirectoryDto itemUpFromDirectoryDto) {


        ChargeItemDefinition chargeItemDefinition = new ChargeItemDefinition();
        BeanUtils.copyProperties(itemUpFromDirectoryDto, chargeItemDefinition);

        boolean insertCIDSuccess = chargeItemDefinitionService.save(chargeItemDefinition);

        if (insertCIDSuccess) {
            List<ChargeItemDefDetail> shargeItemDefDetails = new ArrayList<>();
            //插入购入价
            ChargeItemDefDetail chargeItemDefDetail1 = new ChargeItemDefDetail();
            chargeItemDefDetail1.setDefinitionId(chargeItemDefinition.getId())
                // 条件:采购
                .setConditionCode(ConditionCode.PROCUREMENT.getCode())
                // 购入价
                .setAmount(itemUpFromDirectoryDto.getPurchasePrice());
            shargeItemDefDetails.add(chargeItemDefDetail1);
            //插入零售价
            ChargeItemDefDetail chargeItemDefDetail2 = new ChargeItemDefDetail();
            chargeItemDefDetail2.setDefinitionId(chargeItemDefinition.getId())
                // 条件:单位
                .setConditionCode(ConditionCode.UNIT.getCode())
                //单位枚举
                .setConditionValue(itemUpFromDirectoryDto.getUnitCode())
                // 零售价
                .setAmount(itemUpFromDirectoryDto.getRetailPrice());

            shargeItemDefDetails.add(chargeItemDefDetail2);

            //插入最高零售价
            ChargeItemDefDetail chargeItemDefDetail3 = new ChargeItemDefDetail();
            chargeItemDefDetail3.setDefinitionId(chargeItemDefinition.getId())
                // 条件:限制
                .setConditionCode(ConditionCode.LIMIT.getCode())
                // 最高零售价
                .setAmount(itemUpFromDirectoryDto.getMaximumRetailPrice());

            shargeItemDefDetails.add(chargeItemDefDetail3);

            return chargeItemDefDetailService.saveBatch(shargeItemDefDetails);
        }

        return false;
    }


    /**
     * 修改项目定价表
     *
     * @param chargeItemDefinition 项目定价表信息
     */
    @Override
    public boolean updateItem(ChargeItemDefinition chargeItemDefinition) {

        // 关联项目和代码位为key，更新表
        LambdaUpdateWrapper<ChargeItemDefinition> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ChargeItemDefinition::getInstanceId, chargeItemDefinition.getInstanceId());
        updateWrapper.eq(ChargeItemDefinition::getInstanceTable, chargeItemDefinition.getInstanceTable())
            .set(ChargeItemDefinition::getYbType, chargeItemDefinition.getYbType())
            .set(ChargeItemDefinition::getTypeCode, chargeItemDefinition.getTypeCode());

        return chargeItemDefinitionService.update(null, updateWrapper);
    }

}
