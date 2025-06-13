package com.openhis.web.inpatientmanage.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.inpatientmanage.dto.DepositDetailDto;

/**
 * 预交金管理
 *
 * @author gaoyy
 * @since 2025/05/20
 */
@Repository
public interface DepositMapper {

    /**
     * 预交金信息分页查询
     *
     * @param page 分页
     * @param classEnum 类别编码(住院)
     * @param formEnum 类别编码(床)
     * @param queryWrapper 查询条件
     * @return 预交金信息
     */
    IPage<DepositDetailDto> getPage(@Param("page") Page<DepositDetailDto> page, @Param("classEnum") Integer classEnum,
        @Param("formEnum") Integer formEnum, @Param(Constants.WRAPPER) QueryWrapper<DepositDetailDto> queryWrapper);
}
