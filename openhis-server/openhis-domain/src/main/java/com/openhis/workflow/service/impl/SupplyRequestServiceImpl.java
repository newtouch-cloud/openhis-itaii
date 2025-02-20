package com.openhis.workflow.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.common.enums.DelFlag;
import com.openhis.workflow.domain.SupplyRequest;
import com.openhis.workflow.mapper.SupplyRequestMapper;
import com.openhis.workflow.service.ISupplyRequestService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 供应申请管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Slf4j
@Service
@AllArgsConstructor
public class SupplyRequestServiceImpl extends ServiceImpl<SupplyRequestMapper, SupplyRequest>
    implements ISupplyRequestService {

    private final SupplyRequestMapper supplyRequestMapper;

    /**
     * 查询供应申请列表
     *
     * @param supplyRequest 查询条件
     * @return 供应申请列表
     */
    @Override
    public Page<SupplyRequest> getPage(SupplyRequest supplyRequest, Integer pageNo, Integer pageSize) {

        Page<SupplyRequest> supplyRequestList;
        // 判断入库数量
        if (supplyRequest.getItemQuantity() != 0) {
            // 查询供应申请信息列表
            supplyRequestList = supplyRequestMapper.selectPage(new Page<>(pageNo, pageSize),
                new LambdaQueryWrapper<SupplyRequest>().eq(SupplyRequest::getDeleteFlag, DelFlag.NO.getValue()));
        } else {
            return null;
        }
        // 返回入库信息列表
        return supplyRequestList;
    }
}