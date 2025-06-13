/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.catalogmanage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.ybcatalog.domain.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 医保目录Mapper
 *
 * @author SunJQ
 * @date 2025-04-09
 */
@Repository
public interface CatalogMapper {

    IPage<CatalogZySyndrome> get1315Page(@Param("page")Page page, @Param(Constants.WRAPPER)QueryWrapper<CatalogZySyndrome> queryWrapper);

    IPage<CatalogZyDiseaseDiagnosis> get1314Page(@Param("page")Page page, @Param(Constants.WRAPPER)QueryWrapper<CatalogZyDiseaseDiagnosis> queryWrapper);

    IPage<CatalogSurgeryStandardDirectory> get1308Page(@Param("page")Page page, @Param(Constants.WRAPPER)QueryWrapper<CatalogSurgeryStandardDirectory> queryWrapper);

    IPage<CatalogWesternDisease> get1307Page(@Param("page")Page page, @Param(Constants.WRAPPER)QueryWrapper<CatalogWesternDisease> queryWrapper);

    IPage<CatalogMedicalConsumables> get1306Page(@Param("page")Page page, @Param(Constants.WRAPPER)QueryWrapper<CatalogMedicalConsumables> queryWrapper);

    IPage<CatalogMedicalService> get1305Page(@Param("page")Page page, @Param(Constants.WRAPPER)QueryWrapper<CatalogMedicalService> queryWrapper,@Param("tenantId") Integer tenantId);

    IPage<CatalogMedicalHerbInfo> get1302Page(@Param("page")Page page, @Param(Constants.WRAPPER)QueryWrapper<CatalogMedicalHerbInfo> queryWrapper);

    IPage<CatalogDrugInfo> get1301Page(@Param("page")Page page, @Param(Constants.WRAPPER)QueryWrapper<CatalogDrugInfo> queryWrapper,@Param("tenantId") Integer tenantId);
}
