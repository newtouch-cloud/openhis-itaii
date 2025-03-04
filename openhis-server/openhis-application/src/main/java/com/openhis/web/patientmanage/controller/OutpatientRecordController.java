package com.openhis.web.patientmanage.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.openhis.common.enums.AdministrativeGender;
import com.openhis.common.enums.EncounterSubjectStatus;
import com.openhis.web.patientmanage.dto.OutpatientRecordDto;
import com.openhis.web.patientmanage.dto.OutpatientRecordSearchParam;
import com.openhis.web.patientmanage.dto.PatientListDto;
import com.openhis.web.patientmanage.mapper.PatientManageMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 门诊记录
 *
 * @author liuhr
 * @date 2025/2/28
 */
@RestController
@RequestMapping("/patientmanage/records")
@Slf4j
public class OutpatientRecordController {

    @Autowired(required = false)
    PatientManageMapper patientManageMapper;

    /**
     * 获取医生名字列表
     */
    @GetMapping("/list-doctornames")
    public R<?> getDoctorNames() {
        // 获取医生名字列表
        List<String> listDoctorNames = patientManageMapper.getDoctorNames();

        return R.ok(listDoctorNames);
    }

    /**
     * 获取性别列表
     */
    @GetMapping("/list-administrativegender")
    public R<?> getAdministrativeGender() {
        // 获取性别
        List<AdministrativeGender> statusList = Arrays.asList(AdministrativeGender.values());
        List<PatientListDto> dtos = new ArrayList<>();
        // 取得更新值
        for (AdministrativeGender status : statusList) {
            PatientListDto dto = new PatientListDto();
            dto.setValue(status.getValue());
            dto.setInfo(status.getInfo());
            dtos.add(dto);
        }
        return R.ok(dtos);
    }

    /**
     * 获取就诊状态列表
     */
    @GetMapping("/list-encountersubjectstatus")
    public R<?> getEncounterSubjectStatus() {
        // 获取就诊状态
        List<EncounterSubjectStatus> statusList = Arrays.asList(EncounterSubjectStatus.values());
        List<PatientListDto> dtos = new ArrayList<>();
        // 取得更新值
        for (EncounterSubjectStatus status : statusList) {
            PatientListDto dto = new PatientListDto();
            dto.setValue(status.getValue());
            dto.setInfo(status.getInfo());
            dtos.add(dto);
        }
        return R.ok(dtos);
    }

    /**
     * 分页查询门诊记录,可选条件
     *
     * @param outpatientRecordSearchParam 查询条件
     * @param pageNo 页码（默认为1）
     * @param pageSize 每页大小（默认为10）
     */
    @GetMapping("/outpatient-record-page")
    public R<?> getPatient(@RequestParam(required = false) OutpatientRecordSearchParam outpatientRecordSearchParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        // 跳过的记录数
        Integer offset = (pageNo - 1) * pageSize;
        // 连表查询患者信息
        List<OutpatientRecordDto> listOutpatientRecords =
            patientManageMapper.getOutpatientRecord(outpatientRecordSearchParam, pageSize, offset);
        // 查询总记录数
        long total = patientManageMapper.countOutpatientRecords(outpatientRecordSearchParam);
        // 创建Page对象并设置属性
        Page<OutpatientRecordDto> OutpatientRecordPage = new Page<>(pageNo, pageSize, total);
        OutpatientRecordPage.setRecords(listOutpatientRecords);

        return R.ok(OutpatientRecordPage);
    }

}
