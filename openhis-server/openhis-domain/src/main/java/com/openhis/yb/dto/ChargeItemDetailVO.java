package com.openhis.yb.dto;

import com.openhis.administration.domain.ChargeItem;
import com.openhis.common.annotation.Dict;
import lombok.Data;

@Data
public class ChargeItemDetailVO extends ChargeItem {

    @Dict(dictCode = "chrgitm_lv")
    private String dirClass;//医保等级

}
