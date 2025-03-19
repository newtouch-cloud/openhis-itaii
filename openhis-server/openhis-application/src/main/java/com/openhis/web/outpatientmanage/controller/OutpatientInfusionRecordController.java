package com.openhis.web.outpatientmanage.controller;

import java.util.List;

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

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 门诊输液记录
 *
 * @author liuhr
 * @date 2025/3/12
 */
@RestController
@RequestMapping("/outpatient-manage/infusion")
@Slf4j
@AllArgsConstructor
public class OutpatientInfusionRecordController {

    @Autowired
    IOutpatientInfusionRecordService outpatientInfusionRecordService;

    /**
     * 查询门诊输液的患者列表
     *
     * @param outpatientInfusionSearchParam 查询参数
     * @return 门诊输液的患者列表
     */
    @GetMapping(value = "/infusion-patient-list")
    public R<?> getPatientInfusionRecord(OutpatientInfusionSearchParam outpatientInfusionSearchParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return R.ok(outpatientInfusionRecordService.getOutpatientInfusionPatientList(outpatientInfusionSearchParam,
            pageNo, pageSize));
    }

    /**
     * 点击患者，查询该患者的输液记录
     *
     * @param outpatientInfusionPatientDto 患者输液信息
     * @return 当前患者门诊输液待执行列表
     */
    @GetMapping(value = "/patient-infusion-record")
    public R<?> getPatientInfusionRecord(OutpatientInfusionPatientDto outpatientInfusionPatientDto) {

        return R.ok(outpatientInfusionRecordService.getPatientInfusionRecord(outpatientInfusionPatientDto));
    }

    /**
     * 批量执行患者门诊输液
     *
     * @param outpatientInfusionRecordDtoList 患者输液信息列表
     * @return 门诊输液记录列表
     */
    @PutMapping("/infusion-perform/batch")
    public R<?> batchEditPatientInfusionRecord(
        @Validated @RequestBody List<OutpatientInfusionRecordDto> outpatientInfusionRecordDtoList) {

        if (!outpatientInfusionRecordService.batchEditPatientInfusionRecord(outpatientInfusionRecordDtoList)) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }

        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00001, new Object[] {"患者门诊输液执行"}));
    }

    /**
     * 执行输液后,修改执行结束时间
     *
     * @param outpatientInfusionRecordDto 患者输液信息
     */
    @PutMapping("/infusion-perform-time")
    public R<?>
        editPatientInfusionTime(@Validated @RequestBody OutpatientInfusionRecordDto outpatientInfusionRecordDto) {
        boolean res = outpatientInfusionRecordService.editPatientInfusionTime(outpatientInfusionRecordDto);
        if (!res) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }
        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00001, new Object[] {"患者门诊输液记录"}));
    }

    /**
     * 门诊输液待执行记录查询
     *
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 门诊输液待执行记录列表
     */
    @GetMapping(value = "/infusion-wait-perform-record")
    public R<?> getPatientInfusionRecords(@RequestParam(required = false) String beginTime,
        @RequestParam(required = false) String endTime) {

        return R.ok(outpatientInfusionRecordService.getPatientInfusionPerformRecord(beginTime, endTime, false));
    }

    /**
     * 门诊输液执行历史记录查询
     *
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 门诊输液执行历史记录列表
     */
    @GetMapping(value = "/infusion-perform-record")
    public R<?> getPatientInfusionPerformRecord(@RequestParam(required = false) String beginTime,
        @RequestParam(required = false) String endTime) {

        return R.ok(outpatientInfusionRecordService.getPatientInfusionPerformRecord(beginTime, endTime, true));
    }

}
