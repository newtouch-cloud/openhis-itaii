package com.core.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.core.common.annotation.Excel;
import com.core.common.core.domain.BaseEntity;

/**
 * 流程实例关联表单对象 sys_instance_form
 * 
 * @author system
 * @date 2021-03-30
 */
public class SysDeployForm extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 表单主键 */
    @Excel(name = "表单主键")
    private Long formId;

    /** 流程定义主键 */
    @Excel(name = "流程定义主键")
    private String deployId;

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

    public String getDeployId() {
        return deployId;
    }

    public void setDeployId(String deployId) {
        this.deployId = deployId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("id", getId())
            .append("formId", getFormId()).append("deployId", getDeployId()).toString();
    }
}
