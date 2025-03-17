package com.openhis.web.patientmanage.appservice.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.core.common.utils.MessageUtils;
import com.openhis.common.constant.PromptMsgConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.enums.AssignSeqEnum;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.ChineseConvertUtils;
import com.core.common.utils.DateUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.administration.domain.Patient;
import com.openhis.administration.mapper.PatientMapper;
import com.openhis.administration.service.IPatientService;
import com.openhis.common.enums.*;
import com.openhis.common.utils.EnumUtils;
import com.openhis.web.patientmanage.appservice.IPatientInformationService;
import com.openhis.web.patientmanage.dto.PatientInfoInitDto;
import com.openhis.web.patientmanage.dto.PatientInformationDto;
import com.openhis.web.patientmanage.mapper.PatientManageMapper;

/**
 * 门诊患者
 *
 * @author liuhr
 * @date 2025/3/15
 */
@Service
public class PatientInformationServiceImpl implements IPatientInformationService {

    @Autowired
    PatientManageMapper patientManageMapper;

    @Autowired
    PatientMapper patientMapper;

    @Autowired
    IPatientService patientService;

    @Autowired(required = false)
    AssignSeqUtil assignSeqUtil;

    // todo 暂且机构ID写死，后续从登录里取得
    private final Long organizationId = 91L;

    /**
     * 获取病人信息记录初期数据列表
     *
     * @return 病人信息记录初期数据列表
     */
    @Override
    public PatientInfoInitDto getPatientInfoInit() {
        PatientInfoInitDto initDto = new PatientInfoInitDto();
        // 获取婚姻状态列表
        List<PatientInfoInitDto.statusEnumOption> statusEnumOptions1 = Stream.of(MaritalStatus.values())
            .map(status -> new PatientInfoInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        initDto.setMaritalStatus(statusEnumOptions1);

        // 获取职业编码列表
        List<PatientInfoInitDto.statusEnumOption> statusEnumOptions2 = Stream.of(OccupationType.values())
            .map(status -> new PatientInfoInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        initDto.setOccupationType(statusEnumOptions2);

        // 获取性别列表
        List<PatientInfoInitDto.statusEnumOption> statusEnumOptions3 = Stream.of(AdministrativeGender.values())
            .map(status -> new PatientInfoInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        initDto.setAdministrativeGender(statusEnumOptions3);

        // 获取ABO血型列表
        List<PatientInfoInitDto.statusEnumOption> statusEnumOptions4 = Stream.of(BloodTypeABO.values())
            .map(status -> new PatientInfoInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        initDto.setBloodTypeABO(statusEnumOptions4);

        // 获取RH血型列表
        List<PatientInfoInitDto.statusEnumOption> statusEnumOptions5 = Stream.of(BloodTypeRH.values())
            .map(status -> new PatientInfoInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        initDto.setBloodTypeRH(statusEnumOptions5);

        // 获取家庭关系列表
        List<PatientInfoInitDto.statusEnumOption> statusEnumOptions6 = Stream.of(FamilyRelationshipType.values())
            .map(status -> new PatientInfoInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        initDto.setFamilyRelationshipType(statusEnumOptions6);

        return initDto;
    }

    /**
     * 分页查询病人信息
     *
     * @param busNo 病人ID（可选）
     * @param name 病人姓名（可选）
     * @param pageNo 页码（默认为1）
     * @param pageSize 每页大小（默认为10））
     * @return 分页查询
     */
    @Override
    public Page<PatientInformationDto> getPatient(String busNo, String name, Integer pageNo, Integer pageSize) {
        // 跳过的记录数
        Integer offset = (pageNo - 1) * pageSize;
        // 连表查询患者信息
        List<PatientInformationDto> listPatients = patientManageMapper.getPatientPage(busNo, name, pageSize, offset);
        // 查询总记录数
        long total = patientManageMapper.countPatients(busNo, name);
        // 创建Page对象并设置属性
        Page<PatientInformationDto> patientInformationPage = new Page<>(pageNo, pageSize, total);
        patientInformationPage.setRecords(listPatients);
        patientInformationPage.getRecords().forEach(e -> {
            // 性别枚举类回显赋值
            e.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, e.getGenderEnum()));
            // 婚姻状态枚举类回显赋值
            e.setMaritalStatusEnum_enumText(EnumUtils.getInfoByValue(MaritalStatus.class, e.getMaritalStatusEnum()));
            // 职业编码枚举类回显赋值
            e.setPrfsEnum_enumText(EnumUtils.getInfoByValue(OccupationType.class, e.getPrfsEnum()));
            // 血型ABO枚举类回显赋值
            e.setBloodAbo_enumText(EnumUtils.getInfoByValue(BloodTypeABO.class, e.getBloodAbo()));
            // 血型RH枚举类回显赋值
            e.setBloodRh_enumText(EnumUtils.getInfoByValue(BloodTypeRH.class, e.getBloodRh()));
            // 家庭关系枚举类回显赋值
            e.setLinkRelationCode_enumText(
                EnumUtils.getInfoByValue(FamilyRelationshipType.class, e.getLinkRelationCode()));
        });
        return patientInformationPage;
    }

    /**
     * 修改病人信息
     *
     * @param patientInformationDto 病人信息
     * @return 更新结果
     */
    @Override
    public R<?>  editPatient(PatientInformationDto patientInformationDto) {

        Patient patient = new Patient();
        BeanUtils.copyProperties(patientInformationDto, patient);

        // 设置生日
        patient.setBirthDate(DateUtils.extractBirthday(patient.getIdCard()));
        // 设置拼音首拼
        patient.setPyStr(ChineseConvertUtils.toPinyinFirstLetter(patient.getName()));
        // 设置五笔首拼
        patient.setWbStr(ChineseConvertUtils.toWBFirstLetter(patient.getName()));
        // 设置死亡时间,死亡时间未来时报错
        if (DateUtils.isFuture(patientInformationDto.getDeceasedDate())) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00003, new Object[] {"死亡时间未来时"}));
        }
        patient.setDeceasedDate(DateUtils.parseDate(patientInformationDto.getDeceasedDate()));
        // 身份证号存在check
        if (existsByIdCard(patientInformationDto.getIdCard())) {
            // 身份证号存在
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00003, new Object[] {"身份证号已存在"}));
        }

