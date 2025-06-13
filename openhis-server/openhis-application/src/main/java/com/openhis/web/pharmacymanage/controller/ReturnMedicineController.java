package com.openhis.web.pharmacymanage.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.pharmacymanage.appservice.ReturnMedicineAppService;
import com.openhis.web.pharmacymanage.dto.EncounterInfoPageDto;
import com.openhis.web.pharmacymanage.dto.ReturnMedicineDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 退药管理
 *
 * @author yangmo
 * @date 2025/4/4
 */
@RestController
@RequestMapping("/pharmacy-manage/return-medicine")
@Slf4j
@AllArgsConstructor
public class ReturnMedicineController {

    @Autowired
    public ReturnMedicineAppService returnMedicineAppService;

    /**
     * 获取页面初始化信息
     *
     * @return 初始化信息
     */
    @GetMapping(value = "/init")
    public R<?> returnMedicineInit() {
        return returnMedicineAppService.init();
    }

    /**
     * 查询退药患者分页列表
     *
     * @param encounterInfoPageDto 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 退药患者分页列表
     */
    @GetMapping(value = "/return-patient-page")
    public R<?> getReturnMedicinePatientPage(EncounterInfoPageDto encounterInfoPageDto,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        return returnMedicineAppService.getReturnMedicinePatientPage(encounterInfoPageDto, searchKey, pageNo, pageSize,
            request);
    }

    /**
     * 查询退药信息
     *
     * @param encounterId 就诊ID
     * @param refundStatus 退药id
     * @return 退药信息
     */
    @GetMapping("/medicine-return-list")
    public R<?> getReturnMedicineInfo(@RequestParam Long encounterId, @RequestParam Integer refundStatus) {
        return returnMedicineAppService.getReturnMedicineInfo(encounterId, refundStatus);
    }

    /**
     * 退药处理
     *
     * @param medicineReturnList 退药清单
     * @return 处理结果
     */
    @PutMapping("/medicine-return")
    public R<?> medicineReturn(@RequestBody List<ReturnMedicineDto> medicineReturnList) {
        return returnMedicineAppService.medicineReturn(medicineReturnList);
    }

}
