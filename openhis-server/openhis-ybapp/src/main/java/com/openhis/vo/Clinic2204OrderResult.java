/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 *【2204】门诊费用明细信息上传
 *
 * @author SunJQ
 * @date 2025-04-20
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Clinic2204OrderResult {
    //2204门诊费用明细信息上传输出
    @JSONField(name = "result")
    List<Clinic2204FeeDetailResult> result;
}
