package com.openhis.web.chargemanage.appservice.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.openhis.administration.domain.*;
import com.openhis.financial.domain.PaymentReconciliation;
import liquibase.pro.packaged.A;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.AgeCalculatorUtil;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.SecurityUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.administration.mapper.PatientMapper;
import com.openhis.administration.service.*;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisPageUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.financial.domain.Contract;
import com.openhis.financial.mapper.ContractMapper;
import com.openhis.financial.service.IContractService;
import com.openhis.web.basicservice.dto.HealthcareServiceDto;
import com.openhis.web.basicservice.mapper.HealthcareServiceBizMapper;
import com.openhis.web.chargemanage.appservice.IOutpatientRegistrationAppService;
import com.openhis.web.chargemanage.dto.*;
import com.openhis.web.chargemanage.mapper.OutpatientRegistrationAppMapper;
import com.openhis.web.doctorstation.appservice.IDoctorStationAdviceAppService;
import com.openhis.web.paymentmanage.appservice.IPaymentRecService;
import com.openhis.web.paymentmanage.dto.CancelPaymentDto;
import com.openhis.web.paymentmanage.dto.CancelRegPaymentDto;
import com.openhis.workflow.service.IServiceRequestService;
import com.openhis.yb.model.CancelRegPaymentModel;
import com.openhis.yb.service.YbManager;

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

    @Resource
    IOrganizationService iOrganizationService;

    @Resource
    YbManager ybManager;

    @Resource
    IDoctorStationAdviceAppService iDoctorStationAdviceAppService;

    @Resource
    AssignSeqUtil assignSeqUtil;

    @Resource
    IServiceRequestService iServiceRequestService;

    @Resource
    IContractService iContractService;

    @Resource
    IPaymentRecService iPaymentRecService;

    /**
     * 门诊挂号 - 查询患者信息
     *
     * @param searchKey 模糊查询关键字
     * @param pageNo    当前页
     * @param pageSize  每页多少条
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
            e.setAge(e.getBirthDate() != null ? AgeCalculatorUtil.getAge(e.getBirthDate()) : "");
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
     * 查询门诊科室数据
     *
     * @return 门诊科室
     */
    @Override
    public List<OrgMetadata> getOrgMetadata() {
        List<Organization> list =
                iOrganizationService.getList(OrganizationType.DEPARTMENT.getValue(), OrganizationClass.CLINIC.getValue());
        List<OrgMetadata> orgMetadataList = new ArrayList<>();
        OrgMetadata orgMetadata;
        for (Organization organization : list) {
            orgMetadata = new OrgMetadata();
            BeanUtils.copyProperties(organization, orgMetadata);
            orgMetadataList.add(orgMetadata);
        }
        return orgMetadataList;
    }

    /**
     * 根据科室id筛选医生
     *
     * @param orgId     科室ID
     * @param searchKey 模糊查询关键字
     * @param pageNo    当前页
     * @param pageSize  每页多少条
     * @return 筛选医生
     */
    @Override
    public IPage<PractitionerMetadata> getPractitionerMetadataByLocationId(Long orgId, String searchKey, Integer pageNo,
                                                                           Integer pageSize) {
        // 构建查询条件
        QueryWrapper<PractitionerMetadata> queryWrapper = HisQueryUtils.buildQueryWrapper(null, searchKey,
                new HashSet<>(Arrays.asList("name", "py_str", "wb_str")), null);
        IPage<PractitionerMetadata> practitionerMetadataPage =
                outpatientRegistrationAppMapper.getPractitionerMetadataPage(new Page<>(pageNo, pageSize), orgId,
                        PractitionerRoles.DOCTOR.getCode(), queryWrapper);
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
     * @param searchKey      模糊查询关键字
     * @param pageNo         当前页
     * @param pageSize       每页多少条
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
                CommonConstants.TableName.ADM_HEALTHCARE_SERVICE, CommonConstants.TableName.WOR_ACTIVITY_DEFINITION,
                queryWrapper);
    }

    /**
     * 退号
     *
     * @param cancelRegPaymentDto 就诊id
     * @return 结果
     */
    @Override
    public R<?> returnRegister(CancelRegPaymentDto cancelRegPaymentDto) {
        Encounter byId = iEncounterService.getById(cancelRegPaymentDto.getEncounterId());
        if (EncounterStatus.CANCELLED.getValue().equals(byId.getStatusEnum())) {
            return R.fail(null, "该患者已经退号，请勿重复退号");
        }
        iEncounterService.returnRegister(cancelRegPaymentDto.getEncounterId());
        // 查询账户信息
        Account account = iAccountService
                .getOne(new LambdaQueryWrapper<Account>().eq(Account::getEncounterId, cancelRegPaymentDto.getEncounterId())
                        .ne(Account::getTypeCode, AccountType.PERSONAL_CASH_ACCOUNT.getCode())
                        .eq(Account::getEncounterFlag, Whether.YES.getValue()));

        CancelPaymentDto cancelPaymentDto = new CancelPaymentDto();
        BeanUtils.copyProperties(cancelRegPaymentDto, cancelPaymentDto);

        // 开通医保的处理
        if ("1".equals(SecurityUtils.getLoginUser().getOptionJson().getString(CommonConstants.Option.YB_SWITCH))
                && account != null && !"0000".equals(account.getContractNo())) {
            CancelRegPaymentModel model = new CancelRegPaymentModel();
            BeanUtils.copyProperties(cancelRegPaymentDto, model);
            ybManager.cancelReg(model);
            cancelPaymentDto.setSetlId(model.getSetlId());
            //return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, new Object[] {"医保退号"}));
        }

        R<?> result = iPaymentRecService.cancelRegPayment(cancelPaymentDto);

        PaymentReconciliation paymentRecon = null;
        if (PaymentReconciliation.class.isAssignableFrom(result.getData().getClass())) {
            paymentRecon = (PaymentReconciliation) result.getData();
        }
        if(paymentRecon!=null) {
            List<String> strings = Arrays.asList(paymentRecon.getChargeItemIds().split(","));
            List<Long> chargeItemIds = new ArrayList<>();
            for (String string : strings) {
                chargeItemIds.add(Long.parseLong(string));
            }
            // 更新收费状态：已退费
            if (!chargeItemIds.isEmpty()) {
                iChargeItemService.updatePaymentStatus(chargeItemIds, ChargeItemStatus.REFUNDED.getValue());
            }
        }

        // 2025/05/05 该处保存费用项后，会通过统一收费处理进行收费
        return R.ok(paymentRecon, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, new Object[]{"退号"}));
    }

    /**
     * 查询当日就诊数据
     *
     * @param searchKey 模糊查询关键字
     * @param pageNo    当前页
     * @param pageSize  每页多少条
     * @return 当日就诊数据
     */
    @Override
    public IPage<CurrentDayEncounterDto> getCurrentDayEncounter(String searchKey, Integer pageNo, Integer pageSize,
                                                                HttpServletRequest request) {
        // 构建查询条件
        QueryWrapper<CurrentDayEncounterDto> queryWrapper = HisQueryUtils.buildQueryWrapper(null, searchKey,
                new HashSet<>(Arrays.asList("patient_name", "organization_name", "practitioner_name", "healthcare_name")),
                request);

        IPage<CurrentDayEncounterDto> currentDayEncounter = outpatientRegistrationAppMapper.getCurrentDayEncounter(
                new Page<>(pageNo, pageSize), EncounterClass.AMB.getValue(), ParticipantType.ADMITTER.getCode(),
                queryWrapper, ChargeItemContext.REGISTER.getValue(), PaymentStatus.SUCCESS.getValue());
        currentDayEncounter.getRecords().forEach(e -> {
            // 性别
            e.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, e.getGenderEnum()));
            // 就诊状态
            e.setStatusEnum_enumText(EnumUtils.getInfoByValue(EncounterStatus.class, e.getStatusEnum()));
            // 计算年龄
            e.setAge(e.getBirthDate() != null ? AgeCalculatorUtil.getAge(e.getBirthDate()) : "");
        });
        return currentDayEncounter;
    }

    /**
     * 取消挂号
     *
     * @param encounterId 就诊id
     * @return 结果
     */
    @Override
    public R<?> cancelRegister(Long encounterId) {
        iEncounterService.removeById(encounterId);
        return R.ok("已取消挂号");
    }

}
