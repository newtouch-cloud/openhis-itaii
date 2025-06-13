/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openhis.config.InterfaceConfig;
import com.openhis.vo.FileDownload;
import com.openhis.vo.FileResult;
import com.openhis.vo.Result;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件服务
 *
 * @author SunJQ
 * @date 2025-05-04
 */
@Service
public class FileService {

    @Autowired
    private InterfaceConfig interfaceConfig;

    private static final String url = "http://localhost:8097/fsi/api/rsfComIfsService/callService";

    /**
     * 调用普通交易及文件下载交易
     */
    public Result<?> downloadFile(FileResult fileResult) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000).build();
        httppost.setConfig(requestConfig);
        FileDownload fileDownload = new FileDownload();
        fileDownload.setFilename(fileResult.getFilename()).setFileQuryNo(fileResult.getFileQuryNo()).setFixmedinsCode(fileResult.getFixmedinsCode());
        ByteArrayEntity entity = new ByteArrayEntity(JSON.toJSONString(fileDownload).getBytes(StandardCharsets.UTF_8));
        entity.setContentType("text/plain");
        httppost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                httppost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity responseEntity = response.getEntity();
            String result;
            if (responseEntity != null) {
                if (responseEntity.getContentType().getValue().contains("application/octet-stream")) {
                    //拼接文件路径
                    String filePath = interfaceConfig.getFilePath();
                    long time = new Date().getTime();

                    InputStream content = responseEntity.getContent();
                    //返回文件流
                    File file = new File(filePath+"\\\\"+time+".txt");
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    int temp;
                    while ((temp = content.read()) != -1) {
                        fileOutputStream.write(temp);
                    }
                    fileOutputStream.close();
                    return Result.okMsg("文件下载成功"+filePath+"\\\\"+time+".txt");
                } else {
                    //返回字符串
                    result = EntityUtils.toString(responseEntity, "UTF-8");
                    System.out.println(result);
                }
            }
            EntityUtils.consume(entity);
        } catch (ClientProtocolException e) {
            throw new RuntimeException("提交给服务器的请求，不符合HTTP协议", e);
        } catch (IOException e) {
            throw new RuntimeException("向服务器承保接口发起http请求,执行post请求异常", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.error("下载失败");
    }


    /**
     * 调用文件上传交易
     */
    public void uploadFile(String fileName, String text, String mapping) {
        // 例如："testUpload.txt"
        File file = new File(fileName);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000).build();
        httppost.setConfig(requestConfig);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(StandardCharsets.UTF_8);
        builder.addTextBody("jsonStr", text);
        builder.addBinaryBody("file", file, ContentType.DEFAULT_BINARY, fileName);
        HttpEntity entity = builder.build();
        httppost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                httppost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity responseEntity = response.getEntity();
            String result;
            if (responseEntity != null) {
                // 返回字符串
                result = EntityUtils.toString(responseEntity, "UTF-8");
                System.out.println(result);
            }
            EntityUtils.consume(entity);
        } catch (ClientProtocolException e) {
            throw new RuntimeException("提交给服务器的请求，不符合HTTP协议", e);
        } catch (IOException e) {
            throw new RuntimeException("向服务器承保接口发起http请求,执行post请求异常", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public FileResult uploadFile(InputStream fileStream,
                                 String filename,
                                 String fixmedinsCode) throws IOException{
//        // 1. 预处理校验（与之前方案一致）
//        validateZipFile(fileData);
//        List<String> txtLines = extractTxtContent(fileData);
//        validateTxtFormat(txtLines);

        // 2. 构建请求（关键修改点）
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        // 构建multipart请求体（参考医保局示例）
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(StandardCharsets.UTF_8);

        // 添加JSON参数（根据实际接口要求调整）
        builder.addTextBody("jsonStr", buildJsonRequest(filename, fixmedinsCode));

        // 添加文件参数（注意参数名需与接口文档一致）
        builder.addBinaryBody(
                "in",  // 对应接口参数代码"in"
                IOUtils.toByteArray(fileStream),
                ContentType.APPLICATION_OCTET_STREAM,
                filename
        );

        HttpEntity entity = builder.build();
        httpPost.setEntity(entity);

        // 3. 执行请求（关键修改点）
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                throw new RuntimeException("医保接口调用失败，状态码：" + statusCode);
            }

            // 解析响应（参考医保局示例）
            HttpEntity responseEntity = response.getEntity();
            String responseBody = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            return parseMedicareResponse(responseBody);
        }
    }

    private String buildJsonRequest(String filename, String fixmedinsCode) {
        // 根据实际接口要求构建JSON请求体
        return String.format(
                "{" +
                        "\"filename\":\"%s\"," +
                        "\"fixmedins_code\":\"%s\"" +
                        "}",
                filename,
                fixmedinsCode
        );
    }

    private FileResult parseMedicareResponse(String jsonResponse) {
        // 解析医保局返回的JSON响应（根据实际响应结构调整）
        // 示例解析逻辑，需替换为实际解析代码
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonResponse, FileResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("响应解析失败", e);
        }
    }
}
