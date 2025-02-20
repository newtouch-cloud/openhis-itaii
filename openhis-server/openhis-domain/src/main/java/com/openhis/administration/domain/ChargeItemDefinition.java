package com.openhis.administration.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.core.common.core.domain.HisBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 费用定价管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("adm_charge_item_definition")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ChargeItemDefinition extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 名称 */
    private String chargeName;

    /** 标题 */
    private String title;

    /** 状态 */
    private Integer statusEnum;

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

    /** 删除状态 */
    private Integer deleteFlag;

}