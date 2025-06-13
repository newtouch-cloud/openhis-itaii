/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【3505】【3506】商品销售
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Medical3505Result {

    // 返回结果
    @JSONField(name = "retRslt")
    private String retRslt;

    // 返回信息
    @JSONField(name = "msgRslt")
    private String msgRslt;
}
