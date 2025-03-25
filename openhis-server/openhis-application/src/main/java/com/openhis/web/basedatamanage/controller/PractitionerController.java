/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openhis.web.basedatamanage.appservice.IPractitionerAppService;

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

}
