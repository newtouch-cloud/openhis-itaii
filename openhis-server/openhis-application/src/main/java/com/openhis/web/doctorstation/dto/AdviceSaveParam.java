package com.openhis.web.doctorstation.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 医嘱保存参数类
 */
@Data
@Accessors(chain = true)
public class AdviceSaveParam {

    /**
     * 保存医嘱 dto
     */
    private List<AdviceSaveDto> adviceSaveList;

}
