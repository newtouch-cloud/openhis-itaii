package com.openhis.administration.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.Organization;

/**
 * 机构管理Service接口
 *
 * @author system
 * @date 2025-02-21
 */
public interface IOrganizationService extends IService<Organization> {
    Page<Organization> getOrganizationPage(Integer classEnum, Integer activeFlag, Integer pageNo, Integer pageSize);

    /**
     * 通过机构ID查询机构名称
     *
     * @param busNo 机构ID
     * @return 机构名称
     */
    Organization getByBusNo(String busNo);

}