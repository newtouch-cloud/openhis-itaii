package com.openhis.web.patientmanage.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 病人信息查询参数Dto
 *
 * @author liuhr
 * @date 2025/3/31
 */
@Data
@Accessors(chain = true)
public class PatientInfoSearchParam {

    /** 病人编号 */
    private String busNo;
}
