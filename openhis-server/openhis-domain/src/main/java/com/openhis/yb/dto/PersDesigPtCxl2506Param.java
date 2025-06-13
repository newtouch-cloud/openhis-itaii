package com.openhis.yb.dto;

import com.alibaba.fastjson2.annotation.JSONField;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【2506】人员定点备案撤销（输入）
 *
 * @author gaoyy
 * @date 2025-05-08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class PersDesigPtCxl2506Param {
    // 1. 待遇申报明细流水号
    @JSONField(name = "trt_dcla_detl_sn")
    private String trtDclaDetlSn;

    // 2. 人员编号
    @JSONField(name = "psn_no")
    private String psnNo;

    // 3. 备注（填写撤销原因）
    @JSONField(name = "memo")
    private String memo;
}
