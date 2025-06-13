package com.openhis.yb.dto;

import com.core.common.core.domain.model.LoginUser;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BaseParam {

    Object data;

    BaseInfo baseInfo;
}
