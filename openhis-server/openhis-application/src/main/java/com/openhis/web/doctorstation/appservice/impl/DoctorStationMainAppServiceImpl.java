package com.openhis.web.doctorstation.appservice.impl;

import java.util.Arrays;
import java.util.HashSet;

import javax.annotation.Resource;

import com.openhis.web.doctorstation.appservice.IDoctorStationMainAppService;
import com.openhis.web.doctorstation.mapper.DoctorStationMainAppMapper;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.utils.AgeCalculatorUtil;
import com.openhis.common.enums.AdministrativeGender;
import com.openhis.common.enums.ClinicalStatus;
import com.openhis.common.enums.EncounterStatus;
import com.openhis.common.enums.ParticipantType;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.doctorstation.appservice.IDoctorStationEmrAppService;
import com.openhis.web.doctorstation.dto.PatientInfoDto;
import com.openhis.web.doctorstation.mapper.DoctorStationEmrAppMapper;

/**
 * 医生站-电子病历 应用实现类
 */
@Service
public class DoctorStationMainAppServiceImpl implements IDoctorStationMainAppService {

    @Resource
    DoctorStationMainAppMapper doctorStationMainAppMapper;

    /**
     * 查询就诊患者信息
     *
     * @param patientInfoDto 查询条件 (前端传 statusEnum 区分就诊状态tab)
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 就诊患者信息
     */
    @Override
    public IPage<PatientInfoDto> getPatientInfo(PatientInfoDto patientInfoDto, String searchKey, Integer pageNo,
        Integer pageSize) {
        // 构建查询条件
        QueryWrapper<PatientInfoDto> queryWrapper = HisQueryUtils.buildQueryWrapper(patientInfoDto, searchKey,
            new HashSet<>(Arrays.asList("patient_name", "id_card", "phone")), null);
        IPage<PatientInfoDto> patientInfo = doctorStationMainAppMapper.getPatientInfo(new Page<>(pageNo, pageSize),
            ParticipantType.ADMITTER.getCode(), ClinicalStatus.INACTIVE.getValue(), null, null, queryWrapper);
        patientInfo.getRecords().forEach(e -> {
            // 性别
            e.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, e.getGenderEnum()));
            // 计算年龄
            e.setAge(AgeCalculatorUtil.getAge(e.getBirthDate()));
            // 就诊状态
            e.setStatusEnum_enumText(EnumUtils.getInfoByValue(EncounterStatus.class, e.getStatusEnum()));
        });
        return patientInfo;
    }

}
