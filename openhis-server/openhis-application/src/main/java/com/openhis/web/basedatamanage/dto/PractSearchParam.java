/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 员工分页查询条件
 *
 * @author
 * @date 2025-02-21
 */
@Data
@Accessors(chain = true)
public class PractSearchParam implements Serializable {

    /** 活动标记 */
    private Integer activeFlag;

}
