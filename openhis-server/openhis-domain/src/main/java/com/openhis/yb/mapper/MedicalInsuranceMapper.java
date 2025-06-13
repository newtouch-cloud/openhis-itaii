/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.mapper;


import com.openhis.yb.dto.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * mapper提供sql服务
 *
 * @author SunJQ
 * @date 2025-04-29
 */
@Repository
public interface MedicalInsuranceMapper {
    /**
     * 计算对账金额
     *
     * @param stmtBegnDate 开始时间
     * @param stmtEndDate 结束时间
     * @param tenantId 租户id
     * @param orgId 医院id
     * @param InsuType 险种
     * @param clrType 门诊/住院
     * @param admVs 清算机构
     * @return 查询结果
     */
    Settlement3201Dto get3201Param(@Param("startTime") Date stmtBegnDate, @Param("endTime") Date stmtEndDate,
                                   @Param("tenantId") Integer tenantId, @Param("orgId") Long orgId, @Param("InsuType") String InsuType,
                                   @Param("clrType") Integer clrType, @Param("admVs") String admVs, @Param("zhPay") Integer zhPay,
                                   @Param("zhGjPay") Integer zhGjPay, @Param("ybFund") Integer ybFund);

    Settlement3202Dto get3202Param(@Param("startTime") String stmtBegnDate, @Param("endTime") String stmtEndDate,
                                   @Param("tenantId") Integer tenantId, @Param("orgId") Long orgId, @Param("clrType") String clrType,
                                   @Param("admVs") String admVs, @Param("zhPay") Integer zhPay, @Param("zhGjPay") Integer zhGjPay,
                                   @Param("cash") Integer cash, @Param("cashVX") Integer cashVX, @Param("cashAli") Integer cashAli,
                                   @Param("cashUNIN") Integer cashUNIN, @Param("ybFund") Integer ybFund);

    Financial3203Dto get3203Param(@Param("startTime") Date begndate, @Param("endTime") Date enddate,
                                  @Param("tenantId") Integer tenantId, @Param("clrType") String clrType, @Param("zhPay") Integer zhPay,
                                  @Param("zhGjPay") Integer zhGjPay, @Param("cash") Integer cash, @Param("cashVX") Integer cashVX,
                                  @Param("cashAli") Integer cashAli, @Param("cashUNIN") Integer cashUNIN, @Param("ybFund") Integer ybFund);


    List<Settlement3201DetailDto> get3201ParamList(@Param("startTime") String stmtBegnDate, @Param("endTime") String stmtEndDate,
                                                   @Param("tenantId") Integer tenantId, @Param("orgId") Long orgId, @Param("InsuType") String InsuType,
                                                   @Param("clrType") String clrType, @Param("admVs") String admVs, @Param("zhPay") Integer zhPay,
                                                   @Param("zhGjPay") Integer zhGjPay, @Param("ybFund") Integer ybFund);


    List<PaymentDecDetailUniAccountDto> getPaymentDecDetailUniAccountDtoListByPaymentIdList(
            @Param("idList")List<Long> idList, @Param("startTime")Date startTime,
            @Param("endTime")Date endTime);
}
