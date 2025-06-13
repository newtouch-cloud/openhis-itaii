/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import com.alibaba.fastjson2.annotation.JSONField;

import java.util.Date;
import java.util.List;

/**
 * 【3401A】科室信息上传(批量)
 *
 * @author SunJQ
 * @date 2025-04-28
 */
public class HospDept3401AParam {
    private List<HospDept3401Param> deptinfo;
}
