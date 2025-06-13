package com.openhis.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.core.common.core.domain.model.LoginUser;
import com.openhis.administration.domain.Practitioner;
import com.openhis.workflow.domain.DeviceRequest;

import java.util.Date;
import java.util.List;

/**
 * 器材请求管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IDeviceRequestService extends IService<DeviceRequest> {

    /**
     * 器材请求状态：已完成
     *
     * @param id ID
     * @param loginUser 登录用户信息
     */
    boolean completedStatusEnum(Long id, Practitioner loginUser);

    /**
     * 服务申请状态：已发送
     *
     * @param id ID
     * @param loginUser 登录用户信息
     */
    boolean activeStatusEnum(Long id, Practitioner loginUser);

    /**
     * 更新请求状态：已完成
     *
     * @param devRequestIdList 耗材请求id列表
     */
    void updateCompletedStatusBatch(List<Long> devRequestIdList);

    /**
     * 更新请求状态：待发送
     *
     * @param devReqIdList 耗材请求id列表
     */
    void updateDraftStatusBatch(List<Long> devReqIdList);
}