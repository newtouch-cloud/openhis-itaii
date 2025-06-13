package com.openhis.web.inpatientmanage.appservice.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.openhis.web.nursestation.mapper.NurseStationPendAdmAppMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.AgeCalculatorUtil;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.SecurityUtils;
import com.openhis.administration.domain.Account;
import com.openhis.administration.domain.Encounter;
import com.openhis.administration.domain.EncounterLocation;
import com.openhis.administration.service.IAccountService;
import com.openhis.administration.service.IEncounterLocationService;
import com.openhis.administration.service.IEncounterService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.inpatientmanage.appservice.IAdmissionAppService;
import com.openhis.web.inpatientmanage.dto.AdmissionDto;
import com.openhis.web.inpatientmanage.dto.AdmissionInitPageDto;
import com.openhis.web.inpatientmanage.dto.AdmissionSearchParam;
import com.openhis.web.inpatientmanage.dto.AdmissionUpDto;
import com.openhis.web.inpatientmanage.mapper.AdmissionMapper;
import com.openhis.web.patientmanage.appservice.IPatientInformationService;
import com.openhis.web.patientmanage.dto.PatientInformationDto;

/**
 * 住院管理 实现类
 *
 * @author liuhr
 * @since 2025/04/07
 */
@Service
public class AdmissionAppServiceImpl implements IAdmissionAppService {

    @Resource
    private AdmissionMapper admissionMapper;

    @Resource
    private IPatientInformationService patientInformationService;

    @Resource
    private IEncounterService encounterService;

    @Resource
    private IEncounterLocationService encounterLocationService;

    @Resource
    private IAccountService accountService;

    @Resource
    private NurseStationPendAdmAppMapper nurseStationPendAdmAppMapper;

