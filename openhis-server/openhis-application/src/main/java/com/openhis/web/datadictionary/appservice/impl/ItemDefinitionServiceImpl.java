package com.openhis.web.datadictionary.appservice.impl;

import java.util.ArrayList;
import java.util.List;

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
     * 添加项目定价
     *
     * @param medicationManageUpDto 药品目录信息
     * @param medicationDetail 药品信息
     */
    @Override
    public boolean addItem(MedicationManageUpDto medicationManageUpDto, MedicationDetail medicationDetail) {

        ChargeItemDefinition chargeItemDefinition = new ChargeItemDefinition();
        chargeItemDefinition.setChargeName(medicationDetail.getName())
            .setStatusEnum(PublicationStatus.ACTIVE.getValue())
            .setInstanceTable(CommonConstants.TableName.MED_MEDICATION_DEFINITION)
            .setInstanceId(medicationDetail.getMedicationDefId()).setEffectiveStart(DateUtils.getNowDate())
            // 机构ID
            // .setOrgId(SecurityUtils.getLoginUser().getOrgId())
            .setOrgId(1l)// todo 没数据先写死
            // 财务类别
            .setTypeCode(medicationManageUpDto.getMinimalFee())
            // 医保类别
            .setYbType(medicationManageUpDto.getYbType()).setConditionFlag(Whether.YES.getValue())
            .setPrice(medicationManageUpDto.getRetailPrice());
        boolean insertCIDSuccess = chargeItemDefinitionService.save(chargeItemDefinition);

        if (insertCIDSuccess) {
            List<ChargeItemDefDetail> shargeItemDefDetails = new ArrayList<>();
            ChargeItemDefDetail chargeItemDefDetail1 = new ChargeItemDefDetail();
            chargeItemDefDetail1.setDefinitionId(chargeItemDefinition.getId())
                // 单位+批次（unit,pici） 用,符号拼装
                .setConditionCode(StringUtils.joinStrings(
                    medicationManageUpDto.getDoseUnitCode_dictText() + "," + medicationManageUpDto.getLotNumber()))
                // 购入价
                .setAmount(medicationManageUpDto.getPurchasePrice());

            ChargeItemDefDetail chargeItemDefDetail2 = new ChargeItemDefDetail();
            chargeItemDefDetail2.setDefinitionId(chargeItemDefinition.getId())
                // 单位+批次（unit,pici） 用,符号拼装
                .setConditionCode(StringUtils.joinStrings(
                    medicationManageUpDto.getDoseUnitCode_dictText() + "," + medicationManageUpDto.getLotNumber()))
                // 零售价
                .setAmount(medicationManageUpDto.getRetailPrice());

            shargeItemDefDetails.add(chargeItemDefDetail2);

            ChargeItemDefDetail chargeItemDefDetail3 = new ChargeItemDefDetail();
            chargeItemDefDetail3.setDefinitionId(chargeItemDefinition.getId())
                // 单位+批次（unit,pici） 用,符号拼装
                .setConditionCode(StringUtils.joinStrings(
                    medicationManageUpDto.getDoseUnitCode_dictText() + "," + medicationManageUpDto.getLotNumber()))
                // 最高零售价
                .setAmount(medicationManageUpDto.getMaximumRetailPrice());

            shargeItemDefDetails.add(chargeItemDefDetail3);

            return chargeItemDefDetailService.saveBatch(shargeItemDefDetails);
        }

        return false;
    }

}
