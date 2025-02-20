package com.openhis.medication.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.medication.domain.Medication;
import com.openhis.medication.mapper.MedicationMapper;
import com.openhis.medication.service.IMedicationService;

/**
 * 药品基本信息管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class MedicationServiceImpl extends ServiceImpl<MedicationMapper, Medication> implements IMedicationService {

}