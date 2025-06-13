package com.openhis.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.workflow.domain.ElepMedicationRequest;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author system
 * @date 2025-05-07
 */
public interface IElepMedicationRequestService extends IService<ElepMedicationRequest> {
    /**
     * 获取处方信息
     *
     * @param prescriptionNo 处方号
     */
    List<ElepMedicationRequest> selectElepMedicationRequestByPrescriptionNo(String prescriptionNo);
    /**
     * 获取处方信息
     *
     * @param prescriptionNoList 处方号
     */
    List<ElepMedicationRequest> selectElepMedicationRequestByPrescriptionNoList(List<String> prescriptionNoList);
    /**
     * 获取处方信息
     *
     * @param idList id
     */
    List<ElepMedicationRequest> selectElepMedicationRequestById(List<Integer> idList);


    /**
     * 删除处方信息
     *
     * @param prescriptionNoList 处方号
     */
    boolean deleteElepMedicationRequestByPrescriptionNo(List<String> prescriptionNoList);

    /**
     * 查询符合条件的记录数。
     *
     * @param prescriptionNo 处方编号
     * @param list 处方类型代码集合
     * @param tenantId 租户 ID
     * @return 符合条件的记录数
     */
    Long selectWesternOrChineseCount(String prescriptionNo, List<Integer> list, Integer tenantId);
}