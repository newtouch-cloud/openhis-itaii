package com.openhis.web.doctorstation.dto;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 医嘱基础 dto
 */
@Data
@Accessors(chain = true)
public class AdviceBaseDto {

    /** 医嘱类型 */
    private Integer adviceType; // 1:药品 , 2: 耗材 , 3:项目

    /** 医嘱定义ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long adviceDefinitionId;

    /** 医嘱名称 */
    private String adviceName;

    /**
     * 医嘱编码
     */
    private String adviceBusNo;

    /** 拼音码 */
    private String pyStr;

    /** 五笔码 */
    private String wbStr;

    /** 医保编码 */
    private String ybNo;

    /** 商品名称 */
    private String productName;

    /**
     * 活动类型
     */
    private Integer activityType;
    private String activityType_enumText;

    /** 包装单位 */
    @Dict(dictCode = "unit_code")
    private String unitCode;
    private String unitCode_dictText;

    /** 最小单位 */
    @Dict(dictCode = "unit_code")
    private String minUnitCode;
    private String minUnitCode_dictText;

    /**
     * 规格
     */
    private String volume;

    /**
     * 用法
     */
    @Dict(dictCode = "method_code")
    private String methodCode;
    private String methodCode_dictText;

    /**
     * 使用频次
     */
    @Dict(dictCode = "rate_code")
    private String rateCode;
    private String rateCode_dictText;

    /**
     * 单次剂量
     */
    private String dose;

    /** 剂量单位 */
    @Dict(dictCode = "unit_code")
    private String doseUnitCode;
    private String doseUnitCode_dictText;

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /** 费用定价主表ID */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long chargeItemDefinitionId;

    /**
     * 医嘱对应表名
     */
    private String adviceTableName;

    /** 所属科室 */
    private Long orgId;

    /** 所在位置 */
    private Long locationId;

    /**
     * 医嘱库存集合
     */
    private List<AdviceInventoryDto> inventoryList;

    /**
     * 医嘱价格集合
     */
    private List<AdvicePriceDto> priceList;

}
