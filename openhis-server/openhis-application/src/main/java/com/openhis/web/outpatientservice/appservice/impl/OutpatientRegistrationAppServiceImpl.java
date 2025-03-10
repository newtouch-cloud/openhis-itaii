package com.openhis.web.outpatientservice.appservice.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.AgeCalculatorUtil;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.administration.domain.*;
import com.openhis.administration.mapper.PatientMapper;
import com.openhis.administration.service.*;
import com.openhis.clinical.domain.ConditionDefinition;
import com.openhis.clinical.mapper.ConditionDefinitionMapper;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
import com.openhis.common.enums.PractitionerRole;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisPageUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.financial.domain.Contract;
import com.openhis.financial.mapper.ContractMapper;
import com.openhis.web.basicservice.dto.HealthcareServiceDto;
import com.openhis.web.basicservice.mapper.HealthcareServiceBizMapper;
import com.openhis.web.outpatientservice.appservice.IOutpatientRegistrationAppService;
import com.openhis.web.outpatientservice.dto.*;
import com.openhis.web.outpatientservice.mapper.OutpatientRegistrationAppMapper;

/**
 * 门诊挂号 应用实现类
 */
@Service
public class OutpatientRegistrationAppServiceImpl implements IOutpatientRegistrationAppService {

    @Resource
    PatientMapper patientMapper;

    @Resource
    ContractMapper contractMapper;

    @Resource
    ConditionDefinitionMapper conditionDefinitionMapper;

    @Resource
    OutpatientRegistrationAppMapper outpatientRegistrationAppMapper;

    @Resource
    HealthcareServiceBizMapper healthcareServiceBizMapper;

    @Resource
    IEncounterService iEncounterService;

    @Resource
    IEncounterLocationService iEncounterLocationService;

    @Resource
    IEncounterParticipantService iEncounterParticipantService;

    @Resource
    IAccountService iAccountService;

    @Resource
    IChargeItemService iChargeItemService;

