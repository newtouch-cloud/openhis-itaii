/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.ybmanage.service;

import com.openhis.ybelep.domain.*;

/**
 *
 * @author yuxj
 * @date 2025-04-17
 */
public interface IYbEleHttpService {

    /**
     * 采用HTTP协议中的 POST 方法，发送医保电子处方注册预核验请求，并处理响应结果
     *
     * @param pcp 医保电子处方注册预核验入参实体
     * @return 医保电子处方注册预核验响应出参实体
     */
    ElepVeriPrescriptionOutput preCheck(PreCheckPrescription pcp);

    /**
     * 采用HTTP协议中的 POST 方法，发送电子处方医保电子签名请求，并处理响应结果
     *
     * @param eleSign 电子处方医保电子签名入参实体
     * @return 电子处方医保电子签名响应出参实体
     */
    ElepSignatureOutput eleSign(ElepSignatureInput eleSign);

    /**
     * 采用HTTP协议中的 POST 方法，发送电子处方上传请求，并处理响应结果
     *
     * @param eleUploadInput 电子处方上传入参实体
     * @return 电子处方上传响应出参实体
     */
    ElepUploadOutput uploadElePre(ElepUploadInput eleUploadInput);

    /**
     * 采用HTTP协议中的 POST 方法，发送电子处方撤销请求，并处理响应结果
     *
     * @param eleRevokeInput 电子处方撤销入参实体
     * @return 电子处方撤销响应出参实体
     */
    ElepRevokeOutput revokePre(ElepRevokeInput eleRevokeInput);

    /**
     * 采用HTTP协议中的 POST 方法，电子处方信息查询请求，并处理响应结果
     *
     * @param eleQueryPreInput 电子处方信息查询入参实体
     * @return 电子处方信息查询出参实体
     */
    QueryPrescription queryPre(ElepQuerPrescriptionInput eleQueryPreInput);

    /**
     * 采用HTTP协议中的 POST 方法，电子处方取药结果查询请求，并处理响应结果
     *
     * @param eleMedInput 电子处方取药结果查询入参实体
     * @return 电子处方取药结果查询出参实体
     */
    MedicationResultInquiry queryMedPre(ElepMedresultInput eleMedInput);

}
