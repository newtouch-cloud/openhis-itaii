/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *【3206A】清算机构查询(吉林省)
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Clearing3206AResult {
    //总页数
    @JSONField(name = "pages")
    private Integer pages;
    //清算机构（明细）
    @JSONField(name = "clr_optins")
    private String clrOptins;
    //页数
    @JSONField(name = "page_num")
    private Integer pageNum;
    //当前页面查询数量
    @JSONField(name = "page_size")
    private Integer pageSize;
}
