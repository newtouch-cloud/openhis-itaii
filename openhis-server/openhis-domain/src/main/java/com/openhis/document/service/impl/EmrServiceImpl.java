package com.openhis.document.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.document.domain.Emr;
import com.openhis.document.mapper.EmrMapper;
import com.openhis.document.service.IEmrService;

/**
 * 病历信息Service业务层处理
 *
 * @author system
 * @date 2025-02-21
 */
@Service
public class EmrServiceImpl extends ServiceImpl<EmrMapper, Emr> implements IEmrService {

}