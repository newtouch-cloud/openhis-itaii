package com.openhis.web.chargemanage.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 门诊科室信息 元数据
 */
@Data
@Accessors(chain = true)
public class OrgMetadata {

    /**
     * ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 科室名称
     */
    private String name;


}
