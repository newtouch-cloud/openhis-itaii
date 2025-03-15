package com.openhis.web.patientmanage.appservice.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.openhis.common.enums.*;
import com.openhis.web.patientmanage.appservice.IPatientInformationService;
import com.openhis.web.patientmanage.dto.PatientInfoInitDto;
import org.springframework.stereotype.Service;

/**
 * 门诊患者
 *
 * @author liuhr
 * @date 2025/3/15
 */
@Service
public class PatientInformationServiceImpl implements IPatientInformationService {

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

}
