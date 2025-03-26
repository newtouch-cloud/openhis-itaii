/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.inventorymanage.dto;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.Length;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 领用出库分页查询条件
 *
 * @author
 * @date 2025-03-15
 */
@Data
@Accessors(chain = true)
public class RequisitionOutSearchParam implements Serializable {

    /** 单据号 */
    @Length(max = 255)
    private String busNo;

    /** 项目编码 */
    @Length(max = 255)
    private String id;

    /** 项目类型 */
//    @Length(max = 255)
//    private String busNo;

}
