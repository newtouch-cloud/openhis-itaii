package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Sign9001Result {
    /**签到编号*/
    @JSONField(name="signNo")
    private String signNo;

    /**操作员编号*/
    @JSONField(name="opterNo")
    private String opterNo;

    @JSONField(name="signTime")
    private java.util.Date signTime;

    @JSONField(name="status")
    private String status;

}
