/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 【3101】明细审核事前分析服务（输入-手术操作信息）
 * 【3102】明细审核事中分析服务（输入-手术操作信息）
 *
 * @author SunJQ
 * @date 2025-04-15
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FsiOperation3101Param {

    // 1. 手术操作ID
    @JSONField(name = "setl_list_oprn_id")
    private String setlListOprnId;

    // 2. 手术操作代码
    @JSONField(name = "oprn_code")
    private String oprnCode;

    // 3. 手术操作名称
    @JSONField(name = "oprn_name")
    private String oprnName;

    // 4. 主手术操作标志
    @JSONField(name = "main_oprn_flag")
    private String mainOprnFlag;

    // 5. 手术操作日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "oprn_date")
    private Date oprnDate;

    // 6. 麻醉方式
    @JSONField(name = "anst_way")
    private String anstWay;

    // 7. 术者医师姓名
    @JSONField(name = "oper_dr_name")
    private String operDrName;

    // 8. 术者医师代码
    @JSONField(name = "oper_dr_code")
    private String operDrCode;

    // 9. 麻醉医师姓名
    @JSONField(name = "anst_dr_name")
    private String anstDrName;

    // 10. 麻醉医师代码
    @JSONField(name = "anst_dr_code")
    private String anstDrCode;
}
