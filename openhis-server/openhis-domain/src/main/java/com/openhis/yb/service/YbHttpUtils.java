/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.core.common.exception.ServiceException;
import com.core.common.utils.SecurityUtils;
import com.core.common.utils.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.openhis.yb.domain.ClinicReg;
import com.openhis.yb.dto.*;
import com.openhis.yb.model.Clinic2207OrderModel;
import com.openhis.yb.model.Clinic2207OrderParam;
import com.openhis.yb.util.YbParamBuilderUtil;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * http请求接口
 *
 * @author SunJQ
 * @date 2025-04-11
 */
@Service
public class YbHttpUtils {

    @Autowired
    YbParamBuilderUtil ybParamBuilderUtil;

    Logger logger = LoggerFactory.getLogger(YbHttpUtils.class);


    public Info1101Output getPerInfo(Info1101ReadcardParam readcard) {
        // 省名参数
        Result result = null;
        Info1101Output perinfo = null;
        // 发送请求
        String resultString = httpPost(SecurityUtils.getLoginUser().getOptionJson().getString("ybUrl") + "/getPatinfo", readcard);
        if (StringUtils.isEmpty(resultString)) {
            throw new ServiceException("未接收到医保返回参数");
        }
        System.out.println(JSON.toJSONString(resultString));
        logger.info("【1101】返回参数:"+resultString);
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        try {
            result = mapper.readValue(resultString, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result == null) {
            throw new ServiceException("未接收到医保返回参数");
        } else if (result.getCode() == 200) {
            System.out.println(JSON.toJSONString(result.getResult()));
            perinfo = JSON.parseObject(JSON.toJSONString(result.getResult()), Info1101Output.class);
        } else {
            throw new ServiceException(result.getMessage());
        }

        return perinfo;
    }


    public ClinicReg2201Output reg(ClinicReg reg) {
        // 声名参数
        Result result = null;
        ClinicReg2201Output clinicReg2201Output = new ClinicReg2201Output();
        // 发送请求
        String s = httpPost(SecurityUtils.getLoginUser().getOptionJson().getString("ybUrl") + "/reg", reg);
        if (StringUtils.isEmpty(s)) {
            throw new ServiceException("未接收到医保返回参数");
        }
        System.out.println(JSON.toJSONString(s));
        logger.info("【2201】返回参数:"+s);
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        try {
            result = mapper.readValue(s, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result == null) {
            throw new ServiceException("未接收到医保返回参数");
        } else if (result.getCode() == 200) {
            System.out.println(JSON.toJSONString(result.getResult()));
            clinicReg2201Output = JSON.parseObject(JSON.toJSONString(result.getResult()), ClinicReg2201Output.class);
        } else {
            throw new ServiceException(result.getMessage());
        }
        return clinicReg2201Output;
    }


    public ClinicReg2201Output cancelReg(ClinicReg reg) {
        // 声名参数
        Result result = null;
        ClinicReg2201Output clinicReg2201Output = new ClinicReg2201Output();
        // 发送请求
        String s = httpPost(SecurityUtils.getLoginUser().getOptionJson().getString("ybUrl") + "/cancelReg", reg);
        if (StringUtils.isEmpty(s)) {
            throw new ServiceException("未接收到医保返回参数");
        }
        System.out.println(JSON.toJSONString(s));
        logger.info("【2202】返回参数:"+JSON.toJSONString(s));
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        try {
            result = mapper.readValue(s, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result == null) {
            throw new ServiceException("未接收到医保返回参数");
        } else if (result.getCode() == 200) {
            System.out.println(JSON.toJSONString(result.getResult()));
            clinicReg2201Output = JSON.parseObject(JSON.toJSONString(result.getResult()), ClinicReg2201Output.class);
        } else {
            throw new ServiceException(result.getMessage());
        }
        return clinicReg2201Output;
    }


    public Clinic2204OrderResult upload2204Record(Clinic2204OrderParam clinic2204OrderParam) {
        Clinic2204OrderResult clinicFeedetail2204Result = new Clinic2204OrderResult();
        // 声名参数
        Result result = null;
        // 发送请求
        String s = httpPost(SecurityUtils.getLoginUser().getOptionJson().getString("ybUrl") + "/upload2204-record", clinic2204OrderParam);
        if (StringUtils.isEmpty(s)) {
            throw new ServiceException("未接收到医保返回参数");
        }
        System.out.println(JSON.toJSONString(s));
        logger.info("【2204】返回参数:"+s);
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        try {
            result = mapper.readValue(s, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result == null) {
            throw new ServiceException("未接收到医保返回参数");
        } else if (result.getCode() == 200) {
            System.out.println(JSON.toJSONString(result.getResult()));
            clinicFeedetail2204Result =
                JSON.parseObject(JSON.toJSONString(result.getResult()), Clinic2204OrderResult.class);
        } else {
            throw new ServiceException(result.getMessage());
        }

        return clinicFeedetail2204Result;

    }


    public Clinic2206OrderOutput upload2206Record(Clinic2206OrderParam clinic2206OrderParam) {

        Clinic2206OrderOutput clinic2206OrderResult = new Clinic2206OrderOutput();
        // 声名参数
        Result result = null;
        // 发送请求
        String s = httpPost(SecurityUtils.getLoginUser().getOptionJson().getString("ybUrl") + "/preSetl", clinic2206OrderParam);
        if (StringUtils.isEmpty(s)) {
            throw new ServiceException("未接收到医保返回参数");
        }
        System.out.println("【2206】返回参数:"+JSON.toJSONString(s));
        logger.info(s);
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        try {
            result = mapper.readValue(s, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result == null) {
            throw new ServiceException("未接收到医保返回参数");
        } else if (result.getCode() == 200) {
            System.out.println(JSON.toJSONString(result.getResult()));
            clinic2206OrderResult =
                JSON.parseObject(JSON.toJSONString(result.getResult()), Clinic2206OrderOutput.class);
        } else {
            throw new ServiceException(result.getMessage());
        }

        return clinic2206OrderResult;
    }


    public Clinic2207OrderModel settle(Clinic2207OrderParam clinicOrder2207) {

        Clinic2207OrderModel clinic2206OrderResult = new Clinic2207OrderModel();
        // 声名参数
        Result result = null;
        // 发送请求
        String s = httpPost(SecurityUtils.getLoginUser().getOptionJson().getString("ybUrl") + "/setl", clinicOrder2207);
        if (StringUtils.isEmpty(s)) {
            throw new ServiceException("未接收到医保返回参数");
        }
        System.out.println(JSON.toJSONString(s));
        logger.info("【2207】返回参数:"+s);
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        try {
            result = mapper.readValue(s, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result == null) {
            throw new ServiceException("未接收到医保返回参数");
        } else if (result.getCode() == 200) {
            System.out.println(JSON.toJSONString(result.getResult()));
            clinic2206OrderResult =
                JSON.parseObject(JSON.toJSONString(result.getResult()), Clinic2207OrderModel.class);
        } else {
            throw new ServiceException(result.getMessage());
        }

        return clinic2206OrderResult;
    }


    public Sign9001Result sign(Sign signParam) {
        Sign9001Result sign = null;

        //Console.error("BBB:" + signParam.getMac() + "|" + signParam.getIp() + "|" + signParam.getOpterNo());

        //JSONObject optionJson = SecurityUtils.getLoginUser().getOptionJson();

        String resultString = httpPost(SecurityUtils.getLoginUser().getOptionJson().getString("ybUrl") + "/signIn", signParam);
        if (StringUtils.isEmpty(resultString)) {
            throw new ServiceException("未接收到医保返回参数");
        }
        System.out.println(JSON.toJSONString(resultString));
        logger.info("【9001】返回参数:"+resultString);
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        Result result = null;
        try {
            result = mapper.readValue(resultString, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result == null) {
            throw new ServiceException("未接收到医保返回参数");
        } else if (result.getCode() == 200) {
            System.out.println(JSON.toJSONString(result.getResult()));
            sign = JSON.parseObject(JSON.toJSONString(result.getResult()), Sign9001Result.class);
        } else {
            throw new ServiceException(result.getMessage());
        }

        return sign;
    }

    public void unPreSettle(Clinic2205OrderParam clinic2205OrderParam) {

        // 声名参数
        Result result = null;
        // 发送请求
        String s = httpPost(SecurityUtils.getLoginUser().getOptionJson().getString("ybUrl") + "/unPreSettle", clinic2205OrderParam);
        if (StringUtils.isEmpty(s)) {
            throw new ServiceException("未接收到医保返回参数");
        }
        System.out.println(JSON.toJSONString(s));
        logger.info(s);
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        try {
            result = mapper.readValue(s, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result == null) {
            throw new ServiceException("未接收到医保返回参数");
        } else if (result.getCode() == 200) {
            return;
        } else {
            throw new ServiceException(result.getMessage());
        }
    }

    public Clinic2208UnSetlInfoOutput unSettle(Clinic2208UnSetlInfoParam clinicOrder2208) {

        Clinic2208UnSetlInfoOutput clinicOrder2206Result = new Clinic2208UnSetlInfoOutput();
        // 声名参数
        Result result = null;
        // 发送请求
        String s = httpPost(SecurityUtils.getLoginUser().getOptionJson().getString("ybUrl") + "/cancelSetl", clinicOrder2208);
        if (StringUtils.isEmpty(s)) {
            throw new ServiceException("未接收到医保返回参数");
        }
        System.out.println(JSON.toJSONString(s));
        logger.info("【2208】返回参数:"+s);
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        try {
            result = mapper.readValue(s, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result == null) {
            throw new ServiceException("未接收到医保返回参数");
        } else if (result.getCode() == 200) {
            System.out.println(JSON.toJSONString(result.getResult()));
            clinicOrder2206Result =
                JSON.parseObject(JSON.toJSONString(result.getResult()), Clinic2208UnSetlInfoOutput.class);
        } else {
            throw new ServiceException(result.getMessage());
        }
        return clinicOrder2206Result;
    }


    public Result directoryCheck(MedicalDirectory3301ListParam medicalDirectory3301ListParam) {

        Clinic2208UnSetlInfoResult clinicOrder2206Result = new Clinic2208UnSetlInfoResult();
        // 发送请求
        String s = httpPost(SecurityUtils.getLoginUser().getOptionJson().getString("ybUrl") + "/directoryCheck", medicalDirectory3301ListParam);
        if (StringUtils.isEmpty(s)) {
            throw new ServiceException("未接收到医保返回参数");
        }
        System.out.println(JSON.toJSONString(s));
        logger.info("【3301】返回参数:"+s);
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        Result result = null;
        try {
            result = mapper.readValue(s, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public Result directoryUnCheck(MedicalDirectory3302Param medicalDirectory3302Param) {
        // 发送请求
        String s = httpPost(SecurityUtils.getLoginUser().getOptionJson().getString("ybUrl") + "/directoryUnCheck", medicalDirectory3302Param);
        if (StringUtils.isEmpty(s)) {
            throw new ServiceException("未接收到医保返回参数");
        }
        System.out.println(JSON.toJSONString(s));
        logger.info("【3302】返回参数:"+s);
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        Result result = null;
        try {
            result = mapper.readValue(s, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public Result reconcileGeneralLedger(Financial3201Param financial3201Param) {
        // 发送请求
        String s = httpPost(SecurityUtils.getLoginUser().getOptionJson().getString("ybUrl") + "/reconcile", financial3201Param);
        if (StringUtils.isEmpty(s)) {
            throw new ServiceException("未接收到医保返回参数");
        }
        System.out.println(JSON.toJSONString(s));
        logger.info(s);
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        Result result = null;
        try {
            result = mapper.readValue(s, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public FinancialSettlement3202Result reconcileGeneralLedgerDetail(FinancialSettlement3202Param financial3202Param) {
        // 发送请求
        String s = httpPost(SecurityUtils.getLoginUser().getOptionJson().getString("ybUrl") + "/reconcile-detail", financial3202Param);
        if (StringUtils.isEmpty(s)) {
            throw new ServiceException("未接收到医保返回参数");
        }
        System.out.println(JSON.toJSONString(s));
        logger.info(s);
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        FinancialSettlement3202Result result = null;
        try {
            result = mapper.readValue(s, FinancialSettlement3202Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public List<FinancialSettlement3209AResult>
        threePartSearch(FinancialSettlement3209AParam financialSettlement3209AParam) {
        // 发送请求
        String s = httpPost(SecurityUtils.getLoginUser().getOptionJson().getString("ybUrl") + "/three-part-search-err", financialSettlement3209AParam);
        if (StringUtils.isEmpty(s)) {
            throw new ServiceException("未接收到医保返回参数");
        }
        System.out.println(JSON.toJSONString(s));
        logger.info(s);
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        List<FinancialSettlement3209AResult> result = null;
        try {
            result = mapper.readValue(s, List.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public String applyFinancialClearing(Financial3203AParam financial3203AParam) {
        // 发送请求
        String s = httpPost(SecurityUtils.getLoginUser().getOptionJson().getString("ybUrl") + "/apply-clearing", financial3203AParam);
        if (StringUtils.isEmpty(s)) {
            throw new ServiceException("未接收到医保返回参数");
        }
        return s;
    }


    public Result cancelFinancialClearing(Financial3204Param financial3204Param) {

        // 发送请求
        String s = httpPost(SecurityUtils.getLoginUser().getOptionJson().getString("ybUrl") + "/cancel-clearing", financial3204Param);
        if (StringUtils.isEmpty(s)) {
            throw new ServiceException("未接收到医保返回参数");
        }
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        Result result = null;
        try {
            result = mapper.readValue(s, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public Clearing3205AResult getFinancialClearingStatus(Clearing3205AParma clearing3205AParma) {
        // 发送请求
        String s = httpPost(SecurityUtils.getLoginUser().getOptionJson().getString("ybUrl") + "/get-clearing-status", clearing3205AParma);
        if (StringUtils.isEmpty(s)) {
            throw new ServiceException("未接收到医保返回参数");
        }
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        Clearing3205AResult result = null;
        try {
            result = mapper.readValue(s, Clearing3205AResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public Result uploadInventoryCount(MedicalInventory3501Param medicalInventory3501Param) {
        // 发送请求
        String s = httpPost(SecurityUtils.getLoginUser().getOptionJson().getString("ybUrl") + "/upload-inventory-count", medicalInventory3501Param);
        if (StringUtils.isEmpty(s)) {
            throw new ServiceException("未接收到医保返回参数");
        }
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        Result result = null;
        try {
            result = mapper.readValue(s, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public Result updateInventoryCount(MedicalInventory3502Param medicalInventory3502Param) {
        // 发送请求
        String s = httpPost(SecurityUtils.getLoginUser().getOptionJson().getString("ybUrl") + "/update-inventory", medicalInventory3502Param);
        if (StringUtils.isEmpty(s)) {
            throw new ServiceException("未接收到医保返回参数");
        }
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        Result result = null;
        try {
            result = mapper.readValue(s, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public Result procurement(Medical3503Param medical3503Param) {
        // 发送请求
        String s = httpPost(SecurityUtils.getLoginUser().getOptionJson().getString("ybUrl") + "/procurement", medical3503Param);
        if (StringUtils.isEmpty(s)) {
            throw new ServiceException("未接收到医保返回参数");
        }
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        Result result = null;
        try {
            result = mapper.readValue(s, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public Result cancelProcurement(MedicalPurchase3504Param medicalPurchase3504Param) {
        // 发送请求
        String s = httpPost(SecurityUtils.getLoginUser().getOptionJson().getString("ybUrl") + "/procurement-cancel", medicalPurchase3504Param);
        if (StringUtils.isEmpty(s)) {
            throw new ServiceException("未接收到医保返回参数");
        }
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        Result result = null;
        try {
            result = mapper.readValue(s, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public Result merchandise(Medical3505Param medical3505Param) {
        // 发送请求
        String s = httpPost(SecurityUtils.getLoginUser().getOptionJson().getString("ybUrl") + "/merchandise", medical3505Param);
        if (StringUtils.isEmpty(s)) {
            throw new ServiceException("未接收到医保返回参数");
        }
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        Result result = null;
        try {
            result = mapper.readValue(s, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public Result cancelMerchandise(Medical3506Param medical3506Param) {
        // 发送请求
        String s = httpPost(SecurityUtils.getLoginUser().getOptionJson().getString("ybUrl") + "/cancel-merchandise", medical3506Param);
        if (StringUtils.isEmpty(s)) {
            throw new ServiceException("未接收到医保返回参数");
        }
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        Result result = null;
        try {
            result = mapper.readValue(s, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public Result deleteGoodsInfo(Medical3507Param medical3507Param) {
        // 发送请求
        String s = httpPost(SecurityUtils.getLoginUser().getOptionJson().getString("ybUrl") + "/del-goods", medical3507Param);
        if (StringUtils.isEmpty(s)) {
            throw new ServiceException("未接收到医保返回参数");
        }
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        Result result = null;
        try {
            result = mapper.readValue(s, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public Result cancelFeeDetail(Clinic2205OrderParam clinic2205OrderParam) {
        // 发送请求
        String s = httpPost(SecurityUtils.getLoginUser().getOptionJson().getString("ybUrl") + "/cancel2205", clinic2205OrderParam);
        if (StringUtils.isEmpty(s)) {
            throw new ServiceException("未接收到医保返回参数");
        }
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        Result result = null;
        try {
            result = mapper.readValue(s, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public Result upload2203Record(Clinic2203MedicalParam medical2203Param) {
        // 发送请求
        String s = httpPost(SecurityUtils.getLoginUser().getOptionJson().getString("ybUrl") + "/upload2203-record", medical2203Param);
        if (StringUtils.isEmpty(s)) {
            throw new ServiceException("未接收到医保返回参数");
        }
        // 参数处理
        ObjectMapper mapper = new ObjectMapper();
        Result result = null;
        try {
            result = mapper.readValue(s, Result.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 发送http请求（2025/05/02经测试，若以自带的工具类发送请求失败，故使用原peis系统中成功调用的写法重新封装）
     *
     * @param url 路径
     * @param o 参数
     * @return
     */
    private String httpPost(String url, Object o) {
        String resultString = "";
        // 拼参数
        BaseParam baseParam = new BaseParam();
        baseParam.setBaseInfo(ybParamBuilderUtil.getBaseInfo(JSON.parseObject(JSON.toJSONString(o)))).setData(o);
        logger.info("【请求路径】:" + url + ";【入参】: " + JSON.toJSONString(baseParam));
        // 创建Http请求
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setConnectionRequestTimeout(30000)
            .setSocketTimeout(30000).build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        CloseableHttpResponse response = null;
        // 发送请求
        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity stringEntity = new StringEntity(JSON.toJSONString(baseParam), ContentType.APPLICATION_JSON);
            httpPost.setEntity(stringEntity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            if(response==null){
                throw new ServiceException("Http请求异常，未接受返回参数");
            }
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("Http请求异常，请稍后再试。");
        }
        finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    logger.error("关闭响应异常", e);
                    throw new ServiceException("未关闭系统资源："+e.getStackTrace());
                }
            }
        }
        return resultString;
    }


    public List<Catalogue1312Output> queryYbCatalogue(Catalogue1312QueryParam catalogue1312QueryParam) {
        List<Catalogue1312Output> outputList = new ArrayList<>();
        try {

            String resultString = httpPost(SecurityUtils.getLoginUser().getOptionJson().getString("ybUrl") + "/queryYbCatalogue", catalogue1312QueryParam);
            // System.out.println("--------1312resultString-------------" + resultString);
            // 1. 解析外层 JSON
            ObjectMapper mapper = new ObjectMapper();
            mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE); // 启用 Snake Case 自动映射
            JsonNode rootNode = mapper.readTree(resultString);
            String resultStr = rootNode.get("result").asText();

            // 2. 解析 result 中的 JSON 数组
            outputList = mapper.readValue(resultStr, new TypeReference<List<Catalogue1312Output>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return outputList;
    }

}
