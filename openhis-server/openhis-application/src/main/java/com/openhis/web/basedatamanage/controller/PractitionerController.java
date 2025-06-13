/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.controller;

import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.core.common.core.domain.R;
import com.openhis.web.basedatamanage.appservice.IPractitionerAppService;
import com.openhis.web.basedatamanage.dto.UserAndPractitionerDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 参与者 Controller业务层处理
 */
@RestController
@RequestMapping("/base-data-manage/practitioner")
@Slf4j
@AllArgsConstructor
public class PractitionerController {

    private final IPractitionerAppService practitionerAppService;

    /**
     * 新增用户及参与者
     * 
     * @param userAndPractitionerDto 用户及参与者dto
     * @return 结果
     */
    @PostMapping(value = "/user-practitioner")
    public R<?> saveUserPractitioner(@RequestBody UserAndPractitionerDto userAndPractitionerDto) {
        return practitionerAppService.saveUserPractitioner(userAndPractitionerDto);
    }

    /**
     * 查询用户及参与者
     * 
     * @param userAndPractitionerDto 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 用户及参与者
     */
    @GetMapping(value = "/user-practitioner-page")
    public R<?> getUserPractitionerPage(UserAndPractitionerDto userAndPractitionerDto,
        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return R
            .ok(practitionerAppService.getUserPractitionerPage(userAndPractitionerDto, searchKey, pageNo, pageSize));
    }

    /**
     * 查询用户及参与者详情
     * 
     * @param userId 系统用户id
     * @return 用户及参与者详情
     */
    @GetMapping(value = "/user-practitioner-detail")
    public R<?> getUserPractitionerPage(@RequestParam Long userId) {
        UserAndPractitionerDto userAndPractitionerDto = new UserAndPractitionerDto();
        userAndPractitionerDto.setUserId(userId);
        IPage<UserAndPractitionerDto> userPractitionerPage =
            practitionerAppService.getUserPractitionerPage(userAndPractitionerDto, "", 1, 1);
        return R.ok(userPractitionerPage.getRecords().get(0));
    }

    /**
     * 修改用户及参与者
     *
     * @param userAndPractitionerDto 用户及参与者dto
     * @return 结果
     */
    @PutMapping(value = "/user-practitioner")
    public R<?> editUserPractitioner(@RequestBody UserAndPractitionerDto userAndPractitionerDto) {
        return practitionerAppService.editUserPractitioner(userAndPractitionerDto);
    }

    /**
     * 删除用户及参与者 ; admin不允许删除
     * 
     * @param userId 系统用户id
     * @return 结果
     */
    @DeleteMapping(value = "/user-practitioner")
    public R<?> delUserPractitioner(@RequestParam Long userId) {
        return practitionerAppService.delUserPractitioner(userId);
    }

    /**
     * 查询可选择切换科室集合
     *
     * @return 可选择切换科室集合
     */
    @GetMapping(value = "/get-selectable-org-list")
    public R<?> getSelectableOrgList() {
        return R.ok(practitionerAppService.getSelectableOrgList());
    }

    /**
     * 切换科室
     * 
     * @param orgId 科室id
     * @return 结果
     */
    @PutMapping(value = "/switch-org")
    public R<?> switchOrg(@RequestParam Long orgId) {
        return practitionerAppService.switchOrg(orgId);
    }
}
