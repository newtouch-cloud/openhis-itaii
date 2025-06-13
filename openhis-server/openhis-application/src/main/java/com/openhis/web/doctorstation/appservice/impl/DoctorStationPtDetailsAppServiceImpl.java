package com.openhis.web.doctorstation.appservice.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.common.core.domain.R;
import com.core.common.utils.DateUtils;
import com.openhis.common.enums.*;
import com.openhis.common.utils.EnumUtils;
import com.openhis.web.doctorstation.appservice.IDoctorStationPtDetailsAppService;
import com.openhis.web.doctorstation.dto.PatientDetailsDto;
import com.openhis.web.doctorstation.mapper.DoctorStationPtDetailsAppMapper;

/**
 * 医生站-患者详情应用实现类
 *
 * @author liuhr
 * @date 2025/4/10
 */
@Service
public class DoctorStationPtDetailsAppServiceImpl implements IDoctorStationPtDetailsAppService {

    @Autowired
    DoctorStationPtDetailsAppMapper doctorStationPtDetailsAppMapper;

    /**
     * 查询患者详情
     *
     * @param encounterId 就诊Id
     * @return 患者详情
     */
    @Override
    public R<?> getPtDetails(Long encounterId) {

        // 收费状态List(1:待收费,2:待结算,5:已结算)
        List<Integer> statusList = new ArrayList<>();
        // 向列表中添加元素,1:待收费
        statusList.add(ChargeItemStatus.PLANNED.getValue());
        // 向列表中添加元素,2:待结算
        statusList.add(ChargeItemStatus.BILLABLE.getValue());
        // 向列表中添加元素,5:已结算
        statusList.add(ChargeItemStatus.BILLED.getValue());

        PatientDetailsDto patientDetailsDto = doctorStationPtDetailsAppMapper.getPtDetailsList(encounterId,
            ChargeItemContext.MEDICATION.getValue(), ChargeItemContext.DEVICE.getValue(),
            ChargeItemContext.ACTIVITY.getValue(), ClinicalStatus.ACTIVE.getValue(), LocationForm.BED.getValue(),
            ParticipantType.ADMITTER.getCode(), statusList);

        // 住院的场合，获取现在时间，计算住院天数
        if (patientDetailsDto.getClassEnum() == EncounterClass.IMP.getValue()) {
            // 截至时间,用于计算当前时刻下显示的住院天数
            Date endTime;
            if (patientDetailsDto.getEndTime() == null) {
                endTime = DateUtils.getNowDate();
            } else {
                endTime = patientDetailsDto.getEndTime();
            }

            int days = DateUtils.differentDaysByMillisecond(patientDetailsDto.getStartTime(), endTime);
            patientDetailsDto.setAdmissionDays(days);
        }

        // 性别枚举类回显赋值
        patientDetailsDto.setGenderEnum_enumText(
            EnumUtils.getInfoByValue(AdministrativeGender.class, patientDetailsDto.getGenderEnum()));
        // 婚姻状态枚举类回显赋值
        patientDetailsDto.setMaritalStatusEnum_enumText(
            EnumUtils.getInfoByValue(MaritalStatus.class, patientDetailsDto.getMaritalStatusEnum()));
        // 职业编码枚举类回显赋值
        patientDetailsDto
            .setPrfsEnum_enumText(EnumUtils.getInfoByValue(OccupationType.class, patientDetailsDto.getPrfsEnum()));
        // 家庭关系枚举类回显赋值
        patientDetailsDto.setLinkRelationCode_enumText(
            EnumUtils.getInfoByValue(FamilyRelationshipType.class, patientDetailsDto.getLinkRelationCode()));

        return R.ok(patientDetailsDto);
    }

}
