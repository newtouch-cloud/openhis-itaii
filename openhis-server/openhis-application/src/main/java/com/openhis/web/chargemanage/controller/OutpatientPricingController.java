package com.openhis.web.chargemanage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.core.common.core.domain.R;
import com.openhis.web.chargemanage.appservice.IOutpatientPricingAppService;
import com.openhis.web.doctorstation.dto.AdviceBaseDto;
import com.openhis.web.doctorstation.dto.PatientInfoDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 门诊划价 controller
 *
 * @author yangmo
 * @date 2025-04-14
 */
@RestController
@RequestMapping("/charge-manage/pricing")
@Slf4j
@AllArgsConstructor
public class OutpatientPricingController {

    private final IOutpatientPricingAppService iOutpatientPricingAppService;

    /**
     * 查询就诊患者信息
     *
     * @param patientInfoDto 查询条件 (前端可传 statusEnum 区分就诊状态tab)
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 就诊患者信息
     */
    @GetMapping(value = "/patient-info")
    public R<?> getPatientInfo(PatientInfoDto patientInfoDto,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        return R.ok(iOutpatientPricingAppService.getPatientInfo(patientInfoDto, searchKey, pageNo, pageSize, request));
    }

    /**
     * 查询医嘱信息
     *
     * @param adviceBaseDto 查询条件
     * @param searchKey 模糊查询关键字
     * @param locationId 药房id
     * @param organizationId 患者挂号对应的科室id
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 医嘱信息
     */
    @GetMapping(value = "/advice-base-info")
    public R<?> getAdviceBaseInfo(AdviceBaseDto adviceBaseDto,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "locationId", required = false) Long locationId,
        @RequestParam(value = "organizationId") Long organizationId,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return R.ok(iOutpatientPricingAppService.getAdviceBaseInfo(adviceBaseDto, searchKey, locationId, organizationId,
            pageNo, pageSize));
    }

}
