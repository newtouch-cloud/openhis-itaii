package com.openhis.administration.domain;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.core.domain.HisBaseEntity;
import com.openhis.common.enums.ActPriority;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 费用定价管理子Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("adm_charge_item_def_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ChargeItemDefDetail extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 费用定价主键ID */
    private Long definitionId;

    /** 条件规则 */
    private Long conditionRuleId;

    /** 条件 */
    private String conditionCode;

    /** 命中值 */
    private String conditionValue;

    /** 优先级 */
    private Integer priority;

    /** 价格 */
    private BigDecimal amount;

    public ChargeItemDefDetail() {
        // 默认优先级：常规
        this.priority = ActPriority.ROUTINE.getValue();
    }
}