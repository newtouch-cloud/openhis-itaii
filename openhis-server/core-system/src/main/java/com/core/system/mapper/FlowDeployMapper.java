package com.core.system.mapper;

import java.util.List;

import com.core.system.domain.FlowProcDefDto;

/**
 * 流程定义查询
 *
 * @author system
 * @email
 * @date 2022/1/29 5:44 下午
 **/
public interface FlowDeployMapper {

    /**
     * 流程定义列表
     * 
     * @param name
     * @return
     */
    List<FlowProcDefDto> selectDeployList(String name);
}
