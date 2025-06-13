/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.openhis.web.reportmanage.dto.ChargeReportPageDto;
import com.openhis.web.reportmanage.dto.ChargeReportSearchParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 费用明细报表查询用 mapper
 *
 * @author yuxj
 * @date 2025-05-20
 */
@Repository
public interface ChargeReportMapper {

    /**
     * 查询费用明细
     *
     * @param queryWrapper 查询条件
     * @param success 状态 ：支付成功
     * @param refundAll 状态 ：全部退款
     * @param medication 药品
     * @param device 耗材
     * @param activity 项目
     * @param register 挂号
     * @param statisticsFlg 统计类型flg
     * @return 费用明细
     */
    Page<ChargeReportPageDto> selectRevenueReportPage(@Param("page") Page<ChargeReportPageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<ChargeReportSearchParam> queryWrapper, @Param("success") Integer success,
        @Param("refundAll") Integer refundAll, @Param("medication") Integer medication, @Param("device") Integer device,
        @Param("activity") Integer activity, @Param("register") Integer register,
        @Param("statisticsFlg") Integer statisticsFlg);
}
