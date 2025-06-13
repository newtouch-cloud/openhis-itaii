package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【2502】转院备案撤销（输入）
 *
 * @author gaoyy
 * @date 2025-05-08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)

public class HospTrfCxl2502Param {
    // 1. 待遇申报明细流水号
    @JSONField(name = "trt_dcla_detl_sn")
    private String trtDclaDetlSn;

    // 2. 人员编号
    @JSONField(name = "psn_no")
    private String psnNo;

    // 3. 备注
    @JSONField(name = "memo")
    private String memo;
}
