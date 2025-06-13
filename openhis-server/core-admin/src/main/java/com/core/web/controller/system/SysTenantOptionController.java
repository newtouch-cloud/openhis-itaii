package com.core.web.controller.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.core.common.core.controller.BaseController;
import com.core.common.core.domain.R;
import com.core.common.enums.TenantOptionDict;
import com.core.system.domain.SysTenantOption;
import com.core.system.domain.dto.SysTenantOptionDto;
import com.core.system.service.ISysTenantOptionService;

/**
 * 租户配置项信息controller
 * 
 * @author system
 */
@RestController
@RequestMapping("/system/tenant-option")
public class SysTenantOptionController extends BaseController {
    @Autowired
    private ISysTenantOptionService sysTenantOptionService;

    /**
     * 查询租户配置项分页列表
     *
     * @param tenantId 租户ID查询
     * @param pageNum 当前页
     * @param pageSize 每页多少条
     * @return 租户配置项分页列表
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:operate')")
    @GetMapping("/page")
    public R<IPage<SysTenantOption>> getTenantOptionPage(@RequestParam Integer tenantId,
        @RequestParam(required = false) String optionCode, @RequestParam(defaultValue = "1") Integer pageNum,
        @RequestParam(defaultValue = "10") Integer pageSize) {
        return sysTenantOptionService.getTenantOptionPage(tenantId, optionCode, pageNum, pageSize);
    }

    /**
     * 查询租户配置项详情
     *
     * @param id ID
     * @return 租户配置项详情
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:operate')")
    @GetMapping("/{id}")
    public R<?> getTenantOptionDetail(@PathVariable Long id) {
        return R.ok(sysTenantOptionService.getById(id));
    }

    /**
     * 新增租户配置项
     * 
     * @param sysTenantOption 租户配置项实体
     * @return 结果
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:operate')")
    @PostMapping
    public R<?> addTenantOption(@RequestBody SysTenantOption sysTenantOption) {
        return sysTenantOptionService.addTenantOption(sysTenantOption);
    }

    /**
     * 修改租户配置项
     *
     * @param sysTenantOption 租户配置项实体
     * @return 结果
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:operate')")
    @PutMapping
    public R<?> editTenantOption(@RequestBody SysTenantOption sysTenantOption) {
        sysTenantOptionService.updateById(sysTenantOption);
        return R.ok("修改成功");
    }

    /**
     * 删除租户配置项
     *
     * @param tenantIdList 租户配置项ID列表
     * @return 结果
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:operate')")
    @DeleteMapping
    public R<?> delTenantOption(@RequestBody List<Integer> tenantIdList) {
        sysTenantOptionService.removeBatchByIds(tenantIdList);
        return R.ok("删除成功");
    }

    /**
     * 查询租户配置项下拉列表
     * 
     * @return 租户配置项下拉列表
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:operate')")
    @GetMapping("/dropdown")
    public R<List<SysTenantOptionDto>> getDropdown() {
        return R.ok(sysTenantOptionService.getTenantOptionDropdown());
    }

    // TODO: 出于安全性考虑，下面两项配置接口是否开放再议

    /**
     * 查询全部租户配置项
     *
     * @param tenantId 租户ID
     * @return 全部租户配置项
     */
    // @GetMapping("/all")
    public R<List<SysTenantOptionDto>> getAllTenantOption(@RequestParam Integer tenantId) {
        return R.ok(sysTenantOptionService.getAllTenantOption(tenantId));
    }

    /**
     * 查询指定的租户配置项内容
     *
     * @param tenantId 租户ID
     * @param optionCode 配置项编码
     * @return 指定的租户配置项内容（不存在返回NULL）
     */
    // @GetMapping("/content")
    public R<SysTenantOptionDto> getTenantOptionContent(@RequestParam Integer tenantId,
        @RequestParam String optionCode) {
        TenantOptionDict tenantOptionDict = TenantOptionDict.getByCode(optionCode);
        if (tenantOptionDict == null) {
            return R.fail("未知配置项");
        }
        SysTenantOptionDto sysTenantOptionDto =
            sysTenantOptionService.getTenantOptionContent(tenantId, tenantOptionDict);
        if (sysTenantOptionDto == null) {
            return R.fail("此项未配置");
        }
        return R.ok(sysTenantOptionDto);
    }
}
