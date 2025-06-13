package com.openhis.web.pharmacymanage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.pharmacymanage.appservice.IChineseMedicineDispenseAppService;
import com.openhis.web.pharmacymanage.dto.EncounterInfoSearchParam;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 西药发药
 *
 * @author wangyang
 * @date 2025/3/14
 */
@RestController
@RequestMapping("/pharmacy-manage/chinese-medicine-dispense")
@Slf4j
@AllArgsConstructor
public class ChineseMedicineDispenseController {

    @Autowired
    public IChineseMedicineDispenseAppService chineseMedicineDispenseService;

    /**
     * 获取页面初始化信息
     *
     * @return 初始化信息
     */
    @GetMapping(value = "/init")
    public R<?> medicineDispenseInit() {
        return chineseMedicineDispenseService.init();
    }

    /**
     * 分页查询就诊病人列表
     *
     * @param encounterInfoSearchParam 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 就诊病人列表
     */
    @GetMapping("/encounter-list")
    public R<?> getEncounterInfoList(EncounterInfoSearchParam encounterInfoSearchParam, String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        return chineseMedicineDispenseService.getEncounterInfoListPage(encounterInfoSearchParam, searchKey, pageNo,
            pageSize, request);
    }

    /**
     * 处方单查询
     *
     * @param encounterId 就诊Id
     */
    @GetMapping("/prescription-list")
    public R<?> getPatientInfoList(@RequestParam(value = "encounterId") Long encounterId) {
        return chineseMedicineDispenseService.getPrescriptionInfo(encounterId);
    }

    /**
     * 配药
     *
     * @param prescriptionNo 处方号
     * @param preparerId 配药人
     */
    @PutMapping("/prepare")
    public R<?> prepare(@RequestParam(value = "prescriptionNo") String prescriptionNo,
        @RequestParam(value = "preparerId") Long preparerId) {
        return chineseMedicineDispenseService.prepare(prescriptionNo, preparerId);
    }

    /**
     * 核对发药
     *
     * @param prescriptionNo 处方号
     */
    @PutMapping("/medicine-dispense")
    public R<?> medicineDispense(@RequestParam(value = "prescriptionNo") String prescriptionNo) {
        return chineseMedicineDispenseService.medicineDispense(prescriptionNo);
    }

    /**
     * 作废
     *
     * @param prescriptionNo 处方号
     * @param notPerformedReasonEnum 未发药原因
     */
    @PutMapping("/medicine-cancel")
    public R<?> medicineCancel(@RequestParam(value = "prescriptionNo") String prescriptionNo,
        @RequestParam(value = "notPerformedReasonEnum") Integer notPerformedReasonEnum) {
        return chineseMedicineDispenseService.medicineCancel(prescriptionNo, notPerformedReasonEnum);
    }
}
