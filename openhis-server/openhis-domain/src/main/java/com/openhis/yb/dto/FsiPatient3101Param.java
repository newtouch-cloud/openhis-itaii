/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import java.util.Date;
import java.util.List;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 【3101】明细审核事前分析服务（输入-参保人信息）
 * 【3102】明细审核事中分析服务（输入-参保人信息）
 *
 * @author SunJQ
 * @date 2025-04-15
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FsiPatient3101Param {

    // 1. 参保人标识
    @JSONField(name = "patn_id")
    private String patnId;

    // 2. 姓名
    @JSONField(name = "patn_name")
    private String patnName;

    // 3. 性别
    @JSONField(name = "gend")
    private String gend;

    // 4. 出生日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "brdy")
    private Date brdy;

    // 5. 统筹区编码
    @JSONField(name = "poolarea")
    private String poolarea;

    // 6. 当前就诊标识
    @JSONField(name = "curr_mdtrt_id")
    private String currMdtrtId;

    // 7. 就诊信息集合
    @JSONField(name = "fsi_encounter_dtos")
    private List<FsiEncounter3101Param> fsiEncounterDtos;

//    // 8. 医院信息集合（非必填项）
//    @JSONField(name = "fsi_his_data_dto")
//    private FsiHisData fsiHisDataDto;
}
