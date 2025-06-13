package com.openhis.web.doctorstation.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 就诊费用性质 dto
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@Accessors(chain = true)
public class EncounterContractDto {

    /** 关联账户ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long accountId;

    /** 合同名称 */
    private String contractName;

    /** 合同编码 */
    private String busNo;
}
