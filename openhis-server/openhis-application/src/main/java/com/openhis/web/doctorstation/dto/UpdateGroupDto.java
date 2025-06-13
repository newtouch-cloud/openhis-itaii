package com.openhis.web.doctorstation.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 更新组号 dto
 */
@Data
@Accessors(chain = true)
public class UpdateGroupDto {

    /**
     * 请求id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long requestId;

    /**
     * 分组id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long groupId;

}
