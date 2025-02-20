package com.openhis.administration.mapper;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.openhis.administration.domain.Patient;

/**
 * 患者管理Mapper接口
 *
 * @author system
 * @date 2025-02-20
 */
@Repository
public interface PatientMapper extends BaseMapper<Patient> {

}