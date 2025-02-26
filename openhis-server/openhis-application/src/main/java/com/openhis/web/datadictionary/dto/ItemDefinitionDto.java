/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.datadictionary.dto;

import com.openhis.common.enums.PublicationStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 收费项目保存dto
 *
 * @author zxy
 * @date 2025-02-21
 */
@Data
@Accessors(chain = true)
public class ItemDefinitionDto {

    /** ID */
    private Long id;

    /** 子表ID */
    private Long itemId;

    /** 名称 */
    private String chargeName;

    /** 标题 */
    private String title;

    /** 状态 */
    private PublicationStatus statusEnum;

    /** 机构编码 */
    private String orgCode;

    /** 描述 */
    private String description;

    /** 代码 */
    private String instanceTable;

    /** 关联项目 */
    private Long instanceId;

    /** 有效时间开始 */
    private Date effectiveStart;

    /** 有效时间结束 */
    private Date effectiveEnd;

    /** 财务类别 */
    private String typeCode;

    /** 医保类别 */
    private Integer ybType;

    /** 是否使用详细价格规则 */
    private Integer conditionFlag;

    /** 基础价格 */
    private BigDecimal price;

    /** 条件规则 */
    private Long conditionRuleId;

    /** 批次号 */
    private String conditionLotnumber;

    /** 医保相关价格 */
    private String conditionYbCode;

    /** 采购售卖条件 */
    private String conditionInoutCode;

    /** 条件类型 */
    private String conditionUnitCode;

    /** 条件 */
    private String conditionCode;

    /** 优先级 */
    private Integer priority;

    /** 价格 */
    private BigDecimal amount;

    /** 名称拼音码 */
    private String pyCode;

    /** 类型 */
    private String typeEnum;
}
