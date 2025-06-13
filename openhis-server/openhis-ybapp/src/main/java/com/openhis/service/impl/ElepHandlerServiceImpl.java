package com.openhis.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.core.common.exception.ServiceException;
import com.openhis.domain.BaseResponse;
import com.openhis.pojo.RequestData;
import com.openhis.service.IElepHandlerService;
import com.openhis.utils.HseEncAndDecUtil;
import com.openhis.utils.RedisUtil;
import com.openhis.utils.SignUtil;
import com.openhis.vo.BaseParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * 医保交易
 */
@Component
@Slf4j
public abstract class ElepHandlerServiceImpl implements IElepHandlerService {
    Logger logger = LoggerFactory.getLogger(ElepHandlerServiceImpl.class);
    /**********************************************************************
     *
     * @method handle
     * @author TianYi
     * @Date 2021-08-13
     * @param        requestData
     * @return
     * @description 发送数据
     **********************************************************************/
    @Override
    public abstract BaseResponse elepHandle(RequestData requestData, BaseParam baseParam);


    /**********************************************************************
     *
     * @method sendDatas
     * @author TianYi
     * @Date 2021-08-13
     * @param        intNum
     * @param        requestData
     * @return
     * @description 初始化数据体
     **********************************************************************/
    @Override
    public JSONObject initTransBody(String intNum, RequestData requestData, BaseParam baseParam) {
        String prePrvKey = baseParam.getBaseInfo().getPrePrvKey();
        String appId = baseParam.getBaseInfo().getPreAppId();
        String appSecret = baseParam.getBaseInfo().getPreAppSecret();

        String sortedData = SignUtil.getValue(JSONObject.toJSONString(requestData.getData()));
        logger.info("【" + intNum + "】 整体入参：====" + JSONObject.toJSONString(sortedData));

        // 加密数据
        try {
            JSONObject jsonObject = JSON.parseObject(sortedData);
            JSONObject inputObject = HseEncAndDecUtil.encryptMsg(appId, appSecret, prePrvKey, "", jsonObject);

            logger.info("【" + intNum + "】 加密后的整体入参：====" + JSONObject.toJSONString(inputObject));
            return inputObject;
        } catch (Exception e) {
            throw new ServiceException("加密数据异常");
        }

    }


    /**********************************************************************
     *
     * @method sendDatas
     * @author TianYi
     * @Date 2021-08-13
     * @param        url
     * @param        request
     * @return
     * @description 发送数据
     **********************************************************************/
    @Override
    public BaseResponse sendDatas(String url, JSONObject request, BaseParam baseParam) {
        String appId = baseParam.getBaseInfo().getPreAppId();
        String appSecret = baseParam.getBaseInfo().getPreAppSecret();

        logger.info("响应URL：====" + url);

        CloseableHttpClient client = HttpClients.createDefault();
        String encData = request.getString("encData");
        String signData = request.getString("signData");
        request.remove("encData");
        request.remove("signData");
        String sortjson = SignUtil.getValue(request);
        sortjson = sortjson.substring(0, sortjson.length() - 1) + ",\"encData\":\"" + encData + "\"" + ",\"signData\":\"" + signData + "\"}";

        BaseResponse response = null;
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity;
        try {
            entity = new StringEntity(sortjson);//解决中文乱码问题
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("解决中文乱码问题失败");
        }

        httpPost.addHeader("Content-Type", "application/json; charset=utf-8");
        httpPost.setEntity(entity);
        try {
            HttpResponse resp = client.execute(httpPost);//执行时机

            String respResult = "";
            StatusLine statusLine = resp.getStatusLine();//获取请求对象中的响应行对象
            int responseCode = statusLine.getStatusCode();//从状态行中获取状态码
            if (responseCode == 200) {
                HttpEntity entity2 = resp.getEntity();
                if (entity2 != null) {
                    System.out.println("响应内容：");
                    System.out.println(JSON.toJSONString(response));
                    //根据httpclient的官方说明中，EntityUtils.toString(httpEntity) 这个被调用一次后就会自动销毁
                    BufferedReader in = new BufferedReader(new InputStreamReader(resp.getEntity().getContent(), "UTF-8"));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";
                    String NL = System.getProperty("line.separator");
                    while ((line = in.readLine()) != null) {
                        sb.append(line + NL);
                    }
                    in.close();
                    String content = sb.toString();
                    response = JSONObject.parseObject(content, BaseResponse.class);
                    System.out.println("响应之后出参：====" + response);
                    logger.info("出参数据：====" + response);
                    //数据解密
                    String testdecData = null;
                    try {
                        testdecData = HseEncAndDecUtil.sm4Decrypt(appId, appSecret, response.getEncData());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("解密后的数据" + testdecData);
                    logger.info("解密后的出参数据：====" + testdecData);
                    response.setEncData(testdecData);
                    return response;
                }
            } else {
                throw new ServiceException("电子处方网络请求失败");
            }
            client.close();
        } catch (IOException e) {
            throw new ServiceException("电子处方网络请求失败");
        }
        return response;
    }
}
