package com.openhis.clinical.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.clinical.domain.Condition;
import com.openhis.clinical.mapper.ConditionMapper;
import com.openhis.clinical.service.IConditionService;

/**
 * 疾病与诊断管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class ConditionServiceImpl extends ServiceImpl<ConditionMapper, Condition> implements IConditionService {

}