/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.ybmanage.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 本地部署方案-医保配置类
 *
 * @author SunJQ
 * @date 2025-04-11
 */
@Configuration
@ConstructorBinding
@PropertySource("classpath:properties/yb.properties")
@ConfigurationProperties(prefix = "ybapp.config")
@EnableConfigurationProperties
public class YbServiceConfig {

    private String url;

    private String eleUrl;

    private String key;

    private String timeOut;

    private String clrOptins;// 清算机构；必须是准确的6位编码，可以从3206A交易获取

    private String fixmedinsCode;// 定点医药机构编号,如H22010200XXX

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEleUrl() {
        return eleUrl;
    }

    public void setEleUrl(String eleUrl) {
        this.eleUrl = eleUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public YbServiceConfig(String url, String key, String timeOut) {
        this.url = url;
        this.key = key;
        this.timeOut = timeOut;
    }

    public YbServiceConfig() {}

    public String getClrOptins() {
        return clrOptins;
    }

    public void setClrOptins(String clrOptins) {
        this.clrOptins = clrOptins;
    }

    public String getFixmedinsCode() {
        return fixmedinsCode;
    }

    public void setFixmedinsCode(String fixmedinsCode) {
        this.fixmedinsCode = fixmedinsCode;
    }
}
