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
 * 合同管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("fin_contract")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Contract extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 合同名称 */
    private String contractName;

    /** 状态 */
    private Integer statusEnum;

    /** 合同的类别 */
    private Integer categoryEnum;

    /** 合同编码 */
    private String code;

    /** 机构 */
    private Long orgId;

    /** 删除状态 */
    private Integer deleteFlag;

}