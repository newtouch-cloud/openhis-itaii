/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【3502】商品库存变更
 *
 * @author SunJQ
 * @date 2025-04-16
 */

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MedicalInventory3502Param {
    // 1. 医疗目录编码（字符型，50位，必填）
    @JSONField(name = "med_list_codg")
    private String medListCodg;

    // 2. 库存变更类型（字符型，6位，必填）
    @JSONField(name = "inv_chg_type")
    private String invChgType;

    // 3. 定点医药机构目录编号（字符型，30位，必填）
    @JSONField(name = "fixmedins_hilist_id")
    private String fixmedinsHilistId;

    // 4. 定点医药机构目录名称（字符型，200位，必填）
    @JSONField(name = "fixmedins_hilist_name")
    private String fixmedinsHilistName;

    // 5. 定点医药机构批次流水号（字符型，30位，必填）
    @JSONField(name = "fixmedins_bchno")
    private String fixmedinsBchno;

    // 6. 单价（数值型，16位含6位小数，必填）
    @JSONField(name = "pric")
    private BigDecimal pric;

    // 7. 数量（数值型，16位含4位小数，必填）
    @JSONField(name = "cnt")
    private BigDecimal cnt;

    // 8. 处方药标志（字符型，3位，必填）
    @JSONField(name = "rx_flag")
    private String rxFlag;

    // 9. 库存变更时间（日期时间型，必填，格式：yyyy-MM-dd HH:mm:ss）
    @JSONField(name = "inv_chg_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date invChgTime;

    // 10. 库存变更经办人姓名（字符型，50位，非必填）
    @JSONField(name = "inv_chg_opter_name")
    private String invChgOpterName;

    // 11. 备注（字符型，500位，非必填）
    @JSONField(name = "memo")
    private String memo;

    // 12. 拆零标志（字符型，2位，必填）
    @JSONField(name = "trdn_flag")
    private String trdnFlag;

    // 13. 溯源码节点信息（字符型，长度未明确）
    @JSONField(name = "drugtracinfo")
    private JSONArray drugtracinfo;
}