        // 调用服务层更新病人信息
        boolean result = patientService.updateById(patient);
        if (result) {
            return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00001, new Object[] {"病人信息"}));
        } else {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }

    }

    /**
     * 添加病人信息
     *
     * @param patientInformationDto 病人信息
     */
    @Override
    public R<?> addPatient(PatientInformationDto patientInformationDto) {

        Patient patient = new Patient();
        BeanUtils.copyProperties(patientInformationDto, patient);

        // 使用基础采番，设置病人ID
        String code = assignSeqUtil.getSeq(AssignSeqEnum.PATIENT_NUM.getPrefix());
        patient.setBusNo(code);

        // 设置机构ID
        patient.setOrganizationId(organizationId);
        // 设置生日
        patient.setBirthDate(DateUtils.extractBirthday(patient.getIdCard()));
        // 设置拼音首拼
        patient.setPyStr(ChineseConvertUtils.toPinyinFirstLetter(patient.getName()));
        // 设置五笔首拼
        patient.setWbStr(ChineseConvertUtils.toWBFirstLetter(patient.getName()));
        // 设置死亡时间,死亡时间未来时报错
        if (DateUtils.isFuture(patientInformationDto.getDeceasedDate())) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00003, new Object[] {"死亡时间未来时"}));
        }
        // 身份证号存在check
        if (existsByIdCard(patientInformationDto.getIdCard())) {
            // 身份证号存在
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00003, new Object[] {"身份证号已存在"}));
        }
        patient.setDeceasedDate(DateUtils.parseDate(patientInformationDto.getDeceasedDate()));

        // 调用服务层保存病人信息
        boolean result = patientService.save(patient);
        if (result) {
            return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00001, new Object[] {"病人信息"}));
        } else {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00003, null));
        }
    }

    /**
     * 判断身份证号是否存在
     *
     * @param idCard 身份证号
     * @return 是/否
     */
    public boolean existsByIdCard(String idCard) {
        // 构造查询条件
        LambdaQueryWrapper<Patient> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Patient::getIdCard, idCard);
        if (patientMapper.selectOne(queryWrapper) == null) {
            return false;
        }
        return true;
    }

}
