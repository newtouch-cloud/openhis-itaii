package com.openhis.workflow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.workflow.domain.SupplyRequest;

/**
 * 供应申请管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface ISupplyRequestService extends IService<SupplyRequest> {

    /**
     * 查询供应申请分页列表
     *
     * @param supplyRequest 查询条件
     * @return 供应申请列表
     */
    Page<SupplyRequest> getPage(SupplyRequest supplyRequest, Integer pageNo, Integer pageSize);


}