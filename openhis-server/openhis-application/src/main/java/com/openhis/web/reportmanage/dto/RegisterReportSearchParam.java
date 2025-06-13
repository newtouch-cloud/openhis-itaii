/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 挂号明细查询条件
 *
 * @author yuxj
 * @date 2025-05-20
 */
@Data
@Accessors(chain = true)
public class RegisterReportSearchParam {

    /** 科室id */
    private Long departmentId;

    /** 医生id */
    private Long doctorId;
}
