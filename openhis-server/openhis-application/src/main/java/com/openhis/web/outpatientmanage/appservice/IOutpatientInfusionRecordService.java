package com.openhis.web.outpatientmanage.appservice;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionPatientDto;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionRecordDto;

import javax.servlet.http.HttpServletRequest;

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
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 分页查询
     */
    IPage<OutpatientInfusionPatientDto> getOutpatientInfusionPatientList(
        String searchKey, Integer pageNo, Integer pageSize, HttpServletRequest request);

    /**
     * 点击患者，查询该患者的输液记录
     *
     * @param outpatientInfusionPatientDto 患者输液信息
     * @return 当前患者门诊输液待执行列表
     */
    List<OutpatientInfusionRecordDto>
        getPatientInfusionRecord(OutpatientInfusionPatientDto outpatientInfusionPatientDto, HttpServletRequest request);

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
     * @param historyFlag 查询的是否为执行履历
     * @return 门诊输液记录列表
     */
    List<OutpatientInfusionRecordDto> getPatientInfusionPerformRecord(HttpServletRequest request,boolean historyFlag);

}
