package com.openhis.web.outpatientmanage.appservice;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionPatientDto;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionRecordDto;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionSearchParam;

/**
 * 门诊管理——输液实现类
 *
 * @author liuhr
 * @date 2025/3/12
 */
public interface IOutpatientInfusionRecordService {

    /**
     * 获取门诊输液记录的患者列表
     *
     * @param outpatientInfusionSearchParam 门诊输液记录的患者列表查询参数
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 分页查询
     */
    IPage<OutpatientInfusionPatientDto> getOutpatientInfusionPatientList(
        OutpatientInfusionSearchParam outpatientInfusionSearchParam, Integer pageNo, Integer pageSize);

    /**
     * 点击患者，查询该患者的输液记录
     *
     * @param outpatientInfusionPatientDto 患者输液信息
     * @return 当前患者门诊输液待执行列表
     */
    List<OutpatientInfusionRecordDto>
        getPatientInfusionRecord(OutpatientInfusionPatientDto outpatientInfusionPatientDto);

    /**
     * 执行患者门诊输液
     *
     * @param outpatientInfusionRecordDtoList 输液记录列表
     * @return 执行成功/失败
     */
    boolean batchEditPatientInfusionRecord(List<OutpatientInfusionRecordDto> outpatientInfusionRecordDtoList);

    /**
     * 执行输液后,修改执行结束时间
     *
     * @param outpatientInfusionRecordDto 患者输液信息
     * @return 修改成功/失败
     */
    boolean editPatientInfusionTime(OutpatientInfusionRecordDto outpatientInfusionRecordDto);

    /**
     * 门诊输液执行历史记录查询
     *
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param historyFlag 查询的是否为执行履历
     * @return 门诊输液记录列表
     */
    List<OutpatientInfusionRecordDto> getPatientInfusionPerformRecord(String beginTime, String endTime,boolean historyFlag);

}
