package com.openhis.administration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.Organization;

import java.util.List;

/**
 * 机构管理Service接口
 *
 * @author system
 * @date 2025-02-21
 */
public interface IOrganizationService extends IService<Organization> {

    /**
     * 机构启用
     *
     * @param orgId 机构信息id
     * @return 操作结果
     */
    boolean activeOrg(Long orgId);

    /**
     * 机构停用
     *
     * @param orgId 机构信息id
     * @return 操作结果
     */
    boolean inactiveOrg(Long orgId);

    /**
     * 获取机构下拉列表
     *
     * @param classEnum 机构分类
     * @return 机构下拉列表
     */
    List<Organization> getList(Integer classEnum);
}