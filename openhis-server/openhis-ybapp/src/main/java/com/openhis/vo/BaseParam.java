package com.openhis.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BaseParam {

    Object data;

    BaseInfo baseInfo;
}
