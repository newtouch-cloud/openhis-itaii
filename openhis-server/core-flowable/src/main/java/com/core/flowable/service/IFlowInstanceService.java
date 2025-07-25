package com.core.flowable.service;

import java.util.Map;

import org.flowable.engine.history.HistoricProcessInstance;

import com.core.common.core.domain.AjaxResult;
import com.core.flowable.domain.vo.FlowTaskVo;

/**
 * @author system
 * @date 2021-04-03 14:40
 */
public interface IFlowInstanceService {

    /**
     * 结束流程实例
     *
     * @param vo
     */
    void stopProcessInstance(FlowTaskVo vo);

    /**
     * 激活或挂起流程实例
     *
     * @param state 状态
     * @param instanceId 流程实例ID
     */
    void updateState(Integer state, String instanceId);

    /**
     * 删除流程实例ID
     *
     * @param instanceId 流程实例ID
     * @param deleteReason 删除原因
     */
    void delete(String instanceId, String deleteReason);

    /**
     * 根据实例ID查询历史实例数据
     *
     * @param processInstanceId
     * @return
     */
    HistoricProcessInstance getHistoricProcessInstanceById(String processInstanceId);

    /**
     * 根据流程定义ID启动流程实例
     *
     * @param procDefId 流程定义Id
     * @param variables 流程变量
     * @return
     */
    AjaxResult startProcessInstanceById(String procDefId, Map<String, Object> variables);
}
