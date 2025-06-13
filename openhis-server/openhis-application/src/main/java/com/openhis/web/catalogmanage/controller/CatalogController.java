/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.catalogmanage.controller;

import com.core.common.annotation.Anonymous;
import com.core.common.core.domain.R;
import com.openhis.web.catalogmanage.appservice.ICatalogService;
import com.openhis.web.patientmanage.appservice.IPatientInformationService;
import com.openhis.web.patientmanage.dto.PatientInfoSearchParam;
import com.openhis.web.ybmanage.config.YbServiceConfig;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;

/**
 * 医保目录
 *
 * @author SunJQ
 * @date 2025-04-09
 */
@RestController
@RequestMapping("/catalog")
@Slf4j
@AllArgsConstructor
public class CatalogController {

    @Autowired
    ICatalogService iCatalogService;
    @Autowired
    YbServiceConfig ybServiceConfig;
    /**
     * 分页查询医保目录信息,可选条件
     *
     * @param catalogType 查询参数
     * @param searchKey 查询条件-模糊查询
     * @param pageNo 页码（默认为1）
     * @param pageSize 每页大小（默认为10）
     */
    @Anonymous
    @GetMapping("/page")
    public R<?> getPage(Integer catalogType,
                        @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
                        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {

        System.out.println(ybServiceConfig.getUrl());
        return R.ok(iCatalogService.getPage(catalogType, searchKey, pageNo, pageSize,request));
    }
}
