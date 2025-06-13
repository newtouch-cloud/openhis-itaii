package com.openhis.web.inpatientmanage.dto;

import lombok.Data;

/**
 * 住院登记查询参数
 *
 * @author liuhr
 * @since 2025/04/08
 */
@Data
public class AdmissionSearchParam {

    /** 状态编码 */
    private Integer statusEnum;

}
