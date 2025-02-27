package com.openhis.web.datadictionary.dto;

import com.openhis.common.enums.ConditionDefinitionSource;
import com.openhis.common.enums.PublicationStatus;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 疾病目录分页检索条件
 *
 * @author lpt
 * @date 2025-02-25
 */
@Data
@Accessors(chain = true)
public class DiseaseManageSelParam {
    /** 所属分类 */
    private Integer sourceEnum;
    /** 状态 */
    private Integer statusEnum;
}
