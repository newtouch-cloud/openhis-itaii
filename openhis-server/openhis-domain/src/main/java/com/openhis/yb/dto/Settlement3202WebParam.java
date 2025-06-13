/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 【3202】前台入参
 *
 * @author SunJQ
 * @date 2025-04-17
 */
@Data
public class Settlement3202WebParam {
    /** 结算经办机构 */
    private String setlOptins;//前台传入,注意:先上传文件后核对明细的
    /** 清算类别 */
    private String clrType;
    /** 文件查询号 */
    private String fileQuryNo;
    /** 开始时间 */
    //@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String stmtBegnDate;
    /** 结束时间 */
    //@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String stmtEndDate;
    /** 医院id */
    private Long orgId;
}
