package com.openhis.web.outpatientmanage.appservice;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.core.common.core.domain.R;
import com.openhis.web.outpatientmanage.dto.OutpatientInfusionPatientDto;

/**
 * 门诊管理——输液实现类
 *
 * @author liuhr
 * @date 2025/3/12
 */
public interface IOutpatientInfusionAppService {

    /**
     * 获取门诊输液记录的患者列表
     *
     * @param outpatientInfusionPatientDto 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 分页查询
     */
    R<?> getOutpatientInfusionPatientList(OutpatientInfusionPatientDto outpatientInfusionPatientDto, String searchKey,
        Integer pageNo, Integer pageSize, HttpServletRequest request);

    /**
     * 门诊输液执行历史记录查询
     *
     * @param serviceReqId 诊疗项目id
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 门诊输液执行历史记录列表
     */
    R<?> getInfusionPerformRecord(Long serviceReqId, Integer pageNo, Integer pageSize);

    /**
     * 门诊输液待执行记录查询
     *
     * @param encounterId 就诊ID
     * @param serviceStatus 就诊状态
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 门诊输液待执行记录列表
     */
    R<?> getInfusionPendingRecord(Long encounterId, Integer serviceStatus, Integer pageNo, Integer pageSize);

    /**
     * 输液执行
     *
     * @param serviceReqIdList 输液请求id列表
     * @return 执行结果
     */
    R<?> infusionPerform(List<Long> serviceReqIdList);

    /**
     * 修改输液执行时间
     *
     * @param serviceReqId 患者输液信息
     * @param performTime 患者输液信息
     * @return 执行结果
     */
    R<?> editPatientInfusionTime(Long serviceReqId, String performTime);

    /**
     * 撤销执行
     *
     * @param serviceReqId 输液请求id
     * @return 撤销结果
     */
    R<?> cancelInfusionPerform(Long serviceReqId);

    /**
     * 门诊输液初始化
     *
     * @return 初始化信息
     */
    R<?> init();
}
