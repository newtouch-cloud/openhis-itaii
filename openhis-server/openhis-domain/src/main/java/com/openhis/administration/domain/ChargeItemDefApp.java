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
@TableName("adm_charge_item_def_app")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ChargeItemDefApp extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 费用定价主键ID */
    private Long definitionId;

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

    public ChargeItemDefApp() {
        // 默认优先级：常规
        this.priority = ActPriority.ROUTINE.getValue();
    }
}