package com.openhis.web.paymentmanage.dto;

import com.openhis.yb.dto.Clinic2207OrderResult;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Clinic2207OrderResultDto extends Clinic2207OrderResult {

    private Long accountId;

}
