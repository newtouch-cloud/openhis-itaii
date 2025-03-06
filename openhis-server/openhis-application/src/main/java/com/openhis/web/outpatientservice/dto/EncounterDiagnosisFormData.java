package com.openhis.web.outpatientservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 就诊诊断 表单数据
 */
@Data
@Accessors(chain = true)
public class EncounterDiagnosisFormData {

    /**
     * 就诊ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long encounterId;

    /**
     * 诊断ID
     */
    @NotBlank(message = "诊断ID不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long conditionId;

}
