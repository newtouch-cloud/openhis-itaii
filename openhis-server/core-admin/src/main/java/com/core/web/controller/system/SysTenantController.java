package com.core.web.controller.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.core.common.annotation.Anonymous;
import com.core.common.core.controller.BaseController;
import com.core.common.core.domain.R;
import com.core.common.core.domain.entity.SysUser;
import com.core.system.domain.SysTenant;
import com.core.system.service.ISysTenantService;

/**
 * 租户信息controller
 * 
 * @author system
 */
@RestController
@RequestMapping("/system/tenant")
public class SysTenantController extends BaseController {
    @Autowired
    private ISysTenantService sysTenantService;

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
    @PreAuthorize("@ss.hasPermi('system:tenant:operate')")
    @GetMapping("/page")
    public R<IPage<SysTenant>> getTenantPage(@RequestParam(required = false) Integer tenantId,
        @RequestParam(required = false) String tenantCode, @RequestParam(required = false) String tenantName,
        @RequestParam(required = false) String status, @RequestParam(defaultValue = "1") Integer pageNum,
        @RequestParam(defaultValue = "10") Integer pageSize) {
        return sysTenantService.getTenantPage(tenantId, tenantCode, tenantName, status, pageNum, pageSize);
    }

    /**
     * 查询租户详情
     *
     * @param tenantId 租户ID
     * @return 租户分页列表
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:operate')")
    @GetMapping("/{tenantId}")
    public R<SysTenant> getTenantDetail(@PathVariable Integer tenantId) {
        return R.ok(sysTenantService.getById(tenantId));
    }

    /**
     * 查询租户所属用户分页列表
     *
     * @param tenantId 租户ID查询
     * @param userName 用户昵称模糊查询
     * @param nickName 用户昵称模糊查询
     * @param phoneNumber 手机号码模糊查询
     * @param pageNum 当前页
     * @param pageSize 每页多少条
     * @return 租户所属用户分页列表
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:operate')")
    @GetMapping("/user/page")
    public R<IPage<SysUser>> getTenantUserPage(@RequestParam(required = false) Integer tenantId,
        @RequestParam(required = false) String userName, @RequestParam(required = false) String nickName,
        @RequestParam(required = false) String phoneNumber, @RequestParam(defaultValue = "1") Integer pageNum,
        @RequestParam(defaultValue = "10") Integer pageSize) {
        return sysTenantService.getTenantUserPage(tenantId, userName, nickName, phoneNumber, pageNum, pageSize);
    }

    /**
     * 新增租户
     *
     * @param sysTenant 租户实体
     * @return 结果
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:operate')")
    @PostMapping
    public R<?> addTenant(@RequestBody SysTenant sysTenant) {
        sysTenantService.save(sysTenant);
        return R.ok("新增成功");
    }

    /**
     * 修改租户
     *
     * @param sysTenant 租户实体
     * @return 结果
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:operate')")
    @PutMapping
    public R<?> editTenant(@RequestBody SysTenant sysTenant) {
        sysTenantService.updateById(sysTenant);
        return R.ok("修改成功");
    }

    /**
     * 删除租户
     *
     * @param tenantIdList 租户ID列表
     * @return 结果
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:operate')")
    @DeleteMapping
    public R<?> delTenant(@RequestBody List<Integer> tenantIdList) {
        return sysTenantService.delTenant(tenantIdList);
    }

    /**
     * 启用租户
     *
     * @param tenantIdList 租户ID列表
     * @return 结果
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:operate')")
    @PutMapping("/enable")
    public R<?> enableTenant(@RequestBody List<Integer> tenantIdList) {
        sysTenantService.enableTenant(tenantIdList);
        return R.ok("启用成功");
    }

    /**
     * 停用租户
     *
     * @param tenantIdList 租户ID列表
     * @return 结果
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:operate')")
    @PutMapping("/disable")
    public R<?> disableTenant(@RequestBody List<Integer> tenantIdList) {
        sysTenantService.disableTenant(tenantIdList);
        return R.ok("停用成功");
    }

    /**
     * 查询租户未绑定的用户列表
     *
     * @param tenantId 租户ID
     * @param pageNum 当前页
     * @param pageSize 每页多少条
     * @return 结果
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:operate')")
    @GetMapping("/{tenantId}/unbind-users")
    public R<IPage<SysUser>> getUnbindTenantUserList(@PathVariable Integer tenantId,
        @RequestParam(required = false) String userName, @RequestParam(required = false) String nickName,
        @RequestParam(required = false) String phoneNumber, @RequestParam(defaultValue = "1") Integer pageNum,
        @RequestParam(defaultValue = "10") Integer pageSize) {
        return sysTenantService.getUnbindTenantUserList(tenantId, userName, nickName, phoneNumber, pageNum, pageSize);
    }

    /**
     * 绑定租户用户
     *
     * @param tenantId 租户ID
     * @param userIdList 用户ID列表
     * @return 结果
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:operate')")
    @PostMapping("/{tenantId}/bind-users")
    public R<?> bindTenantUser(@PathVariable Integer tenantId, @RequestBody List<Long> userIdList) {
        return sysTenantService.bindTenantUser(tenantId, userIdList);
    }

    /**
     * 解绑租户用户
     *
     * @param tenantId 租户ID
     * @param userIdList 用户ID列表
     * @return 结果
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:operate')")
    @PostMapping("/{tenantId}/unbind-users")
    public R<?> unbindTenantUser(@PathVariable Integer tenantId, @RequestBody List<Long> userIdList) {
        return sysTenantService.unbindTenantUser(tenantId, userIdList);
    }

    /**
     * 查询用户绑定的租户列表
     *
     * @param username 用户账号
     * @return 用户绑定的租户列表
     */
    @Anonymous
    @GetMapping("/user-bind/{username}")
    public R<List<SysTenant>> getUserBindTenantList(@PathVariable String username) {
        return sysTenantService.getUserBindTenantList(username);
    }
}
