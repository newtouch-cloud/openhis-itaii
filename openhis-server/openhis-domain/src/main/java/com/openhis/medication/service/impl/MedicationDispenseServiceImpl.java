package com.openhis.medication.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.core.common.utils.DateUtils;
import com.openhis.common.enums.DispenseStatus;
import com.openhis.medication.domain.MedicationDispense;
import com.openhis.medication.domain.MedicationRequest;
import com.openhis.medication.mapper.MedicationDispenseMapper;
import com.openhis.medication.service.IMedicationDispenseService;

/**
 * 药品发放管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class MedicationDispenseServiceImpl extends ServiceImpl<MedicationDispenseMapper, MedicationDispense>
    implements IMedicationDispenseService {

    /**
     * 新增草稿状态的药品发放信息
     *
     * @param medicationRequestList 药品请求信息
     */
    @Override
    public void addMedicationDispense(List<MedicationRequest> medicationRequestList) {
        for (MedicationRequest medicationRequest : medicationRequestList) {
            MedicationDispense medicationDispense = new MedicationDispense();
            // 药品发放id
            medicationDispense.setBusNo(medicationRequest.getBusNo());
            // 药品发放状态
            medicationDispense.setStatusEnum(DispenseStatus.DRAFT.getValue());
            // 状态变更时间
            medicationDispense.setStatusChangedTime(DateUtils.getNowDate());
            // 发药类型
            medicationDispense.setDispenseEnum(medicationRequest.getCategoryEnum());
            // 药品编码
            medicationDispense.setMedicationId(medicationRequest.getMedicationId());
            // 请求数量
            medicationDispense.setQuantity(medicationRequest.getQuantity());
            // 请求单位编码
            medicationDispense.setUnitCode(medicationRequest.getUnitCode());
            // 产品批号
            medicationDispense.setLotNumber(medicationRequest.getLotNumber());
            // 患者id
            medicationDispense.setPatientId(medicationRequest.getPatientId());
            // 就诊id
            medicationDispense.setEncounterId(medicationRequest.getEncounterId());
            // 支持用药信息
            medicationDispense.setSupportInfo(medicationRequest.getSupportInfo());
            // 发药人
            medicationDispense.setPractitionerId(medicationRequest.getPractitionerId());
            // 发放药房
            medicationDispense.setLocationId(medicationRequest.getPerformOrg());
            // 药品请求id
            medicationDispense.setMedReqId(medicationRequest.getId());
            // 已发药数量
            medicationDispense.setDispenseQuantity(0);
            // 用法
            medicationDispense.setMethodCode(medicationRequest.getMethodCode());
            // 用药频次
            medicationDispense.setFrequencyCode(medicationRequest.getRateCode());
            // 单次剂量
            medicationDispense.setDose(medicationRequest.getDose());
            // 剂量单位
            medicationDispense.setDoseUnitCode(medicationRequest.getDoseUnitCode());
            // 单次最大剂量
            medicationDispense.setMaxUnit(medicationRequest.getMaxDose());
            baseMapper.insert(medicationDispense);
        }
    }
}