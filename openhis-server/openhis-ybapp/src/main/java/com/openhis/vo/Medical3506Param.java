/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 【3506】商品销售退货
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Medical3506Param {
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

    // 5. 结算ID（字符型，30位，非必填）
    @JSONField(name = "setl_id")
    private String setlId;

    // 6. 人员编号（字符型，30位，非必填）
    @JSONField(name = "psn_no")
    private String psnNo;

    // 7. 人员证件类型（字符型，6位，必填）
    @JSONField(name = "psn_cert_type")
    private String psnCertType;

    // 8. 证件号码（字符型，50位，非必填）
    @JSONField(name = "certno")
    private String certno;

    // 9. 人员姓名（字符型，50位，非必填）
    @JSONField(name = "psn_name")
    private String psnName;

    // 10. 生产批号（字符型，30位，必填）
    @JSONField(name = "manu_lotnum")
    private String manuLotnum;

    // 11. 生产日期（日期型，必填，格式：yyyy-MM-dd）
    @JSONField(name = "manu_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date manuDate;

    // 12. 有效期止（日期型，必填，格式：yyyy-MM-dd）
    @JSONField(name = "expy_end")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expyEnd;

    // 13. 处方药标志（字符型，3位，必填）
    @JSONField(name = "rx_flag")
    private String rxFlag;

    // 14. 拆零标志（字符型，3位，必填）
    @JSONField(name = "trdn_flag")
    private String trdnFlag;

    // 15. 最终成交单价（数值型，16位含6位小数，非必填）
    @JSONField(name = "finl_trns_pric")
    private BigDecimal finlTrnsPric;

    // 16. 销售/退货数量（数值型，16位含4位小数，必填）
    @JSONField(name = "sel_retn_cnt")
    private BigDecimal selRetnCnt;

    // 17. 销售/退货时间（日期时间型，必填，格式：yyyy-MM-dd HH:mm:ss）
    @JSONField(name = "sel_retn_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date selRetnTime;

    // 18. 销售/退货经办人姓名（字符型，50位，必填）
    @JSONField(name = "sel_retn_opter_name")
    private String selRetnOpterName;

    // 19. 备注（字符型，500位，非必填）
    @JSONField(name = "memo")
    private String memo;

    // 20. 商品销售流水号（字符型，50位，非必填）
    @JSONField(name = "medins_prod_sel_no")
    private String medinsProdSelNo;

    // 21. 就医流水号（字符型，30位，必填）
    @JSONField(name = "mdtrt_sn")
    private String mdtrtSn;

    // 22. 溯源码节点信息（字符型，长度未明确，暂定500位）
    @JSONField(name = "drugtracinfo")
    private JSONArray drugtracinfo;
}
