package com.openhis.medication.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.medication.domain.MedicationRequest;
import com.openhis.medication.mapper.MedicationRequestMapper;
import com.openhis.medication.service.IMedicationRequestService;

/**
 * 药品请求管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class MedicationRequestServiceImpl extends ServiceImpl<MedicationRequestMapper, MedicationRequest> implements IMedicationRequestService {

}