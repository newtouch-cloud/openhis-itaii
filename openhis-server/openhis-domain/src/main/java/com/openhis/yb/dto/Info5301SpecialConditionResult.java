package com.openhis.yb.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Info5301SpecialConditionResult {

    private static final long serialVersionUID = 1L;

    //门慢门特病种目录代码
    private String opspDiseCode;
    //门慢门特病种名称
    private String opspDiseName;
    //开始日期
    private String begndate;
    //结束日期
    private String enddate;
}
