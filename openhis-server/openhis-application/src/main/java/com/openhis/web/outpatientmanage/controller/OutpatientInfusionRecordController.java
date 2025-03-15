package com.openhis.web.outpatientmanage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.web.outpatientmanage.appservice.IOutpatientInfusionRecordService;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionPatientDto;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionRecordDto;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionSearchParam;
import com.openhis.workflow.service.IServiceRequestService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 门诊输液记录
 *
 * @author liuhr
 * @date 2025/3/12
 */
@RestController
@RequestMapping("/outpatientmanage/infusion")
@Slf4j
@AllArgsConstructor
public class OutpatientInfusionRecordController {

    @Autowired
    IOutpatientInfusionRecordService outpatientInfusionRecordService;

    @Autowired
    IServiceRequestService serviceRequestService;

    /**
     * 门诊输液记录初期数据
     *
     * @return
     */
    @GetMapping("/init")
    public R<?> getOutpatientInfusionInit() {

        return R.ok(outpatientInfusionRecordService.getOutpatientInfusionInit());
    }

    /**
     * 查询门诊输液患者列表
     *
     * @param outpatientInfusionSearchParam 查询参数
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 就诊患者信息
     */
    @GetMapping(value = "/patients")
    public R<?> getOutpatientInfusionPatient(OutpatientInfusionSearchParam outpatientInfusionSearchParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return R.ok(outpatientInfusionRecordService.getOutpatientInfusionPatient(outpatientInfusionSearchParam, pageNo,
            pageSize));
    }

    /**
     * 查询单个患者门诊输液记录查询
     *
     * @param outpatientInfusionPatientDto 患者输液信息
     * @return 门诊输液记录列表
     */
    @GetMapping(value = "/patient-infusion")
    public R<?> getPatientInfusionRecord(OutpatientInfusionPatientDto outpatientInfusionPatientDto) {

        return R.ok(outpatientInfusionRecordService.getPatientInfusionRecord(outpatientInfusionPatientDto));
    }

    /**
     * 执行单个患者门诊输液
     *
     * @param outpatientInfusionRecordDto 患者输液信息
     * @return 门诊输液记录列表
     */
    @PutMapping("/outpatient-record-skintest")
    public R<?>
        editPatientInfusionRecord(@Validated @RequestBody OutpatientInfusionRecordDto outpatientInfusionRecordDto) {
        //获取执行次数
        Long exeCount =
            serviceRequestService.countServiceRequestByBasedOnId(outpatientInfusionRecordDto.getServiceId());
        outpatientInfusionRecordService.editPatientInfusionRecord(outpatientInfusionRecordDto,exeCount);

        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00001, new Object[] {"患者门诊输液执行"}));
    }

}
