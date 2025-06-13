package com.openhis.web.nursestation.appservice.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.common.core.domain.R;
import com.core.common.utils.AgeCalculatorUtil;
import com.core.common.utils.DateUtils;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.SecurityUtils;
import com.openhis.administration.domain.*;
import com.openhis.administration.service.*;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
import com.openhis.common.utils.EnumUtils;
import com.openhis.web.nursestation.appservice.INurseStationPendAdmAppService;
import com.openhis.web.nursestation.dto.BedForAdmissionDto;
import com.openhis.web.nursestation.dto.BedForAdmissionInitDto;
import com.openhis.web.nursestation.dto.PendingAdmissionDto;
import com.openhis.web.nursestation.mapper.NurseStationPendAdmAppMapper;

/**
 * 待入科 应用接口实现
 *
 * @author liuhr
 * @date 2025/4/14
 */
@Service
public class NurseStationPendAdmAppServiceImpl implements INurseStationPendAdmAppService {

    @Autowired
    private NurseStationPendAdmAppMapper nsPendAdmAppMapper;

    @Autowired
    private IEncounterLocationService encounterLocationService;

    @Autowired
    private IPractitionerRoleService practitionerRoleService;

    @Autowired
    private ILocationService locationService;

    @Autowired
    private IEncounterService encounterService;
    @Autowired
    private IEncounterParticipantService encounterParticipantService;

