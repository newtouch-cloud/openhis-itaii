/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.chargemanage.dto;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 就诊患者分页查询条件
 *
 * @author zwh
 * @date 2025-03-12
 */
@Data
@Accessors(chain = true)
public class EncounterPatientPageParam {

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 患者院内编码/病历号
     */
    private String patientBusNo;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 拼音码
     */
    private String patientPyStr;

    /**
     * 五笔码
     */
    private String patientWbStr;

    /**
     * 就诊编码
     */
    private String encounterBusNo;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 收费时间
     */
    private Date billTime;

    /**
     * 收费状态
     */
    private Integer statusEnum;
}
