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
 * 发票管理Entity实体
 *
 * @author system
 * @date 2025-02-20
 */
@Data
@TableName("adm_invoice")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Invoice extends HisBaseEntity {

    /** ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 患者ID */
    private Long patientId;

    /** 状态 */
    private Integer statusEnum;

    /** 类别 */
    private String typeCode;

    /** 取消原因 */
    private String cancelledReason;

    /** 收费项 */
    private String chargeItemIds;

    /** 发票净额总记 */
    private BigDecimal totalNet;

    /** 发票总计金额 */
    private BigDecimal totalGross;

    /** 付款详情 */
    private String paymentTerms;

    /** 账单批次号 */
    private String batchCode;

    /** 结算批次号 */
    private String qrCode;

    /** 图片网络路径 */
    private String pictureNetUrl;

    /** 图片路径 */
    private String pictureUrl;

    /** 删除状态 */
    private Integer deleteFlag;

}