package com.openhis.administration.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.Encounter;
import com.openhis.administration.mapper.EncounterMapper;
import com.openhis.administration.service.IEncounterService;

/**
 * 就诊管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class EncounterServiceImpl extends ServiceImpl<EncounterMapper, Encounter> implements IEncounterService {

}