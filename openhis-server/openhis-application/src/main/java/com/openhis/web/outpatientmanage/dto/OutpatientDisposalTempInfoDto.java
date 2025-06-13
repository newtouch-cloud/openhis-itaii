/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.outpatientmanage.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 列表
 *
 * @author yuxj
 * @date 2025-04-11
 */
@Data
@Accessors(chain = true)
public class OutpatientDisposalTempInfoDto {

    /** id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 类型 */
    private Integer type;

    /** 申请状态 */
    private Integer statusEnum;
    private String statusEnum_enumText;

    /** 编码 */
    private String busNo;

    /** 诊疗id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long activityId;

    /** 产品批号 */
    private String lotNumber;

    /** 单位 */
    private String unitCode;

    /** 拆零比 */
    private BigDecimal partPercent;

    /** 仓库 */
    private Long locationId;

    /** 分组编号 */
    private String groupNo;

    /** 项目名称 */
    private String itemName;

    /** 单价 */
    private BigDecimal unitPrice;

    /** 总价 */
    private BigDecimal totalPrice;

    /** 数量 */
    private Integer quantity;

    /** 次数 */
    private Integer frequency;

    /** 收费状态 */
    private Integer chargeStatusEnum;
    private String chargeStatusEnum_enumText;

    /** 单位 */
    @Dict(dictCode = "unit_code")
    private String quantityUnit;
    private String quantityUnit_dictText;

    /** 诊疗耗材信息 */
    /** 器材请求id */
    private Long deviceRequestId;

    /** 器材发放Id */
    private Long deviceDispenseId;

    /** 编码 */
    private String deviceBusNo;

    /** 耗材id */
    private Long deviceId;

    /** 产品批号 */
    private String deviceLotNumber;

    /** 单位 */
    private String deviceUnitCode;

    /** 拆零比 */
    private BigDecimal devicePartPercent;

    /** 仓库 */
    private Long deviceLocationId;

    /** 分组编号 */
    private String deviceGroupNo;

    /** 项目名称 */
    private String deviceItemName;

    /** 单价 */
    private BigDecimal deviceUnitPrice;

    /** 总价 */
    private BigDecimal deviceTotalPrice;

    /** 数量 */
    private Integer deviceQuantity;

    /** 次数 */
    private Integer deviceFrequency;

    /** 收费状态 */
    private Integer deviceStatusEnum;
    private String deviceStatusEnum_enumText;

    /** 单位 */
    @Dict(dictCode = "unit_code")
    private String deviceQuantityUnit;
    private String deviceQuantityUnit_dictText;
}
