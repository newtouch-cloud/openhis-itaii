package com.openhis.domain;

import com.core.common.core.domain.HisBaseEntity;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 基础请求模型
 * 包含所有接口共有的请求参数
 */
@Data
public class BaseRequest {
    private String appId;         // 应用id
    private String version;       // 版本号 1.0.0
    private String timestamp;     // 当前时间
    private String encType;       // 加密方式 SM4
    private String encData;       // 加密数据
    private String signType;      // 签名方式 SM2
    private String signData;      // 签名串
    private String data;          // 未签名数据
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("appId", this.appId);
        map.put("version", this.version);
        map.put("timestamp", this.timestamp);
        map.put("encType", this.encType);
        map.put("encData", this.encData);
        map.put("signType", this.signType);
        map.put("signData", this.signData);
        return map;
    }
}

