/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.appservice.impl;

import java.util.Arrays;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import com.openhis.common.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.enums.CategoryType;
import com.openhis.common.enums.SupplyStatus;
import com.openhis.common.enums.SupplyType;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.reportmanage.appservice.ITransferReportAppService;
import com.openhis.web.reportmanage.dto.TransferReportPageDto;
import com.openhis.web.reportmanage.dto.TransferReportSearchParam;
import com.openhis.web.reportmanage.mapper.TransferReportMapper;

/**
 * 商品调拨明细报表 impl
 *
 * @author GYY
 * @date 2025-04-15
 */
@Service
public class TransferReportAppServiceImpl implements ITransferReportAppService {

    @Autowired
    private TransferReportMapper transferReportMapper;

    /**
     * 商品调拨明细列表
     *
     * @param transferReportSearchParam 商品调拨明细查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param searchKey 模糊查询关键字
     * @param request 请求数据
     * @return 商品调拨明细
     */
    @Override
    public R<?> getPage(TransferReportSearchParam transferReportSearchParam, Integer pageNo, Integer pageSize,
        String searchKey, HttpServletRequest request) {

        // 构建查询条件
        QueryWrapper<TransferReportSearchParam> queryWrapper = HisQueryUtils.buildQueryWrapper(
            transferReportSearchParam, searchKey, new HashSet<>(Arrays.asList("name", "supply_bus_no")), request);

        // 查询商品调拨明细分页列表
        Page<TransferReportPageDto> transferReportPage =
            transferReportMapper.selectTransferReportPage(new Page<>(pageNo, pageSize), queryWrapper,
                CommonConstants.TableName.MED_MEDICATION_DEFINITION, CommonConstants.TableName.ADM_DEVICE_DEFINITION,
                SupplyType.PRODUCT_TRANSFER.getValue(), SupplyType.PRODUCT_BATCH_TRANSFER.getValue(),
                SupplyStatus.AGREE.getValue(), EventStatus.COMPLETED.getValue());

        transferReportPage.getRecords().forEach(e -> {
            // 单据状态
            e.setStatusEnum_enumText(EnumUtils.getInfoByValue(SupplyStatus.class, e.getStatusEnum()));
        });
        return R.ok(transferReportPage);
    }
}
