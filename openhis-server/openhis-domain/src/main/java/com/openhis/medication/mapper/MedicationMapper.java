package com.openhis.medication.mapper;

import com.openhis.medication.domain.MedicationDetail;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.openhis.medication.domain.Medication;

import java.util.List;

/**
 * 药品基本信息管理Mapper接口
 *
 * @author system
 * @date 2025-02-20
 */
@Repository
public interface MedicationMapper extends BaseMapper<Medication> {

    /**
     * 查询药品详细信息列表
     *
     * @return 药品详细信息列表
     */
    List<MedicationDetail> selectDetailList();
}