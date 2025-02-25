package com.openhis.clinical.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openhis.clinical.domain.ConditionDefinition;
import com.openhis.clinical.mapper.ConditionDefinitionMapper;
import com.openhis.clinical.service.IConditionDefinitionService;

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

    /**
     * 新增病种
     * 
     * @param conditionDefinition 病种目录实体
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addDisease(ConditionDefinition conditionDefinition) {
        // 根据病种编码判断病种是否存在
        List<ConditionDefinition> conditionDefinitions =
            conditionDefinitionMapper.selectList(new LambdaQueryWrapper<ConditionDefinition>()
                .eq(ConditionDefinition::getConditionCode, conditionDefinition.getConditionCode()));
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

    /**
     * 新增医保病种
     * 
     * @param conditionDefinition 病种目录实体
     * @return
     */
    @Override
    public boolean addYbDisease(ConditionDefinition conditionDefinition) {
        return false;
    }
}