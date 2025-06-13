/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.catalogmanage.appservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.SecurityUtils;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.constant.YbCommonConstants;
import com.openhis.common.enums.CatalogType;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.catalogmanage.appservice.ICatalogService;
import com.openhis.web.catalogmanage.mapper.CatalogMapper;
import com.openhis.ybcatalog.domain.*;
import com.openhis.ybcatalog.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;

/**
 * 医保目录服务层
 *
 * @author SunJQ
 * @date 2025-04-09
 */
@Service
public class CatalogServiceImpl implements ICatalogService {

    @Autowired
    CatalogMapper catalogMapper;
    @Autowired
    ICatalogDrugInfoService iCatalogDrugInfoService;
    @Autowired
    ICatalogMedicalServiceService iCatalogMedicalServiceService;
    @Autowired
    ICatalogMedicalConsumablesService iCatalogMedicalConsumablesService;
    @Autowired
    ICatalogSurgeryStandardDirectoryService iCatalogSurgeryStandardDirectoryService;
    @Autowired
    ICatalogZyDiseaseDiagnosisService iCatalogZyDiseaseDiagnosisService;
    @Autowired
    ICatalogWesternDiseaseService iCatalogWesternDiseaseService;
    @Autowired
    ICatalogZySyndromeService iCatalogZySyndromeService;
    @Autowired
    ICatalogMedicalHerbInfoService iCatalogMedicalHerbInfoService;

    @Override
    public R<?> getPage(Integer catalogType, String searchKey, Integer pageNo, Integer pageSize,
        HttpServletRequest request) {
        // 分类
        CatalogType catalog = CatalogType.getByValue(catalogType);
        if (catalog == null) {
            return R.fail(
                MessageUtils.createMessage(PromptMsgConstant.Yb.M00001, new String[] {String.valueOf(catalogType)}));
        }
        // 查询对应的目录
        switch (catalog) {
            case CATALOG_TYPE_1301:
                return get1301Page(searchKey, pageNo, pageSize, request);
            case CATALOG_TYPE_1302:
                return get1302Page(searchKey, pageNo, pageSize, request);
            case CATALOG_TYPE_1305:
                return get1305Page(searchKey, pageNo, pageSize, request);
            case CATALOG_TYPE_1306:
                return get1306Page(searchKey, pageNo, pageSize, request);
            case CATALOG_TYPE_1307:
                return get1307Page(searchKey, pageNo, pageSize, request);
            case CATALOG_TYPE_1308:
                return get1308Page(searchKey, pageNo, pageSize, request);
            case CATALOG_TYPE_1314:
                return get1314Page(searchKey, pageNo, pageSize, request);
            case CATALOG_TYPE_1315:
                return get1315Page(searchKey, pageNo, pageSize, request);
            default:
                return R.fail(MessageUtils.createMessage(PromptMsgConstant.Yb.M00001,
                    new String[] {String.valueOf(catalogType)}));
        }
    }

    /**
     * 中医证候目录
     *
     * @param searchKey 模糊条件
     * @param pageNo 分页参数
     * @param pageSize 分页参数
     * @param request 请求参数
     * @return 分页内容
     */
    private R<?> get1315Page(String searchKey, Integer pageNo, Integer pageSize, HttpServletRequest request) {
        // 构建查询条件
        // QueryWrapper<CatalogZySyndrome> queryWrapper = HisQueryUtils.buildQueryWrapper(
        // null, searchKey, new HashSet<>(Arrays.asList(YBCommonConstants.FieldName.SyndromeTypeCode,
        // YBCommonConstants.FieldName.SyndromeTypeName, YBCommonConstants.FieldName.UniqueRecordId)),
        // request);
        //
        // return R.ok(catalogMapper.get1315Page(new Page(pageNo, pageSize), queryWrapper));
        QueryWrapper<CatalogZySyndrome> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(YbCommonConstants.FieldName.SyndromeTypeCode, searchKey).or()
            .like(YbCommonConstants.FieldName.SyndromeTypeName, searchKey).or()
            .like(YbCommonConstants.FieldName.UniqueRecordId, searchKey);
        return R.ok(iCatalogZySyndromeService.page(new Page<>(pageNo, pageSize), queryWrapper));
    }

