package com.openhis.web.inpatientmanage.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.openhis.web.paymentmanage.dto.PaymentDto;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.inpatientmanage.appservice.IDepositAppService;
import com.openhis.web.inpatientmanage.dto.DepositSearchParam;

import lombok.extern.slf4j.Slf4j;

/**
 * 预交金管理 controller
 *
 * @author gyy
 * @date 2025-05-19
 */
@RestController
@RequestMapping("/deposit-manage")
@Slf4j
public class DepositController {

    @Resource
    private IDepositAppService depositAppService;

    /**
     * 获取预交金信息初期数据列表
     *
     * @return 预交金初期数据列表
     */
    @GetMapping("/init")
    public R<?> getDepositInfoInit() {
        return depositAppService.getDepositInfoInit();

    }

    /**
     * 获取预交金信息 分页显示
     *
     * @param depositSearchParam 查询参数
     * @param searchKey 模糊查询
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return 住院信息
     */
    @GetMapping("/deposit-page")
    R<?> getDepositInfoPage(DepositSearchParam depositSearchParam,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {

        return depositAppService.getDepositInfoPage(depositSearchParam, searchKey, pageNo, pageSize, request);

    }

    /**
     * 付款
     *
     * @param paymentDto 付款实体
     */
    @PostMapping("/charge")
    public R<?> savePayment(@Validated @RequestBody PaymentDto paymentDto) {
        return depositAppService.savePayment(paymentDto);
    }
}
