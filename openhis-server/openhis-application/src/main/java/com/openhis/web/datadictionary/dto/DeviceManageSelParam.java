package com.openhis.web.datadictionary.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 器材目录分页检索条件
 *
 * @author lpt
 * @date 2025-02-25
 */
@Data
@Accessors(chain = true)
public class DeviceManageSelParam {

    /** 器材分类 */
    private String categoryCode;

    /** 医保对码标记 */
    private Integer ybMatchFlag;

    /** 状态 */
    private Integer statusEnum;

    /** 执行科室 */
    private Long ruleId;
}
