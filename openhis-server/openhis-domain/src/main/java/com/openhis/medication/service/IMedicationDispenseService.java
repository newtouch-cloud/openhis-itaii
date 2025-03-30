package com.openhis.medication.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.medication.domain.MedicationDispense;
import com.openhis.medication.domain.MedicationRequest;

/**
 * 药品发放管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IMedicationDispenseService extends IService<MedicationDispense> {

    /**
     * 新增草稿状态的药品发放信息
     *
     * @param medicationRequestList 药品请求信息
     */
    void addMedicationDispense(List<MedicationRequest> medicationRequestList);

    /**
     * 更新未发放药品状态：停止发放
     *
     * @param medDisIdList 发放id列表
     * @param refund 停止原因：退费
     */
    void updateStopDispenseStatus(List<Long> medDisIdList, Integer refund);
}