package com.openhis.web.outpatientmanage.dto;

import lombok.Data;

/**
 * 门诊皮试记录查询体体条件类
 *
 * @author liuhr
 * @date 2025/3/5
 */
@Data
public class OutpatientSkinTestRecordSearchParam {

    /** 就诊号 */
    private String encounterBusNo;

    /** 处方号 */
    private String prescriptionNo;

    /** 病人ID */
    private String patientBusNo;

    /** 手机号 */
    private String phone;

    /** 皮试项目检查状态 */
    private Integer status;

    /** 筛选开始时间 */
    private String beginTime;

    /** 筛选结束时间 */
    private String endTime;

}
