/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.appservice.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.AgeCalculatorUtil;
import com.core.common.utils.MessageUtils;
import com.openhis.administration.service.IAccountService;
import com.openhis.administration.service.IChargeItemService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.AdministrativeGender;
import com.openhis.common.enums.ChargeItemContext;
import com.openhis.common.enums.ChargeItemStatus;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.chargemanage.appservice.IOutpatientChargeAppService;
import com.openhis.web.chargemanage.dto.EncounterPatientPageDto;
import com.openhis.web.chargemanage.dto.EncounterPatientPageParam;
import com.openhis.web.chargemanage.dto.EncounterPatientPrescriptionDto;
import com.openhis.web.chargemanage.dto.OutpatientInitDto;
import com.openhis.web.chargemanage.mapper.OutpatientChargeAppMapper;

/**
 * 门诊收费 impl
 *
 * @author zwh
 * @date 2025-03-12
 */
@Service
public class OutpatientChargeAppServiceImpl implements IOutpatientChargeAppService {

    @Autowired
    private OutpatientChargeAppMapper outpatientChargeAppMapper;
    @Autowired
    private IChargeItemService chargeItemService;
    @Autowired
    private IAccountService accountService;

    /**
     * 门诊收费页面初始化
     *
     * @return 初始化信息
     */
    @Override
    public R<?> outpatientChargeInit() {
        OutpatientInitDto initDto = new OutpatientInitDto();
        List<OutpatientInitDto.chargeItemStatusOption> chargeItemStatusOptions = new ArrayList<>();
        chargeItemStatusOptions.add(new OutpatientInitDto.chargeItemStatusOption(ChargeItemStatus.PLANNED.getValue(),
            ChargeItemStatus.PLANNED.getInfo()));
        chargeItemStatusOptions.add(new OutpatientInitDto.chargeItemStatusOption(ChargeItemStatus.BILLABLE.getValue(),
            ChargeItemStatus.BILLABLE.getInfo()));
        chargeItemStatusOptions.add(new OutpatientInitDto.chargeItemStatusOption(ChargeItemStatus.BILLED.getValue(),
            ChargeItemStatus.BILLED.getInfo()));
        initDto.setChargeItemStatusOptions(chargeItemStatusOptions);
        return R.ok(initDto);
    }

    /**
     * 查询就诊患者分页列表
     *
     * @param encounterPatientPageParam 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @param request 请求
     * @return 就诊患者分页列表
     */
    @Override
    public R<?> getEncounterPatientPage(EncounterPatientPageParam encounterPatientPageParam, String searchKey,
        Integer pageNo, Integer pageSize, HttpServletRequest request) {
        // 构建查询条件
        QueryWrapper<EncounterPatientPageParam> queryWrapper = HisQueryUtils.buildQueryWrapper(
            encounterPatientPageParam, searchKey,
            new HashSet<>(Arrays.asList(CommonConstants.FieldName.PatientWbStr, CommonConstants.FieldName.PatientPyStr,
                CommonConstants.FieldName.PatientName, CommonConstants.FieldName.PatientBusNo,
                CommonConstants.FieldName.EncounterBusNo, CommonConstants.FieldName.idCard)),
            request);
        // 就诊患者分页列表
        Page<EncounterPatientPageDto> encounterPatientPage =
            outpatientChargeAppMapper.selectEncounterPatientPage(new Page<>(pageNo, pageSize), queryWrapper);

        encounterPatientPage.getRecords().forEach(e -> {
            // 性别枚举
            e.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, e.getGenderEnum()));
            // 收费状态枚举
            e.setStatusEnum_enumText(EnumUtils.getInfoByValue(ChargeItemStatus.class, e.getStatusEnum()));
            // 计算年龄
            e.setAge(e.getBirthDate() != null ? AgeCalculatorUtil.getAge(e.getBirthDate()) : "");
        });
        return R.ok(encounterPatientPage);
    }

    /**
     * 根据就诊id查询患者处方列表
     *
     * @param encounterId 就诊id
     * @return 患者处方列表
     */
    @Override
    public List<EncounterPatientPrescriptionDto> getEncounterPatientPrescription(Long encounterId) {
        List<EncounterPatientPrescriptionDto> prescriptionDtoList =
            outpatientChargeAppMapper.selectEncounterPatientPrescription(encounterId,
                ChargeItemContext.ACTIVITY.getValue(), ChargeItemContext.MEDICATION.getValue(),
                ChargeItemContext.DEVICE.getValue(), ChargeItemContext.REGISTER.getValue(),
                ChargeItemStatus.PLANNED.getValue(), ChargeItemStatus.BILLABLE.getValue(),
                ChargeItemStatus.BILLED.getValue(), ChargeItemStatus.REFUNDING.getValue(),
                ChargeItemStatus.REFUNDED.getValue(), ChargeItemStatus.PART_REFUND.getValue());
        prescriptionDtoList.forEach(e -> {
            // 收费状态枚举
            e.setStatusEnum_enumText(EnumUtils.getInfoByValue(ChargeItemStatus.class, e.getStatusEnum()));
        });
        return prescriptionDtoList;
    }

    /**
     * 医保转自费
     *
     * @param encounterId 就诊id
     * @return 操作结果
     */
    @Override
    public R<?> changeToSelfPay(Long encounterId) {
        // 获取就诊患者的自费账户id
        Long accountId = accountService.getSelfPayAccount(encounterId);
        if (accountId == null) {
            R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00006, null));
        }
        // 医保转自费
        boolean result = chargeItemService.updateAccountType(encounterId, accountId);
        if (!result) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00006, null));
        }
        return R.ok();
    }

    /**
     * 自费转医保
     *
     * @param encounterId 就诊id
     * @return 操作结果
     */
    @Override
    public R<?> changeToMedicalInsurance(Long encounterId) {
        // 获取就诊患者的医保账户id
        Long accountId = accountService.getMedicalInsuranceAccount(encounterId);
        if (accountId == null) {
            R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00006, null));
        }
        // 自费转医保
        boolean result = chargeItemService.updateAccountType(encounterId, accountId);
        if (!result) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00006, null));
        }
        return R.ok();
    }
}
