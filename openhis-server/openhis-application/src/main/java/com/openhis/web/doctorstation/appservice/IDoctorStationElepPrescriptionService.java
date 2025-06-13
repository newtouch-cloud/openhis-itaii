package com.openhis.web.doctorstation.appservice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.core.common.core.domain.R;
import com.openhis.common.enums.AssignSeqEnum;
import com.openhis.web.doctorstation.dto.AdviceBaseDto;
import com.openhis.web.doctorstation.dto.AdviceSaveParam;
import com.openhis.web.doctorstation.dto.DeletePrescriptionInfoParam;
import com.openhis.web.doctorstation.dto.ElepPrescriptionInfoParam;

import java.util.List;

/**
 * 医生站-电子处方 应用Service
 */
public interface IDoctorStationElepPrescriptionService {

    /**
     * 电子处方下拉框
     *
     * @return 下拉框信息
     */
    R<?> elepPrescriptionInit();

    /**
     * 获取全部药品信息
     *
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @param searchKey 模糊查询关键字
     * @return 药品信息
     */
    R<?> getAllMedicationInfo(String searchKey, Integer pageNo, Integer pageSize);

    /**
     * 获取处方信息
     *
     * @param patientId 患者id
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 药品信息
     */
    R<?> getPrescriptionInfo(Long patientId, Integer pageNo, Integer pageSize);

    /**
     * 获取药品信息
     *
     * @param prescriptionNo 处方号
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 药品详细信息
     */
    R<?> getMedicationInfo(String prescriptionNo, Integer pageNo, Integer pageSize);

    /**
     * 获取处方编号
     *
     * @return 初始化信息
     */
    R<?> prescriptionNoInit();

    /**
     * 保存处方
     *
     * @param prescriptionInfo 处方信息
     * @return 执行结果
     */
    R<?> savePrescriptionInfo(ElepPrescriptionInfoParam prescriptionInfo);

    /**
     * 修改处方
     *
     * @param prescriptionInfo 处方信息
     * @return 执行结果
     */
    R<?> updatePrescriptionInfo(ElepPrescriptionInfoParam prescriptionInfo);

    /**
     * 删除处方
     *
     * @param deletePrescriptionInfoParam 处方信息
     * @return 执行结果
     */
    R<?> deletePrescriptionInfo(DeletePrescriptionInfoParam deletePrescriptionInfoParam);

    /**
     * 签发处方
     *
     * @param prescriptionNoList 处方号
     * @return 药品详细信息
     */
    R<?> issuancePrescription(List<String> prescriptionNoList);

}
