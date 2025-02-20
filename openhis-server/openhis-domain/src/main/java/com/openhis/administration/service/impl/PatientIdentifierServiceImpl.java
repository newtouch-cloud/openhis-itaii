package com.openhis.administration.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.PatientIdentifier;
import com.openhis.administration.mapper.PatientIdentifierMapper;
import com.openhis.administration.service.IPatientIdentifierService;

/**
 * 患者标识管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class PatientIdentifierServiceImpl extends ServiceImpl<PatientIdentifierMapper, PatientIdentifier> implements IPatientIdentifierService {

}