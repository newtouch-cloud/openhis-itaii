package com.openhis.web.outpatientmanage.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.outpatientmanage.appservice.IOutpatientInfusionAppService;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionPatientDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 门诊输液记录
 *
 * @author liuhr
 * @date 2025/3/12
 */
@RestController
@RequestMapping("/outpatient-manage/infusion")
@Slf4j
@AllArgsConstructor
public class OutpatientInfusionController {

    @Autowired
    private IOutpatientInfusionAppService outpatientInfusionAppService;

    /**
     * 门诊输液初始化
     *
     * @return 初始化信息
     */
    @GetMapping("/init")
    public R<?> outpatientInfusionInit() {
        return outpatientInfusionAppService.init();
    }

    /**
     * 查询门诊输液的患者列表
     *
     * @param outpatientInfusionPatientDto 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 返回门诊输液的患者列表
     */
    @GetMapping(value = "/infusion-patient-list")
    public R<?> getOutpatientInfusionPatientList(OutpatientInfusionPatientDto outpatientInfusionPatientDto,
        String searchKey, @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        return outpatientInfusionAppService.getOutpatientInfusionPatientList(outpatientInfusionPatientDto, searchKey,
            pageNo, pageSize, request);
    }

    /**
     * 门诊输液待执行记录查询
     *
     * @param encounterId 就诊ID
     * @param serviceStatus 就诊状态
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 门诊输液待执行记录列表
     */
    @GetMapping(value = "/infusion-pending-record")
    public R<?> getInfusionPendingRecord(@RequestParam Long encounterId,@RequestParam Integer serviceStatus,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return outpatientInfusionAppService.getInfusionPendingRecord(encounterId, serviceStatus,pageNo, pageSize);
    }

    /**
     * 门诊输液执行历史记录查询
     *
     * @param serviceReqId 诊疗项目id
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 门诊输液执行历史记录列表
     */
    @GetMapping(value = "/infusion-perform-record")
    public R<?> getInfusionPerformRecord(@RequestParam Long serviceReqId,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return outpatientInfusionAppService.getInfusionPerformRecord(serviceReqId, pageNo, pageSize);
    }

    /**
     * 输液执行
     *
     * @param serviceReqIdList 输液请求id列表
     * @return 执行结果
     */
    @PutMapping("/infusion-perform")
    public R<?> infusionPerform(@RequestBody List<Long> serviceReqIdList) {
        return outpatientInfusionAppService.infusionPerform(serviceReqIdList);
    }

    /**
     * 修改输液执行时间
     *
     * @param serviceReqId 患者输液信息
     * @param performTime 患者输液信息
     * @return 执行结果
     */
    @PutMapping("/infusion-perform-time")
    public R<?> editPatientInfusionTime(@RequestParam Long serviceReqId, @RequestParam String performTime) {
        return outpatientInfusionAppService.editPatientInfusionTime(serviceReqId, performTime);
    }

    /**
     * 撤销执行
     *
     * @param serviceReqId 输液请求id
     * @return 撤销结果
     */
    @PutMapping(value = "/cancel-perform")
    public R<?> cancelInfusionPerform(@RequestParam Long serviceReqId) {
        return outpatientInfusionAppService.cancelInfusionPerform(serviceReqId);
    }

}
