package com.openhis.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * http相关配置
 *
 * @author system
 */
@Data
@Component
@ConfigurationProperties(prefix = "http")
@PropertySource(value = {"classpath:http.yml"})
public class HttpConfig {
    private String appId;
    private String key;
    private String url;
    private String fixmedinsCode;
}