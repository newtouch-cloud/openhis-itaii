package com.openhis.web.outpatientmanage.appservice;

import java.util.List;

import com.openhis.web.outpatientmanage.dto.OutpatientSkinTestInitDto;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.web.outpatientmanage.dto.OutpatientSkinTestRecordDto;
import com.openhis.web.outpatientmanage.dto.OutpatientSkinTestRecordSearchParam;

/**
 * 门诊管理 应用实现类
 *
 * @author liuhr
 * @date 2025/3/7
 */
public interface IOutpatientSkinTestRecordService {


    /**
     * 获取门诊皮试记录初期数据列表
     *
     * @return 获取门诊皮试记录初期数据列表
     */
    OutpatientSkinTestInitDto getOutpatientSkinTestInit();

    /**
     * 分页查询门诊皮试记录,可选条件
     *
     * @param outpatientSkinTestRecordSearchParam 查询条件
     * @param pageNo 页码（默认为1）
     * @param pageSize 每页大小（默认为10）
     * @return 获取门诊皮试记录列表
     */
    Page<OutpatientSkinTestRecordDto> getSkinTestRecords(
        OutpatientSkinTestRecordSearchParam outpatientSkinTestRecordSearchParam, Integer pageNo, Integer pageSize);

    /**
     * 护士确认执行皮试后，更新皮试记录信息（服务申请管理与过敏与不耐受的相关字段更新）
     *
     * @param outpatientSkinTestRecordDto 皮试记录信息
     * @return 更新结果
     */
    boolean editSkinTestRecord(OutpatientSkinTestRecordDto outpatientSkinTestRecordDto);

    /**
     * 护士核对皮试结果后，确认签名（服务申请管理与过敏与不耐受的相关字段更新）
     *
     * @param outpatientSkinTestRecordDto 皮试记录信息
     * @return 更新结果
     */
    boolean nurseSignChkPs(OutpatientSkinTestRecordDto outpatientSkinTestRecordDto);

}
