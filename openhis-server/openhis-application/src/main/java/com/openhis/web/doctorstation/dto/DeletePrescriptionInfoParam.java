package com.openhis.web.doctorstation.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 电子处方 dto
 */
@Data
@Accessors(chain = true)
public class DeletePrescriptionInfoParam {

    /** ID */
    private List<Integer> idList;

    /** 医院内部处方编号*/
    private List<String> prescriptionNoList;

}
