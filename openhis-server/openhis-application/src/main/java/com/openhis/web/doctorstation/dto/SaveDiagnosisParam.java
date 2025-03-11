package com.openhis.web.doctorstation.dto;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 保存诊断 主参数类
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@Accessors(chain = true)
public class SaveDiagnosisParam {

    /**
     * 患者id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long patientId;

    /**
     * 就诊ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long encounterId;

    /**
     * 诊断子集合
     */
    private List<SaveDiagnosisChildParam> diagnosisChildList;


}
