package com.openhis.administration.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.ChineseConvertUtils;
import com.core.common.utils.DateUtils;
import com.core.common.utils.SecurityUtils;
import com.openhis.administration.domain.Patient;
import com.openhis.administration.mapper.PatientMapper;
import com.openhis.administration.service.IPatientService;
import com.openhis.common.enums.AssignSeqEnum;

/**
 * 患者管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements IPatientService {

    @Autowired(required = false)
    AssignSeqUtil assignSeqUtil;

    /**
     * 更新或者插入患者管理
     *
     * @param patient 患者实体
     */
    @Override
    public boolean saveOrUpdatePatient(Patient patient) {

        // 身份证ID，患者ID，确定唯一患者
        LambdaQueryWrapper<Patient> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Patient::getId, patient.getId()).eq(Patient::getIdCard, patient.getIdCard());

        Patient existingPatient = baseMapper.selectOne(queryWrapper);
        if (existingPatient != null) {
            // 如果记录存在，更新记录
            patient.setId(existingPatient.getId());
            return baseMapper.updateById(patient) > 0;
        } else {
            // 如果记录不存在，插入新记录
            return baseMapper.insert(patient) > 0;
        }
    }

    @Override
    public boolean savePatient(Patient patient) {

        // 使用基础采番，设置病人ID，10位数
        String code = assignSeqUtil.getSeq(AssignSeqEnum.PATIENT_NUM.getPrefix(), 10);
        patient.setBusNo(code);

        Long organizationId = SecurityUtils.getLoginUser().getOrgId();
        // 设置机构ID
        patient.setOrganizationId(organizationId);

        // 设置拼音首拼
        patient.setPyStr(ChineseConvertUtils.toPinyinFirstLetter(patient.getName()));
        // 设置五笔首拼
        patient.setWbStr(ChineseConvertUtils.toWBFirstLetter(patient.getName()));
        // 死亡时间不为空，check死亡时间
        if (patient.getDeceasedDate() != null) {
            // 设置死亡时间,死亡时间未来时报错
            if (DateUtils.isFuture(patient.getDeceasedDate())) {
                return false;
            }
        }
        // 身份证号存在check
        if (existsByIdCard(patient.getIdCard(), patient.getId())) {
            // 身份证号存在
            return false;
        }
        saveOrUpdatePatient(patient);
        return true;
    }

    /**
     * 判断身份证号是否存在
     *
     * @param idCard 身份证号
     * @param patientId 患者ID
     * @return 是/否
     */
    public boolean existsByIdCard(String idCard, Long patientId) {
        // 构造查询条件
        LambdaQueryWrapper<Patient> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Patient::getIdCard, idCard);
        Patient patient = baseMapper.selectOne(queryWrapper);
        // 记录不存在
        if (patient == null) {
            return false;
        }
        // 新增时病人的身份证号已存在
        if (patientId == null) {
            return true;
        }
        // 记录存在时，患者ID相同，即为该患者未修改其身份证号码
        if (patient.getId().compareTo(patientId) == 0) {
            return false;
        }

        return true;
    }

    /**
     * 根据证件号查询，查询患者存在否
     *
     * @param patient 患者实体
     * @return
     */
    public Patient idCardExists(Patient patient) {
        // 根据证件号查询，查询患者存在否
        LambdaQueryWrapper<Patient> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Patient::getIdCard, patient.getIdCard());
        Patient patientExists = baseMapper.selectOne(queryWrapper);

        return patientExists;
    }

    /**
     * 添加病人
     *
     * @param patient 患者实体
     */
    @Override
    public boolean addPatient(Patient patient) {

        patientCommonSet(patient);

        // 不存在重复的证件号，新增患者
        if (idCardExists(patient) == null) {
            // 添加患者
            return baseMapper.insert(patient) > 0;
        }

        return false;
    }

    /**
     * 更新病人
     *
     * @param patient 患者实体
     */
    @Override
    public boolean updatePatient(Patient patient) {

        Patient patientExists = idCardExists(patient);
        patientCommonSet(patient);

        // 根据证件号查询，不存在重复的证件号
        if (patientExists == null) {
            // 患者修改了证件号码且不重复，更新患者
            return baseMapper.updateById(patient) > 0;
        } else if (patientExists.getId().compareTo(patient.getId()) == 0) {
            // 患者未修改证件号码，更新患者
            return baseMapper.updateById(patient) > 0;
        }

        return false;
    }

    /**
     * 设置病人共通项目赋值
     *
     * @param patient 患者实体
     */
    public Patient patientCommonSet(Patient patient) {

        // 使用基础采番，设置病人ID，10位数
        String code = assignSeqUtil.getSeq(AssignSeqEnum.PATIENT_NUM.getPrefix(), 10);
        patient.setBusNo(code);
        Long organizationId = SecurityUtils.getLoginUser().getOrgId();
        // 设置机构ID
        patient.setOrganizationId(organizationId);
        // 设置拼音首拼
        patient.setPyStr(ChineseConvertUtils.toPinyinFirstLetter(patient.getName()));
        // 设置五笔首拼
        patient.setWbStr(ChineseConvertUtils.toWBFirstLetter(patient.getName()));

        return patient;
    }

}