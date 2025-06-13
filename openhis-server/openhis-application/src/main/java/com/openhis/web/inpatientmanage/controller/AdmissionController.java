package com.openhis.web.inpatientmanage.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.inpatientmanage.appservice.IAdmissionAppService;
import com.openhis.web.inpatientmanage.dto.AdmissionSearchParam;
import com.openhis.web.inpatientmanage.dto.AdmissionUpDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/inpatient-manage")
@Slf4j
@AllArgsConstructor
public class AdmissionController {

    @Resource
    private IAdmissionAppService admissionAppService;

    /**
     * 获取住院信息初期数据列表
     *
     * @return 住院信息初期数据列表
     */
    @GetMapping("/init")
    public R<?> getAdmissionInfoInit() {
        return admissionAppService.getAdmissionInfoInit();

    }

    /**
     * 获取住院信息 分页显示
     *
     * @param admissionSearchParam 查询参数
     * @param searchKey 模糊查询
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return 住院信息
     */
    @GetMapping("/admission-page")
    R<?> getAdmissionInfoPage(AdmissionSearchParam admissionSearchParam,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {

        return admissionAppService.getAdmissionInfoPage(admissionSearchParam, searchKey, pageNo, pageSize, request);

    }

    /**
     * 住院无档登记
     *
     * @param admissionUpDto 住院登记详细信息
     */
    @PostMapping("/admission-information")
    public R<?> addAdmissionInfo(@Validated @RequestBody AdmissionUpDto admissionUpDto) {
        return admissionAppService.addAdmissionInfo(admissionUpDto);
    }

    /**
     * 登记
     *
     * @param admissionUpDto 住院登记详细信息
     */
    @PutMapping("/admission-information")
    public R<?> editAdmissionInfo(@Validated @RequestBody AdmissionUpDto admissionUpDto) {
        // 调用服务层更新病人信息
        return admissionAppService.editAdmissionInfo(admissionUpDto);

    }

    /**
     * 根据id查询登记详情
     *
     * @param id 就诊ID
     * @return 登记详情
     */
    @GetMapping("/admission-one")
    public R<?> getDeviceOne(@RequestParam Long id) {

        return admissionAppService.getAdmissionOne(id);
    }

}
