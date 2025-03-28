package com.openhis.web.datadictionary.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.openhis.common.annotation.Dict;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 从目录添加定价表信息用的Dto
 *
 * @author liuhr
 * @date 2025/3/28
 */
@Data
@Accessors(chain = true)
public class ItemUpFromDirectoryDto {

    /** 药品单位 */
    private String unitCode;

    /** 购入价 */
    private BigDecimal purchasePrice;

    /** 零售价 */
    private BigDecimal retailPrice;

    /** 最高零售价 */
    private BigDecimal maximumRetailPrice;

    /** 名称 */
    private String chargeName;

    /** 状态 */
    private Integer statusEnum;

    /** 机构ID */
    private Long orgId;

    /** 代码 */
    private String instanceTable;

    /** 关联项目 */
    private Long instanceId;

    /** 有效时间开始 */
    private Date effectiveStart;

    /** 财务类别 */
    private String typeCode;

    /** 医保类别 */
    private String ybType;

    /** 是否使用详细价格规则 */
    private Integer conditionFlag;

    /** 基础价格 */
    private BigDecimal price;

    /** 费用定价主键ID */
    private Long definitionId;

    /** 条件 */
    private String conditionCode;

    /** 命中值 */
    private String conditionValue;

    /** 价格 */
    private BigDecimal amount;

}
