package com.core.system.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 租户配置项Dto
 * 
 * @author system
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SysTenantOptionDto {

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 内容
     */
    private String content;

}
