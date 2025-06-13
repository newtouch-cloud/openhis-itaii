package com.openhis.web.chargemanage.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.enums.EncounterLocationStatus;
import com.openhis.common.enums.LocationForm;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 就诊位置 表单数据
 */
@Data
@Accessors(chain = true)
public class EncounterLocationFormData {

    /**
     * 就诊ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long encounterId;

    /**
     * 位置ID
     */
    private Long locationId;

    /** 状态枚举 */
    private Integer statusEnum;

    /** 物理形式枚举 */
    private Integer formEnum;

    /**
     * 设置默认值
     */
    public EncounterLocationFormData() {
        this.statusEnum = EncounterLocationStatus.PLANNED.getValue();
        this.formEnum = LocationForm.ROOM.getValue();
    }

}
