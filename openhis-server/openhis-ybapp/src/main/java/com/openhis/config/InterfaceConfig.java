package com.openhis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 系统参数配置类
 */
@Data
@Component
@PropertySource(value = "classpath:/config/sys-config.properties", encoding = "UTF-8", ignoreResourceNotFound = true)
@ConfigurationProperties(prefix = "prod")
// @ConfigurationProperties(prefix = "dev")
public class InterfaceConfig {

    /** 文件下载路径 */
    private String filePath;

    /** address */
    private String address;

    /** 缓存时间 */
    private int time;

    /** 是否加密 */
    private Boolean isEncrypt;

    /** 应用密钥 */
    private String cliPubKey;

    /** 秘钥 */
    private String cliPrvKey;

    /** appId */
    private String clientId;

    /** eleAddress */
    private String eleAddress;

}
