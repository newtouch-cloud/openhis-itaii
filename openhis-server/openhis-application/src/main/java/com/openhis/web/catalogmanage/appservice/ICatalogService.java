/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.catalogmanage.appservice;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.openhis.ybcatalog.domain.CatalogZySyndrome;

import javax.servlet.http.HttpServletRequest;

/**
 * 医保目录服务层
 *
 * @author SunJQ
 * @date 2025-04-09
 */

public interface ICatalogService {
    R<?> getPage(Integer catalogType, String searchKey, Integer pageNo, Integer pageSize, HttpServletRequest request);
}