    /**
     * 待入科的初始化数据
     *
     * @return
     */
    @Override
    public R<?> getPendAdmInit() {

        BedForAdmissionInitDto initDto = new BedForAdmissionInitDto();

        // 获取当前登录账号的参与者id
        Long practitionerId = SecurityUtils.getLoginUser().getPractitionerId();
        // 当前id权限下可以切换科室的列表
        List<Long> orgIds = practitionerRoleService.getOrgIdsByPractitionerId(practitionerId);
        // 查询该账号权限可以切换的科室列表
        List<Location> locationList = locationService.getLocationList(orgIds);

        List<BedForAdmissionInitDto.exeOrganization> locationListOptions = locationList.stream()
            .map(location -> new BedForAdmissionInitDto.exeOrganization(location.getId(), location.getName()))
            .collect(Collectors.toList());
        initDto.setLocationListOptions(locationListOptions);

        // 获取患者病情列表
        List<BedForAdmissionInitDto.statusEnumOption> statusEnumOptions = Stream.of(PriorityLevel.values())
            .map(status -> new BedForAdmissionInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        initDto.setPriorityListOptions(statusEnumOptions);

        return R.ok(initDto);

    }

    /**
     * 选择科室后，查询科室对应病区列表
     *
     * @return 病区列表
     */
    @Override
    public R<?> getWardLocationList(Long orgId) {

        List<Location> locationList = locationService.getWardList(orgId);
        List<BedForAdmissionInitDto.exeOrganization> wardList = locationList.stream()
            .map(location -> new BedForAdmissionInitDto.exeOrganization(location.getId(), location.getName()))
            .collect(Collectors.toList());

        return R.ok(wardList);

    }

    /**
     * 查询待入科的患者列表
     *
     * @return 待入科的患者列表
     */
    @Override
    public R<?> getPendAdmInfo() {

        List<PendingAdmissionDto> pendingAdmissionList =
            nsPendAdmAppMapper.getPendAdmInfo(EncounterClass.IMP.getValue(), EncounterLocationStatus.ACTIVE.getValue(),
                LocationForm.WARD.getValue(), LocationForm.BED.getValue());

        pendingAdmissionList.forEach(e -> {
            // 计算年龄
            e.setAgeString(AgeCalculatorUtil.getAge(e.getBirthDate()));
            // 取病区对应的科室
            Location location = locationService.getLocationById(e.getWardLocationId());
            Long orgId = null;
            if (location != null) {
                orgId = location.getOrganizationId();
            }

            // 判断是否跨科
            if (orgId == e.getOrganizationId()) {
                e.setCrossDeptStatus(Whether.NO.getValue());
            } else {
                e.setCrossDeptStatus(Whether.YES.getValue());
            }
            // 是否跨科 回显赋值
            e.setCrossDeptStatus_enumText(EnumUtils.getInfoByValue(Whether.class, e.getCrossDeptStatus()));

            List<String> descriptionList = nsPendAdmAppMapper.getDescriptionList(e.getId());
            // 过滤掉 null 值
            List<String> filteredList = descriptionList.stream().filter(Objects::nonNull).collect(Collectors.toList());
            // 诊断病情拼接
            if (descriptionList != null && !descriptionList.isEmpty()) {
                // 拼接字符串
                if (!filteredList.isEmpty()) {
                    e.setDescriptions(String.join(CommonConstants.Common.COMMA, filteredList));
                }
            }

        });

        return R.ok(pendingAdmissionList);
    }

    /**
     * 病区的病床信息
     *
     * @param wardLocationId 病区的ID
     * @return 病床列表
     */
    @Override
    public R<?> getBedList(Long wardLocationId) {

        List<Location> locationList = locationService.getBedList(wardLocationId);
        List<BedForAdmissionInitDto.exeOrganization> bedList = locationList.stream()
            .map(location -> new BedForAdmissionInitDto.exeOrganization(location.getId(), location.getName()))
            .collect(Collectors.toList());

        return R.ok(bedList);

    }

    /**
     * 根据入院科室查询医生列表
     *
     * @param orgId 入院科室Id
     * @return 医生列表
     */
    @Override
    public R<?> getDoctorList(Long orgId) {

        List<PractitionerRole> practitionerRoleList = practitionerRoleService.getDoctorList(orgId);
        List<BedForAdmissionInitDto.exeOrganization> doctorList = practitionerRoleList.stream()
            .map(practitionerRole -> new BedForAdmissionInitDto.exeOrganization(practitionerRole.getPractitionerId(),
                practitionerRole.getName()))
            .collect(Collectors.toList());

        return R.ok(doctorList);
    }

    /**
     * 根据入院科室查询护士列表
     *
     * @param orgId 入院科室Id
     * @return 护士列表
     */
    @Override
    public R<?> getNurseList(Long orgId) {

        List<PractitionerRole> practitionerRoleList = practitionerRoleService.getNurseList(orgId);
        List<BedForAdmissionInitDto.exeOrganization> nurseList = practitionerRoleList.stream()
            .map(practitionerRole -> new BedForAdmissionInitDto.exeOrganization(practitionerRole.getPractitionerId(),
                practitionerRole.getName()))
            .collect(Collectors.toList());

        return R.ok(nurseList);
    }

    /**
     * 患者选床入科
     *
     * @param bedInfoDto 病床信息
     * @return 选床入科
     */
    @Override
    public R<?> selectBedForAdmission(BedForAdmissionDto bedInfoDto) {

        // 1.更新就诊表得患者病情(优先级编码)
        Encounter encounter = new Encounter();
        boolean encounterSuccess =
            encounterService.updatePriorityEnumById(bedInfoDto.getId(), bedInfoDto.getPriorityEnum());

        // 2-1.添加就诊病床location
        EncounterLocation encounterLocation = new EncounterLocation();
        encounterLocation.setLocationId(bedInfoDto.getBedLocationId()).setEncounterId(bedInfoDto.getId())
            // 设置物理枚举为 8:病床
            .setFormEnum(LocationForm.BED.getValue())
            // 状态为使用中
            .setStatusEnum(EncounterLocationStatus.ACTIVE.getValue())
            // 入科时间
            .setStartTime(bedInfoDto.getStartTime());
        boolean encounterLocationSuccess = encounterLocationService.saveOrUpdateEncounterLocation(encounterLocation);

        // 2-2.如果病区更改了，把之前的病区状态修改为已完成，并插入该就诊号的一个病区记录
        // todo：分床的选的病区，科室，需要存吗，还是只需要存病床号？？

        // 3.更新location表中病床状态
        boolean updateLocationSuccess =
            locationService.updateStatusById(bedInfoDto.getBedLocationId(), LocationBedStatus.O.getValue());

        // 4.添加就诊参与者信息
        List<EncounterParticipant> encounterParticipants = new ArrayList<>();
        // 4-1 住院医生
        EncounterParticipant ep1 = new EncounterParticipant();
        ep1.setTypeCode(ParticipantType.ATTENDING_DOCTOR.getCode()).setEncounterId(bedInfoDto.getId())
            .setPractitionerId(bedInfoDto.getAttendingDoctorId()).setStartTime(DateUtils.getNowDate());

        // 4-2 主任医生
        EncounterParticipant ep2 = new EncounterParticipant();
        ep2.setTypeCode(ParticipantType.CHIEF_DOCTOR.getCode()).setEncounterId(bedInfoDto.getId())
            .setPractitionerId(bedInfoDto.getChiefDoctorId()).setStartTime(DateUtils.getNowDate());

        // 4-3 主治医生
        EncounterParticipant ep3 = new EncounterParticipant();
        ep3.setTypeCode(ParticipantType.PRINCIPAL_DOCTOR.getCode()).setEncounterId(bedInfoDto.getId())
            .setPractitionerId(bedInfoDto.getPrincipalDoctorId()).setStartTime(DateUtils.getNowDate());

        // 4-4 责任护士
        EncounterParticipant ep4 = new EncounterParticipant();
        ep4.setTypeCode(ParticipantType.ATTENDER.getCode()).setEncounterId(bedInfoDto.getId())
            .setPractitionerId(bedInfoDto.getAttenderId()).setStartTime(DateUtils.getNowDate());

        encounterParticipants.add(ep1);
        encounterParticipants.add(ep2);
        encounterParticipants.add(ep3);
        encounterParticipants.add(ep4);
        boolean epSaveSuccess = encounterParticipantService.saveBatch(encounterParticipants);

        return encounterSuccess && epSaveSuccess && updateLocationSuccess && encounterLocationSuccess
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, new Object[] {"选床入科"}))
            : R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00003, new Object[] {"选床入科"}));

    }

}
