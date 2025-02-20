package com.openhis.medication.mapper;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.openhis.medication.domain.Medication;

/**
 * 药品基本信息管理Mapper接口
 *
 * @author system
 * @date 2025-02-20
 */
@Repository
public interface MedicationMapper extends BaseMapper<Medication> {

}