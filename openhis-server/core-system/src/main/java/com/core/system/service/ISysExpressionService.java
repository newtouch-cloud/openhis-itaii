package com.core.system.service;

import java.util.List;

import com.core.system.domain.SysExpression;

/**
 * 流程达式Service接口
 * 
 * @author system
 * @date 2022-12-12
 */
public interface ISysExpressionService {
    /**
     * 查询流程达式
     * 
     * @param id 流程达式主键
     * @return 流程达式
     */
    public SysExpression selectSysExpressionById(Long id);

    /**
     * 查询流程达式列表
     * 
     * @param sysExpression 流程达式
     * @return 流程达式集合
     */
    public List<SysExpression> selectSysExpressionList(SysExpression sysExpression);

    /**
     * 新增流程达式
     * 
     * @param sysExpression 流程达式
     * @return 结果
     */
    public int insertSysExpression(SysExpression sysExpression);

    /**
     * 修改流程达式
     * 
     * @param sysExpression 流程达式
     * @return 结果
     */
    public int updateSysExpression(SysExpression sysExpression);

    /**
     * 批量删除流程达式
     * 
     * @param ids 需要删除的流程达式主键集合
     * @return 结果
     */
    public int deleteSysExpressionByIds(Long[] ids);

    /**
     * 删除流程达式信息
     * 
     * @param id 流程达式主键
     * @return 结果
     */
    public int deleteSysExpressionById(Long id);
}
