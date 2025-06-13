package com.openhis.web.doctorstation.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.doctorstation.dto.*;

/**
 * 医生站-医嘱/处方 应用Mapper
 */
@Repository
public interface DoctorStationAdviceAppMapper {

    /**
     * 查询医嘱信息
     * 
     * @param page 分页参数
     * @param statusEnum 状态
     * @param organizationId 患者挂号对应的科室id
     * @param medicationTableName 药品定义表名
     * @param deviceTableName 耗材定义表名
     * @param activityTableName 诊疗定义表名
     * @param singleUse 单次消耗类 (耗材只查这类)
     * @param pricingFlag 划价标记
     * @param adviceDefinitionIdParamList 医嘱定义id参数集合
     * @param queryWrapper 查询条件
     * @return 医嘱信息
     */
    IPage<AdviceBaseDto> getAdviceBaseInfo(@Param("page") Page<AdviceBaseDto> page,
        @Param("statusEnum") Integer statusEnum, @Param("organizationId") Long organizationId,
        @Param("medicationTableName") String medicationTableName, @Param("deviceTableName") String deviceTableName,
        @Param("activityTableName") String activityTableName, @Param("singleUse") String singleUse,
        @Param("pricingFlag") Integer pricingFlag,
        @Param("adviceDefinitionIdParamList") List<Long> adviceDefinitionIdParamList,
        @Param(Constants.WRAPPER) QueryWrapper<AdviceBaseDto> queryWrapper);

    /**
     * 查询医嘱库存
     * 
     * @param locationId 药房id
     * @param adviceDefinitionIdList 医嘱定义ID集合
     * @param SqlCondition sql条件
     * @param status 状态
     * @return 医嘱库存
     */
    List<AdviceInventoryDto> getAdviceInventory(@Param("locationId") Long locationId,
        @Param("adviceDefinitionIdList") List<Long> adviceDefinitionIdList, @Param("SqlCondition") String SqlCondition,
        @Param("status") Integer status);

    /**
     * 查询待发放数量信息
     * 
     * @param medicationTableName 药品定义表名
     * @param deviceTableName 耗材定义表名
     * @param status 发放状态
     * @return 待发放数量信息
     */
    List<AdviceInventoryDto> getAdviceDraftInventory(@Param("medicationTableName") String medicationTableName,
        @Param("deviceTableName") String deviceTableName, @Param("status") Integer status);

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

    /**
     * 查询医嘱请求数据
     *
     * @param encounterId 就诊id
     * @param patientId 患者id
     * @param MED_MEDICATION_REQUEST 药品请求表名
     * @param WOR_DEVICE_REQUEST 耗材请求表名
     * @param WOR_SERVICE_REQUEST 诊疗请求表名
     * @param practitionerId 当前账号的参与者id
     * @param historyFlag 历史医嘱标记
     * @return 医嘱请求数据
     */
    List<RequestBaseDto> getRequestBaseInfo(@Param("encounterId") Long encounterId, @Param("patientId") Long patientId,
        @Param("MED_MEDICATION_REQUEST") String MED_MEDICATION_REQUEST,
        @Param("WOR_DEVICE_REQUEST") String WOR_DEVICE_REQUEST,
        @Param("WOR_SERVICE_REQUEST") String WOR_SERVICE_REQUEST, @Param("practitionerId") Long practitionerId,
        @Param("historyFlag") String historyFlag);

    /**
     * 查询就诊费用性质
     *
     * @param encounterId 就诊id
     * @return 就诊费用性质
     */
    List<EncounterContractDto> getEncounterContract(@Param("encounterId") Long encounterId);

}
