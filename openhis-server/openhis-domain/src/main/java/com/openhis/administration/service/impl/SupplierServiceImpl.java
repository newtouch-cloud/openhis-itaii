package com.openhis.administration.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.utils.StringUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.clinical.domain.ConditionDefinition;
import com.openhis.clinical.mapper.ConditionDefinitionMapper;
import com.openhis.common.enums.DelFlag;
import com.openhis.workflow.domain.SupplyRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.Supplier;
import com.openhis.administration.mapper.SupplierMapper;
import com.openhis.administration.service.ISupplierService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
                supplierMapper.selectList(new LambdaQueryWrapper<Supplier>()
                        .eq(Supplier::getBusNo, supplier.getBusNo()));
        if (suppliers.size() > 0) {
            return false;
        }
        // 新增供应商
        int insert = supplierMapper.insert(supplier);
        if (insert != 1) {
            return false;
        }
        return true;
    }
}