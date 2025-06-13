package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 【25008】生育津贴登记信息查询（输出）
 *
 * @author gaoyy
 * @date 2025-05-09
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MatAllowRegSel25008Param {
    // 1. 生育津贴登记ID
    @JSONField(name = "matn_alwn_reg_id")
    private String matnAlwnRegId;

    // 2. 人员编号
    @JSONField(name = "psn_no")
    private String psnNo;

    // 3. 人员姓名
    @JSONField(name = "psn_name")
    private String psnName;

    // 4. 生育类别
    @JSONField(name = "matn_type")
    private String matnType;

    // 5. 计划生育手术或生育日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "birctrl_matn_date")
    private Date birctrlMatnDate;

    // 6. 孕周数
    @JSONField(name = "geso_val")
    private BigDecimal gesoVal;

    // 7. 胎次
    @JSONField(name = "fetts")
    private BigDecimal fetts;

    // 8. 胎儿数
    @JSONField(name = "fetus_cnt")
    private BigDecimal fetusCnt;

    // 9. 结算ID
    @JSONField(name = "setl_id")
    private String setlId;

    // 10. 有效标志
    @JSONField(name = "vali_flag")
    private String valiFlag;

    // 11. 审核标志
    @JSONField(name = "rchk_flag")
    private String rchkFlag;

    // 12. 审核意见
    @JSONField(name = "memo")
    private String memo;

    // 13. 经办人姓名
    @JSONField(name = "opter_name")
    private String opterName;

    // 14. 经办时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(name = "opt_time")
    private Date optTime;
}