    /**
     * 中医疾病目录
     *
     * @param searchKey 模糊条件
     * @param pageNo 分页参数
     * @param pageSize 分页参数
     * @param request 请求参数
     * @return 分页内容
     */
    private R<?> get1314Page(String searchKey, Integer pageNo, Integer pageSize, HttpServletRequest request) {
        // 构建查询条件
        // QueryWrapper<CatalogZyDiseaseDiagnosis> queryWrapper = HisQueryUtils.buildQueryWrapper(
        // null, searchKey, new HashSet<>(Arrays.asList(YBCommonConstants.FieldName.DiseaseCategoryName,
        // YBCommonConstants.FieldName.DiseaseCategoryCode, YBCommonConstants.FieldName.UniqueRecordId)),
        // request);
        //
        // return R.ok(catalogMapper.get1314Page(new Page(pageNo, pageSize), queryWrapper));
        QueryWrapper<CatalogZyDiseaseDiagnosis> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(YbCommonConstants.FieldName.DiseaseCategoryName, searchKey).or()
            .like(YbCommonConstants.FieldName.DiseaseCategoryCode, searchKey).or()
            .like(YbCommonConstants.FieldName.UniqueRecordId, searchKey);
        return R.ok(iCatalogZyDiseaseDiagnosisService.page(new Page<>(pageNo, pageSize), queryWrapper));
    }

    /**
     * 手术标准目录
     *
     * @param searchKey 模糊条件
     * @param pageNo 分页参数
     * @param pageSize 分页参数
     * @param request 请求参数
     * @return 分页内容
     */
    private R<?> get1308Page(String searchKey, Integer pageNo, Integer pageSize, HttpServletRequest request) {
        // 构建查询条件
        // QueryWrapper<CatalogSurgeryStandardDirectory> queryWrapper = HisQueryUtils.buildQueryWrapper(
        // null, searchKey, new HashSet<>(Arrays.asList(YBCommonConstants.FieldName.CategoryName,
        // YBCommonConstants.FieldName.SubcategoryName, YBCommonConstants.FieldName.ItemName,
        // YBCommonConstants.FieldName.ItemName, YBCommonConstants.FieldName.OperationName,
        // YBCommonConstants.FieldName.OperationCode)),
        // request);
        //
        // return R.ok(catalogMapper.get1308Page(new Page(pageNo, pageSize), queryWrapper));
        QueryWrapper<CatalogSurgeryStandardDirectory> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(YbCommonConstants.FieldName.CategoryName, searchKey).or()
            .like(YbCommonConstants.FieldName.SubcategoryName, searchKey).or()
            .like(YbCommonConstants.FieldName.ItemName, searchKey).or()
            .like(YbCommonConstants.FieldName.OperationName, searchKey).or()
            .like(YbCommonConstants.FieldName.OperationCode, searchKey);
        return R.ok(iCatalogSurgeryStandardDirectoryService.page(new Page<>(pageNo, pageSize), queryWrapper));
    }

    /**
     * 疾病与诊断目录
     *
     * @param searchKey 模糊条件
     * @param pageNo 分页参数
     * @param pageSize 分页参数
     * @param request 请求参数
     * @return 分页内容
     */
    private R<?> get1307Page(String searchKey, Integer pageNo, Integer pageSize, HttpServletRequest request) {
        // 构建查询条件
        // QueryWrapper<CatalogWesternDisease> queryWrapper = HisQueryUtils.buildQueryWrapper(
        // null, searchKey, new HashSet<>(Arrays.asList(YBCommonConstants.FieldName.CategoryName,
        // YBCommonConstants.FieldName.SubcategoryName, YBCommonConstants.FieldName.ItemName,
        // YBCommonConstants.FieldName.ItemName, YBCommonConstants.FieldName.OperationName,
        // YBCommonConstants.FieldName.OperationCode)),
        // request);
        //
        // return R.ok(catalogMapper.get1307Page(new Page(pageNo, pageSize), queryWrapper));
        QueryWrapper<CatalogWesternDisease> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(YbCommonConstants.FieldName.CategoryName, searchKey).or()
            .like(YbCommonConstants.FieldName.SubcategoryName, searchKey).or()
            .like(YbCommonConstants.FieldName.ChapterName, searchKey).or()
            .like(YbCommonConstants.FieldName.SectionName, searchKey);
        return R.ok(iCatalogWesternDiseaseService.page(new Page<>(pageNo, pageSize), queryWrapper));
    }

    /**
     * 医用耗材目录
     *
     * @param searchKey 模糊条件
     * @param pageNo 分页参数
     * @param pageSize 分页参数
     * @param request 请求参数
     * @return 分页内容
     */
    private R<?> get1306Page(String searchKey, Integer pageNo, Integer pageSize, HttpServletRequest request) {
        // 构建查询条件
        // QueryWrapper<CatalogMedicalConsumables> queryWrapper = HisQueryUtils.buildQueryWrapper(
        // null, searchKey, new HashSet<>(Arrays.asList(YBCommonConstants.FieldName.MedicalCatalogCode,
        // YBCommonConstants.FieldName.ConsumableCategory, YBCommonConstants.FieldName.MaterialType,
        // YBCommonConstants.FieldName.Specification)),
        // request);
        //
        // return R.ok(catalogMapper.get1306Page(new Page(pageNo, pageSize), queryWrapper));
        QueryWrapper<CatalogMedicalConsumables> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(YbCommonConstants.FieldName.MedicalCatalogCode, searchKey).or()
            .like(YbCommonConstants.FieldName.ConsumableName, searchKey).or()
            .like(YbCommonConstants.FieldName.ConsumableCategory, searchKey).or()
            .like(YbCommonConstants.FieldName.MaterialType, searchKey).or()
            .like(YbCommonConstants.FieldName.Specification, searchKey);
        return R.ok(iCatalogMedicalConsumablesService.page(new Page<>(pageNo, pageSize), queryWrapper));
    }

