package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【2404】入院撤销
 * 【2405】出院撤销
 *
 * @author yuanzs
 * @date 2025-05-08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class HospAdm2404AdmCnlParam {

    // 1. 就诊ID
    @JSONField(name = "mdtrt_id")
    private String mdtrtId;

    // 2. 人员编号
    @JSONField(name = "psn_no")
    private String psnNo;

    // 3. 字段扩展
    @JSONField(name = "exp_content")
    private String expContent;

}
