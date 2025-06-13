/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.core.common.annotation.Anonymous;
import com.openhis.service.IYbElepService;
import com.openhis.vo.BaseParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openhis.config.InterfaceConfig;
import com.openhis.domain.*;
import com.openhis.vo.Result;

/**
 *
 * @author yuxj
 * @date 2025-04-17
 */
@RestController
@RequestMapping("/ybElep")
public class YbElepController {

    /** 医保服务 */
    @Autowired
    private IYbElepService ybElepService;

    /**
     * 电子处方上传预核验
     *
     * @param baseParam 处方信息
     * @return
     */
    @PostMapping(value = "/preCheckPrescription")
    @Anonymous
    public Result<?> preCheckPrescription(@RequestBody BaseParam baseParam) {
        return ybElepService.preCheckPrescription(baseParam);
    }


    /**
     * 电子处方医保电子签名
     *
     * @param baseParam 电子签名信息
     * @return
     */
    @PostMapping(value = "/signature")
    @Anonymous
    public Result<?> signature(@RequestBody BaseParam baseParam) {
        return ybElepService.signature(baseParam);
    }


    /**
     * 电子处方上传
     *
     * @param baseParam 上传信息
     * @return
     */
    @PostMapping(value = "/upload")
    @Anonymous
    public Result<?> upload(@RequestBody BaseParam baseParam) {
        return ybElepService.upload(baseParam);
    }

    /**
     * 电子处方撤销
     *
     * @param baseParam 撤销信息
     * @return
     */
    @PostMapping(value = "/revoke")
    @Anonymous
    public Result<?> revoke(@RequestBody BaseParam baseParam) {
        return ybElepService.revoke(baseParam);
    }

    /**
     * 电子处方信息查询
     *
     * @param baseParam 查询信息
     * @return
     */
    @PostMapping(value = "/querPrescription")
    @Anonymous
    public Result<?> querPrescription(@RequestBody BaseParam baseParam) {
        return ybElepService.querPrescription(baseParam);
    }

    /**
     * 电子处方取药结果查询
     *
     * @param baseParam 撤销信息
     * @return
     */
    @PostMapping(value = "/medresult")
    @Anonymous
    public Result<?> medresult(@RequestBody BaseParam baseParam) {
        return ybElepService.medresult(baseParam);
    }

}
