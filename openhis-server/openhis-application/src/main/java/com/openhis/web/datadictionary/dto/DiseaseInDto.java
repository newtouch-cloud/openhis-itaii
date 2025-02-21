package com.openhis.web.datadictionary.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 病种目录列表 dto
 *
 * @author lpt
 * @date 2025-02-20
 */
@Data
@Accessors(chain = true)
public class DiseaseInDto {
    /** 所属分类 */
    private Integer sourceEnum;

    /** 编码 */
    private String code;

    /** 诊断名称 */
    private String name;

    /** 诊断名称拼音 */
    private String pyCode;

    /** 诊断名称五笔拼音 */
    private String wbCode;

    /** 类型 */
    private String typeCode;

    /** 描述 */
    private String description;

    /** 医保标记 */
    private Integer ybFlag;

    /** 医保编码 */
    private String ybCode;

    /** 医保对码标记 */
    private Integer ybMatchFlag;

    /** 状态 */
    private Integer statusEnum;

    /** 删除状态 */
    private Integer deleteFlag;
    /** 租户 */
    private Integer tenantId;
}
