package com.openhis.workflow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.workflow.domain.ServiceRequest;

/**
 * 服务申请管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IServiceRequestService extends IService<ServiceRequest> {


    /**
     * 查询服务申请管理中basedOnId相同的个数
     *
     * @param basedOnId 请求基于什么的ID
     * @return basedOnId相同的个数
     */
    Long countServiceRequestByBasedOnId(Long basedOnId) ;

}