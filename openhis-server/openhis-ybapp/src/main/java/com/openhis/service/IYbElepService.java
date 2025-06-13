/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.openhis.domain.*;
import com.openhis.vo.BaseParam;
import com.openhis.vo.Result;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author yuxj
 * @date 2025-04-17
 */
public interface IYbElepService {

    /**
     * 电子处方上传预核验
     *
     * @param baseParam 处方信息
     * @return
     */
    Result<?> preCheckPrescription(BaseParam baseParam);

    /**
     * 电子处方医保电子签名
     *
     * @param baseParam 电子签名信息
     * @return
     */
    Result<?> signature(BaseParam baseParam);

    /**
     * 电子处方上传
     *
     * @param baseParam 上传信息
     * @return
     */
    Result<?> upload(BaseParam baseParam);

    /**
     * 电子处方撤销
     *
     * @param baseParam 撤销信息
     * @return
     */
    Result<?> revoke(BaseParam baseParam);

    /**
     * 电子处方信息查询
     *
     * @param baseParam 查询信息
     * @return
     */
    Result<?> querPrescription(BaseParam baseParam);

    /**
     * 电子处方取药结果查询
     *
     * @param baseParam 撤销信息
     * @return
     */
    Result<?> medresult(BaseParam baseParam);
}
