package com.openhis.administration.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.core.common.core.domain.HisBaseEntity;
import com.openhis.common.enums.InvoiceStatus;
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

    /** 付款ID */
    private Long reconciliationId;

    /** 状态 */
    private InvoiceStatus statusEnum;

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

    /** 发票编号 */
    private String busNo;

    /** 开票员 */
    private Long invoicingStaffId;
    /** 电子票据代码 */
    private String billBatchCode;
    /** 电子票据号码 */
    private String billNo;
    /** 电子校验码 */
    private String random;
    /** 电子票据生成时间 */
    private String billCreateTime;
    /** 电子票据二维码图片数据 */
    private String billQrCode;
    /** 电子票据H5页面URL */
    private String pictureUrl;
    /** 电子票据外网H5页面URL */
    private String pictureNetUrl;
    /** 微信插卡URL */
    private String wxCardUrl;
    /** 票据营业日期 */
    private Date billBusDate;
}
