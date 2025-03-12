package com.openhis.web.outpatientmanage.appservice;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.outpatientmanage.dto.OutpatientSkinTestRecordDto;
import com.openhis.web.outpatientmanage.dto.OutpatientSkinTestRecordSearchParam;
import com.openhis.web.patientmanage.dto.PatientListDto;

/**
 * 门诊管理 应用实现类
 *
 * @author liuhr
 * @date 2025/3/7
 */
public interface IOutpatientSkinTestRecordService {

    /**
     * 获取皮试项目检查状态列表
     */
    List<PatientListDto> getSkinTestStatus();

    /**
     * 获取皮试结果列表
     */
    List<PatientListDto> getSkinTestResult();

    /**
     * 获取药品状态列表
     */
    List<PatientListDto> getMedicationStatus();

    /**
     * 分页查询门诊皮试记录,可选条件
     *
     * @param outpatientSkinTestRecordSearchParam 查询条件
     * @param pageNo 页码（默认为1）
     * @param pageSize 每页大小（默认为10）
     */
    Page<OutpatientSkinTestRecordDto> getSkinTestRecords(
        OutpatientSkinTestRecordSearchParam outpatientSkinTestRecordSearchParam, Integer pageNo, Integer pageSize);

    /**
     * 获取门诊皮试记录列表
     *
     * @param outpatientSkinTestRecordSearchParam 门诊皮试记录查询参数
     * @param pageSize 页面大小
     * @param offset 跳过条数
     * @return 分页查询
     */
    List<OutpatientSkinTestRecordDto> getOutpatientSkinTestRecord(
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
     * 护士确认执行皮试后，更新皮试记录信息（服务申请管理与过敏与不耐受的相关字段更新）
     *
     * @param outpatientSkinTestRecordDto 皮试记录信息
     */
    boolean editSkinTestRecord(OutpatientSkinTestRecordDto outpatientSkinTestRecordDto);

    /**
     * 护士核对皮试结果后，确认签名（服务申请管理与过敏与不耐受的相关字段更新）
     *
     * @param outpatientSkinTestRecordDto 皮试记录信息
     */
    boolean nurseSignChkPs(OutpatientSkinTestRecordDto outpatientSkinTestRecordDto);

}
