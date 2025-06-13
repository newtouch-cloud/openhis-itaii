package com.openhis.web.inpatientmanage.dto;

import lombok.Data;

/**
 * 预交金管理查询参数
 *
 * @author gaoyy
 * @since 2025/05/19
 */
@Data
public class DepositSearchParam {

    /** 住院号 */
    private String admissionNo;

    /** 在院病区 */
    private Long wardForm;

}
