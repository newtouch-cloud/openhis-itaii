package com.core.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.core.common.annotation.Excel;
import com.core.common.core.domain.BaseEntity;

/**
 * 流程达式对象 sys_expression
 *
 * @author system
 * @date 2022-12-12
 */
public class SysExpression extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 表单主键 */
    private Long id;

    /** 表达式名称 */
    @Excel(name = "表达式名称")
    private String name;

    /** 表达式内容 */
    @Excel(name = "表达式内容")
    private String expression;
    /** 表达式类型 */
    @Excel(name = "表达式类型")
    private String dataType;

    /** 状态 */
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("id", getId()).append("name", getName())
            .append("expression", getExpression()).append("dataType", getDataType())
            .append("createTime", getCreateTime()).append("updateTime", getUpdateTime())
            .append("createBy", getCreateBy()).append("updateBy", getUpdateBy()).append("status", getStatus())
            .append("remark", getRemark()).toString();
    }
}
