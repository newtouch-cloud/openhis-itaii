/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【3507】
 *
 * @author SunJQ
 * @date 2025-04-30
 */
@Data
@Accessors(chain = true)
@TableName("yb_inventory_del_record")
@EqualsAndHashCode(callSuper = false)
public class InventoryDelRecord {
    //主键
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    //入参
    private String param;

    //出参
    private String outResult;

    // 定点医药机构批次流水号
    @JSONField(name = "fixmedins_bchno")
    private String fixmedinsBchno;

    // 进销存数据类型
    @JSONField(name = "inv_data_type")
    private String invDataType;
}
