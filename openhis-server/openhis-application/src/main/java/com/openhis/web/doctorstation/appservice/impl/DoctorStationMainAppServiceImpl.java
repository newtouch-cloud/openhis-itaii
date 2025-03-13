package com.openhis.web.doctorstation.appservice.impl;

import java.util.Arrays;
import java.util.HashSet;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.AgeCalculatorUtil;
import com.core.common.utils.SecurityUtils;
import com.openhis.administration.domain.Encounter;
import com.openhis.administration.mapper.EncounterMapper;
import com.openhis.common.enums.*;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.doctorstation.appservice.IDoctorStationMainAppService;
import com.openhis.web.doctorstation.dto.PatientInfoDto;
import com.openhis.web.doctorstation.mapper.DoctorStationMainAppMapper;

/**
 * 医生站-主页面 应用实现类
 */
@Service
public class DoctorStationMainAppServiceImpl implements IDoctorStationMainAppService {

    @Resource
    DoctorStationMainAppMapper doctorStationMainAppMapper;

    @Resource
    EncounterMapper encounterMapper;

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
        // TODO: userId(当前登录账号ID) 和 currentUserOrganizationId(当前登录账号所属的科室ID) 待补充
        // 当前登录账号ID
        Long userId = SecurityUtils.getLoginUser().getUserId();
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

    /**
     * 医生接诊
     *
     * @param encounterId 就诊id
     * @return 结果
     */
    @Override
    public R<?> receiveEncounter(Long encounterId) {
        int update = encounterMapper.update(null,
            new LambdaUpdateWrapper<Encounter>().eq(Encounter::getId, encounterId)
                .set(Encounter::getStatusEnum, EncounterStatus.IN_PROGRESS.getValue())
                .set(Encounter::getSubjectStatusEnum, EncounterSubjectStatus.RECEIVING_CARE.getValue()));
        return update > 0 ? R.ok() : R.fail();
    }

    /**
     * 患者暂离
     *
     * @param encounterId 就诊id
     * @return 结果
     */
    @Override
    public R<?> leaveEncounter(Long encounterId) {
        int update = encounterMapper.update(null,
            new LambdaUpdateWrapper<Encounter>().eq(Encounter::getId, encounterId)
                .set(Encounter::getStatusEnum, EncounterStatus.ON_HOLD.getValue())
                .set(Encounter::getSubjectStatusEnum, EncounterSubjectStatus.ON_LEAVE.getValue()));
        return update > 0 ? R.ok() : R.fail();
    }

    /**
     * 就诊完成
     *
     * @param encounterId 就诊id
     * @return 结果
     */
    @Override
    public R<?> completeEncounter(Long encounterId) {
        int update = encounterMapper.update(null,
            new LambdaUpdateWrapper<Encounter>().eq(Encounter::getId, encounterId)
                .set(Encounter::getStatusEnum, EncounterStatus.DISCHARGED.getValue())
                .set(Encounter::getSubjectStatusEnum, EncounterSubjectStatus.DEPARTED.getValue()));
        return update > 0 ? R.ok() : R.fail();
    }

}
