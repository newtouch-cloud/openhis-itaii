/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *【3507】商品信息删除
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Medical3507Param {

    // 定点医药机构批次流水号
    @JSONField(name = "fixmedins_bchno")
    private String fixmedinsBchno;

    // 进销存数据类型
    @JSONField(name = "inv_data_type")
    private String invDataType;
}
