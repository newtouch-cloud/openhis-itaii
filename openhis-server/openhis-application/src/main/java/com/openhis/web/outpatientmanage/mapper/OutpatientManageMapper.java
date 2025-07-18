package com.openhis.web.outpatientmanage.mapper;

import java.util.Date;
import java.util.List;

import com.openhis.workflow.domain.ServiceRequest;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionPatientDto;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionRecordDto;
import com.openhis.web.outpatientmanage.dto.OutpatientSkinTestRecordDto;
import com.openhis.web.outpatientmanage.dto.OutpatientSkinTestRecordSearchParam;

/**
 * 门诊管理
 *
 * @author liuhr
 * @date 2025/3/5
 */
public interface OutpatientManageMapper {

    /**
     * 门诊皮试记录分页查询
     *
     * @param outpatientSkinTestRecordSearchParam 门诊皮试记录查询参数
     * @param pageSize 页面大小
     * @param offset 跳过条数
     * @return 分页查询
     */
    List<OutpatientSkinTestRecordDto> getSkinTestRecords(
        @Param("OutpatientSkinTestRecordSearchParam") OutpatientSkinTestRecordSearchParam outpatientSkinTestRecordSearchParam,
        @Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    /**
     * 统计门诊皮试记录数的方法
     *
     * @param outpatientSkinTestRecordSearchParam 门诊皮试记录查询参数
     * @return 分页查询
     */
    long countOutpatientSkinTestRecords(
        @Param("OutpatientSkinTestRecordSearchParam") OutpatientSkinTestRecordSearchParam outpatientSkinTestRecordSearchParam);

    /**
     * 门诊输液患者记录分页查询
     *
     * @param page 分页参数
     * @param queryWrapper 查询条件
     * @return 门诊输液患者记录
     */
    IPage<OutpatientInfusionPatientDto> getOutpatientInfusionPatient(
        @Param("page") Page<OutpatientInfusionPatientDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<OutpatientInfusionPatientDto> queryWrapper);

    /**
     * 门诊输液记录查询
     *
     * @param page 分页参数，默认100条记录
     * @param queryWrapper 查询条件
     * @return 门诊输液记录列表
     */
    IPage<OutpatientInfusionRecordDto> getOutpatientInfusionRecord(
        @Param("page") Page<OutpatientInfusionRecordDto> page,
        @Param(Constants.WRAPPER) QueryWrapper<OutpatientInfusionRecordDto> queryWrapper);

    /**
     * 查询药品已执行数量/查询同组内药品数量
     *
     * @param paramId baseOnId/服务请求ID
     * @param prefixBusNo 服务请求编码
     * @param groupId 分组ID
     * @param flag 控制查询条件
     * @return 查询个数
     */
    long countExecuteNumOrGroupNum(@Param("paramId") Long paramId, @Param("prefixBusNo") String prefixBusNo,
        @Param("groupId") Long groupId, @Param("flag") boolean flag);

}
