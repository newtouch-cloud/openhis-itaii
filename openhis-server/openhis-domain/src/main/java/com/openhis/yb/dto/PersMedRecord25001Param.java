package com.openhis.yb.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【25001】人员特药备案
 *
 * @author gaoyy
 * @date 2025-05-08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class PersMedRecord25001Param {

    // 1. 人员编号
    @JSONField(name = "psn_no")
    private String psnNo;

    // 2. 险种类型
    @JSONField(name = "insutype")
    private String insutype;

    // 3. 参保机构医保区划
    @JSONField(name = "insu_admdv")
    private String insuAdmdv;

    // 4. 定点医药机构编号
    @JSONField(name = "fixmedins_code")
    private String fixmedinsCode;

    // 5. 定点医药机构名称
    @JSONField(name = "fixmedins_name")
    private String fixmedinsName;

    // 6. 目录类别
    @JSONField(name = "list_type")
    private String listType;

    // 7. 医保目录编码
    @JSONField(name = "hilist_code")
    private String hilistCode;

    // 8. 医保目录名称
    @JSONField(name = "hilist_name")
    private String hilistName;

    // 9. 数量
    @JSONField(name = "cnt")
    private BigDecimal cnt;

    // 10. 数量单位
    @JSONField(name = "cnt_prcunt")
    private String cntPrcunt;

    // 11. 开始日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "begndate")
    private Date begndate;

    // 12. 结束日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "enddate")
    private Date enddate;

    // 13. 诊断代码
    @JSONField(name = "diag_code")
    private String diagCode;

    // 14. 诊断名称
    @JSONField(name = "diag_name")
    private String diagName;

}
