package com.openhis.document.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.document.domain.EmrTemplate;
import com.openhis.document.mapper.EmrTemplateMapper;
import com.openhis.document.service.IEmrTemplateService;

/**
 * 病历模板Service业务层处理
 *
 * @author system
 * @date 2025-02-21
 */
@Service
public class EmrTemplateServiceImpl extends ServiceImpl<EmrTemplateMapper, EmrTemplate> implements IEmrTemplateService {

}