    /**
     * 病获取住院信息初期数据列表
     *
     * @return 住院信息初期数据列表
     */
    @Override
    public R<?> getAdmissionInfoInit() {

        AdmissionInitPageDto initDto = new AdmissionInitPageDto();
        // 入院类型列表
        List<AdmissionInitPageDto.statusEnumOption> statusEnumOptions1 = Stream.of(AdmissionType.values())
            .map(status -> new AdmissionInitPageDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        initDto.setAdmissionTypeList(statusEnumOptions1);

        // 入院方式列表
        List<AdmissionInitPageDto.statusEnumOption> statusEnumOptions2 = Stream.of(AdmissionMethod.values())
            .map(status -> new AdmissionInitPageDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        initDto.setAdmissionMethodList(statusEnumOptions2);

        // 优先级编码列表：患者病情下拉选
        List<AdmissionInitPageDto.statusEnumOption> statusEnumOptions3 = Stream.of(PriorityLevel.values())
            .map(status -> new AdmissionInitPageDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        initDto.setPriorityEnumList(statusEnumOptions3);

        return R.ok(initDto);

    }

    /**
     * 获取住院信息 分页显示
     *
     * @param admissionSearchParam 查询参数
     * @param searchKey 模糊查询
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return 住院信息
     */
    @Override
    public R<?> getAdmissionInfoPage(AdmissionSearchParam admissionSearchParam, String searchKey, Integer pageNo,
        Integer pageSize, HttpServletRequest request) {

        // 构建查询条件
        QueryWrapper<AdmissionDto> queryWrapper = HisQueryUtils.buildQueryWrapper(admissionSearchParam, searchKey,
            new HashSet<>(Arrays.asList(CommonConstants.FieldName.Name, CommonConstants.FieldName.PyStr,
                CommonConstants.FieldName.WbStr)),
            request);

        // 分页查询,查询住院
        IPage<AdmissionDto> admissionInfoPage = admissionMapper.getPage(new Page<>(pageNo, pageSize),
            EncounterClass.IMP.getValue(), LocationForm.WARD.getValue(), queryWrapper);

        admissionInfoPage.getRecords().forEach(e -> {
            // 性别枚举类回显赋值
            e.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, e.getGenderEnum()));
            // 计算年龄
            e.setAgeString(AgeCalculatorUtil.getAge(e.getBirthDate()));

        });

        // 返回【住院信息列表DTO】分页
        return R.ok(admissionInfoPage);
    }

    /**
     * 住院无档登记
     *
     * @param admissionUpDto 住院登记信息
     */
    @Override
    public R<?> addAdmissionInfo(AdmissionUpDto admissionUpDto) {

        // 1.添加病人信息
        PatientInformationDto patientInformationDto = convertToPatientInformationDto(admissionUpDto);

        R<?> addPatientResult = patientInformationService.addPatient(patientInformationDto);
        // 检查返回值的状态码
        boolean insertPatientSuccess = (addPatientResult.getCode() == 200);

        // 2.添加就诊信息,3.添加就诊账户,4.添加就诊病区
        admissionUpDto.setYbClassEnum(EncounterYbClass.ORDINARY_HOSPITALIZATION.getValue());
        boolean encounterLocationAndAccountSuccess = handleEncounterLocationAccount(admissionUpDto);

        if (insertPatientSuccess && encounterLocationAndAccountSuccess) {
            return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00001, new Object[] {"住院无档登记"}));
        } else {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00003, null));
        }

    }

    /**
     * 登记
     *
     * @param admissionUpDto 住院登记信息
     */
    @Override
    public R<?> editAdmissionInfo(AdmissionUpDto admissionUpDto) {

        // 1.修改病人信息
        PatientInformationDto patientInformationDto = convertToPatientInformationDto(admissionUpDto);

        R<?> editPatientResult = patientInformationService.editPatient(patientInformationDto);
        // 检查返回值的状态码
        boolean updatePatientSuccess = (editPatientResult.getCode() == 200);

        // 2.修改就诊信息,3.添加就诊账户,4.添加就诊病区
        boolean encounterLocationAndAccountSuccess = handleEncounterLocationAccount(admissionUpDto);

        if (updatePatientSuccess && encounterLocationAndAccountSuccess) {
            return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"住院登记"}));
        } else {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }

    }

    /**
     * 住院登记,添加就诊账户和就诊病区
     *
     * @param admissionUpDto 住院登记信息
     * @return 住院登记详细查询结果
     */
    public boolean handleEncounterLocationAccount(AdmissionUpDto admissionUpDto) {

        // 2.修改或者插入就诊信息
        Encounter encounter = new Encounter();
        encounter.setId(admissionUpDto.getId()).setPatientId(admissionUpDto.getPatientId())
            .setStatusEnum(EncounterStatus.IN_PROGRESS.getValue()).setOrganizationId(admissionUpDto.getOrganizationId())
            .setAdmitSourceCode(admissionUpDto.getAdmitSourceCode()).setInWayCode(admissionUpDto.getInWayCode())
            .setStartTime(admissionUpDto.getStartTime()).setClassEnum(EncounterClass.IMP.getValue())
            .setPriorityEnum(admissionUpDto.getPriorityEnum()).setYbClassEnum(admissionUpDto.getYbClassEnum())
            .setSubjectStatusEnum(EncounterSubjectStatus.PLANNED.getValue());

        boolean encounterSuccess = encounterService.saveOrUpdateEncounter(encounter);

        // 3.添加就诊账户account
        Account account = new Account();
        account.setName(admissionUpDto.getName()).setPatientId(admissionUpDto.getPatientId())
            .setEncounterId(admissionUpDto.getId())
            //账户状态是有效的
            .setStatusEnum(AccountStatus.ACTIVE.getValue())
            //结账状态是可用的
            .setBillingStatusEnum(AccountBillingStatus.OPEN.getValue());
        boolean updateAccountSuccess = accountService.saveOrUpdateAccount(account);

        // 4.添加就诊病区location
        EncounterLocation encounterLocation = new EncounterLocation();
        encounterLocation.setLocationId(admissionUpDto.getWardLocationId()).setEncounterId(admissionUpDto.getId())
            .setFormEnum(LocationForm.WARD.getValue()).setStartTime(admissionUpDto.getStartTime())
            .setStatusEnum(EncounterLocationStatus.ACTIVE.getValue());
        boolean encounterLocationSuccess = encounterLocationService.saveOrUpdateEncounterLocation(encounterLocation);

        return encounterSuccess && updateAccountSuccess && encounterLocationSuccess;
    }

    /**
     * 住院登记详细查询
     *
     * @param id 就诊ID
     * @return 住院登记详细查询结果
     */
    @Override
    public R<?> getAdmissionOne(@RequestParam Long id) {

        // 获取租户ID
        Integer tenantId = SecurityUtils.getLoginUser().getTenantId();
        // 根据ID查询 住院登记详细
        AdmissionUpDto admissionUpDto = admissionMapper.getAdmissionOne(EncounterClass.IMP.getValue(),
            EncounterStatus.DISCHARGED.getValue(), LocationForm.BED.getValue(), LocationForm.WARD.getValue(),
            ParticipantType.ADMITTER.getCode(), LocationBedStatus.O.getValue(), id, tenantId);

        //查询患者病情站诊断
        List<String> descriptionList = nurseStationPendAdmAppMapper.getDescriptionList(id);
        // 诊断病情拼接
        if (descriptionList != null && !descriptionList.isEmpty()) {
            admissionUpDto.setDescriptions(String.join(CommonConstants.Common.COMMA, descriptionList));
        }

        return R.ok(admissionUpDto);
    }

    /**
     * 将 AdmissionUpDto 的字段值手动设置到 PatientInformationDto 中
     * 
     * @param admissionUpDto 源对象
     * @return 目标对象
     */
    public PatientInformationDto convertToPatientInformationDto(AdmissionUpDto admissionUpDto) {
        return new PatientInformationDto().setId(admissionUpDto.getPatientId()).setName(admissionUpDto.getName())
            .setGenderEnum(admissionUpDto.getGenderEnum()).setBirthDate(admissionUpDto.getBirthDate())
            .setMaritalStatusEnum(admissionUpDto.getMaritalStatusEnum()).setPrfsEnum(admissionUpDto.getPrfsEnum())
            .setPhone(admissionUpDto.getPhone()).setAddress(admissionUpDto.getAddress())
            .setNationalityCode(admissionUpDto.getNationalityCode()).setIdCard(admissionUpDto.getIdCard())
            .setPyStr(admissionUpDto.getPyStr()).setWbStr(admissionUpDto.getWbStr())
            .setWorkCompany(admissionUpDto.getWorkCompany()).setNativePlace(admissionUpDto.getNativePlace())
            .setCountryCode(admissionUpDto.getCountryCode()).setLinkName(admissionUpDto.getLinkName())
            .setLinkRelationCode(admissionUpDto.getLinkRelationCode()).setLinkTelcom(admissionUpDto.getLinkTelcom())
            .setOrganizationId(admissionUpDto.getOrganizationId());
    }

}
