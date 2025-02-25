package com.openhis.web.datadictionary.dto;

import com.core.common.core.domain.entity.SysDept;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

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
    List<DiseaseSourceDto> children = new ArrayList<>();
}
