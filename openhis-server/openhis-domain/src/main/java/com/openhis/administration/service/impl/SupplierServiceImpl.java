package com.openhis.administration.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.utils.StringUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.common.enums.DelFlag;
import com.openhis.workflow.domain.SupplyRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.Supplier;
import com.openhis.administration.mapper.SupplierMapper;
import com.openhis.administration.service.ISupplierService;

/**
 * 供应商管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Slf4j
@Service
@AllArgsConstructor
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements ISupplierService {

    private final SupplierMapper supplierMapper;
    /**
     * 查询厂商/产地列表
     *
     * @param supplier 查询条件
     * @param pageNo 查询条件
     * @param pageSize 查询条件
     * @return 厂商/产地列表
     */
    @Override
    public Page<Supplier> getPage(Supplier supplier, Integer pageNo, Integer pageSize) {
        Page<Supplier> supplierList = new Page<>();
//        // 判断入库数量
//        if (supplyRequest.getItemQuantity() != 0) {
//            // 查询供应申请信息列表
//            supplyRequestList = supplyRequestMapper.selectPage(new Page<>(pageNo, pageSize),
//                    new LambdaQueryWrapper<SupplyRequest>().eq(SupplyRequest::getDeleteFlag, DelFlag.NO.getValue()));
//        } else {
//            return null;
//        }

        // 设置查询条件
        LambdaQueryWrapper<Supplier> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(supplier.getBusNo())) {
            lambdaQueryWrapper.eq(Supplier::getBusNo, supplier.getBusNo());
        }
        if (StringUtils.isNotEmpty(supplier.getName())) {
            lambdaQueryWrapper.eq(Supplier::getName, supplier.getName());
        }
        if (StringUtils.isNotNull(supplier.getTypeEnum())) {
            lambdaQueryWrapper.eq(Supplier::getTypeEnum, supplier.getTypeEnum());
        }
        lambdaQueryWrapper.eq(Supplier::getDeleteFlag, DelFlag.NO.getValue());

        supplierList = supplierMapper.selectPage(new Page<>(pageNo, pageSize), lambdaQueryWrapper);
        // 返回入库信息列表
        return supplierList;
    }
}