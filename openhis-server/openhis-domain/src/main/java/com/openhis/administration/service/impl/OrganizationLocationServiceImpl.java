package com.openhis.administration.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.OrganizationLocation;
import com.openhis.administration.mapper.OrganizationLocationMapper;
import com.openhis.administration.service.IOrganizationLocationService;

/**
 * 机构位置关系管理Service业务层处理
 *
 * @author system
 * @date 2025-02-25
 */
@Service
public class OrganizationLocationServiceImpl extends ServiceImpl<OrganizationLocationMapper, OrganizationLocation>
    implements IOrganizationLocationService {

}