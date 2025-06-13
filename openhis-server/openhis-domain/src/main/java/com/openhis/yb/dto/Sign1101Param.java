package com.openhis.yb.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Sign1101Param {
    /**操作员编号*/
    @JSONField(name="opter_no")
    private String opterNo;
    /** 签到MAC地址 */
    @JSONField(name="mac")
    private String mac;
    /** 签到IP地址 */
    @JSONField(name="ip")
    private String ip;
}
