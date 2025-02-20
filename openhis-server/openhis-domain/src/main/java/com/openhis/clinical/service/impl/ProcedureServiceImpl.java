package com.openhis.clinical.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.clinical.domain.Procedure;
import com.openhis.clinical.mapper.ProcedureMapper;
import com.openhis.clinical.service.IProcedureService;

/**
 * 手术与治疗管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class ProcedureServiceImpl extends ServiceImpl<ProcedureMapper, Procedure> implements IProcedureService {

}