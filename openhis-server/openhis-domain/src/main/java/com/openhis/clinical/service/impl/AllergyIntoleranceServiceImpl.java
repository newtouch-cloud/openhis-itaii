package com.openhis.clinical.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.clinical.domain.AllergyIntolerance;
import com.openhis.clinical.mapper.AllergyIntoleranceMapper;
import com.openhis.clinical.service.IAllergyIntoleranceService;

/**
 * 过敏与不耐受Service业务层处理
 *
 * @author system
 * @date 2025-03-07
 */
@Service
public class AllergyIntoleranceServiceImpl extends ServiceImpl<AllergyIntoleranceMapper, AllergyIntolerance> implements IAllergyIntoleranceService {

}