package com.openhis.web.doctorstation.dto;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.enums.Whether;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 医嘱保存参数类
 */
@Data
@Accessors(chain = true)
public class AdviceSaveParam {

    /**
     * 患者挂号对应的科室id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long organizationId;

    /**
     * 代煎标识 | 0:否 , 1:是
     */
    private Integer sufferingFlag;

    /**
     * 保存医嘱 dto
     */
    private List<AdviceSaveDto> adviceSaveList;

    public AdviceSaveParam() {
        this.sufferingFlag = Whether.NO.getValue();
    }

}
