package com.openhis.web.datadictionary.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 疾病目录种别dto
 *
 * @author lpt
 * @date 2025-02-25
 */
@Data
@Accessors(chain = true)
public class DiseaseSourceDto {
    private Integer value;
    private String code;
    private String info;
}
