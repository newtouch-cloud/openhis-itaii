/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.basedatamanage.dto;

import java.io.Serializable;
import java.util.List;

import com.openhis.administration.domain.Location;
import com.openhis.administration.domain.Organization;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 科室位置关系初始化 dto
 *
 * @author
 * @date 2025-02-25
 */
@Data
@Accessors(chain = true)
public class OrgLocInitDto implements Serializable {

    /** 机构科室信息 */
    private List<Organization> organization;

    /** 位置药房信息 */
    private List<Location> location;
}
