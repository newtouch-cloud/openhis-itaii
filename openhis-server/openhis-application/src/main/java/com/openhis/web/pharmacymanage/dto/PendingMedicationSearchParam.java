package com.openhis.web.pharmacymanage.dto;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 待发药明细查询条件
 *
 * @author yuanzs
 * @date 2025-04-14
 */
@Data
@Accessors(chain = true)
public class PendingMedicationSearchParam {

//    /** 开始时间 */
//    private Date startTime;
//
//    /** 结束时间 */
//    private Date endTime;

    /** 开单时间 */
    private String createTime;

    /** 药品编码 */
    private String medicineNo;

    /** 药品名称 */
    private String medicineName;

    /** 统计类型 */
//    private Integer statisticTypeEnum;

}
