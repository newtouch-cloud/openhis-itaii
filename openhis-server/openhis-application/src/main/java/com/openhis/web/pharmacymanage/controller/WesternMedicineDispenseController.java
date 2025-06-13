package com.openhis.web.pharmacymanage.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.pharmacymanage.appservice.IWesternMedicineDispenseAppService;
import com.openhis.web.pharmacymanage.dto.DispenseMedicineDto;
import com.openhis.web.pharmacymanage.dto.EncounterInfoPageDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 西药发药
 *
 * @author wangyang
 * @date 2025/3/14
 */
@RestController
@RequestMapping("/pharmacy-manage/western-medicine-dispense")
@Slf4j
@AllArgsConstructor
public class WesternMedicineDispenseController {

    @Autowired
    public IWesternMedicineDispenseAppService westernMedicineDispenseService;

    /**
     * 获取页面初始化信息
     *
     * @return 初始化信息
     */
    @GetMapping(value = "/init")
    public R<?> medicineDispenseInit() {
        return westernMedicineDispenseService.init();
    }

    /**
     * 分页查询发药病人列表
     *
     * @param encounterInfoPageDto 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 发药病人列表
     */
    @GetMapping("/encounter-list")
    public R<?> getEncounterInfoList(EncounterInfoPageDto encounterInfoPageDto, String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        return westernMedicineDispenseService.getEncounterInfoListPage(encounterInfoPageDto, searchKey, pageNo,
            pageSize, request);
    }

    /**
     * 处方单查询
     *
     * @param encounterId 就诊Id
     * @param dispenseStatus 发药id
     */
    @GetMapping("/prescription-list")
    public R<?> getPatientInfoList(@RequestParam Long encounterId,
        @RequestParam Integer dispenseStatus) {
        return westernMedicineDispenseService.getPrescriptionInfo(encounterId,dispenseStatus);
    }

    /**
     * 配药
     *
     * @param dispenseMedicineList 配药信息
     * @return 处理结果
     */
    @PutMapping("/prepare")
    public R<?> medicinePrepare(@RequestBody List<DispenseMedicineDto> dispenseMedicineList) {
        return westernMedicineDispenseService.medicinePrepare(dispenseMedicineList);
    }

    /**
     * 核对发药
     *
     * @param dispenseMedicineList 发药信息
     * @return 处理结果
     */
    @PutMapping("/medicine-dispense")
    public R<?> medicineDispense(@RequestBody List<DispenseMedicineDto> dispenseMedicineList) {
        return westernMedicineDispenseService.medicineDispense(dispenseMedicineList);
    }

    /**
     * 作废
     *
     * @param dispenseMedicineList 作废信息
     * @return 处理结果
     */
    @PutMapping("/medicine-cancel")
    public R<?> medicineCancel(@RequestBody List<DispenseMedicineDto> dispenseMedicineList) {
        return westernMedicineDispenseService.medicineCancel(dispenseMedicineList);
    }
}
