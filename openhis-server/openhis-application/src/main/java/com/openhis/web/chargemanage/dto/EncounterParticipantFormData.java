package com.openhis.web.chargemanage.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.enums.ParticipantType;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 就诊参数者 表单数据
 */
@Data
@Accessors(chain = true)
public class EncounterParticipantFormData {

    /**
     * 就诊ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long encounterId;

    /** 参与者类型 */
    private String typeCode;

    /** 参与者ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long practitionerId;

    /**
     * 设置默认值
     */
    public EncounterParticipantFormData() {
        this.typeCode = ParticipantType.REGISTRATION_DOCTOR.getCode();// 挂号医生
    }

}
