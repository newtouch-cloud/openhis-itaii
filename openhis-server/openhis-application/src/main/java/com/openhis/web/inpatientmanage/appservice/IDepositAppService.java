package com.openhis.web.inpatientmanage.appservice;

import javax.servlet.http.HttpServletRequest;

import com.core.common.core.domain.R;
import com.openhis.web.inpatientmanage.dto.DepositSearchParam;
import com.openhis.web.paymentmanage.dto.PaymentDto;

/**
 * 住院登记 应用实现
 *
 * @author gyy
 * @since 2025/05/19
 */
public interface IDepositAppService {

    /**
     * 病获取预交金信息初期数据列表
     *
     * @return 预交金信息初期数据列表
     */
    R<?> getDepositInfoInit();

    /**
     * 获取预交金信息 分页显示
     *
     * @param depositSearchParam 查询参数
     * @param searchKey 模糊查询
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return 住院信息
     */
    R<?> getDepositInfoPage(DepositSearchParam depositSearchParam, String searchKey, Integer pageNo, Integer pageSize,
        HttpServletRequest request);

    /**
     * 付款
     *
     * @param paymentDto 入参
     * @return 结果
     */
    R<?> savePayment(PaymentDto paymentDto);
}
