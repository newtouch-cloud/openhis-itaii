package com.openhis.web.outpatientmanage.appservice;

import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionInitDto;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionPatientDto;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionRecordDto;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionSearchParam;
import org.apache.ibatis.annotations.Param;

/**
 * 门诊管理——输液实现类
 *
 * @author liuhr
 * @date 2025/3/12
 */
public interface IOutpatientInfusionRecordService {

    /**
     * 获取门诊输液记录初期数据列表
     *
     * @return 门诊输液记录初期数据列表
     */
    OutpatientInfusionInitDto getOutpatientInfusionInit();


    /**
     * 获取门诊输液记录的患者列表
     *
     * @param outpatientInfusionSearchParam 门诊输液记录的患者列表查询参数
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 分页查询
     */
    IPage<OutpatientInfusionPatientDto> getOutpatientInfusionPatient(
        OutpatientInfusionSearchParam outpatientInfusionSearchParam, Integer pageNo, Integer pageSize);


    /**
     * 查询单个患者门诊输液记录查询
     *
     * @param outpatientInfusionPatientDto 患者输液信息
     * @return 门诊输液记录列表
     */
    List<OutpatientInfusionRecordDto> getPatientInfusionRecord(OutpatientInfusionPatientDto outpatientInfusionPatientDto);

    /**
     * 执行单个患者门诊输液
     *
     * @param exeCount 执行记录数
     * @param outpatientInfusionRecordDto 患者输液信息
     * @return 修改成功/失败
     */
    boolean editPatientInfusionRecord(OutpatientInfusionRecordDto outpatientInfusionRecordDto,Long exeCount);

    /**
     * 执行输液后,修改执行结束时间
     *
     * @param outpatientInfusionRecordDto 患者输液信息
     * @return 修改成功/失败
     */
    boolean editPatientInfusionTime(OutpatientInfusionRecordDto outpatientInfusionRecordDto);

    /**
     * 显示门诊输液执行记录查询
     *
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 门诊输液记录列表
     */
    List<OutpatientInfusionRecordDto> getPatientInfusionPerformRecord(String beginTime,String endTime);

}
