package com.openhis.web.outpatientmanage.controller;

import com.openhis.web.inventorymanage.dto.ReceiptApprovalSearchParam;
import com.openhis.web.outpatientmanage.dto.OutpatientDisposalActivityInfoDto;
import com.openhis.web.outpatientmanage.dto.OutpatientDisposalExecuteInfoDto;
import com.openhis.web.pharmacymanage.appservice.IWesternMedicineDispenseAppService;
import com.openhis.web.pharmacymanage.dto.EncounterInfoSearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.outpatientmanage.appservice.IOutpatientDisposalService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 门诊处置
 *
 * @author yuxj
 * @date 2025/4/10
 */
@RestController
@RequestMapping("/outpatient-manage/disposal")
@Slf4j
@AllArgsConstructor
public class OutpatientDisposalController {

    @Resource
    private IOutpatientDisposalService outpatientDisposalService;

    @Resource
    public IWesternMedicineDispenseAppService iWesternMedicineDispenseService;

    /**
     * 门诊处置初期数据
     *
     * @return 初始化信息
     */
    @GetMapping("/init")
    public R<?> outpatientDisposalInit() {
        return outpatientDisposalService.init();
    }

    /**
     * 分页查询就诊病人列表
     *
     * @param encounterInfoSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 就诊病人列表
     */
    @GetMapping("/encounter-list")
    public R<?> getEncounterInfoList(EncounterInfoSearchParam encounterInfoSearchParam,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
        @RequestParam(name = "searchKey", required = false) String searchKey, HttpServletRequest request) {
        return outpatientDisposalService.getEncounterInfoListPage(encounterInfoSearchParam, pageNo, pageSize,
            searchKey, request);
    }

    /**
     * 诊疗单查询
     *
     * @param encounterId 就诊Id
     */
    @GetMapping("/disposal-list")
    public R<?> getDisposalInfoList(@RequestParam(value = "encounterId") Long encounterId) {
        return outpatientDisposalService.getDisposalInfoList(encounterId);
    }

    /**
     * 执行单查询
     *
     * @param busNo 编码
     * @param activityId 诊疗Id
     * @param type 类型
     */
    @GetMapping("/execute-list")
    public R<?> getExecuteInfoList(@RequestParam(value = "busNo") String busNo,
        @RequestParam(value = "activityId") Long activityId, @RequestParam(value = "type") Integer type) {
        return outpatientDisposalService.getExecuteInfoList(busNo, activityId, type);
    }

    /**
     * 执行
     *
     * @param itemId 诊疗id
     * @param type 诊疗类型
     */
    @PutMapping("/execute")
    public R<?> execute(@RequestParam(value = "itemId") Long itemId, @RequestParam(value = "type") Integer type) {
        return outpatientDisposalService.execute(itemId, type);
    }

    /**
     * 取消
     *
     * @param busNo 编码
     * @param activityId 诊疗Id
     * @param type 类型
     */
    @PutMapping("/cancel")
    public R<?> cancel(@RequestParam(value = "busNo") String busNo, @RequestParam(value = "activityId") Long activityId,
        @RequestParam(value = "type") Integer type) {
        return outpatientDisposalService.cancel(busNo, activityId, type);
    }
}
