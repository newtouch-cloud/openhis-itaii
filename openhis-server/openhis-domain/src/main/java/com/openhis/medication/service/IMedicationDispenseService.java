package com.openhis.medication.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.Practitioner;
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
     * 处理药品发放信息
     *
     * @param medicationRequest 药品请求信息
     * @param dbOpType db操作类型
     */
    void handleMedicationDispense(MedicationRequest medicationRequest, String dbOpType);

    /**
     * 删除药品发放信息
     * 
     * @param medReqId 药品请求id
     */
    void deleteMedicationDispense(Long medReqId);

    /**
     * 更新未发放药品状态：停止发放
     *
     * @param medDisIdList 发放id列表
     * @param refund 停止原因：退费
     */
    void updateStopDispenseStatus(List<Long> medDisIdList, Integer refund);

    /**
     * 更新药品状态：待配药
     *
     * @param medicationRequestIdList 请求id列表
     */
    void updatePreparationDispenseStatus(List<Long> medicationRequestIdList);

    /**
     * 更新药品状态：暂停
     *
     * @param medReqIdList 请求id列表
     */
    void updateOnHoldDispenseStatus(List<Long> medReqIdList);

    /**
     * 验证是否发过药
     *
     * @param medicationDefId 药品定义id
     * @return 验证结果
     */
    boolean verifyAbleEdit(Long medicationDefId);
}