package com.openhis.web.pharmacymanage.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 发药明细查询条件
 *
 * @author yuanzs
 * @date 2025-04-14
 */
@Data
@Accessors(chain = true)
public class MedDetailsSearchParam {

    /** 发药时间 */
    private Date dispenseTime;

    /** 药房 */
    private Long locationId;

    /** 患者姓名 */
    @Length(max = 2000)
    private String patientName;

    /** 发药人 */
    private Long practitionerId;

    /** 药品名称 */
    private String medicationName;

    /** 药品项目（药品编码） */
    private String busNo;

    /** 出院状态 */
    private Integer dischargeStatus;

}
