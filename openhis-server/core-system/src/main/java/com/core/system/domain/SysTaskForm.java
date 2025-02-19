package com.core.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.core.common.annotation.Excel;
import com.core.common.core.domain.BaseEntity;

/**
 * 流程任务关联单对象 sys_task_form
 * 
 * @author system
 * @date 2021-04-03
 */
public class SysTaskForm extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 表单主键 */
    @Excel(name = "表单主键")
    private Long formId;

    /** 所属任务 */
    @Excel(name = "所属任务")
    private String taskId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("id", getId())
            .append("formId", getFormId()).append("taskId", getTaskId()).toString();
    }
}
