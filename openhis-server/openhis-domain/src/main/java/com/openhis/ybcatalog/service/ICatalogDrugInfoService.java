package com.openhis.ybcatalog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.workflow.domain.ElepMedicationRequest;
import com.openhis.ybcatalog.domain.CatalogDrugInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 西药中成药目录Service接口
 *
 * @author system
 * @date 2025-04-09
 */
public interface ICatalogDrugInfoService extends IService<CatalogDrugInfo> {
    /**
     * 获取药品信息
     *
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     */
    Page<CatalogDrugInfo> selectCatalogDrugInfo(Integer pageNo,
        Integer pageSize, String searchKey);

}