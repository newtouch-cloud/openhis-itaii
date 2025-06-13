package com.openhis.ybcatalog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.ybcatalog.domain.CatalogDrugInfo;
import com.openhis.ybcatalog.mapper.CatalogDrugInfoMapper;
import com.openhis.ybcatalog.service.ICatalogDrugInfoService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 西药中成药目录Service业务层处理
 *
 * @author system
 * @date 2025-04-09
 */
@Service
public class CatalogDrugInfoServiceImpl extends ServiceImpl<CatalogDrugInfoMapper, CatalogDrugInfo> implements ICatalogDrugInfoService {
    /**
     * 获取药品信息
     */

    @Override
    public Page<CatalogDrugInfo> selectCatalogDrugInfo(Integer pageNo,
                                                       Integer pageSize, String searchKey) {
        Page<CatalogDrugInfo> page = new Page<>(pageNo, pageSize);
        return (baseMapper.selectPage(page, new LambdaQueryWrapper<CatalogDrugInfo>().like(CatalogDrugInfo::getRegisteredName, searchKey).or().like(CatalogDrugInfo::getPinyinCode, searchKey).or().like(CatalogDrugInfo::getWubiCode, searchKey)));
    }
}