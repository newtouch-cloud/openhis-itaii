package com.openhis.web.pharmacymanage.appservice;

import com.openhis.medication.domain.MedicationRequest;

import java.util.List;

/**
 * 新增药品发放草稿 应用实现接口
 *
 * @author wangyang
 * @date 2025/3/14
 */
public interface IDispenseAddAppService {

    /**
     * 新增药品发放草稿
     *
     * @param medicationRequestList
     * @return 无
     */
    boolean addMedicationDispense(List<MedicationRequest> medicationRequestList);
}
