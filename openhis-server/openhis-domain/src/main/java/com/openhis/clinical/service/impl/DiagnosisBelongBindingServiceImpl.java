package com.openhis.clinical.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.clinical.domain.DiagnosisBelongBinding;
import com.openhis.clinical.mapper.DiagnosisBelongBindingMapper;
import com.openhis.clinical.service.IDiagnosisBelongBindingService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 诊断归属绑定Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Slf4j
@Service
@AllArgsConstructor
public class DiagnosisBelongBindingServiceImpl extends ServiceImpl<DiagnosisBelongBindingMapper, DiagnosisBelongBinding>
    implements IDiagnosisBelongBindingService {

}