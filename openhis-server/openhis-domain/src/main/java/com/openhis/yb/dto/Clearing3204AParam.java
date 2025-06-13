/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【3204A】清算申请撤销(吉林省)
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Clearing3204AParam {

    // 机构清算申请事件ID
    @JSONField(name = "clr_apply_evt_id")
    private String clrApplyEvtId;

    // 清算机构
    @JSONField(name = "clr_optins")
    private String clrOptins;
}
