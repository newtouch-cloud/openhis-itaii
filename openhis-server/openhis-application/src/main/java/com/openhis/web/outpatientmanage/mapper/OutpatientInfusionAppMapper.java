package com.openhis.web.outpatientmanage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionPatientDto;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionRecordDto;
import com.openhis.web.outpatientmanage.dto.OutpatientSkinTestRecordDto;

/**
 * 门诊管理
 *
 * @author liuhr
 * @date 2025/3/5
 */
public interface OutpatientInfusionAppMapper {

    /**
     * 门诊皮试记录分页查询
     *
     * @param page 分页参数
     * @param queryWrapper 查询条件
     * @return 门诊输液记录列表
     */
    IPage<OutpatientSkinTestRecordDto> getSkinTestRecords(@Param("page") Page<OutpatientSkinTestRecordDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<OutpatientSkinTestRecordDto> queryWrapper);

    /**
     * 门诊输液患者记录分页查询
     *
     * @param page 分页参数
     * @param queryWrapper 查询条件
     * @param inProgress 诊疗请求状态：待执行
     * @param completed 诊疗请求状态：已完成
     * @param cancelled 诊疗请求状态：取消
     * @param medMedicationRequest 药品请求表
     * @return 门诊输液患者记录
     */
    IPage<OutpatientInfusionPatientDto> getOutpatientInfusionPatient(
        @Param("page") Page<OutpatientInfusionPatientDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<OutpatientInfusionPatientDto> queryWrapper,
        @Param("inProgress") Integer inProgress, @Param("completed") Integer completed,
        @Param("cancelled") Integer cancelled, @Param("medMedicationRequest") String medMedicationRequest);

    /**
     * 查询门诊输液待执行记录
     * 
     * @param page 分页参数
     * @param queryWrapper 查询条件
     * @param encounterId 就诊id
     * @param completed 执行状态：已完成
     * @param medMedicationRequest 药品请求表
     * @return 门诊输液待执行记录
     */
    Page<OutpatientInfusionRecordDto> selectInfusionPendingRecord(@Param("page") Page<OutpatientInfusionRecordDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<OutpatientInfusionRecordDto> queryWrapper,
        @Param("encounterId") Long encounterId, @Param("completed") Integer completed,
        @Param("medMedicationRequest") String medMedicationRequest);

    /**
     * 查询门诊输液执行记录
     *
     * @param page 分页参数
     * @param queryWrapper 查询条件
     * @param serviceReqId 诊疗项目id
     * @param completed 执行状态：已完成
     * @return 门诊输液执行记录
     */
    Page<OutpatientInfusionRecordDto> selectInfusionPerformRecord(@Param("page") Page<OutpatientInfusionRecordDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<OutpatientInfusionRecordDto> queryWrapper,
        @Param("serviceReqId") Long serviceReqId, @Param("completed") Integer completed);

    /**
     * 获取诊疗执行信息
     *
     * @param serviceReqIdList 待执行诊疗id列表
     * @param completed 执行状态：已完成
     * @return 诊疗执行信息
     */
    List<OutpatientInfusionRecordDto> selectPerformInfo(@Param("serviceReqIdList") List<Long> serviceReqIdList,
        @Param("completed") Integer completed);
}
