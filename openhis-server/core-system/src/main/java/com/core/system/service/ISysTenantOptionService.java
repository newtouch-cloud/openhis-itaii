package com.core.system.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.core.common.core.domain.R;
import com.core.common.enums.TenantOptionDict;
import com.core.system.domain.SysTenantOption;
import com.core.system.domain.dto.SysTenantOptionDto;

/**
 * 租户配置项表Service接口
 * 
 * @author system
 */
public interface ISysTenantOptionService extends IService<SysTenantOption> {

    /**
     * 查询租户配置项分页列表
     *
     * @param tenantId 租户ID查询
     * @param optionCode 配置项编码查询
     * @param pageNum 当前页
     * @param pageSize 每页多少条
     * @return 租户配置项分页列表
     */
    R<IPage<SysTenantOption>> getTenantOptionPage(Integer tenantId, String optionCode, Integer pageNum,
        Integer pageSize);

    /**
     * 新增租户配置项
     *
     * @param sysTenantOption 租户配置项实体
     * @return 结果
     */
    R<?> addTenantOption(SysTenantOption sysTenantOption);

    /**
     * 查询租户配置项下拉列表
     *
     * @return 租户配置项下拉列表
     */
    List<SysTenantOptionDto> getTenantOptionDropdown();

    /**
     * 查询全部租户配置项
     * 
     * @param tenantId 租户ID
     * @return 全部租户配置项
     */
    List<SysTenantOptionDto> getAllTenantOption(Integer tenantId);

    /**
     * 查询指定的租户配置项内容
     *
     * @param tenantId 租户ID
     * @param tenantOptionDict 配置项字典枚举
     * @return 指定的租户配置项内容（不存在返回NULL）
     */
    SysTenantOptionDto getTenantOptionContent(Integer tenantId, TenantOptionDict tenantOptionDict);
}
