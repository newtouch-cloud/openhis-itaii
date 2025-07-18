package com.core.flowable.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.AjaxResult;
import com.core.common.core.domain.entity.SysUser;
import com.core.common.utils.SecurityUtils;
import com.core.flowable.common.constant.ProcessConstants;
import com.core.flowable.common.enums.FlowComment;
import com.core.flowable.factory.FlowServiceFactory;
import com.core.flowable.service.IFlowDefinitionService;
import com.core.flowable.service.ISysDeployFormService;
import com.core.system.domain.FlowProcDefDto;
import com.core.system.domain.SysForm;
import com.core.system.mapper.FlowDeployMapper;
import com.core.system.mapper.SysUserMapper;
import com.core.system.service.ISysDeptService;
import com.core.system.service.ISysUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * 流程定义
 *
 * @author system
 * @date 2021-04-03
 */
@Service
@Slf4j
public class FlowDefinitionServiceImpl extends FlowServiceFactory implements IFlowDefinitionService {

    private static final String BPMN_FILE_SUFFIX = ".bpmn";
    @Resource
    private ISysDeployFormService sysDeployFormService;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysDeptService sysDeptService;
    @Resource
    private FlowDeployMapper flowDeployMapper;
    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public boolean exist(String processDefinitionKey) {
        ProcessDefinitionQuery processDefinitionQuery =
            repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey);
        long count = processDefinitionQuery.count();
        return count > 0 ? true : false;
    }

    /**
     * 流程定义列表
     *
     * @param pageNum 当前页码
     * @param pageSize 每页条数
     * @return 流程定义分页列表数据
     */
    @Override
    public Page<FlowProcDefDto> list(String name, Integer pageNum, Integer pageSize) {
        Page<FlowProcDefDto> page = new Page<>();
        // // 流程定义列表数据查询
        // final ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        // if (StringUtils.isNotEmpty(name)) {
        // processDefinitionQuery.processDefinitionNameLike(name);
        // }
        //// processDefinitionQuery.orderByProcessDefinitionKey().asc();
        // page.setTotal(processDefinitionQuery.count());
        // List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(pageSize * (pageNum - 1),
        // pageSize);
        //
        // List<FlowProcDefDto> dataList = new ArrayList<>();
        // for (ProcessDefinition processDefinition : processDefinitionList) {
        // String deploymentId = processDefinition.getDeploymentId();
        // Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
        // FlowProcDefDto reProcDef = new FlowProcDefDto();
        // BeanUtils.copyProperties(processDefinition, reProcDef);
        // SysForm sysForm = sysDeployFormService.selectSysDeployFormByDeployId(deploymentId);
        // if (Objects.nonNull(sysForm)) {
        // reProcDef.setFormName(sysForm.getFormName());
        // reProcDef.setFormId(sysForm.getFormId());
        // }
        // // 流程定义时间
        // reProcDef.setDeploymentTime(deployment.getDeploymentTime());
        // dataList.add(reProcDef);
        // }
        PageHelper.startPage(pageNum, pageSize);
        final List<FlowProcDefDto> dataList = flowDeployMapper.selectDeployList(name);
        // 加载挂表单
        for (FlowProcDefDto procDef : dataList) {
            SysForm sysForm = sysDeployFormService.selectSysDeployFormByDeployId(procDef.getDeploymentId());
            if (Objects.nonNull(sysForm)) {
                procDef.setFormName(sysForm.getFormName());
                procDef.setFormId(sysForm.getFormId());
            }
        }
        page.setTotal(new PageInfo(dataList).getTotal());
        page.setRecords(dataList);
        return page;
    }

    /**
     * 导入流程文件
     *
     * 当每个key的流程第一次部署时，指定版本为1。对其后所有使用相同key的流程定义， 部署时版本会在该key当前已部署的最高版本号基础上加1。key参数用于区分流程定义
     * 
     * @param name
     * @param category
     * @param in
     */
    @Override
    public void importFile(String name, String category, InputStream in) {
        Deployment deploy = repositoryService.createDeployment().addInputStream(name + BPMN_FILE_SUFFIX, in).name(name)
            .category(category).deploy();
        ProcessDefinition definition =
            repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
        repositoryService.setProcessDefinitionCategory(definition.getId(), category);

    }

    /**
     * 读取xml
     *
     * @param deployId
     * @return
     */
    @Override
    public AjaxResult readXml(String deployId) throws IOException {
        ProcessDefinition definition =
            repositoryService.createProcessDefinitionQuery().deploymentId(deployId).singleResult();
        InputStream inputStream =
            repositoryService.getResourceAsStream(definition.getDeploymentId(), definition.getResourceName());
        String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
        return AjaxResult.success("", result);
    }

    /**
     * 读取xml
     *
     * @param deployId
     * @return
     */
    @Override
    public InputStream readImage(String deployId) {
        ProcessDefinition processDefinition =
            repositoryService.createProcessDefinitionQuery().deploymentId(deployId).singleResult();
        // 获得图片流
        DefaultProcessDiagramGenerator diagramGenerator = new DefaultProcessDiagramGenerator();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        // 输出为图片
        return diagramGenerator.generateDiagram(bpmnModel, "png", Collections.emptyList(), Collections.emptyList(),
            "宋体", "宋体", "宋体", null, 1.0, false);

    }

    /**
     * 根据流程定义ID启动流程实例
     *
     * @param procDefId 流程模板ID
     * @param variables 流程变量
     * @return
     */
    @Override
    public AjaxResult startProcessInstanceById(String procDefId, Map<String, Object> variables) {
        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(procDefId).latestVersion().singleResult();
            if (Objects.nonNull(processDefinition) && processDefinition.isSuspended()) {
                return AjaxResult.error("流程已被挂起,请先激活流程");
            }
            // 设置流程发起人Id到流程中
            SysUser sysUser;
            try {
                sysUser = SecurityUtils.getLoginUser().getUser();
            } catch (Exception e) {
                // 非登录状态默认为超级管理员
                sysUser = sysUserMapper.selectUserById(1L);
            }
            identityService.setAuthenticatedUserId(sysUser.getUserId().toString());
            variables.put(ProcessConstants.PROCESS_INITIATOR, sysUser.getUserId());

            // 流程发起时 跳过发起人节点
            ProcessInstance processInstance = runtimeService.startProcessInstanceById(procDefId, variables);
            // 给第一步申请人节点设置任务执行人和意见
            Task task =
                taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();
            if (Objects.nonNull(task)) {
                taskService.addComment(task.getId(), processInstance.getProcessInstanceId(),
                    FlowComment.NORMAL.getType(), sysUser.getNickName() + "发起流程申请");
                taskService.complete(task.getId(), variables);
            }
            return AjaxResult.success("流程启动成功");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("流程启动错误");
        }
    }

    /**
     * 激活或挂起流程定义
     *
     * @param state 状态
     * @param deployId 流程部署ID
     */
    @Override
    public void updateState(Integer state, String deployId) {
        ProcessDefinition procDef =
            repositoryService.createProcessDefinitionQuery().deploymentId(deployId).singleResult();
        // 激活
        if (state == 1) {
            repositoryService.activateProcessDefinitionById(procDef.getId(), true, null);
        }
        // 挂起
        if (state == 2) {
            repositoryService.suspendProcessDefinitionById(procDef.getId(), true, null);
        }
    }

    /**
     * 删除流程定义
     *
     * @param deployId 流程部署ID act_ge_bytearray 表中 deployment_id值
     */
    @Override
    public void delete(String deployId) {
        // true 允许级联删除 ,不设置会导致数据库外键关联异常
        repositoryService.deleteDeployment(deployId, true);
    }

}
