/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.ybmanage.dto;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 医保电子处方信息查询条件
 *
 * @author yuxj
 * @date 2025-05-06
 */
@Data
@Accessors(chain = true)
public class VeriPrescriptionParam {

    /** 门诊号/姓名 */
    private String name;
}
