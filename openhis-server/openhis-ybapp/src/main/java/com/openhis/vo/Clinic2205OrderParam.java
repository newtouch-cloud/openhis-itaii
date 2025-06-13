/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 就诊明细撤销
 *
 * @author SunJQ
 * @date 2025-05-07
 */
@Data
@Accessors(chain = true)
public class Clinic2205OrderParam {

    //就诊ID
    @JSONField(name="mdtrt_id")
    private String mdtrtId;

    //收费批次号
    @JSONField(name="chrg_bchno")
    private String chrgBchno;

    //人员编号
    @JSONField(name="psn_no")
    private String psnNo;

    //人员编号
    @JSONField(name="exp_content")
    private String expContent;
}
