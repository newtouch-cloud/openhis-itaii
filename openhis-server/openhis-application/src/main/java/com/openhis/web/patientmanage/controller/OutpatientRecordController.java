package com.openhis.web.patientmanage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.core.common.core.domain.R;
import com.openhis.web.patientmanage.appservice.IOutpatientRecordService;
import com.openhis.web.patientmanage.dto.OutpatientRecordSearchParam;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class OutpatientRecordController {

    @Autowired
    IOutpatientRecordService outpatientRecordService;

    /**
     * 门诊输液记录初期数据
     *
     * @return
     */
    @GetMapping("/init")
    public R<?> getDoctorNames() {
        // 获取医生名字列表
        return R.ok(outpatientRecordService.getDoctorNames());
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

        return R.ok(outpatientRecordService.getPatient(outpatientRecordSearchParam, pageNo, pageSize));
    }

}
