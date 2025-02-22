package com.openhis.administration.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.openhis.administration.domain.ChargeItemDefinition;

/**
 * 费用定价管理Service接口
 *
 * @author system
 * @date 2025-02-20
 */
public interface IChargeItemDefinitionService extends IService<ChargeItemDefinition> {

    /**
     * 获取定价分页列表
     * 
     * @param chargeItemDefinition 定价查询条件
     * @param pageNo 页码
     * @param pageSize 页面大小
     * @return 定价分页列表
     */
    Page<ChargeItemDefinition> getPage(ChargeItemDefinition chargeItemDefinition, Integer pageNo, Integer pageSize);

    /**
     * 新增费用定价
     * 
     * @param chargeItemDefinition 新增内容
     * @return 新增结果
     */
    boolean addChargeItemDefinition(ChargeItemDefinition chargeItemDefinition);

    /**
     * 删除费用定价
     * 
     * @param id 费用定价id
     * @return 新增结果
     */
    boolean deleteChargeItemDefinition(Long id);
}