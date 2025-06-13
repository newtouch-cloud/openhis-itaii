package com.openhis.web.patientmanage.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 门诊记录查询参数
 *
 * @author liuhr
 * @date 2025/2/28
 */
@Data
@Accessors(chain = true)
public class OutpatientRecordSearchParam {

    /** 手机号码 */
    private String phone;

    /** 医生姓名 */
    private String doctorName;

}
