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
public class Clearing3206AParam {

    //定点编号
    @JSONField(name = "fixmedins_code")
    private String fixmedinsCode;

    //页数
    @JSONField(name = "page_num")
    private Integer pageNum = 1;

    //当前页面
    @JSONField(name = "page_size")
    private Integer pageSize;
}
