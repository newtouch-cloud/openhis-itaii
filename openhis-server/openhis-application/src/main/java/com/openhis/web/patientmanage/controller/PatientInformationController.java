package com.openhis.web.patientmanage.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.openhis.web.patientmanage.appservice.IPatientInformationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.enums.AssignSeqEnum;
import com.core.common.utils.*;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.administration.domain.Patient;
import com.openhis.administration.service.IPatientService;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
import com.openhis.common.utils.EnumUtils;
import com.openhis.web.patientmanage.dto.PatientInformationDto;
import com.openhis.web.patientmanage.dto.PatientInfoInitDto;
import com.openhis.web.patientmanage.mapper.PatientManageMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 病人管理——病人信息
 *
 * @author liuhr
 * @date 2025/2/22
 */
@RestController
@RequestMapping("/patientmanage/information")
@Slf4j
@AllArgsConstructor
public class PatientInformationController {

    @Autowired
    private IPatientService patientService;

    @Autowired(required = false)
    private AssignSeqUtil assignSeqUtil;

    @Autowired(required = false)
    PatientManageMapper patientManageMapper;
//
//    @Autowired(required = false)
//    StringUtils stringUtils;

    @Autowired
    IPatientInformationService patientInformationService;

    // todo 暂且机构ID写死，后续从登录里取得
    private final Long organizationId = 91L;

    /**
     * 病人信息记录初期数据列表
     *
     * @return
     */
    @GetMapping("/init")
    public R<?> getPatientInfoInit() {

        return R.ok(patientInformationService.getPatientInfoInit());
    }

    /**
     * 添加病人信息
     *
     * @param patientInformationDto 病人信息
     */
//    @PostMapping("/patient-information")
//    public R<?> addPatient(@Validated @RequestBody PatientInformationDto patientInformationDto) {
//
//        Patient patient = new Patient();
//        BeanUtils.copyProperties(patientInformationDto, patient);
//
//        // 使用基础采番，设置病人ID
//        String code = assignSeqUtil.getSeq(AssignSeqEnum.PATIENT_NUM.getPrefix());
//        patient.setBusNo(code);
//
//        // 设置机构ID
//        patient.setOrganizationId(organizationId);
//        // 设置生日
//        patient.setBirthDate(patientService.extractBirthday(patient.getIdCard()));
//        // 设置拼音首拼
//        patient.setPyStr(ChineseConvertUtils.toPinyinFirstLetter(patient.getName()));
//        // 设置五笔首拼
//        patient.setWbStr(ChineseConvertUtils.toWBFirstLetter(patient.getName()));
//        // 设置死亡时间
//        if (patientService.isFuture(patientInformationDto.getDeceasedDate())) {
//            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00003, new Object[] {"死亡时间未来时"}));
//        }
//        patient.setDeceasedDate(DateUtils.parseDate(patientInformationDto.getDeceasedDate()));
//        // 调用服务层保存病人信息
//        boolean savePatientSuccess = patientService.save(patient);
//
//        if (!savePatientSuccess) {
//            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00003, null));
//        }
//
//        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00001, new Object[] {"病人信息"}));
//    }

    /**
     * 修改病人信息
     *
     * @param patientInformationDto 病人信息
     */
    @PutMapping("/patient-information")
    public R<?> editPatient(@Validated @RequestBody PatientInformationDto patientInformationDto) {

        Patient patient = new Patient();
        BeanUtils.copyProperties(patientInformationDto, patient);

        // 设置生日
        patient.setBirthDate(patientService.extractBirthday(patient.getIdCard()));
        // 设置拼音首拼
        patient.setPyStr(ChineseConvertUtils.toPinyinFirstLetter(patient.getName()));
        // 设置五笔首拼
        patient.setWbStr(ChineseConvertUtils.toWBFirstLetter(patient.getName()));
        // 设置死亡时间
        if (patientService.isFuture(patientInformationDto.getDeceasedDate())) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, new Object[] {"死亡时间未来时"}));
        }
        patient.setDeceasedDate(DateUtils.parseDate(patientInformationDto.getDeceasedDate()));

        // 调用服务层更新病人信息
        return patientService.updateById(patient)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00001, new Object[] {"病人信息"}))
            : R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));

    }

    /**
     * 分页查询病人信息,可选条件
     *
     * @param busNo 病人ID（可选）
     * @param name 病人姓名（可选）
     * @param pageNo 页码（默认为1）
     * @param pageSize 每页大小（默认为10）
     */
    @GetMapping("/patient-information-page")
    public R<?> getPatient(@RequestParam(required = false) String busNo, @RequestParam(required = false) String name,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

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
        return R.ok(patientInformationPage);
    }

}
