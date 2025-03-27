package com.openhis.web.datadictionary.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 药品查询信息
 *
 * @author liuhr
 * @date 2025/3/27
 */
@Data
@Accessors(chain = true)
public class MedicationSearchParam {

    /** 医保是否对码 */
    private Integer ybMatchFlag;
    /** 药品状态 */
    private Integer statusEnum;
    /** 药品分类 */
    private String categoryCode;
}
