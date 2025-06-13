package com.openhis.web.patientmanage.appservice.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import com.core.common.exception.ServiceException;
import com.core.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.administration.domain.Patient;
import com.openhis.administration.domain.PatientIdentifier;
import com.openhis.administration.mapper.PatientMapper;
import com.openhis.administration.service.IPatientIdentifierService;
import com.openhis.administration.service.IPatientService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.patientmanage.appservice.IPatientInformationService;
import com.openhis.web.patientmanage.dto.PatientInfoInitDto;
import com.openhis.web.patientmanage.dto.PatientInfoSearchParam;
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
    @Autowired
    IPatientIdentifierService patientIdentifierService;

    @Autowired(required = false)
    AssignSeqUtil assignSeqUtil;

    /**
     * 获取病人信息记录初期数据列表
     *
     * @return 病人信息记录初期数据列表
     */
    @Override
    public PatientInfoInitDto getPatientInfoInit() {
        PatientInfoInitDto initDto = new PatientInfoInitDto();
        // 获取婚姻状态列表
        List<PatientInfoInitDto.statusEnumOption> maritalStatusOptions = Stream.of(MaritalStatus.values())
            .map(status -> new PatientInfoInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        initDto.setMaritalStatus(maritalStatusOptions);

        // 获取职业编码列表
        List<PatientInfoInitDto.statusEnumOption> occupationTypeOptions = Stream.of(OccupationType.values())
            .map(status -> new PatientInfoInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        initDto.setOccupationType(occupationTypeOptions);

        // 获取性别列表
        List<PatientInfoInitDto.statusEnumOption> sexOptions = new ArrayList<>();
        sexOptions.add(new PatientInfoInitDto.statusEnumOption(AdministrativeGender.MALE.getValue(),
            AdministrativeGender.MALE.getInfo()));
        sexOptions.add(new PatientInfoInitDto.statusEnumOption(AdministrativeGender.FEMALE.getValue(),
            AdministrativeGender.FEMALE.getInfo()));
        initDto.setSex(sexOptions);

        // 获取ABO血型列表
        List<PatientInfoInitDto.statusEnumOption> bloodTypeABOOptions = Stream.of(BloodTypeABO.values())
            .map(status -> new PatientInfoInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        initDto.setBloodTypeABO(bloodTypeABOOptions);

        // 获取RH血型列表
        List<PatientInfoInitDto.statusEnumOption> bloodTypeRHOptions = Stream.of(BloodTypeRH.values())
            .map(status -> new PatientInfoInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        initDto.setBloodTypeRH(bloodTypeRHOptions);

        // 获取家庭关系列表
        List<PatientInfoInitDto.statusEnumOption> familyRelationshipType = Stream.of(FamilyRelationshipType.values())
            .map(status -> new PatientInfoInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        initDto.setFamilyRelationshipType(familyRelationshipType);

        // 获取是/否状态
        List<PatientInfoInitDto.statusEnumOption> whetherOptions = Stream.of(Whether.values())
            .map(status -> new PatientInfoInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        initDto.setWhetherStatus(whetherOptions);

        return initDto;
    }

    /**
     * 分页查询门诊记录
     *
     * @param patientInfoSearchParam 病人查询参数
     * @param searchKey 查询条件-模糊查询
     * @param pageNo 页码（默认为1）
     * @param pageSize 每页大小（默认为10）
     * @return 分页查询
     */
    @Override
    public IPage<PatientInformationDto> getPatientInfo(PatientInfoSearchParam patientInfoSearchParam, String searchKey,
        Integer pageNo, Integer pageSize, HttpServletRequest request) {

        // 构建查询条件
        QueryWrapper<PatientInformationDto> queryWrapper = HisQueryUtils.buildQueryWrapper(
            patientInfoSearchParam, searchKey, new HashSet<>(Arrays.asList(CommonConstants.FieldName.Name,
                CommonConstants.FieldName.BusNo, CommonConstants.FieldName.PyStr, CommonConstants.FieldName.WbStr)),
            request);

        IPage<PatientInformationDto> patientInformationPage =
            patientManageMapper.getPatientPage(new Page<>(pageNo, pageSize), queryWrapper);

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
    public R<?> editPatient(PatientInformationDto patientInformationDto) {

        Patient patient = new Patient();
        BeanUtils.copyProperties(patientInformationDto, patient);

        // 当死亡时间不为空，检查死亡时间
        if (patient.getDeceasedDate() != null) {
            // 检验死亡时间未来时报错
            if (DateUtils.isFuture(patient.getDeceasedDate())) {
                R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00011, new Object[] {"死亡时间未来时！"}));
            }
        }
        // 调用服务层更新病人信息
        boolean resPatient = patientService.updatePatient(patient);

        // 根据患者的标识，判断是否需要修改
        PatientIdentifier patientIdentifier = patientIdentifierService.selectByPatientId(patient.getId());
        boolean resPatId = true;
        if (patientIdentifier == null) {
            PatientIdentifier newIdentifier = new PatientIdentifier();
            newIdentifier.setPatientId(patient.getId()).setTypeCode(patientInformationDto.getTypeCode())
                // 标识状态默认：常规
                .setStateEnum(IdentifierStatusEnum.USUAL.getValue());
            resPatId = patientIdentifierService.save(newIdentifier);
        } else if (patientIdentifier.getTypeCode() != patientInformationDto.getTypeCode()) {
            resPatId =
                patientIdentifierService.updateTypeByPatientId(patient.getId(), patientInformationDto.getTypeCode());
        }

        return resPatient && resPatId
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00001, new Object[] {"病人信息"}))
            : R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));

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
        // 当死亡时间不为空，检查死亡时间
        if (patient.getDeceasedDate() != null) {
            // 检验死亡时间未来时报错
            if (DateUtils.isFuture(patient.getDeceasedDate())) {
                R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00011, new Object[] {"死亡时间未来时！"}));
            }
        }
        // 调用服务层，添加病人
        boolean resPatient = patientService.addPatient(patient);
        if(!resPatient){
            throw new ServiceException("身份证号已存在");
        }
        // 调用服务层，添加病人标识
        PatientIdentifier patientIdentifier = new PatientIdentifier();
        patientIdentifier.setPatientId(patient.getId()).setTypeCode(patientInformationDto.getTypeCode())
            // 标识状态默认：常规
            .setStateEnum(IdentifierStatusEnum.USUAL.getValue());
        boolean resPatId = patientIdentifierService.save(patientIdentifier);

        return resPatient && resPatId
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00001, new Object[] {"病人信息"}))
            : R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00003, new Object[] {"病人信息"}));
    }

}
