package com.core.system.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.core.common.core.domain.R;
import com.core.common.core.domain.entity.SysUser;
import com.core.system.domain.SysTenant;

/**
 * 租户信息表Service接口
 * 
 * @author system
 */
public interface ISysTenantService extends IService<SysTenant> {

    /**
     * 查询租户分页列表
     *
     * @param tenantId 租户ID查询
     * @param tenantCode 租户编码模糊查询
     * @param tenantName 租户名称模糊查询
     * @param status 状态
     * @param pageNum 当前页
     * @param pageSize 每页多少条
     * @return 租户分页列表
     */
    R<IPage<SysTenant>> getTenantPage(Integer tenantId, String tenantCode, String tenantName, String status,
        Integer pageNum, Integer pageSize);

    /**
     * 查询租户所属用户分页列表
     *
     * @param tenantId 租户ID查询
     * @param userName 用户名称模糊查询
     * @param nickName 用户昵称模糊查询
     * @param phoneNumber 手机号码模糊查询
     * @param pageNum 当前页
     * @param pageSize 每页多少条
     * @return 租户所属用户分页列表
     */
    R<IPage<SysUser>> getTenantUserPage(Integer tenantId, String userName, String nickName, String phoneNumber,
        Integer pageNum, Integer pageSize);

    /**
     * 删除租户
     *
     * @param tenantIdList 租户ID列表
     * @return 结果
     */
    R<?> delTenant(List<Integer> tenantIdList);

    /**
     * 启用租户
     *
     * @param tenantIdList 租户ID列表
     */
    void enableTenant(List<Integer> tenantIdList);

    /**
     * 停用租户
     *
     * @param tenantIdList 租户ID列表
     */
    void disableTenant(List<Integer> tenantIdList);

    /**
     * 查询租户未绑定的用户列表
     *
     * @param tenantId 租户ID
     * @param userName 用户名称模糊查询
     * @param nickName 用户昵称模糊查询
     * @param phoneNumber 手机号码模糊查询
     * @param pageNum 当前页
     * @param pageSize 每页多少条
     * @return 结果
     */
    R<IPage<SysUser>> getUnbindTenantUserList(Integer tenantId, String userName, String nickName, String phoneNumber,
        Integer pageNum, Integer pageSize);

    /**
     * 绑定租户用户
     *
     * @param tenantId 租户ID
     * @param userIdList 用户ID列表
     * @return 结果
     */
    R<?> bindTenantUser(Integer tenantId, List<Long> userIdList);

    /**
     * 解绑租户用户
     *
     * @param tenantId 租户ID
     * @param userIdList 用户ID列表
     * @return 结果
     */
    R<?> unbindTenantUser(Integer tenantId, List<Long> userIdList);

    /**
     * 查询用户绑定的租户列表
     *
     * @param username 用户账号
     * @return 用户绑定的租户列表
     */
    R<List<SysTenant>> getUserBindTenantList(String username);

    /**
     * 初始化租户绑定
     * 
     * @param userId 用户ID
     */
    void initTenantBind(Long userId);
}
