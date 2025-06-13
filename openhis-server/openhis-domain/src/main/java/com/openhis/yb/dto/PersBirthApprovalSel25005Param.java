package com.openhis.yb.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【25005】人员生育审批查询
 *
 * @author gaoyy
 * @date 2025-05-09
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class PersBirthApprovalSel25005Param {
    // 1. 人员编号（既是输入参数也是输出参数）
    @JSONField(name = "psn_no")
    private String psnNo;

    // --- 输出参数 ---

    // 1. 待遇申报明细流水号（字符型，30位，必填）
    @JSONField(name = "trtDclaDetlSn")
    private String trtDclaDetlSn;

    // 2. 险种类型（字符型，6位，必填）
    @JSONField(name = "insutype")
    private String insutype;

    // 3. 人员证件号码（字符型，30位，必填）
    @JSONField(name = "certno")
    private String certno;

    // 4. 审批定点医药机构编号（字符型，30位，必填）
    @JSONField(name = "fixmedinscode")
    private String fixmedinscode;

    // 5. 审批定点医药机构名称（字符型，200位，必填）
    @JSONField(name = "fixmedinsname")
    private String fixmedinsname;

    // 6. 人员姓名（字符型，50位，必填）
    @JSONField(name = "psn_name")
    private String psnName;

    // 7. 开始时间（日期型，必填）
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(name = "begndate")
    private Date begndate;

    // 8. 终止时间（日期型，必填）
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(name = "enddate")
    private Date enddate;

}
