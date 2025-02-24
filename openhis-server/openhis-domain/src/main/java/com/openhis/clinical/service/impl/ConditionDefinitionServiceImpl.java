package com.openhis.clinical.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.clinical.domain.ConditionDefinition;
import com.openhis.clinical.mapper.ConditionDefinitionMapper;
import com.openhis.clinical.service.IConditionDefinitionService;
import com.openhis.common.enums.DelFlag;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 诊断定义管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Slf4j
@Service
@AllArgsConstructor
public class ConditionDefinitionServiceImpl extends ServiceImpl<ConditionDefinitionMapper, ConditionDefinition>
    implements IConditionDefinitionService {

    private final ConditionDefinitionMapper conditionDefinitionMapper;

    @Override
    public List<ConditionDefinition> getMedicationCategory() {
        return null;
    }

    /**
     * 查询病种目录分页列表
     * 
     * @param searchKey 查询条件
     * @param status 查询条件-状态
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return
     */
    @Override
    public Page<ConditionDefinition> getPage(String searchKey, Integer status, Integer sourceEnum, Long id, Integer pageNo,
        Integer pageSize) {
        Page<ConditionDefinition> conditionList;
        // 生成查询条件
        LambdaQueryWrapper<ConditionDefinition> queryWrapper = new LambdaQueryWrapper<>();
        // 模糊查询项目
        if (StringUtils.isNotEmpty(searchKey)) {
            // 模糊查询项目为【疾病编码】，【疾病名称】，【拼音】，【五笔】
            queryWrapper.and(q -> q.like(ConditionDefinition::getCondition_code, searchKey).or()
                .like(ConditionDefinition::getName, searchKey).or().like(ConditionDefinition::getPyStr, searchKey).or()
                .like(ConditionDefinition::getWbStr, searchKey));
        }
        // 查询状态
        if (status != -1) {
            queryWrapper.eq(ConditionDefinition::getStatusEnum, status);
        }
        // 分类查询
        if (sourceEnum != -1) {
            queryWrapper.eq(ConditionDefinition::getSourceEnum, sourceEnum);
        }
        // Id查询
        if (id != -1) {
            queryWrapper.eq(ConditionDefinition::getId, id);
        }
        // 查询删除状态
        queryWrapper.eq(ConditionDefinition::getDeleteFlag, DelFlag.NO.getCode());
        // 查询病种目录列表
        conditionList = conditionDefinitionMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
        // 返回病种目录列表
        return conditionList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addDisease(ConditionDefinition conditionDefinition) {
        // 根据病种编码判断病种是否存在
        List<ConditionDefinition> conditionDefinitions =
            conditionDefinitionMapper.selectList(new LambdaQueryWrapper<ConditionDefinition>()
                .eq(ConditionDefinition::getCondition_code, conditionDefinition.getCondition_code()));
        if (conditionDefinitions.size() > 0) {
            return false;
        }
        // 新增病种
        int insert = conditionDefinitionMapper.insert(conditionDefinition);
        if (insert != 1) {
            return false;
        }
        return true;
    }

    @Override
    public boolean addYbDisease(ConditionDefinition conditionDefinition) {
        return false;
    }
}