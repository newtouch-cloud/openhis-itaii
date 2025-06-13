/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.ybmanage.service.impl;

import com.core.common.exception.ServiceException;
import com.openhis.yb.dto.BaseParam;
import com.openhis.yb.util.YbParamBuilderUtil;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openhis.web.ybmanage.config.YbServiceConfig;
import com.openhis.web.ybmanage.dto.Result;
import com.openhis.web.ybmanage.service.IYbEleHttpService;
import com.openhis.ybelep.domain.*;

import java.io.IOException;

/**
 * http请求接口
 *
 * @author yuxj
 * @date 2025-04-17
 */
@Service
public class YbEleHttpServiceImpl implements IYbEleHttpService {

    @Autowired
    YbServiceConfig ybServiceConfig;

    @Autowired
    YbParamBuilderUtil ybParamBuilderUtil;
    /**
     * 采用HTTP协议中的 POST 方法，发送医保电子处方注册预核验请求，并处理响应结果
     *
     * @param pcp 医保电子处方注册预核验入参实体
     * @return 医保电子处方注册预核验响应出参实体
     */
    @Override
    public ElepVeriPrescriptionOutput preCheck(PreCheckPrescription pcp) {
        ElepVeriPrescriptionOutput pcpResult = new ElepVeriPrescriptionOutput();
        // 发送请求
        String s = httpPost(ybServiceConfig.getEleUrl() + "/preCheckPrescription", pcp);
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        Result result = null;
        try {
            result = mapper.readValue(s, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result != null && result.isSuccess()) {
            pcpResult = JSON.parseObject(JSON.toJSONString(result.getResult()), ElepVeriPrescriptionOutput.class);;
            pcpResult.setPrescriptionNo(pcp.getHospRxno());
        }

        return pcpResult;
    }

    /**
     * 采用HTTP协议中的 POST 方法，发送电子处方医保电子签名请求，并处理响应结果
     *
     * @param eleSign 电子处方医保电子签名入参实体
     * @return 电子处方医保电子签名响应出参实体
     */
    public ElepSignatureOutput eleSign(ElepSignatureInput eleSign) {

        ElepSignatureOutput esResult = new ElepSignatureOutput();
        // 发送请求
        String s = httpPost(ybServiceConfig.getEleUrl() + "/signature", eleSign);
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        Result result = null;
        try {
            result = mapper.readValue(s, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result != null && result.isSuccess()) {
            esResult = JSON.parseObject(JSON.toJSONString(result.getResult()), ElepSignatureOutput.class);
        }

        return esResult;
    }

    /**
     * 采用HTTP协议中的 POST 方法，发送电子处方上传请求，并处理响应结果
     *
     * @param eleUploadInput 电子处方上传入参实体
     * @return 电子处方上传响应出参实体
     */
    @Override
    public ElepUploadOutput uploadElePre(ElepUploadInput eleUploadInput) {

        ElepUploadOutput euResult = new ElepUploadOutput();
        // 发送请求
        String s = httpPost(ybServiceConfig.getEleUrl() + "/upload", eleUploadInput);
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        Result result = null;
        try {
            result = mapper.readValue(s, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result != null && result.isSuccess()) {
            euResult = JSON.parseObject(JSON.toJSONString(result.getResult()), ElepUploadOutput.class);
        }

        return euResult;
    }

    /**
     * 采用HTTP协议中的 POST 方法，发送电子处方撤销请求，并处理响应结果
     *
     * @param eleRevokeInput 电子处方撤销入参实体
     * @return 电子处方撤销响应出参实体
     */
    @Override
    public ElepRevokeOutput revokePre(ElepRevokeInput eleRevokeInput) {

        ElepRevokeOutput ereResult = new ElepRevokeOutput();
        // 发送请求
        String s = httpPost(ybServiceConfig.getEleUrl() + "/revoke", eleRevokeInput);
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        Result result = null;
        try {
            result = mapper.readValue(s, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result != null && result.isSuccess()) {
            ereResult = JSON.parseObject(JSON.toJSONString(result.getResult()), ElepRevokeOutput.class);
        }

        return ereResult;
    }

    /**
     * 采用HTTP协议中的 POST 方法，电子处方信息查询请求，并处理响应结果
     *
     * @param eleQueryPreInput 电子处方信息查询入参实体
     * @return 电子处方信息查询出参实体
     */
    @Override
    public QueryPrescription queryPre(ElepQuerPrescriptionInput eleQueryPreInput) {
        //拼参数
//        BaseParam baseParam = new BaseParam();
//        baseParam.setBaseInfo(ybParamBuilderUtil.getBaseInfo()).setData(eleQueryPreInput);
//        baseParam.setBaseInfo(ybParamBuilderUtil.getBaseInfo()).setData(o);
        QueryPrescription emrResult = new QueryPrescription();
        // 发送请求
        String s =
                httpPost(ybServiceConfig.getEleUrl() + "/querPrescription", eleQueryPreInput);
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        Result result = null;
        try {
            result = mapper.readValue(s, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result != null && result.isSuccess()) {
            emrResult = JSON.parseObject(JSON.toJSONString(result.getResult()), QueryPrescription.class);
        }

        return emrResult;
    }

    /**
     * 采用HTTP协议中的 POST 方法，电子处方取药结果查询请求，并处理响应结果
     *
     * @param eleMedInput 电子处方取药结果查询入参实体
     * @return 电子处方取药结果查询出参实体
     */
    public MedicationResultInquiry queryMedPre(ElepMedresultInput eleMedInput){

        MedicationResultInquiry medResult = new MedicationResultInquiry();
        // 发送请求
        String s =
                httpPost(ybServiceConfig.getEleUrl() + "/medresult", eleMedInput);
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        Result result = null;
        try {
            result = mapper.readValue(s, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result != null && result.isSuccess()) {
            medResult = JSON.parseObject(JSON.toJSONString(result.getResult()), MedicationResultInquiry.class);
        }

        return medResult;
    }
    /**
     * 发送http请求（2025/05/02经测试，若以自带的工具类发送请求失败，故使用原peis系统中成功调用的写法重新封装）
     * @param url 路径
     * @param o 参数
     * @return
     */
    private String httpPost(String url,Object o){
        String resultString = "";
//        //拼参数
        BaseParam baseParam = new BaseParam();
        baseParam.setBaseInfo(ybParamBuilderUtil.getBaseInfo()).setData(o);
        //创建Http请求
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setConnectionRequestTimeout(30000)
                .setSocketTimeout(30000).build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        CloseableHttpResponse response = null;
        //发送请求
        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity stringEntity = new StringEntity(JSON.toJSONString(baseParam), ContentType.APPLICATION_JSON);
            httpPost.setEntity(stringEntity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("Http请求异常，请稍后再试。");
        }
        finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }
}
