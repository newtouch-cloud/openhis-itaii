/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.reportmanage.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 费用明细查询条件
 *
 * @author yuxj
 * @date 2025-05-20
 */
@Data
@Accessors(chain = true)
public class ChargeReportSearchParam {

    /**
     * 统计类型
     */
    private String statisticsType;
    /**
     * 医保号
     */
    private String ybCode;
    /**
     * 门诊号
     */
    private String busNo;
    /**
     * 姓名
     */
    private String name;

    /**
     * 科室id
     */
    private Long departmentId;

    /**
     * 开单人id
     */
    private Long issuerId;
    /**
     * 收款人id
     */
    private Long payeeId;

    /**
     * 项目类型
     */
    private Integer clinicalType;
    /**
     * 项目名
     */
    private String clinicalName;
}
