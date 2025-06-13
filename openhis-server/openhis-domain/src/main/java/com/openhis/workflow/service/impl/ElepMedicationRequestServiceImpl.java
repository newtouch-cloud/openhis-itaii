package com.openhis.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.openhis.common.enums.EventStatus;
import com.openhis.common.enums.RequestStatus;
import com.openhis.medication.domain.MedicationRequest;
import com.openhis.workflow.domain.ServiceRequest;
import com.openhis.workflow.domain.SupplyDelivery;
import com.openhis.workflow.domain.SupplyRequest;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.workflow.domain.ElepMedicationRequest;
import com.openhis.workflow.mapper.ElepMedicationRequestMapper;
import com.openhis.workflow.service.IElepMedicationRequestService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author system
 * @date 2025-05-07
 */
@Service
public class ElepMedicationRequestServiceImpl extends ServiceImpl<ElepMedicationRequestMapper, ElepMedicationRequest>
    implements IElepMedicationRequestService {

    /**
     * 获取处方信息
     *
     * @param prescriptionNo 处方号
     */
    @Override
    public List<ElepMedicationRequest> selectElepMedicationRequestByPrescriptionNo(String prescriptionNo) {
        return (baseMapper.selectList(new LambdaQueryWrapper<ElepMedicationRequest>()
            .eq(ElepMedicationRequest::getPrescriptionNo, prescriptionNo)));
    }
    /**
     * 获取处方信息
     *
     * @param prescriptionNoList 处方号
     */
    @Override
    public List<ElepMedicationRequest> selectElepMedicationRequestByPrescriptionNoList(List<String> prescriptionNoList) {
        return (baseMapper.selectList(new LambdaQueryWrapper<ElepMedicationRequest>()
            .in(ElepMedicationRequest::getPrescriptionNo, prescriptionNoList)));
    }

    /**
     * 获取处方信息
     *
     * @param idList id
     */
    @Override
    public List<ElepMedicationRequest> selectElepMedicationRequestById(List<Integer> idList) {
        return (baseMapper.selectList(new LambdaQueryWrapper<ElepMedicationRequest>()
            .in(ElepMedicationRequest::getId, idList)));

    }

    /**
     * 删除处方信息
     *
     * @param prescriptionNoList 处方号
     */
    @Override
    public boolean deleteElepMedicationRequestByPrescriptionNo(List<String> prescriptionNoList) {
        int count = baseMapper.delete(new LambdaQueryWrapper<ElepMedicationRequest>()
            .in(ElepMedicationRequest::getPrescriptionNo, prescriptionNoList));
        if (count > 0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 查询符合条件的 MedicationRequest 记录数。
     *
     * @param prescriptionNo 处方编号
     * @param list 处方类型代码集合
     * @param tenantId 租户 ID
     * @return 符合条件的记录数
     */
    @Override
    public Long selectWesternOrChineseCount(String prescriptionNo, List<Integer> list, Integer tenantId) {
        // 创建查询条件
        LambdaQueryWrapper<ElepMedicationRequest> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ElepMedicationRequest::getPrescriptionNo, prescriptionNo)
            .in(ElepMedicationRequest::getRxTypeCode, list).eq(ElepMedicationRequest::getTenantId, tenantId);

        // 调用 Mapper 的 selectCount 方法
        return this.baseMapper.selectCount(queryWrapper);
    }
}