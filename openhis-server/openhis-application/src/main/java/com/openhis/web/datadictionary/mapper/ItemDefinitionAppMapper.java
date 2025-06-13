package com.openhis.web.datadictionary.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.datadictionary.dto.ItemDefinitionDetailDto;
import com.openhis.web.datadictionary.dto.ItemDefinitionDto;

/**
 * 项目定价 应用Mapper
 */
@Repository
public interface ItemDefinitionAppMapper {

    /**
     * 查询项目定价信息
     * 
     * @param page 分页参数
     * @param chargeItemContext 收费项目类型
     * @param MED_MEDICATION_DEFINITION 药品定义
     * @param ADM_DEVICE_DEFINITION 器材定义
     * @param WOR_ACTIVITY_DEFINITION 活动定义
     * @param ADM_HEALTHCARE_SERVICE 服务管理
     * @param queryWrapper 查询条件
     * @return 项目定价信息
     */
    IPage<ItemDefinitionDto> getChargeItemInfo(@Param("page") Page<ItemDefinitionDto> page,
        @Param("chargeItemContext") Integer chargeItemContext,
        @Param("MED_MEDICATION_DEFINITION") String MED_MEDICATION_DEFINITION,
        @Param("ADM_DEVICE_DEFINITION") String ADM_DEVICE_DEFINITION,
        @Param("WOR_ACTIVITY_DEFINITION") String WOR_ACTIVITY_DEFINITION,
        @Param("ADM_HEALTHCARE_SERVICE") String ADM_HEALTHCARE_SERVICE,
        @Param(Constants.WRAPPER) QueryWrapper<ItemDefinitionDto> queryWrapper);

    /**
     * 项目定价详细
     * 
     * @param id id
     * @return 项目定价详细
     */
    List<ItemDefinitionDetailDto> getChargeItemInfoDetail(@Param("id") Long id);

}
