/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.paymentmanage.dto;

import java.math.BigDecimal;

import com.openhis.common.annotation.Dict;

import com.openhis.common.enums.InvoiceStatus;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *  返回信息
 *
 * @author yuxj
 * @date 2025-04-22
 */
@Data
@Accessors(chain = true)
public class EleInvoiceResultDto {

    private Long id;

    /** 患者ID */
    private Long patientId;

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

    /** 账单批次号 */
    private String batchCode;

    /** 结算批次号 */
    private String qrCode;

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
}
