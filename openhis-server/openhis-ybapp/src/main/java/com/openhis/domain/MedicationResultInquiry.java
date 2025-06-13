package com.openhis.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 电子处方取药结果查询
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MedicationResultInquiry {

    /** 医保处方编号 */
    private String hiRxno;

    /** 医保结算时间(yyyy-MM-dd HH:mm:ss) */
    private String setlTime;

    /** 医保处方状态编码(参考rx_stas_codg) */
    private String rxStasCodg;

    /** 医保处方状态名称 */
    private String rxStasName;

    /** 处方使用状态编号(参考rx_used_stas_codg) */
    private String rxUsedStasCodg;

    /** 处方使用状态名称 */
    private String rxUsedStasName;

    // 输出明细信息
    private List<ElepMedresultDetail> seltdelts;

}
