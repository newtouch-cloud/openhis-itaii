package com.openhis.administration.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.administration.domain.ChargeItemDefinition;
import com.openhis.administration.mapper.ChargeItemDefinitionMapper;
import com.openhis.administration.service.IChargeItemDefinitionService;
import com.openhis.common.enums.DelFlag;

/**
 * 费用定价管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class ChargeItemDefinitionServiceImpl extends ServiceImpl<ChargeItemDefinitionMapper, ChargeItemDefinition>
    implements IChargeItemDefinitionService {

    /**
     * 获取分页列表
     *
     * @param chargeItemDefinition 查询条件
     * @param pageNo 页码
     * @param pageSize 页面大小
     * @return 分页列表
     */
    @Override
    public Page<ChargeItemDefinition> getPage(ChargeItemDefinition chargeItemDefinition, Integer pageNo,
        Integer pageSize) {

        LambdaQueryWrapper<ChargeItemDefinition> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChargeItemDefinition::getDeleteFlag, DelFlag.NO.getValue());

        //拼接查询条件
        if (chargeItemDefinition.getStatusEnum() != null) {
            queryWrapper.eq(ChargeItemDefinition::getStatusEnum, chargeItemDefinition.getStatusEnum());
        }

        return baseMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
    }

    /**
     * 新增费用定价
     *
     * @param chargeItemDefinition 新增内容
     * @return 新增结果
     */
    @Override
    public boolean addChargeItemDefinition(ChargeItemDefinition chargeItemDefinition) {
        //此判断是为了避免插入时主键重复
        if (chargeItemDefinition.getId() != null) {
            return false;
        } else {
            return baseMapper.insert(chargeItemDefinition) > 0;
        }
    }

    /**
     * 删除费用定价
     *
     * @param id 费用定价id
     * @return 新增结果
     */
    @Override
    public boolean deleteChargeItemDefinition(Long id) {
        if (baseMapper.selectById(id) == null) {
            return false;
        } else {
            return baseMapper.deleteById(id) > 0;
        }
    }
}