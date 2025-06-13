package com.openhis.medication.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.DateUtils;
import com.openhis.common.enums.AssignSeqEnum;
import com.openhis.common.enums.DbOpType;
import com.openhis.common.enums.DelFlag;
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

    @Resource
    AssignSeqUtil assignSeqUtil;

    /**
     * 处理药品发放信息
     *
     * @param medicationRequest 药品请求信息
     * @param dbOpType db操作类型
     */
    @Override
    public void handleMedicationDispense(MedicationRequest medicationRequest, String dbOpType) {
        MedicationDispense medicationDispense = new MedicationDispense();
        // 药品发放id
        medicationDispense.setBusNo(assignSeqUtil.getSeq(AssignSeqEnum.MEDICATION_DIS_NO.getPrefix(), 10));
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
        medicationDispense.setLocationId(medicationRequest.getPerformLocation());
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

        if (DbOpType.INSERT.getCode().equals(dbOpType)) {
            baseMapper.insert(medicationDispense);
        } else if (DbOpType.UPDATE.getCode().equals(dbOpType)) {
            baseMapper.update(medicationDispense, new LambdaUpdateWrapper<MedicationDispense>()
                .eq(MedicationDispense::getMedReqId, medicationRequest.getId()));
        }
    }

    /**
     * 删除药品发放信息
     *
     * @param medReqId 药品请求id
     */
    @Override
    public void deleteMedicationDispense(Long medReqId) {
        baseMapper.delete(new LambdaQueryWrapper<MedicationDispense>().eq(MedicationDispense::getMedReqId, medReqId));
    }

    /**
     * 更新未发放药品状态：停止发放
     *
     * @param medDisIdList 发放id列表
     * @param refund 停止原因：退费
     */
    @Override
    public void updateStopDispenseStatus(List<Long> medDisIdList, Integer refund) {
        baseMapper.update(
            new MedicationDispense().setStatusEnum(DispenseStatus.STOPPED.getValue()).setNotPerformedReasonEnum(refund),
            new LambdaUpdateWrapper<MedicationDispense>().in(MedicationDispense::getId, medDisIdList));
    }

    /**
     * 更新药品状态：待配药
     *
     * @param medicationRequestIdList 请求id列表
     */
    @Override
    public void updatePreparationDispenseStatus(List<Long> medicationRequestIdList) {
        baseMapper.update(null,
            new LambdaUpdateWrapper<MedicationDispense>()
                .set(MedicationDispense::getStatusEnum, DispenseStatus.PREPARATION.getValue())
                .set(MedicationDispense::getStatusChangedTime, DateUtils.getNowDate())
                .in(MedicationDispense::getMedReqId, medicationRequestIdList));
    }

    /**
     * 更新药品状态：暂停
     *
     * @param medReqIdList 请求id列表
     */
    @Override
    public void updateOnHoldDispenseStatus(List<Long> medReqIdList) {
        baseMapper.update(null,
            new LambdaUpdateWrapper<MedicationDispense>()
                .set(MedicationDispense::getStatusEnum, DispenseStatus.ON_HOLD.getValue())
                .set(MedicationDispense::getStatusChangedTime, DateUtils.getNowDate())
                .in(MedicationDispense::getMedReqId, medReqIdList));
    }

    /**
     * 验证是否发过药
     *
     * @param medicationDefId 药品定义id
     * @return 验证结果
     */
    @Override
    public boolean verifyAbleEdit(Long medicationDefId) {
        return baseMapper.exists(
            new LambdaQueryWrapper<MedicationDispense>().eq(MedicationDispense::getMedicationId, medicationDefId)
                .and(q -> q.eq(MedicationDispense::getStatusEnum, DispenseStatus.COMPLETED.getValue()).or()
                    .eq(MedicationDispense::getStatusEnum, DispenseStatus.REFUNDED.getValue()))
                .eq(MedicationDispense::getDeleteFlag, DelFlag.NO.getCode()));
    }
}