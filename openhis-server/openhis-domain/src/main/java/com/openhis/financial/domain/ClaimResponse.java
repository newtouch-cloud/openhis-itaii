package com.openhis.financial.domain;

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
 * 索赔响应管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("fin_claim_response")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ClaimResponse extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 交易号 */
    private Long traceNumber;

    /** 状态 */
    private Integer statusEnum;

    /** 类别 */
    private String typeCode;

    /** （详细）索赔类别 */
    private String subtypeCode;

    /** 用途 */
    private Integer useEnum;

    /** 索赔指向 */
    private Integer insurer;

    /** 最终结果 */
    private Integer outcomeEnum;

    /** 索赔结果的额外文本信息 */
    private String disposition;

    /** 索赔结果 */
    private Integer claimResult;

    /** 索赔金额 */
    private BigDecimal total;

    /** 删除状态 */
    private Integer deleteFlag;

}