package com.openhis.web.outpatientmanage.dto;

import lombok.Data;

/**
 * 门诊输液记录查询体体条件类
 *
 * @author liuhr
 * @date 2025/3/12
 */
@Data
public class OutpatientInfusionSearchParam {

    /** 病人ID/门诊号/病人姓名 */
    private String searchKey;

    /** 筛选开始时间 */
    private String beginTime;

    /** 筛选结束时间 */
    private String endTime;

}

