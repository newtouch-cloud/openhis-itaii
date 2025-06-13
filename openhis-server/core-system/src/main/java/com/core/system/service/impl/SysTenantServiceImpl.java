package com.core.system.service.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.core.common.constant.CacheConstants;
import com.core.common.core.domain.R;
import com.core.common.core.domain.entity.SysUser;
import com.core.common.core.domain.model.LoginUser;
import com.core.common.core.redis.RedisCache;
import com.core.common.enums.DeleteFlag;
import com.core.common.enums.TenantStatus;
import com.core.common.utils.SecurityUtils;
import com.core.common.utils.StringUtils;
import com.core.system.domain.SysTenant;
import com.core.system.domain.SysUserTenant;
import com.core.system.mapper.SysTenantMapper;
import com.core.system.mapper.SysUserMapper;
import com.core.system.mapper.SysUserTenantMapper;
import com.core.system.service.ISysTenantService;

/**
 * 租户信息表Service业务层处理
 * 
 * @author system
 */
@Service
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {
    @Autowired
    private SysUserTenantMapper sysUserTenantMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private RedisCache redisCache;

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
    @Override
    public R<IPage<SysTenant>> getTenantPage(Integer tenantId, String tenantCode, String tenantName, String status,
        Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<SysTenant> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 指定租户ID
        if (tenantId != null) {
            lambdaQueryWrapper.eq(SysTenant::getId, tenantId);
        }
        // 指定租户编码
        if (StringUtils.isNotEmpty(tenantCode)) {
            lambdaQueryWrapper.like(SysTenant::getTenantCode, tenantCode);
        }
        // 指定租户名称
        if (StringUtils.isNotEmpty(tenantName)) {
            lambdaQueryWrapper.like(SysTenant::getTenantName, tenantName);
        }
        // 指定状态
        if (StringUtils.isNotEmpty(status)) {
            lambdaQueryWrapper.eq(SysTenant::getStatus, status);
        }
        // 未删除
        lambdaQueryWrapper.eq(SysTenant::getDeleteFlag, DeleteFlag.NOT_DELETED.getCode());
        lambdaQueryWrapper.orderByDesc(SysTenant::getId);
        return R.ok(baseMapper.selectPage(new Page<>(pageNum, pageSize), lambdaQueryWrapper));
    }

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
    @Override
    public R<IPage<SysUser>> getTenantUserPage(Integer tenantId, String userName, String nickName, String phoneNumber,
        Integer pageNum, Integer pageSize) {
        // 租户ID不可为空
        if (tenantId == null) {
            return R.fail("请指定租户");
        }
        // 查询租户绑定的所有用户ID
        List<SysUserTenant> userTenantList = sysUserTenantMapper
            .selectList(new LambdaUpdateWrapper<SysUserTenant>().eq(SysUserTenant::getTenantId, tenantId));
        IPage<SysUser> page = new Page<>(pageNum, pageSize);
        if (userTenantList.isEmpty()) {
            // 无人绑定返回空分页列表
            page.setTotal(0L);
            page.setRecords(Collections.emptyList());
            return R.ok(page);
        }
        List<Long> userIdList = userTenantList.stream().map(SysUserTenant::getUserId).collect(Collectors.toList());
        // 查询租户绑定的用户分页列表
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(userName)) {
            lambdaQueryWrapper.like(SysUser::getUserName, userName);
        }
        if (StringUtils.isNotEmpty(nickName)) {
            lambdaQueryWrapper.like(SysUser::getNickName, nickName);
        }
        if (StringUtils.isNotEmpty(phoneNumber)) {
            lambdaQueryWrapper.like(SysUser::getPhonenumber, phoneNumber);
        }
        lambdaQueryWrapper.in(SysUser::getUserId, userIdList);
        lambdaQueryWrapper.orderByDesc(SysUser::getUserId);
        return R.ok(sysUserMapper.selectPage(page, lambdaQueryWrapper));
    }

    /**
     * 删除租户
     *
     * @param tenantIdList 租户ID列表
     */
    @Override
    public R<?> delTenant(List<Integer> tenantIdList) {
        if (!tenantIdList.isEmpty()) {
            List<SysUserTenant> sysUserTenantList = sysUserTenantMapper
                .selectList(new LambdaQueryWrapper<SysUserTenant>().in(SysUserTenant::getTenantId, tenantIdList));
            if (!sysUserTenantList.isEmpty()) {
                return R.fail("该租户还存在绑定的用户，请确认");
            }
            baseMapper.update(new SysTenant(), new LambdaUpdateWrapper<SysTenant>()
                .set(SysTenant::getDeleteFlag, DeleteFlag.DELETED.getCode()).in(SysTenant::getId, tenantIdList));
        }
        return R.ok();
    }

    /**
     * 启用租户
     *
     * @param tenantIdList 租户ID列表
     */
    @Override
    public void enableTenant(List<Integer> tenantIdList) {
        if (!tenantIdList.isEmpty()) {
            baseMapper.update(new SysTenant(), new LambdaUpdateWrapper<SysTenant>()
                .set(SysTenant::getStatus, TenantStatus.ENABLE.getCode()).in(SysTenant::getId, tenantIdList));
        }
    }

    /**
     * 停用租户
     *
     * @param tenantIdList 租户ID列表
     */
    @Override
    public void disableTenant(List<Integer> tenantIdList) {
        if (!tenantIdList.isEmpty()) {
            baseMapper.update(new SysTenant(), new LambdaUpdateWrapper<SysTenant>()
                .set(SysTenant::getStatus, TenantStatus.DISABLE.getCode()).in(SysTenant::getId, tenantIdList));
            // 强退租户所属的所有用户
            forceLogoutByTenantId(tenantIdList);
        }
    }

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
    @Override
    public R<IPage<SysUser>> getUnbindTenantUserList(Integer tenantId, String userName, String nickName,
        String phoneNumber, Integer pageNum, Integer pageSize) {
        // 租户ID不可为空
        if (tenantId == null) {
            return R.fail("请指定租户");
        }
        // 查询已绑定的用户ID
        List<SysUserTenant> sysUserTenantList = sysUserTenantMapper
            .selectList(new LambdaUpdateWrapper<SysUserTenant>().eq(SysUserTenant::getTenantId, tenantId));
        List<Long> userIdList = sysUserTenantList.stream().map(SysUserTenant::getUserId).collect(Collectors.toList());
        // 查询未绑定的用户列表
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!userIdList.isEmpty()) {
            lambdaQueryWrapper.notIn(SysUser::getUserId, userIdList);
        }
        if (StringUtils.isNotEmpty(phoneNumber)) {
            lambdaQueryWrapper.like(SysUser::getPhonenumber, phoneNumber);
        }
        if (StringUtils.isNotEmpty(nickName)) {
            lambdaQueryWrapper.like(SysUser::getNickName, nickName);
        }
        if (StringUtils.isNotEmpty(userName)) {
            lambdaQueryWrapper.like(SysUser::getUserName, userName);
        }
        lambdaQueryWrapper.eq(SysUser::getDeleteFlag, DeleteFlag.NOT_DELETED.getCode());
        lambdaQueryWrapper.orderByDesc(SysUser::getUserId);
        return R.ok(sysUserMapper.selectPage(new Page<>(pageNum, pageSize), lambdaQueryWrapper));
    }

    /**
     * 绑定租户用户
     *
     * @param tenantId 租户ID
     * @param userIdList 用户ID列表
     * @return 结果
     */
    @Override
    public R<?> bindTenantUser(Integer tenantId, List<Long> userIdList) {
        // 租户ID不可为空
        if (tenantId == null) {
            return R.fail("请指定租户");
        }
        // 用户ID列表不可为空
        if (userIdList.isEmpty()) {
            return R.fail("请选择绑定的用户");
        }
        // 创建用户与租户间的绑定
        SysUserTenant sysUserTenant;
        for (Long userId : userIdList) {
            sysUserTenant = new SysUserTenant();
            sysUserTenant.setTenantId(tenantId).setUserId(userId);
            sysUserTenantMapper.insert(sysUserTenant);
        }
        return R.ok();
    }

    /**
     * 解绑租户用户
     *
     * @param tenantId 租户ID
     * @param userIdList 用户ID列表
     * @return 结果
     */
    @Override
    public R<?> unbindTenantUser(Integer tenantId, List<Long> userIdList) {
        // 租户ID不可为空
        if (tenantId == null) {
            return R.fail("请指定租户");
        }
        // 用户ID列表不可为空
        if (userIdList.isEmpty()) {
            return R.fail("请选择解绑的用户");
        }
        // 删除用户与租户间的绑定
        sysUserTenantMapper.delete(new LambdaQueryWrapper<SysUserTenant>().eq(SysUserTenant::getTenantId, tenantId)
            .in(SysUserTenant::getUserId, userIdList));
        // 强退用户
        forceLogoutByUserId(tenantId, userIdList);
        return R.ok();
    }

    /**
     * 查询用户绑定的租户列表
     *
     * @param username 用户账号
     * @return 用户绑定的租户列表
     */
    @Override
    public R<List<SysTenant>> getUserBindTenantList(String username) {
        SysUser sysUser = sysUserMapper.selectUserByUserName(username);
        if (sysUser == null) {
            return R.ok(Collections.emptyList());
        }
        // 查询已绑定的租户列表
        List<SysUserTenant> sysUserTenantList = sysUserTenantMapper
            .selectList(new LambdaUpdateWrapper<SysUserTenant>().eq(SysUserTenant::getUserId, sysUser.getUserId()));
        List<Integer> tenantIdList =
            sysUserTenantList.stream().map(SysUserTenant::getTenantId).collect(Collectors.toList());
        if (tenantIdList.isEmpty()) {
            return R.ok(Collections.emptyList());
        }
        List<SysTenant> userBindTenantList =
            baseMapper.selectList(new LambdaUpdateWrapper<SysTenant>().in(SysTenant::getId, tenantIdList));
        // 优先将上次选择的租户排在前面
        try {
            Integer cacheTenant = redisCache.getCacheObject(CacheConstants.LOGIN_SELECTED_TENANT + username);
            if (cacheTenant != null) {
                userBindTenantList
                    .sort(Comparator.comparing(e -> !cacheTenant.equals(e.getId()), Comparator.naturalOrder()));
            }
        } catch (Exception ignored) {
        }
        return R.ok(userBindTenantList);
    }

    /**
     * 初始化租户绑定
     *
     * @param userId 用户ID
     */
    @Override
    public void initTenantBind(Long userId) {
        if (userId == null) {
            return;
        }
        // 查询当前登录者租户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            return;
        }
        if (loginUser.getTenantId() == null) {
            return;
        }
        // 查询绑定是否已存在
        List<SysUserTenant> existBind = sysUserTenantMapper.selectList(new LambdaQueryWrapper<SysUserTenant>()
            .eq(SysUserTenant::getUserId, userId).eq(SysUserTenant::getTenantId, loginUser.getTenantId()));
        if (!existBind.isEmpty()) {
            return;
        }
        // 新增绑定关系
        sysUserTenantMapper.insert(new SysUserTenant().setUserId(userId).setTenantId(loginUser.getTenantId()));
    }

    /**
     * 强退租户所属的所有用户
     * 
     * @param tenantIdList 租户ID列表
     */
    private void forceLogoutByTenantId(List<Integer> tenantIdList) {
        if (tenantIdList.isEmpty()) {
            return;
        }
        // 查询租户所属的所有用户信息
        List<SysUserTenant> sysUserTenantList = sysUserTenantMapper
            .selectList(new LambdaQueryWrapper<SysUserTenant>().in(SysUserTenant::getTenantId, tenantIdList));
        if (sysUserTenantList.isEmpty()) {
            return;
        }
        List<String> tenantUserStringList =
            sysUserTenantList.stream().map(e -> e.getTenantId() + "_" + e.getUserId()).collect(Collectors.toList());
        // 遍历所有当前登录用户，强退该租户的用户
        Collection<String> keys = redisCache.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
        for (String key : keys) {
            LoginUser user = redisCache.getCacheObject(key);
            if (user != null) {
                if (user.getTenantId() != null && user.getUserId() != null) {
                    if (tenantUserStringList.contains(user.getTenantId() + "_" + user.getUserId())) {
                        redisCache.deleteObject(key);
                    }
                }
            }
        }
    }

    /**
     * 强退用户
     *
     * @param tenantId 租户ID
     * @param userIdList 用户ID列表
     */
    private void forceLogoutByUserId(Integer tenantId, List<Long> userIdList) {
        if (tenantId == null || userIdList.isEmpty()) {
            return;
        }
        // 遍历所有当前登录用户，强退该用户
        Collection<String> keys = redisCache.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
        for (String key : keys) {
            LoginUser user = redisCache.getCacheObject(key);
            if (user != null) {
                if (tenantId.equals(user.getTenantId()) && userIdList.contains(user.getUserId())) {
                    redisCache.deleteObject(key);
                }
            }
        }
    }

}
