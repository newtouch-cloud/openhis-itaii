package com.openhis.web.pharmacymanage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.core.common.core.domain.R;
import com.openhis.web.pharmacymanage.appservice.IMedicationDetailsAppService;
import com.openhis.web.pharmacymanage.dto.MedDetailsSearchParam;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 发药明细
 *
 * @author yuanzs
 * @date 2025/4/14
 */
@RestController
@RequestMapping("/pharmacy-manage/medication-details")
@Slf4j
@AllArgsConstructor
public class MedicationDetailsController {

    @Autowired
    public IMedicationDetailsAppService medicationDetailsAppService;

    /**
     * 获取页面初始化信息
     *
     * @return 初始化信息
     */
    @GetMapping(value = "/init")
    public R<?> init() {
        return medicationDetailsAppService.init();
    }

    /**
     * 门诊人员发药明细表
     *
     * @param medDetailsSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 门诊发药明细表
     */
    @GetMapping(value = "/amb-practitioner-detail")
    public R<?> getAmbPractitionerDetailPage(MedDetailsSearchParam medDetailsSearchParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
        @RequestParam(name = "searchKey", required = false) String searchKey, HttpServletRequest request) {
        return medicationDetailsAppService.getAmbPractitionerDetailPage(medDetailsSearchParam, pageNo, pageSize,
            searchKey, request);
    }

    /**
     * 门诊发药明细流水账
     *
     * @param medDetailsSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 门诊发药明细流水账
     */
    @GetMapping(value = "/amb-medication-detail")
    public R<?> getAmbMedicationDispenseDetailPage(MedDetailsSearchParam medDetailsSearchParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
        @RequestParam(name = "searchKey", required = false) String searchKey, HttpServletRequest request) {
        return medicationDetailsAppService.getAmbMedicationDispenseDetailPage(medDetailsSearchParam, pageNo, pageSize,
            searchKey, request);
    }

//    /**
//     * 门诊/住院人员发药明细帐、住院耗材记账领用明细
//     *
//     * @param medDetailsSearchParam 查询条件
//     * @param pageNo 当前页码
//     * @param pageSize 查询条数
//     * @param searchKey 模糊查询关键字
//     * @param request 请求数据
//     * @return 门诊/住院人员发药明细、住院耗材记账领用明细分页列表
//     */
//    @GetMapping(value = "/med-detail-page")
//    public R<?> getMedDetailedAccountPage(MedDetailsSearchParam medDetailsSearchParam,
//        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
//        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
//        @RequestParam(name = "searchKey", required = false) String searchKey, HttpServletRequest request) {
//        return medicationDetailsAppService.getMedDetailedAccountPage(medDetailsSearchParam, pageNo, pageSize, searchKey,
//            request);
//    }
//
//    /**
//     * 门诊/住院发药明细流水帐、住院耗材记账领用流水账
//     *
//     * @param medDetailsSearchParam 查询条件
//     * @param pageNo 当前页码
//     * @param pageSize 查询条数
//     * @param searchKey 模糊查询关键字
//     * @param request 请求数据
//     * @return 门诊/住院发药明细流水帐、住院耗材记账领用流水账分页列表
//     */
//    @GetMapping(value = "/med-running-page")
//    public R<?> getMedRunningAccountPage(MedDetailsSearchParam medDetailsSearchParam,
//        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
//        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
//        @RequestParam(name = "searchKey", required = false) String searchKey, HttpServletRequest request) {
//        return medicationDetailsAppService.getMedRunningAccountPage(medDetailsSearchParam, pageNo, pageSize, searchKey,
//            request);
//    }
}
