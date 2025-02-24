package com.openhis.document.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.document.domain.EmrDetail;
import com.openhis.document.mapper.EmrDetailMapper;
import com.openhis.document.service.IEmrDetailService;

/**
 * 电子病历详情Service业务层处理
 *
 * @author system
 * @date 2025-02-22
 */
@Service
public class EmrDetailServiceImpl extends ServiceImpl<EmrDetailMapper, EmrDetail> implements IEmrDetailService {

}