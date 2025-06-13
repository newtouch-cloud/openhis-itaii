package com.openhis.web.pharmacymanage.dto;

import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 待发药明细分页列表 dto
 *
 * @author yuanzs
 * @date 2025-04-14
 */
@Data
@Accessors(chain = true)
public class PendingMedicationPageDto {

    /** 药品编码 */
    private String medicineNo;

    /** 药品名称 */
    private String medicineName;

    /** 待发数量 */
    private String dispenseQuantity;

    /** 请求单位编码 */
    @Dict(dictCode = "unit_code")
    private String unitCode;
    private String unitCode_dictText;

    /** 发药类型(处方类型) */
    private Integer dispenseEnum;
    private String dispenseEnum_enumText;

    /** 患者姓名 */
    private String patientName;

    /** 处方号 */
    private String prescriptionNo;

    /** 门诊号 */
    private String outpatientNo;

    /** 住院号 */
    private String admissionNo;

    /** 开单时间 */
    private String createTime;
}
