package com.openhis.web.reportmanage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.core.common.core.domain.R;
import com.openhis.web.reportmanage.appservice.IPrintReportAppService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 院内处方和医嘱报告打印
 *
 * @author liuhr
 * @date 2025/5/6
 */
@RestController
@RequestMapping("/report-manage/print")
@Slf4j
@AllArgsConstructor
public class PrintReportController {

    @Autowired
    private IPrintReportAppService printReportService;

    /**
     * 打印——处置单
     *
     * @param encounterId 患者就诊Id
     * @return 处置单信息
     */
    @GetMapping(value = "/disposal-print")
    public R<?> disposalPrint(@RequestParam Long encounterId) {

        return printReportService.disposalPrint(encounterId);
    }

    /**
     * 打印——检验申请单
     *
     * @param encounterId 患者就诊Id
     * @return 检验申请单信息
     */
    @GetMapping(value = "/check-print")
    public R<?> checkApplicationPrint(@RequestParam Long encounterId) {
        return printReportService.checkApplicationPrint(encounterId);
    }

    /**
     * 打印——检验申请单
     *
     * @param encounterId 患者就诊Id
     * @return 检验申请单信息
     */
    @GetMapping(value = "/inspection-print")
    public R<?> inspectionApplicationPrint(@RequestParam Long encounterId) {
        return printReportService.inspectionApplicationPrint(encounterId);
    }

    /**
     * 打印——处方单
     *
     * @param prescriptionNo 院内处方编号
     * @param encounterId    患者就诊Id
     * @return 处方单信息
     */
    @GetMapping(value = "/prescription-print")
    public R<?> prescriptionPrint(@RequestParam String prescriptionNo, @RequestParam Long encounterId) {
        return printReportService.prescriptionPrint(prescriptionNo, encounterId);
    }

    /**
     * 打印——护士输液的瓶签
     *
     * @param serviceId   服务请求id
     * @param encounterId 就诊id
     * @param groupId     药品组id
     * @return 输液的瓶签
     */
    @GetMapping(value = "/bottle-label-print")
    public R<?> bottleLabelPrint(@RequestParam Long serviceId, @RequestParam Long encounterId,
                                 @RequestParam Long groupId) {
        return printReportService.bottleLabelPrint(serviceId, encounterId, groupId);
    }

    /**
     * 打印——护士输液的瓶签 批量
     *
     * @return 输液的瓶签
     */
    @GetMapping(value = "/bottle-label-batch-print")
    public R<?> bottleLabelBatchPrint(@RequestParam List<Long> serviceIds,
                                      @RequestParam List<Long> encounterIds) {
        if (serviceIds == null || serviceIds.isEmpty()) {
            return R.fail("serviceIds cannot be null or empty");
        }
        if (encounterIds == null || encounterIds.isEmpty()) {
            return R.fail("encounterIds cannot be null or empty");
        }
        return printReportService.bottleLabelBatchPrint(serviceIds, encounterIds);
    }

    /**
     * 更新打印次数——护士输液的瓶签的打印次数
     *
     * @param serviceId 服务请求id
     * @param num       打印次数
     * @return 更新结果
     */
    @GetMapping(value = "/print-count")
    public R<?> updatePrintCount(@RequestParam Long serviceId, @RequestParam Integer num) {
        return printReportService.updatePrintCount(serviceId, num);
    }

}
