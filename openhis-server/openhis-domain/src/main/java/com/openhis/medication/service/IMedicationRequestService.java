package com.openhis.medication.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.medication.domain.MedicationRequest;

/**
 * 药品请求管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IMedicationRequestService extends IService<MedicationRequest> {

    /**
     * 更新请求状态：已完成
     *
     * @param requestIdList 药品请求id列表
     */
    void updateCompletedStatusBatch(List<Long> requestIdList);

    /**
     * 更新请求状态：待发送
     *
     * @param requestIdList 药品请求id列表
     */
    void updateDraftStatusBatch(List<Long> requestIdList);


}
