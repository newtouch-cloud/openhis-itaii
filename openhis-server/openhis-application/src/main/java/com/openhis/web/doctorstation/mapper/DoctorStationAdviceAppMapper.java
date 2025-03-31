package com.openhis.web.doctorstation.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.doctorstation.dto.AdviceBaseDto;
import com.openhis.web.doctorstation.dto.AdviceInventoryDto;
import com.openhis.web.doctorstation.dto.AdvicePriceDto;

/**
 * 医生站-医嘱/处方 应用Mapper
 */
@Repository
public interface DoctorStationAdviceAppMapper {

    /**
     * 查询医嘱信息
     * 
     * @param page 分页参数
     * @param medicationTableName 药品定义表名
     * @param deviceTableName 耗材定义表名
     * @param activityTableName 诊疗定义表名
     * @param singleUse 单次消耗类 (耗材只查这类)
     * @param queryWrapper 查询条件
     * @return 医嘱信息
     */
    IPage<AdviceBaseDto> getAdviceBaseInfo(@Param("page") Page<AdviceBaseDto> page,
        @Param("medicationTableName") String medicationTableName, @Param("deviceTableName") String deviceTableName,
        @Param("activityTableName") String activityTableName, @Param("singleUse") String singleUse,
        @Param(Constants.WRAPPER) QueryWrapper<AdviceBaseDto> queryWrapper);

    /**
     * 查询医嘱库存
     * 
     * @param locationId 药房id
     * @param adviceDefinitionIdList 医嘱定义ID集合
     * @param SqlCondition sql条件
     * @return 医嘱库存
     */
    List<AdviceInventoryDto> getAdviceInventory(@Param("locationId") Long locationId,
        @Param("adviceDefinitionIdList") List<Long> adviceDefinitionIdList, @Param("SqlCondition") String SqlCondition);

    /**
     * 查询 费用定价子表
     * 
     * @param conditionCode 命中条件
     * @param chargeItemDefinitionIdList 费用定价主表ID集合
     * @return 费用定价子表
     */
    List<AdvicePriceDto> getChildCharge(@Param("conditionCode") String conditionCode,
        @Param("chargeItemDefinitionIdList") List<Long> chargeItemDefinitionIdList);

    /**
     * 查询 费用定价主表
     *
     * @param chargeItemDefinitionIdList 费用定价主表ID集合
     * @return 费用定价子表
     */
    List<AdvicePriceDto> getMainCharge(@Param("chargeItemDefinitionIdList") List<Long> chargeItemDefinitionIdList);

}
