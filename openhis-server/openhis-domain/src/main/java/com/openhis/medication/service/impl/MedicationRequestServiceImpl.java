package com.openhis.medication.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.common.enums.RequestStatus;
import com.openhis.medication.domain.MedicationRequest;
import com.openhis.medication.mapper.MedicationRequestMapper;
import com.openhis.medication.service.IMedicationRequestService;

/**
 * 药品请求管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class MedicationRequestServiceImpl extends ServiceImpl<MedicationRequestMapper, MedicationRequest>
    implements IMedicationRequestService {

    /**
     * 更新请求状态：已完成
     *
     * @param requestIdList 药品请求id列表
     */
    @Override
    public void updateCompletedStatusBatch(List<Long> requestIdList) {
        baseMapper.update(new MedicationRequest().setStatusEnum(RequestStatus.COMPLETED.getValue()),
            new LambdaUpdateWrapper<MedicationRequest>().in(MedicationRequest::getId, requestIdList));
    }

    /**
     * 更新请求状态：待发送
     *
     * @param requestIdList 药品请求id列表
     */
    @Override
    public void updateDraftStatusBatch(List<Long> requestIdList) {
        baseMapper.update(null, new LambdaUpdateWrapper<MedicationRequest>().in(MedicationRequest::getId, requestIdList)
            .set(MedicationRequest::getStatusEnum, RequestStatus.DRAFT.getValue()));
    }

}
