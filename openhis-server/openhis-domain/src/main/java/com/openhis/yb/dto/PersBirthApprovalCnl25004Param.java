package com.openhis.yb.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【25004】人员生育审批撤销
 *
 * @author gaoyy
 * @date 2025-05-09
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class PersBirthApprovalCnl25004Param {

    // 1. 人员编号
    @JSONField(name = "psn_no")
    private String psnNo;

    // 2. 待遇申报明细流水号
    @JSONField(name = "trt_dcla_detl_sn")
    private String trtDclaDetlSn;

}
