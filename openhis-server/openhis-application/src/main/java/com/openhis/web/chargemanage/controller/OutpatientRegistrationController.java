/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import com.openhis.financial.domain.PaymentReconciliation;
import com.openhis.web.paymentmanage.appservice.IEleInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.common.enums.PriorityLevel;
import com.openhis.web.chargemanage.appservice.IOutpatientRegistrationAppService;
import com.openhis.web.chargemanage.dto.OutpatientRegistrationInitDto;
import com.openhis.web.paymentmanage.dto.CancelRegPaymentDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 门诊挂号 controller
 */
@RestController
@RequestMapping("/charge-manage/register")
@Slf4j
@AllArgsConstructor
public class OutpatientRegistrationController {

    private final IOutpatientRegistrationAppService iOutpatientRegistrationAppService;

    @Autowired
    private IEleInvoiceService eleInvoiceService;
    /**
     * 基础数据初始化
     */
    @GetMapping(value = "/init")
    public R<?> init() {
        OutpatientRegistrationInitDto outpatientRegistrationInitDto = new OutpatientRegistrationInitDto();
        // 优先级
        List<OutpatientRegistrationInitDto.priorityLevelOption> priorityLevelOptionOptions =
            Stream.of(PriorityLevel.values())
                .map(e -> new OutpatientRegistrationInitDto.priorityLevelOption(e.getValue(), e.getInfo()))
                .collect(Collectors.toList());
        outpatientRegistrationInitDto.setPriorityLevelOptionOptions(priorityLevelOptionOptions);
        return R.ok(outpatientRegistrationInitDto);
    }

    /**
     * 查询患者信息
     *
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 患者信息
     */
    @GetMapping(value = "/patient-metadata")
    public R<?> getPatientMetadata(@RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return R.ok(iOutpatientRegistrationAppService.getPatientMetadataBySearchKey(searchKey, pageNo, pageSize));
    }

    /**
     * 查询费用性质
     *
     * @return 费用性质
     */
    @GetMapping(value = "/contract-list")
    public R<?> getContractList() {
        return R.ok(iOutpatientRegistrationAppService.getContractMetadata());
    }

    /**
     * 查询就诊科室
     *
     * @return 就诊科室集合
     */
    @GetMapping(value = "/org-list")
    public R<?> getLocationTree() {
        return R.ok(iOutpatientRegistrationAppService.getOrgMetadata());
    }

    /**
     * 根据科室id筛选医生
     */
    @GetMapping(value = "/practitioner-metadata")
    public R<?> getPractitionerMetadata(@RequestParam(value = "orgId") Long orgId,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return R.ok(
            iOutpatientRegistrationAppService.getPractitionerMetadataByLocationId(orgId, searchKey, pageNo, pageSize));
    }

    /**
     * 根据机构id筛选服务项目
     */
    @GetMapping(value = "/healthcare-metadata")
    public R<?> getHealthcareMetadata(@RequestParam(value = "organizationId") Long organizationId,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return R.ok(iOutpatientRegistrationAppService.getHealthcareMetadataByOrganizationId(organizationId, searchKey,
            pageNo, pageSize));
    }

    /**
     * 退号
     * 
     * @param cancelRegPaymentDto 就诊id
     * @return 结果
     */
    @PutMapping(value = "return")
    public R<?> returnRegister(@RequestBody CancelRegPaymentDto cancelRegPaymentDto) {

        R<?> result = iOutpatientRegistrationAppService.returnRegister(cancelRegPaymentDto);
        // 取消付款成功后，开具发票
        if (result.getCode() == 200) {
            PaymentReconciliation paymentRecon = null;
            if (PaymentReconciliation.class.isAssignableFrom(result.getData().getClass())) {
                paymentRecon = (PaymentReconciliation)result.getData();
            }
            R<?> eleResult = eleInvoiceService.invoiceWriteoff(paymentRecon.getRelationId(), cancelRegPaymentDto.getReason());
            if (eleResult.getCode() != 200) {
                // 因取消付款成功前端需要关闭弹窗，此处信息仅用于提示所以返回ok
                return R.ok(null, " 取消付款成功，电子发票开具失败 :" + eleResult.getMsg());
            }
        }
        return result;

    }

    /**
     * 取消挂号
     *
     * @param encounterId 就诊id
     * @return 结果
     */
    @PutMapping(value = "cancel")
    public R<?> cancelRegister(@RequestParam(value = "encounterId") Long encounterId) {
        return iOutpatientRegistrationAppService.cancelRegister(encounterId);
    }

    /**
     * 查询当日就诊数据
     *
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 当日就诊数据
     */
    @GetMapping(value = "/current-day-encounter")
    public R<?> getCurrentDayEncounter(@RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        return R.ok(iOutpatientRegistrationAppService.getCurrentDayEncounter(searchKey, pageNo, pageSize, request));
    }

}
