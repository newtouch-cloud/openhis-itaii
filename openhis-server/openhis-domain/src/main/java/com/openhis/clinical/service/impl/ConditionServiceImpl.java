package com.openhis.clinical.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.core.common.utils.SecurityUtils;
import com.openhis.clinical.domain.Condition;
import com.openhis.clinical.mapper.ConditionMapper;
import com.openhis.clinical.service.IConditionService;

/**
 * 疾病与诊断管理Service业务层处理
 *
 * @author system
 * @date 2025-02-20
 */
@Service
public class ConditionServiceImpl extends ServiceImpl<ConditionMapper, Condition> implements IConditionService {

    /**
     * 医生门诊就诊时保存诊断
     *
     * @param condition 诊断信息
     * @return 主键ID
     */
    @Override
    public Long saveConditionByDoctor(Condition condition) {
        condition.setRecordedDatetime(new Date());
        condition.setRecorderId(SecurityUtils.getLoginUser().getPractitionerId());// 记录人
        baseMapper.insert(condition);
        return condition.getId();
    }

}