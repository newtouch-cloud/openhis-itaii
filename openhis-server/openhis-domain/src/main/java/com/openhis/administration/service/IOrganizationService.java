package com.openhis.administration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.Organization;

/**
 * 机构管理Service接口
 *
 * @author system
 * @date 2025-02-21
 */
public interface IOrganizationService extends IService<Organization> {
    boolean activeChange(Long orgId);
}