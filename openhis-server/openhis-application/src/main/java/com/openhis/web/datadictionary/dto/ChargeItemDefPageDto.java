/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.datadictionary.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.openhis.common.enums.PublicationStatus;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 费用定价分页Dto
 *
 * @author zxy
 * @date 2025-02-21
 */
@Data
@Accessors(chain = true)
public class ChargeItemDefPageDto {

    /** ID */
    private Long id;

    /** 费用定价子表主键 */
    private Long itemId;

    /** 名称 */
    private String chargeName;

    /** 项目编号 */
    private String itemNo;

    /** 规格 */
    private String totalVolume;

    /** 状态 */
    private PublicationStatus statusEnum;

    /** 单位 */
    private String unitCode;

    /** 拆零比 */
    private BigDecimal partPercent;

    /** 指导价 */
    private BigDecimal conditionYbCode;

    /** 批次号 */
    private String conditionLotnumber;

    /** 价格 */
    private BigDecimal price;

    /** 有效时间开始 */
    private Date effectiveStart;

    /** 有效时间结束 */
    private Date effectiveEnd;

    /** 拆零最小单位  */
    private String partMinUnitCode;

    /** 拆零指导价 */
    private BigDecimal partConditionPrice;

    /** 拆零价格 */
    private BigDecimal partPrice;

    /** 条件价格 */
    private BigDecimal amount;

    /** 调价说明  */
    private String description;

    /** 优先级  */
    private Integer priority;

    /** 条件规则  */
    private Integer conditionRuleId;

    /** 创建时间  */
    private Date createTime;

    /** 调价时间  */
    private Date updateTime;

    /** 总条数  */
    private Integer totalCount;

    /** 拼音助记码 */
    private String pyStr;

    /** 收费项目 */
    private Integer categoryCode;

    /** 编码 */
    private String instanceTable;

    /** 定价类型 */
    private String definitionType;

    /** 定价项目 */
    private Integer chargeItem;
}