    /**
     * 门诊挂号 - 查询患者信息
     *
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 患者信息
     */
    @Override
    public Page<PatientMetadata> getPatientMetadataBySearchKey(String searchKey, Integer pageNo, Integer pageSize) {
        // 构建查询条件
        QueryWrapper<Patient> queryWrapper = HisQueryUtils.buildQueryWrapper(null, searchKey,
            new HashSet<>(Arrays.asList("id_card", "name", "py_str", "wb_str")), null);
        // 设置排序
        queryWrapper.orderByDesc("update_time");
        // 患者信息
        Page<PatientMetadata> patientMetadataPage =
            HisPageUtils.selectPage(patientMapper, queryWrapper, pageNo, pageSize, PatientMetadata.class);
        // 现有就诊过的患者id集合
        List<Long> patientIdList =
            iEncounterService.list().stream().map(e -> e.getPatientId()).collect(Collectors.toList());

        patientMetadataPage.getRecords().forEach(e -> {
            // 性别枚举
            e.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, e.getGenderEnum()));
            // 计算年龄
            e.setAge(AgeCalculatorUtil.getAge(e.getBirthDate()));
            // 初复诊
            e.setFirstEnum_enumText(patientIdList.contains(e.getId()) ? EncounterType.FOLLOW_UP.getInfo()
                : EncounterType.INITIAL.getInfo());

        });
        return patientMetadataPage;
    }

    /**
     * 查询费用性质
     *
     * @return 费用性质
     */
    @Override
    public List<ContractMetadata> getContractMetadata() {
        // TODO: Contract表的基础数据维护还没做,具体不知道状态字段的取值是什么,先查询默认值为0的数据
        List<Contract> ContractList =
            contractMapper.selectList(new LambdaQueryWrapper<Contract>().eq(Contract::getStatusEnum, 0));
        // 复制同名字段并 return
        return ContractList.stream().map(contract -> {
            ContractMetadata metadata = new ContractMetadata();
            try {
                BeanUtils.copyProperties(contract, metadata);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return metadata;
        }).collect(Collectors.toList());
    }

    /**
     * 查询诊断信息
     *
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 诊断信息
     */
    @Override
    public Page<ConditionDefinitionMetadata> getConditionDefinitionMetadataSearchKey(String searchKey, Integer pageNo,
        Integer pageSize) {
        // 构建查询条件
        ConditionDefinition conditionDefinition = new ConditionDefinition();
        // 查询状态是有效的
        conditionDefinition.setStatusEnum(PublicationStatus.ACTIVE.getValue());
        QueryWrapper<ConditionDefinition> queryWrapper = HisQueryUtils.buildQueryWrapper(conditionDefinition, searchKey,
            new HashSet<>(Arrays.asList("name", "py_str", "wb_str")), null);
        // 设置排序
        queryWrapper.orderByDesc("update_time");
        // 诊断信息
        Page<ConditionDefinitionMetadata> conditionDefinitionMetadataPage = HisPageUtils
            .selectPage(conditionDefinitionMapper, queryWrapper, pageNo, pageSize, ConditionDefinitionMetadata.class);
        conditionDefinitionMetadataPage.getRecords().forEach(e -> {
            // 医保标记
            e.setYbFlag_enumText(EnumUtils.getInfoByValue(WhetherContainUnknown.class, e.getYbFlag()));
            // 医保对码标记
            e.setYbMatchFlag_enumText(EnumUtils.getInfoByValue(WhetherContainUnknown.class, e.getYbMatchFlag()));
        });
        return conditionDefinitionMetadataPage;
    }

    /**
     * 根据位置id筛选医生
     *
     * @param locationId 位置ID
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 筛选医生
     */
    @Override
    public IPage<PractitionerMetadata> getPractitionerMetadataByLocationId(Long locationId, String searchKey,
        Integer pageNo, Integer pageSize) {
        // 构建查询条件
        QueryWrapper<PractitionerMetadata> queryWrapper = HisQueryUtils.buildQueryWrapper(null, searchKey,
            new HashSet<>(Arrays.asList("name", "py_str", "wb_str")), null);
        IPage<PractitionerMetadata> practitionerMetadataPage =
            outpatientRegistrationAppMapper.getPractitionerMetadataPage(new Page<>(pageNo, pageSize), locationId,
                PractitionerRole.DOCTOR.getCode(), queryWrapper);
        practitionerMetadataPage.getRecords().forEach(e -> {
            // 性别
            e.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, e.getGenderEnum()));
        });
        return practitionerMetadataPage;
    }

    /**
     * 根据机构id筛选服务项目
     *
     * @param organizationId 机构id
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 服务项目
     */
    @Override
    public IPage<HealthcareServiceDto> getHealthcareMetadataByOrganizationId(Long organizationId, String searchKey,
        Integer pageNo, Integer pageSize) {
        // 构建查询条件
        HealthcareServiceDto healthcareServiceDto = new HealthcareServiceDto();
        healthcareServiceDto.setOfferedOrgId(organizationId);
        QueryWrapper<HealthcareServiceDto> queryWrapper = HisQueryUtils.buildQueryWrapper(healthcareServiceDto,
            searchKey, new HashSet<>(Arrays.asList("name", "charge_name")), null);
        return healthcareServiceBizMapper.getHealthcareServicePage(new Page<>(pageNo, pageSize),
            CommonConstants.TableName.ADM_HEALTHCARE_SERVICE, queryWrapper);
    }

    /**
     * 保存挂号
     *
     * @param outpatientRegistrationAddParam 就诊表单信息
     * @return 结果
     */
    @Override
    public R<?> saveRegister(OutpatientRegistrationAddParam outpatientRegistrationAddParam) {
        // 就诊管理-表单数据
        EncounterFormData encounterFormData = outpatientRegistrationAddParam.getEncounterFormData();
        // 就诊位置管理-表单数据
        EncounterLocationFormData encounterLocationFormData =
            outpatientRegistrationAddParam.getEncounterLocationFormData();
        // 就诊参数者管理-表单数据
        EncounterParticipantFormData encounterParticipantFormData =
            outpatientRegistrationAddParam.getEncounterParticipantFormData();
        // 就诊账户管理-表单数据
        AccountFormData accountFormData = outpatientRegistrationAddParam.getAccountFormData();
        // 费用项管理-表单数据
        ChargeItemFormData chargeItemFormData = outpatientRegistrationAddParam.getChargeItemFormData();

        // 患者ID
        Long patientId = encounterFormData.getPatientId();
        // 服务项目ID
        Long serviceTypeId = encounterFormData.getServiceTypeId();
        // 校验是否重复挂号
        Integer num = outpatientRegistrationAppMapper.getNumByPatientIdAndOrganizationId(patientId, serviceTypeId);
        if (num > 0) {
            return R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00008, null));
        }
        // 保存就诊信息
        Encounter encounter = new Encounter();
        BeanUtils.copyProperties(encounterFormData, encounter);
        // 就诊ID
        Long encounterId = iEncounterService.saveEncounterByRegister(encounter);
        // 保存就诊位置信息
        encounterLocationFormData.setEncounterId(encounterId);
        EncounterLocation encounterLocation = new EncounterLocation();
        BeanUtils.copyProperties(encounterLocationFormData, encounterLocation);
        iEncounterLocationService.saveEncounterLocationByRegister(encounterLocation);
        // 保存就诊参数者信息
        encounterParticipantFormData.setEncounterId(encounterId);
        EncounterParticipant encounterParticipant = new EncounterParticipant();
        BeanUtils.copyProperties(encounterParticipantFormData, encounterParticipant);
        iEncounterParticipantService.saveEncounterParticipantByRegister(encounterParticipant);
        // 保存就诊账户信息
        accountFormData.setEncounterId(encounterId);
        Account account = new Account();
        BeanUtils.copyProperties(accountFormData, account);
        // 账户ID
        Long accountId = iAccountService.saveAccountByRegister(account);
        // 保存就诊费用项
        chargeItemFormData.setEncounterId(encounterId);
        chargeItemFormData.setAccountId(accountId);
        ChargeItem chargeItem = new ChargeItem();
        BeanUtils.copyProperties(chargeItemFormData, chargeItem);
        iChargeItemService.saveChargeItemByRegister(chargeItem);

        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, new Object[] {"挂号"}));
    }

    /**
     * 查询当日就诊数据
     *
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 当日就诊数据
     */
    @Override
    public IPage<CurrentDayEncounterDto> getCurrentDayEncounter(String searchKey, Integer pageNo, Integer pageSize) {
        // 构建查询条件
        QueryWrapper<CurrentDayEncounterDto> queryWrapper = HisQueryUtils.buildQueryWrapper(null, searchKey,
            new HashSet<>(Arrays.asList("patient_name", "organization_name", "practitioner_name", "healthcare_name")),
            null);

        IPage<CurrentDayEncounterDto> currentDayEncounter = outpatientRegistrationAppMapper
            .getCurrentDayEncounter(new Page<>(pageNo, pageSize), ParticipantType.ADMITTER.getCode(), queryWrapper);
        currentDayEncounter.getRecords().forEach(e -> {
            // 性别
            e.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, e.getGenderEnum()));
            // 就诊状态
            e.setStatusEnum_enumText(EnumUtils.getInfoByValue(EncounterStatus.class, e.getStatusEnum()));
        });
        return currentDayEncounter;
    }

}
