/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;

import java.util.Date;

/**
 * 【3403】科室信息撤销
 *
 * @author SunJQ
 * @date 2025-04-28
 */
public class HospDept3403Param {
    // 1. 医院科室编码（院内唯一编码）
    @JSONField(name = "hosp_dept_codg")
    private String hospDeptCodg;

    // 2. 医院科室名称
    @JSONField(name = "hosp_dept_name")
    private String hospDeptName;

    // 3. 开始时间（日期时间格式）
    @JSONField(name = "begntime", format = "yyyy-MM-dd HH:mm:ss")
    private Date begnTime;
}
