package com.openhis.web.nursestation.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.nursestation.appservice.INurseStationPendAdmAppService;
import com.openhis.web.nursestation.dto.BedForAdmissionDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 护士站——住院待入科
 *
 * @author liuhr
 * @date 2025/4/14
 */
@RestController
@RequestMapping("/nurse-station/pending-admission")
@Slf4j
@AllArgsConstructor
public class NurseStationPendAdmController {

    @Autowired
    private INurseStationPendAdmAppService nsPendAdmAppService;

    /**
     * 待入科数据初始化
     *
     * @return 基础数据
     */
    @GetMapping(value = "/init")
    public R<?> init() {

        return nsPendAdmAppService.getPendAdmInit();
    }

    /**
     * 选择科室后，查询科室对应病区列表
     *
     * @param orgId 科室id
     * @return 病区列表
     */
    @GetMapping(value = "/ward-location-list")
    public R<?> getWardLocationList(@RequestParam Long orgId) {

        return nsPendAdmAppService.getWardLocationList(orgId);
    }

    /**
     * 根据病区查询该病区下的病床信息
     *
     * @param wardLocationId 病区ID
     * @return 病床列表
     */
    @GetMapping(value = "/bed-info")
    public R<?> getBedList(@RequestParam @NotNull(message = "病区ID不能为空") Long wardLocationId) {

        return nsPendAdmAppService.getBedList(wardLocationId);
    }

    /**
     * 根据入院科室,查询医生列表
     *
     * @param orgId 科室id
     * @return 医生列表
     */
    @GetMapping(value = "/doctor-list")
    public R<?> getDoctorList(@RequestParam Long orgId) {

        return nsPendAdmAppService.getDoctorList(orgId);
    }

    /**
     * 根据入院科室,查询护士列表
     *
     * @param orgId 科室id
     * @return 护士列表
     */
    @GetMapping(value = "/nurse-list")
    public R<?> getNurseList(@RequestParam Long orgId) {

        return nsPendAdmAppService.getNurseList(orgId);
    }

    /**
     * 查询待入科的患者列表
     *
     * @return 待入科的患者列表
     */
    @GetMapping(value = "/pending-admission-info")
    public R<?> getPendAdmInfo() {

        return nsPendAdmAppService.getPendAdmInfo();
    }

    /**
     * 患者选床入科
     *
     * @param bedInfoDto 选床信息
     * @return 选床入科
     */
    @PutMapping(value = "/select-bed")
    public R<?> selectBedForAdmission(@Valid @RequestBody BedForAdmissionDto bedInfoDto) {
        return nsPendAdmAppService.selectBedForAdmission(bedInfoDto);
    }

}
