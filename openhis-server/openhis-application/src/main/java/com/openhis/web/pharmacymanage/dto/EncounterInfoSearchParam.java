/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.pharmacymanage.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 处方列表查询条件
 *
 * @author wangyang
 * @date 2025-03-14
 */
@Data
@Accessors(chain = true)
public class EncounterInfoSearchParam implements Serializable {

    /** 科室 */
    private Long departmentId;

    /** 患者姓名或者证件号 */
    private String condition;

    /** 患者姓名 */
    private String patientName;

    /** 证件号 */
    private String idCard;
}
