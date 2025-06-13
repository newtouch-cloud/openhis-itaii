/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.openhis.yb.dto.MedicalTraceNoDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【3505】
 *
 * @author SunJQ
 * @date 2025-04-30
 */
@Data
@Accessors(chain = true)
@TableName("yb_inventory_sale_record")
@EqualsAndHashCode(callSuper = false)
public class InventorySaleRecord {
    // 主键
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    // 入参
    private String param;

    // 出参
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

    // 5. 开方医师证件类型（字符型，6位，非必填）
    @JSONField(name = "prsc_dr_cert_type")
    private String prscDrCertType;

    // 6. 开方医师证件号码（字符型，50位，非必填）
    @JSONField(name = "prsc_dr_certno")
    private String prscDrCertno;

    // 7. 开方医师姓名（字符型，50位，必填）
    @JSONField(name = "prsc_dr_name")
    private String prscDrName;

    // 8. 药师证件类型（字符型，6位，非必填）
    @JSONField(name = "phar_cert_type")
    private String pharCertType;

    // 9. 药师证件号码（字符型，50位，非必填）
    @JSONField(name = "phar_certno")
    private String pharCertno;

    // 10. 药师姓名（字符型，50位，必填）
    @JSONField(name = "phar_name")
    private String pharName;

    // 11. 药师执业资格证号（字符型，50位，必填）
    @JSONField(name = "phar_prac_cert_no")
    private String pharPracCertNo;

    // 12. 医保费用结算类型（字符型，6位，必填）
    @JSONField(name = "hi_feesetl_type")
    private String hiFeesetlType;

    // 13. 结算ID（字符型，30位，非必填）
    @JSONField(name = "setl_id")
    private String setlId;

    // 14. 就医流水号（字符型，30位，必填）
    @JSONField(name = "mdtrt_sn")
    private String mdtrtSn;

    // 15. 人员编号（字符型，30位，非必填）
    @JSONField(name = "psn_no")
    private String psnNo;

    // 16. 人员证件类型（字符型，6位，必填）
    @JSONField(name = "psn_cert_type")
    private String psnCertType;

    // 17. 证件号码（字符型，50位，非必填）
    @JSONField(name = "certno")
    private String certno;

    // 18. 人员姓名（字符型，50位，非必填）
    @JSONField(name = "psn_name")
    private String psnName;

    // 19. 生产批号（字符型，30位，必填）
    @JSONField(name = "manu_lotnum")
    private String manuLotnum;

    // 20. 生产日期（日期型，必填，格式：yyyy-MM-dd）
    @JSONField(name = "manu_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date manuDate;

    // 21. 有效期止（日期型，必填，格式：yyyy-MM-dd）
    @JSONField(name = "expy_end")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expyEnd;

    // 22. 处方药标志（字符型，3位，必填）
    @JSONField(name = "rx_flag")
    private String rxFlag;

    // 23. 拆零标志（字符型，3位，必填）
    @JSONField(name = "trdn_flag")
    private String trdnFlag;

    // 24. 最终成交单价（数值型，16位含6位小数，非必填）
    @JSONField(name = "finl_trns_pric")
    private BigDecimal finlTrnsPric;

    // 25. 处方号（字符型，40位，非必填）
    @JSONField(name = "rxno")
    private String rxno;

    // 26. 外购处方标志（字符型，3位，必填）
    @JSONField(name = "rx_circ_flag")
    private String rxCircFlag;

    // 27. 零售单据号（字符型，40位，必填）
    @JSONField(name = "rtal_docno")
    private String rtalDocno;

    // 29. 销售出库单据号（字符型，40位，非必填）
    @JSONField(name = "stoout_no")
    private String stooutNo;

    // 30. 批次号（字符型，30位，非必填）
    @JSONField(name = "bchno")
    private String bchno;

    // 32. 药品条形码（字符型，30位，非必填）
    @JSONField(name = "drug_prod_barc")
    private String drugProdBarc;

    // 33. 货架位（字符型，20位，非必填）
    @JSONField(name = "shelf_posi")
    private String shelfPosi;

    // 34. 销售/退货数量（数值型，16位含4位小数，必填）
    @JSONField(name = "sel_retn_cnt")
    private BigDecimal selRetnCnt;

    // 35. 销售/退货时间（日期时间型，必填，格式：yyyy-MM-dd HH:mm:ss）
    @JSONField(name = "sel_retn_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date selRetnTime;

    // 36. 销售/退货经办人姓名（字符型，50位，必填）
    @JSONField(name = "sel_retn_opter_name")
    private String selRetnOpterName;

    // 37. 备注（字符型，500位，非必填）
    @JSONField(name = "memo")
    private String memo;

    // 38. 就诊结算类型（字符型，6位，非必填）
    @JSONField(name = "mdtrt_setl_type")
    private String mdtrtSetlType;

    // 39. 溯源码节点信息（字符型，长度未明确，暂定500位）
    @JSONField(name = "drugtracinfo")
    private String drugtracinfo;
}
