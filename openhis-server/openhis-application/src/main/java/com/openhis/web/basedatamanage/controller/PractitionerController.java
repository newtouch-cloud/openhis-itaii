/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.core.common.core.domain.R;
import com.openhis.web.basedatamanage.appservice.IPractitionerAppService;
import com.openhis.web.doctorstation.dto.UserAndPractitionerDto;

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
    @PostMapping(value = "/save-user-practitioner")
    public R<?> saveUserPractitioner(@RequestBody UserAndPractitionerDto userAndPractitionerDto) {
        return practitionerAppService.saveUserPractitioner(userAndPractitionerDto);
    }

}
