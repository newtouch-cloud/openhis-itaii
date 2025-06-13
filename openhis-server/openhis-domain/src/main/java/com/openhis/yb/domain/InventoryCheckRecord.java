/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import liquibase.pro.packaged.D;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 【3502】库存信息变更
 *
 * @author SunJQ
 * @date 2025-04-30
 */
@Data
@Accessors(chain = true)
@TableName("yb_inventory_check_record")
@EqualsAndHashCode(callSuper = false)
public class InventoryCheckRecord {
    //主键
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    //入参
    private String param;

    //出参
    private String result;

    // 1. 医疗目录编码（字符型，50位，必填）
    @JSONField(name = "med_list_codg")
    private String medListCodg;

    // 2. 定点医药机构目录编号（字符型，30位，必填）
    @JSONField(name = "fixmedins_hilist_id")
    private String fixmedinsHilistId;

    // 3. 定点医药机构目录名称（字符型，200位，必填）
    @JSONField(name = "fixmedins_hilist_name")
    private String fixmedinsHilistName;

    // 4. 处方药标志（字符型，3位，必填）
    @JSONField(name = "rx_flag")
    private String rxFlag;

    // 5. 盘存日期（日期型，必填，格式：yyyy-MM-dd）
    @JSONField(name = "invdate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date invdate;

    // 6. 库存数量（数值型，16位含2位小数，必填）
    @JSONField(name = "inv_cnt")
    private BigDecimal invCnt;

    // 7. 生产批号（字符型，30位，非必填）
    @JSONField(name = "manu_lotnum")
    private String manuLotnum;

    // 8. 定点医药机构批次流水号（字符型，30位，必填）
    @JSONField(name = "fixmedins_bchno")
    private String fixmedinsBchno;

    // 9. 生产日期（日期型，必填，格式：yyyy-MM-dd）
    @JSONField(name = "manu_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date manuDate;

    // 10. 有效期止（日期型，必填，格式：yyyy-MM-dd）
    @JSONField(name = "expy_end")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expyEnd;
}
