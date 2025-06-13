package com.openhis.web.inpatientmanage.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 预交金管理初始化页面信息
 *
 * @author gaoyy
 * @since 2025/05/19
 */
@Data
@Accessors(chain = true)
public class DepositInitPageDto {

    /**
     * 住院号
     */
    private String admissionNo;

}
