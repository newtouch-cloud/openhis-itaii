package com.openhis.web.patientmanage.controller;

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
import com.openhis.common.utils.EnumUtils;
import com.openhis.web.patientmanage.dto.OutpatientRecordDto;
import com.openhis.web.patientmanage.dto.OutpatientRecordSearchParam;
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
     * 分页查询门诊记录,可选条件
     *
     * @param outpatientRecordSearchParam 查询条件
     * @param pageNo 页码（默认为1）
     * @param pageSize 每页大小（默认为10）
     */
    @GetMapping("/outpatient-record-page")
    public R<?> getPatient(OutpatientRecordSearchParam outpatientRecordSearchParam,
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
        Page<OutpatientRecordDto> outpatientRecordPage = new Page<>(pageNo, pageSize, total);
        outpatientRecordPage.setRecords(listOutpatientRecords);
        outpatientRecordPage.getRecords().forEach(e -> {
            // 性别枚举类回显赋值
            e.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, e.getGenderEnum()));
            // 就诊对象状态枚举类回显赋值
            e.setSubjectStatusEnum_enumText(
                EnumUtils.getInfoByValue(EncounterSubjectStatus.class, e.getSubjectStatusEnum()));
        });

        return R.ok(outpatientRecordPage);
    }

}
