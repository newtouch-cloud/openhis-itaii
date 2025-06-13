/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import com.alibaba.fastjson2.annotation.JSONField;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 药品追溯码dto
 *
 * @author zwh
 * @date 2025-05-28
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MedicalTraceNoDto {

    // 药品追溯码（字符型，100）
    @JSONField(name = "drug_trac_codg")
    private String drugTracCodg;
}
