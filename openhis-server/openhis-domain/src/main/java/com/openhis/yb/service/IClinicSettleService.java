package com.openhis.yb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.yb.domain.ClinicReg;
import com.openhis.yb.domain.ClinicSettle;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author system
 * @date 2025-04-20
 */
public interface IClinicSettleService extends IService<ClinicSettle> {

    /**
     * 通过结算id查询
     *
     * @param paymentId 结算id
     * @return
     */
    ClinicSettle getByPaymentId(Long paymentId);

    /**
     * 通过结算id查询
     *
     * @param settleId 结算id
     * @return
     */
    ClinicSettle getBySettleId(String settleId);

    /**
     *
     * @param settleIds
     * @return
     */
    List<ClinicSettle> getBySettleIds(List<String> settleIds);
}