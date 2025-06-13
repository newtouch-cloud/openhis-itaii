/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 【3504】
 *
 * @author SunJQ
 * @date 2025-04-30
 */
@Data
@Accessors(chain = true)
@TableName("yb_inventory_purchase_return_record")
@EqualsAndHashCode(callSuper = false)
public class InventoryPurchaseReturnRecord {
    //主键
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    //入参
    private String param;

    //出参
    private String outResult;

    // 1. 医疗目录编码（字符型，50位，必填）
    @JSONField(name = "med_list_codg")
    private String medListCodg;

    // 2. 定点医药机构目录编号（字符型，30位，必填）
    @JSONField(name = "fixmedins_hilist_id")
    private String fixmedinsHilistId;

    // 3. 定点医药机构目录名称（字符型，200位，必填）
    @JSONField(name = "fixmedins_hilist_name")
    private String fixmedinsHilistName;

    // 4. 定点医药机构批次流水号（字符型，30位，必填）
    @JSONField(name = "fixmedins_bchno")
    private String fixmedinsBchno;

    // 5. 供应商名称（字符型，200位，必填）
    @JSONField(name = "spler_name")
    private String splerName;

    // 6. 供应商许可证号（字符型，50位，非必填）
    @JSONField(name = "spler_pmtno")
    private String splerPmtno;

    // 7. 生产日期（日期型，必填，格式：yyyy-MM-dd）
    @JSONField(name = "manu_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date manuDate;

    // 8. 有效期止（日期型，必填，格式：yyyy-MM-dd）
    @JSONField(name = "expy_end")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expyEnd;

    // 9. 最终成交单价（数值型，16位含6位小数，非必填）
    @JSONField(name = "finl_trns_pric")
    private BigDecimal finlTrnsPric;

    // 10. 采购/退货数量（数值型，16位含4位小数，必填）
    @JSONField(name = "purc_retn_cnt")
    private BigDecimal purcRetnCnt;

    // 11. 采购发票编码（字符型，50位，非必填）
    @JSONField(name = "purc_invo_codg")
    private String purcInvoCodg;

    // 12. 采购发票号（字符型，50位，必填）
    @JSONField(name = "purc_invo_no")
    private String purcInvoNo;

    // 13. 处方药标志（字符型，3位，必填）
    @JSONField(name = "rx_flag")
    private String rxFlag;

    // 14. 采购/退货入库时间（日期时间型，必填，格式：yyyy-MM-dd HH:mm:ss）
    @JSONField(name = "purc_retn_stoin_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date purcRetnStoinTime;

    // 15. 采购/退货经办人姓名（字符型，50位，必填）
    @JSONField(name = "purc_retn_opter_name")
    private String purcRetnOpterName;

    // 16. 备注（字符型，500位，非必填）
    @JSONField(name = "memo")
    private String memo;

    // 20. 商品采购流水号（字符型，50位，非必填）
    @JSONField(name = "medins_prod_purc_no")
    private String medinsProdPurcNo;
}
