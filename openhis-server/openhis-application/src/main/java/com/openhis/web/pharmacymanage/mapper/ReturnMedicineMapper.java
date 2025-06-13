/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.pharmacymanage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.pharmacymanage.dto.*;

@Repository
public interface ReturnMedicineMapper {

    /**
     * 查询退药患者分页列表
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @return 退药患者分页列表
     */
    Page<EncounterInfoPageDto> selectEncounterInfoListPage(@Param("page") Page<EncounterInfoPageDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<EncounterInfoPageDto> queryWrapper);

    /**
     * 申请退药清单查询
     *
     * @param encounterId 就诊ID
     * @param medMedicationRequest 药品请求表
     * @param worDeviceRequest 耗材请求表
     * @param medicine 项目类型：药品
     * @param device 项目类型：耗材
     * @param refundStatus 退药状态
     * @param inRefund 退药状态:待退药
     * @param completed 退药状态:已完成
     * @return 申请退药清单
     */
    List<ReturnMedicineInfoDto> selectReturnMedicineInfo(@Param("encounterId") Long encounterId,
        @Param("worDeviceRequest") String worDeviceRequest, @Param("medMedicationRequest") String medMedicationRequest,
        @Param("medicine") Integer medicine, @Param("device") Integer device,
        @Param("refundStatus") Integer refundStatus, @Param("inRefund") Integer inRefund,
        @Param("completed") Integer completed);

    /**
     * 患者基本信息查询
     *
     * @param encounterId 就诊ID
     * @return 患者基本信息
     */
    PrescriptionPatientInfoDto selectPrescriptionPatientInfo(@Param("encounterId") Long encounterId);

    /**
     * 库存信息查询
     *
     * @param devDispenseIdList 耗材发放ID列表
     * @param medDispenseIdList 药品发放ID列表
     * @param medMedicationDefinition 药品定义表
     * @param admDeviceDefinition 耗材定义表
     * @return 待发药信息
     */
    List<InventoryDto> selectInventoryInfoList(@Param("devDispenseIdList") List<Long> devDispenseIdList,
        @Param("medDispenseIdList") List<Long> medDispenseIdList,
        @Param("medMedicationDefinition") String medMedicationDefinition,
        @Param("admDeviceDefinition") String admDeviceDefinition);

    /**
     * 退药详细信息查询
     * 
     * @param devDispenseIdList 耗材发放ID列表
     * @param medDispenseIdList 药品发放ID列表
     * @param medMedicationDefinition 药品定义表
     * @param admDeviceDefinition 耗材定义表
     * @return 退药信息
     */
    List<DispenseInventoryDto> selectReturnItemDetail(@Param("devDispenseIdList") List<Long> devDispenseIdList,
        @Param("medDispenseIdList") List<Long> medDispenseIdList,
        @Param("medMedicationDefinition") String medMedicationDefinition,
        @Param("admDeviceDefinition") String admDeviceDefinition);
}
