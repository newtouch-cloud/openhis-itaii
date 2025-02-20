package com.openhis.medication.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.medication.domain.MedicationDefinition;
import com.openhis.medication.mapper.MedicationDefinitionMapper;
import com.openhis.medication.service.IMedicationDefinitionService;

/**
 * 药品定义管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class MedicationDefinitionServiceImpl extends ServiceImpl<MedicationDefinitionMapper, MedicationDefinition> implements IMedicationDefinitionService {

}