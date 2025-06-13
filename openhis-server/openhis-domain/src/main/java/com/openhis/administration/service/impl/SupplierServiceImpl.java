package com.openhis.administration.service.impl;

import java.util.List;

import com.openhis.common.enums.DelFlag;
import com.openhis.common.enums.Whether;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.Supplier;
import com.openhis.administration.mapper.SupplierMapper;
import com.openhis.administration.service.ISupplierService;
import com.openhis.common.enums.SupplierType;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

    /**
     * 新增供应商
     *
     * @param supplier 厂商/产地实体
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addSupplier(Supplier supplier) {
        // 根据病种编码判断病种是否存在
        List<Supplier> suppliers =
            baseMapper.selectList(new LambdaQueryWrapper<Supplier>().eq(Supplier::getBusNo, supplier.getBusNo()));
        if (suppliers.size() > 0) {
            return false;
        }
        // 新增供应商
        int insert = baseMapper.insert(supplier);
        if (insert != 1) {
            return false;
        }
        return true;
    }

    /**
     * 查询供应商下拉列表
     *
     * @return 供应商下拉列表
     */
    @Override
    public List<Supplier> getList() {
        return baseMapper.selectList(new LambdaQueryWrapper<Supplier>().select(Supplier::getId, Supplier::getName)
            .eq(Supplier::getTypeEnum, SupplierType.ACTIVE.getValue())
            .eq(Supplier::getDeleteFlag, DelFlag.NO.getCode()).eq(Supplier::getActiveFlag, Whether.YES.getValue()));
    }

    /**
     * 查询供应商信息
     *
     * @param name 名字
     * @param address 地址
     * @param typeEnum 类型
     * @return 供应商信息
     */
    @Override
    public List<Supplier> getsupplierList(String name, String address, Integer typeEnum) {
        return baseMapper.selectList(new LambdaQueryWrapper<Supplier>().eq(Supplier::getName, name)
            .eq(Supplier::getAddress, address).eq(Supplier::getTypeEnum, typeEnum));
    }
}
