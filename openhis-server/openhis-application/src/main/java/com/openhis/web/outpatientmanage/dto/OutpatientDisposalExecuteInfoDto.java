/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.outpatientmanage.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.openhis.common.annotation.Dict;

import liquibase.pro.packaged.S;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 执行列表
 *
 * @author yuxj
 * @date 2025-04-11
 */
@Data
@Accessors(chain = true)
public class OutpatientDisposalExecuteInfoDto {

    /** 类型*/
    private Integer type;

    /** 服务申请Id */
    private Long serviceRequestId;

    /** 器材发放Id */
    private Long deviceDispenseId;

    /** 器材请求id */
    private Long deviceRequestId;

    /** 器材id */
    private Long deviceId;

    /** 服务请求编码 */
    private String busNo;

    /** 执行人 */
    private String performerName;

    /** 执行科室 */
    private String locationName;

    /** 耗材名 */
    private String deviceName;

    /** 耗材数量 */
    private Integer quantity;

    /** 执行时间 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date occurrenceTime;

    /** 产品批号 */
    private String lotNumber;

    /** 单位 */
    private String unitCode;

    /** 拆零比 */
    private BigDecimal partPercent;

    /** 仓库 */
    private Long locationId;
}
