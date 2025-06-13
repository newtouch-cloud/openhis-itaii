package com.openhis.web.outpatientmanage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.web.outpatientmanage.appservice.IOutpatientSkinTestRecordService;
import com.openhis.web.outpatientmanage.dto.OutpatientSkinTestRecordDto;
import com.openhis.web.outpatientmanage.dto.OutpatientSkinTestRecordSearchParam;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 门诊皮试记录
 *
 * @author liuhr
 * @date 2025/3/5
 */
@RestController
@RequestMapping("/outpatient-manage/skin-test")
@Slf4j
@AllArgsConstructor
public class OutpatientSkinTestRecordController {

    @Autowired
    private IOutpatientSkinTestRecordService OutpatientSkinTestRecordService;

    /**
     * 门诊皮试记录初期数据
     *
     * @return
     */
    @GetMapping("/init")
    public R<?> getOutpatientSkinTestInit() {

        return R.ok(OutpatientSkinTestRecordService.getOutpatientSkinTestInit());
    }

    /**
     * 护士确认执行皮试后，更新皮试记录信息
     *
     * @param outpatientSkinTestRecordDto 皮试记录信息
     */
    @PutMapping("/outpatient-record-skin-test")
    public R<?> editSkinTestRecord(@Validated @RequestBody OutpatientSkinTestRecordDto outpatientSkinTestRecordDto) {

        if (!OutpatientSkinTestRecordService.editSkinTestRecord(outpatientSkinTestRecordDto)) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }
        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00001, new Object[] {"皮试项目检查"}));
    }

    /**
     * 皮试记录护士核对签名
     *
     * @param outpatientSkinTestRecordDto 皮试记录信息
     */
    @PutMapping("/outpatient-record-sign-check")
    public R<?> nurseSignChkPs(@Validated @RequestBody OutpatientSkinTestRecordDto outpatientSkinTestRecordDto) {

        if (!OutpatientSkinTestRecordService.nurseSignChkPs(outpatientSkinTestRecordDto)) {
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00003, null));
        }
        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00001, new Object[] {"皮试记录护士核对签名"}));
    }

    /**
     * 分页查询门诊皮实记录,可选条件
     *
     * @param outpatientSkinTestRecordSearchParam 查询条件
     * @param pageNo 页码（默认为1）
     * @param pageSize 每页大小（默认为10）
     */
    @GetMapping("/outpatient-record-page")
    public R<?> getSkinTestRecords(OutpatientSkinTestRecordSearchParam outpatientSkinTestRecordSearchParam,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {

        return R.ok(OutpatientSkinTestRecordService.getSkinTestRecords(outpatientSkinTestRecordSearchParam, searchKey,
            pageNo, pageSize, request));
    }

}
