/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.inventorymanage.dto.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 采购退货查询用 mapper
 *
 * @author yuanzs
 * @date 2025-04-02
 */
@Repository
public interface PurchaseReturnMapper {

    /**
     * 查询退货单据详情
     *
     * @param busNo 单据号
     * @param purchaseReturn 类型：采购退货
     * @param agree 请求状态：同意
     * @return 退货单据列表
     */
    List<PurchaseReturnDetailDto> selectDetail(@Param("busNo") String busNo,
        @Param("medicationTableName") String medicationTableName, @Param("deviceTableName") String deviceTableName,
        @Param("medicine") Integer medicine, @Param("device") Integer device,
        @Param("purchaseReturn") Integer purchaseReturn, @Param("agree") Integer agree);

    /**
     * 查询已生成的退货单
     *
     * @param busNo 单据号
     * @param purchaseReturn 类型：采购退货
     * @return 就诊患者账单列表
     */
    List<PurchaseReturnPageDto> selectGeneratedDetail(@Param("busNo") String busNo,
        @Param("purchaseReturn") Integer purchaseReturn);
}
