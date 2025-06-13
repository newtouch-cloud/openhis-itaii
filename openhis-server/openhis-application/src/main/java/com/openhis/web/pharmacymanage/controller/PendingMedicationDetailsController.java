package com.openhis.web.pharmacymanage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.core.common.core.domain.R;
import com.openhis.web.pharmacymanage.appservice.IPendingMedicationDetailsAppService;
import com.openhis.web.pharmacymanage.dto.PendingMedicationSearchParam;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 待发药明细
 *
 * @author yuanzs
 * @date 2025/4/14
 */
@RestController
@RequestMapping("/pharmacy-manage/pending-medication")
@Slf4j
@AllArgsConstructor
public class PendingMedicationDetailsController {

    @Autowired
    public IPendingMedicationDetailsAppService pendingMedicationDetailsAppService;

    /**
     * 分页查询待发药明细
     *
     * @param pendingMedicationSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 待发药明细
     */
    @GetMapping("/pending-medication-page")
    public R<?> getPage(PendingMedicationSearchParam pendingMedicationSearchParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
        @RequestParam(name = "searchKey", required = false) String searchKey, HttpServletRequest request) {
        return pendingMedicationDetailsAppService.getPage(pendingMedicationSearchParam, pageNo, pageSize, searchKey,
            request);
    }

}
