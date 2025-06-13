package com.openhis.domain;

import com.core.common.core.domain.HisBaseEntity;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 基础响应模型 包含所有接口共有的响应参数
 */
@Data
public class BaseResponse {
    private int code; // 响应状态码
    private String message; // 响应异常信息
    private Boolean success; // 响应标识
    private String type; // 类型
    private String appId; // 应用id
    private String timestamp; // 当前时间
    private String encType; // 加密方式 SM4
    private String signType; // 签名方式 SM2
    private String signData; // 签名串
    private String encData; // 加密数据

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", this.code);
        map.put("message", this.message);
        map.put("success", this.success);
        map.put("type", this.type);
        map.put("appId", this.appId);
        map.put("timestamp", this.timestamp);
        map.put("encType", this.encType);
        map.put("signType", this.signType);
        map.put("signData", this.signData);
        map.put("encData", this.encData);
        return map;
    }
}