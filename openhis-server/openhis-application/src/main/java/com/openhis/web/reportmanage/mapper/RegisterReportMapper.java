/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.reportmanage.dto.RegisterReportPageDto;
import com.openhis.web.reportmanage.dto.RegisterReportSearchParam;
import com.openhis.web.reportmanage.dto.TransferReportPageDto;
import com.openhis.web.reportmanage.dto.TransferReportSearchParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 挂号明细报表查询用 mapper
 *
 * @author yuxj
 * @date 2025-05-20
 */
@Repository
public interface RegisterReportMapper {

    /**
     * 查询挂号明细
     * 
     * @param page 分页
     * @param queryWrapper 查询条件
     * @param contextEnum 类别 ：挂号费
     * @param typeCode 参与者类型： 首诊医生
     * @param statusEnum 状态 ：支付成功
     * @param paymentEnum 付款类别：付费
     * @return 挂号明细
     */
    Page<RegisterReportPageDto> selectRegisterReportPage(@Param("page") Page<RegisterReportPageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<RegisterReportSearchParam> queryWrapper,
        @Param("contextEnum") Integer contextEnum, @Param("typeCode") String typeCode,
        @Param("success") Integer success, @Param("refundAll") Integer refundAll);
}
