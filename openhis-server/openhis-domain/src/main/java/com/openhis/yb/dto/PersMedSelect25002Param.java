package com.openhis.yb.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【25002】人员特药备案查询（输入）
 *
 * @author gaoyy
 * @date 2025-05-09
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class PersMedSelect25002Param {

    // 1. 人员编号
    @JSONField(name = "psn_no")
    private String psnNo;

    // 2. 险种类型
    @JSONField(name = "insutype")
    private String insutype;

    // 3. 用作比较的时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(name = "compare_date")
    private Date compareDate;

}
