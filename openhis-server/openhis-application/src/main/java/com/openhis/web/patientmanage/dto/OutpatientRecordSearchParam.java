package com.openhis.web.patientmanage.dto;

import java.util.Date;

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

    /** 身份证号/病人ID/门诊号/病人姓名 */
    private String searchKey;

    /** 手机号码 */
    private String phone;

    /** 医生姓名 */
    private String doctorName;

    /** 筛选开始时间 */
    private Date beginTime;

    /** 筛选结束时间 */
    private Date endTime;

}