    /**
     * 医疗服务目录
     *
     * @param searchKey 模糊条件
     * @param pageNo 分页参数
     * @param pageSize 分页参数
     * @param request 请求参数
     * @return 分页内容
     */
    private R<?> get1305Page(String searchKey, Integer pageNo, Integer pageSize, HttpServletRequest request) {
        // 构建查询条件
        QueryWrapper<CatalogMedicalService> queryWrapper =
            HisQueryUtils.buildQueryWrapper(null, searchKey,
                new HashSet<>(Arrays.asList(YbCommonConstants.FieldName.MedicalCatalogCode,
                    YbCommonConstants.FieldName.MedicalServiceName, YbCommonConstants.FieldName.UniqueRecordId)),
                request);
        // 只保留每组中 version_number 最大的记录
        queryWrapper.eq(YbCommonConstants.FieldName.RowNumMax, 1);
        Integer tenantId = SecurityUtils.getLoginUser().getTenantId();
        return R.ok(catalogMapper.get1305Page(new Page(pageNo, pageSize), queryWrapper, tenantId));

        // QueryWrapper<CatalogMedicalService> queryWrapper = new QueryWrapper<>();
        // queryWrapper.like(YbCommonConstants.FieldName.MedicalServiceName, searchKey).or()
        // .like(YbCommonConstants.FieldName.UniqueRecordId, searchKey);
        // return R.ok(iCatalogMedicalServiceService.page(new Page<>(pageNo, pageSize), queryWrapper));
    }

    /**
     * 中药饮片目录
     *
     * @param searchKey 模糊条件
     * @param pageNo 分页参数
     * @param pageSize 分页参数
     * @param request 请求参数
     * @return 分页内容
     */
    private R<?> get1302Page(String searchKey, Integer pageNo, Integer pageSize, HttpServletRequest request) {
        // 构建查询条件
        // QueryWrapper<CatalogMedicalHerbInfo> queryWrapper = HisQueryUtils.buildQueryWrapper(
        // null, searchKey, new HashSet<>(Arrays.asList(YBCommonConstants.FieldName.MedicalServiceName,
        // YBCommonConstants.FieldName.UniqueRecordId)),
        // request);
        //
        // return R.ok(catalogMapper.get1302Page(new Page(pageNo, pageSize), queryWrapper));
        QueryWrapper<CatalogMedicalHerbInfo> queryWrapper = new QueryWrapper<>();
        // queryWrapper.like(YBCommonConstants.FieldName.MedicalServiceName, searchKey).or()
        // .like(YBCommonConstants.FieldName.UniqueRecordId, searchKey);
        return R.ok(iCatalogMedicalHerbInfoService.page(new Page<>(pageNo, pageSize), queryWrapper));
    }

    /**
     * 西药中成药目录
     *
     * @param searchKey 模糊条件
     * @param pageNo 分页参数
     * @param pageSize 分页参数
     * @param request 请求参数
     * @return 分页内容
     */
    private R<?> get1301Page(String searchKey, Integer pageNo, Integer pageSize, HttpServletRequest request) {
        // 构建查询条件
        QueryWrapper<CatalogDrugInfo> queryWrapper = HisQueryUtils.buildQueryWrapper(null, searchKey,
            new HashSet<>(Arrays.asList(YbCommonConstants.FieldName.MedicalCatalogCode,
                YbCommonConstants.FieldName.RegisteredName, YbCommonConstants.FieldName.ApprovalNo,
                YbCommonConstants.FieldName.UniqueRecordId)),
            request);

        // 只保留每组中 version_number 最大的记录
        queryWrapper.eq(YbCommonConstants.FieldName.RowNumMax, 1);
        Integer tenantId = SecurityUtils.getLoginUser().getTenantId();
        return R.ok(catalogMapper.get1301Page(new Page(pageNo, pageSize), queryWrapper, tenantId));

        // QueryWrapper<CatalogDrugInfo> queryWrapper = new QueryWrapper<>();
        // queryWrapper.like(YbCommonConstants.FieldName.MedicalCatalogCode, searchKey).or()
        // .like(YbCommonConstants.FieldName.RegisteredName, searchKey).or()
        // .like(YbCommonConstants.FieldName.ApprovalNo, searchKey).or()
        // .like(YbCommonConstants.FieldName.UniqueRecordId, searchKey);
        // return R.ok(iCatalogDrugInfoService.page(new Page<>(pageNo, pageSize), queryWrapper));
    }
}
