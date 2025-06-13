package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 【25002】人员特药备案查询（输出）
 *
 * @author gaoyy
 * @date 2025-05-09
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class PersMedSelect25002Result {

    // 1. 待遇申报明细流水号
    @JSONField(name = "trt_dcla_detl_sn")
    private String trtDclaDetlSn;

    // 2. 人员编号
    @JSONField(name = "psn_no")
    private String psnNo;

    // 3. 险种类型
    @JSONField(name = "insutype")
    private String insutype;

    // 4. 参保地医保区划
    @JSONField(name = "insu_admdv")
    private String insuAdmdv;

    // 5. 定点医药机构编号
    @JSONField(name = "fixmedins_code")
    private String fixmedinsCode;

    // 6. 定点医药机构名称
    @JSONField(name = "fixmedins_name")
    private String fixmedinsName;

    // 7. 目录类别
    @JSONField(name = "list_type")
    private String listType;

    // 8. 医保目录编码
    @JSONField(name = "hilist_code")
    private String hilistCode;

    // 9. 医保目录名称
    @JSONField(name = "hilist_name")
    private String hilistName;

    // 10. 人员姓名
    @JSONField(name = "psn_name")
    private String psnName;

    // 11. 开始时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(name = "begndate")
    private Date begndate;

    // 12. 终止时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(name = "enddate")
    private Date enddate;

    // 13. 经办人id
    @JSONField(name = "opter_id")
    private String opterId;

    // 14. 经办人姓名
    @JSONField(name = "opter_name")
    private String opterName;

    // 15. 经办时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(name = "opt_time")
    private Date optTime;

}
